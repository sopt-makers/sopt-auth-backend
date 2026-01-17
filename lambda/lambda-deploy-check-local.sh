#!/bin/bash

# AWS Lambda JAR 배포 스크립트
# 사용법: ./lambda/lambda-deploy-check-local.sh [dev]
# 로컬에서 배포를 테스트하고 싶을 때에만 사용할 수 있습니다

set -e  # 에러 발생시 중단

# ========================================
# 초기 설정 - 프로젝트 루트로 이동
# ========================================
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
cd "$PROJECT_ROOT"
source .env

# ========================================
# 설정
# ========================================
ENV=${1:-dev}
PROJECT_NAME="authentication"
S3_BUCKET="sopt-makers-authentication"
STACK_NAME="${PROJECT_NAME}-${ENV}"
AWS_REGION="ap-northeast-2"
PROFILE="dev"  # Gradle build profile (dev or prod)
AWS_PROFILE="makers-platform"

# 색상 정의
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# ========================================
# 함수들
# ========================================
log_info() {
  echo -e "${GREEN}✓${NC} $1"
}

log_step() {
  echo -e "\n${YELLOW}==>${NC} $1"
}

log_error() {
  echo -e "${RED}✗${NC} $1" >&2
}

validate_env() {
  if [[ "$ENV" != "dev" && "$ENV" != "prod" ]]; then
    log_error "환경은 'dev' 또는 'prod'여야 합니다"
    exit 1
  fi

  if ! command -v aws &> /dev/null; then
    log_error "AWS CLI가 설치되어 있지 않습니다"
    exit 1
  fi

  if ! command -v sam &> /dev/null; then
    log_error "AWS SAM CLI가 설치되어 있지 않습니다"
    exit 1
  fi

  log_info "환경 검증 완료 (${ENV})"
}

build_lambda_jar() {
  log_step "1️⃣  Lambda JAR 빌드 중..."
  ./gradlew clean lambdaJar -x test -Pprofile=${ENV}
  log_info "JAR 빌드 완료"
}

upload_to_s3() {
  log_step "2️⃣  S3 업로드 중..."

  # Lambda ZIP 파일 찾기
  JAR_FILE=$(find build/distributions -name "*-lambda.zip" | head -1)

  if [[ ! -f "$JAR_FILE" ]]; then
    log_error "Lambda 파일을 찾을 수 없습니다: build/distributions/*-lambda.zip"
    exit 1
  fi

  # 타임스탐프를 포함한 S3 경로 생성
  TIMESTAMP=$(date +"%Y%m%d-%H%M%S")
  S3_KEY="${ENV}/lambda/${STACK_NAME}-${TIMESTAMP}.zip"

  echo "  파일: $JAR_FILE"
  echo "  버킷: $S3_BUCKET"
  echo "  경로: s3://${S3_BUCKET}/${S3_KEY}"

  # S3에 업로드
  aws s3 cp "$JAR_FILE" "s3://${S3_BUCKET}/${S3_KEY}" \
    --region ${AWS_REGION} \
    --profile ${AWS_PROFILE}
  log_info "S3 업로드 완료"

  # 변수 저장 (다음 함수에서 사용)
  export S3_BUCKET S3_KEY
}

deploy_with_sam() {
  log_step "3️⃣ SAM 배포"
  cd lambda

  sam deploy \
    --config-env ${ENV} \
    --no-fail-on-empty-changeset \
    --parameter-overrides \
      S3Bucket="${S3_BUCKET}" \
      S3Key="${S3_KEY}" \
      Profile="${ENV}" \
      DbUrl="${DB_URL}" \
      DbUsername="${DB_USERNAME}" \
      DbPassword="${DB_PASSWORD}" \
      DbDriverClass="${DB_DRIVER_CLASS}" \
      JpaDatabasePlatform="${JPA_DATABASE_PLATFORM}" \
      JpaDatabase="${JPA_DATABASE}" \
      RedisHost="${REDIS_HOST}" \
      RedisPort="${REDIS_PORT}" \
      RedisPassword="${REDIS_PASSWORD}" \
      AccessTokenExpirationTime="${ACCESS_TOKEN_EXPIRATION_TIME}" \
      RefreshTokenExpirationTime="${REFRESH_TOKEN_EXPIRATION_TIME}" \
      Issuer="${ISSUER}" \
      GabiaSmsId="${GABIA_SMS_ID}" \
      GabiaSmsKey="${GABIA_SMS_KEY}" \
      GabiaSmsUrl="${GABIA_SMS_URL}" \
      GabiaSmsPhone="${GABIA_SMS_PHONE}" \
      AppleAudApp="${APPLE_AUD_APP}" \
      AppleAudWeb="${APPLE_AUD_WEB}" \
      GoogleClientId="${GOOGLE_CLIENT_ID}" \
      PlaygroundXApiKey="${PLAYGROUND_X_API_KEY}" \
      CrewXApiKey="${CREW_X_API_KEY}" \
      AppXApiKey="${APP_X_API_KEY}" \
      AdminXApiKey="${ADMIN_X_API_KEY}" \
      PlatformXApiKey="${PLATFORM_X_API_KEY}" \
      MonitoringXApiKey="${MONITORING_X_API_KEY}" \
      JwtKeyS3Bucket="${JWT_KEY_S3_BUCKET}" \
      JwtPublicKeyS3Path="${JWT_PUBLIC_KEY_S3_PATH}" \
      JwtPrivateKeyS3Path="${JWT_PRIVATE_KEY_S3_PATH}" \
      ActuatorPath="${ACTUATOR_PATH}" \
      UserPath="${USER_PATH}" \
      GetPublicKey="${GET_PUBLIC_KEY}" \
      KeyId="${KEY_ID}" \
      MagicLoginPhone="${MAGIC_LOGIN_PHONE}" \
      MagicLoginCode="${MAGIC_LOGIN_CODE}" \
      MagicLoginName="${MAGIC_LOGIN_NAME}" \
      PlaygroundExternalKey="${PLAYGROUND_EXTERNAL_KEY}" \
      PlaygroundBaseUrl="${PLAYGROUND_BASE_URL}" \
      AppExternalKey="${APP_EXTERNAL_KEY}" \
      AppBaseUrl="${APP_BASE_URL}" \
    --region ${AWS_REGION} \
    --profile ${AWS_PROFILE}

  cd ..
  log_info "SAM 배포 완료"
}

output_api_endpoint() {
  log_step "4️⃣  API 엔드포인트 확인 중..."

  API_ENDPOINT=$(aws cloudformation describe-stacks \
    --stack-name ${STACK_NAME} \
    --query "Stacks[0].Outputs[?OutputKey=='ApiEndpoint'].OutputValue" \
    --output text \
    --region ${AWS_REGION} \
    --profile ${AWS_PROFILE})

  if [[ ! -z "$API_ENDPOINT" ]]; then
    log_info "배포 완료!"
    echo -e "\n${GREEN}🌐 API Endpoint: ${API_ENDPOINT}${NC}\n"
  else
    log_error "API 엔드포인트를 조회할 수 없습니다"
    exit 1
  fi
}

# ========================================
# 실행
# ========================================
echo -e "${GREEN}🚀 Lambda 배포 시작${NC}"
echo "   현재 경로: $(pwd)"
echo "   환경: ${ENV}"
echo "   Stack: ${STACK_NAME}"
echo ""

validate_env
build_lambda_jar
upload_to_s3
deploy_with_sam
output_api_endpoint

echo -e "${GREEN}✅ 배포 프로세스 완료!${NC}"