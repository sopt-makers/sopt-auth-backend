# sopt-auth-backend
> _**Updated at.** 2025/01/11 (Sat)_ <br/>
> _**Updated by.** [@yummygyudon](https://www.github.com/yummygyudon)_

SOPT 회원들이 SOPT makers 프로덕트를 사용하기 위한 **인증**과 **로그인/회원가입 기능**을 지원합니다.

## Tech Stack
- **Language** : Java 21
- **Framework** : Spring Framework
  - Spring Boot 
  - Spring Data JPA 
  - Spring Security (Starter, OAuth 2.0 Resource Server)
  - Java JWT (`jjwt`)
- **Deploy** : Github Actions, AWS S3, AWS ECR, Docker
- **Test** 
  - JUnit5
  - Mockito 
  - Rest Assured
  - Fixture Monkey(1.0.25)
  - Archunit(1.3.0)
- **Client** : OKHttp

<br/>

## Conventions
**협업 프로세스**는 아래와 같습니다.
1. **github issue 생성**
    - 템플릿에 맞춰 Issue 작성
    - Assignees, Label 할당
    - 단, "사소한 변경의 HOTFIX"인 경우, 구체적인 커밋 메시지 작성을 통해 전달
2. **branch 생성**
    - 형식 : `{Issue Tag}/#{Issue Number}` (ex. `feat/#1`)
3. **로컬 작업**
    - **기능단위** 커밋 지향
4. 해당 issue에 대한 작업 완료 시, `dev` 브랜치로 **해당 브랜치 Pull Request 제출**
    - 템플릿에 맞춰 PR 작성
    - Assignees, Label 할당
    - `prod` 브랜치에 대한 PR의 경우, 버전 반영에 영향이 있기 때문에 반드시 적절한 Label을 할당합니다.
5. 리뷰어가 전원 Approve 때까지 리뷰를 주고받으며 기능 수정 반복
6. 리뷰어 전원 Approve 시, merge
    - `feature branch` → `dev` → `prod`

> **프로젝스 소스코드 외** (환경변수, db 필드 및 테이블 수정, 인프라 세팅 등) 수정사항이 있을 경우, <br/>
> 팀원에게 먼저 물어보고 진행하거나, 그러지 못하였더라면 빠르게 전달해야 합니다 (카톡, 슬랙, 디코 등)

<br/>

### 🔖 Issue / Commit Tag
| Tag          | Description                    |
|:-------------|:-------------------------------|
| `[ADD]`      | 주요 기능 관련 코드/파일 추가 |
| `[MODIFY]`   | 주요 기능 관련 코드/파일 수정 |
| `[DEL]`      | 주요 기능 관련 코드/파일 제거 |
| `[CHORE]`    | 주요 기능 **외** 코드/파일 추가 및 수정 |
| `[FEAT]`     | 기능 구현 |
| `[FIX]`      | 기능 버그 및 오류 해결 |
| `[HOTFIX]`   | Issue / QA 과정에서의 급한 버그 및 오류 해결 |
| `[DOCS]`     | README/WIKI 등의 문서 작업           |
| `[REFACTOR]` | 기능/성능/코드 개선 작업                 |

<br/>

### Branch Strategy 🌵
기본적으로 "**Git Flow**" 전략을 바탕으로 적용했습니다.
- `main`, `dev`, `feature` 브랜치 구성
- `main` : **production**용 브랜치
    - 실서비스용 ec2(**makers.operation.prod)**로 배포되도록 파이프라인이 구축되어 있습니다
- `dev` : **development**용 브랜치
    - 테스트용 ec2(**makers.operation)**로 배포되도록 파이프라인이 구축되어 있습니다
    - default 브랜치 입니다
- `feature` : **Isuue** 브랜치
    - 각자 이슈에 대한 작업물 브랜치
      - 이슈에 따라 브랜치 Prefix 지정 (feat/fix/modify 등)
      - 형식 = `{이슈 종류}/#{이슈번호}` 
    - `main` 직접적인 PR 불가능 (`dev` PR을 거쳐 merge)

<br/>

## Authors
- 정동규 : [@yummygyudon](https://www.github.com/yummygyudon)
- 김성은 : [@sung-silver](https://www.github.com/sung-silver)
- 강현욱 : [@hyunw9](https://www.github.com/hyunw9)

![Alt](https://repobeats.axiom.co/api/embed/7ecaed933101e79c17cea76035d79ea7afff6565.svg "Repobeats analytics image")

<br/>

## More About
### Application Architecture 🧸
기본적으로 "**도메인 주도 개발**(DDD)"를 기조로 설계했습니다.
- 유저의 권한, 인증 기능을 다루는 프로젝트인만큼 테스트의 중요도가 높다고 판단하여 도메인에 대한 "**테스트 용이성**" 효용을 취하기 위함.
- SOPT의 경우, 6개월의 활동 주기를 가지며 각 기수마다의 유저 정책 변동성이 존재한다고 판단하여 "**유연성**" 및 "**도메인 독립성**" 효용을 취하기 위함

![img.png](https://miro.medium.com/v2/resize:fit:1400/format:webp/1*JOSyX8Ck85HrJ2WhU_jMcQ.png)
(이미지 출처 : https://medium.com/@hello-every-one/hexagonal-architecture-3729e9a9200b)

<br/>

### Server Architecture ☁️
1. **Merge & Trigger** Github Actions
2. **Run Deploy Workflow** Script
   - [dev script](./.github/workflows/cd-dev.yml)
   - [prod script](./.github/workflows/cd-prod.yml)
3. **Copy Files from AWS S3**
4. **Build and Push Image to ECR**
5. **Send Files to EC2** by SCP
6. **Connect to EC2** by SSH
7. **Run Deploy Scripts** with receive files
8. **Deploy** application on Docker Container


![server_architecture_img.png](https://yummygyudon.notion.site/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F10896002-67a1-441c-9cea-b6f696faae26%2F36522594-3b8d-4995-b24e-fded2b1779e9%2Fimage.png?table=block&id=17779bee-8167-8007-92c1-c4b1fc0800cf&spaceId=10896002-67a1-441c-9cea-b6f696faae26&width=1420&userId=&cache=v2)

<br/>

### Features ⚙️
- **번호 인증**
  - 목적 기능에 따라 Type이 분류됩니다 (`REGISTER`/`CHANGE`/`SEARCH`)
- **회원 가입**
- **로그인**
- **가입 계정 플랫폼 조회**
- **JWKS Public Key 조회**

<br/>

### Environment Variables 🔑
환경변수의 경우, `.env` 파일을 통해 관리되며 주입됩니다.<br/>
주입된 `.env` 파일은 Docker 실행 시에 `--env-files` 옵션을 통해 어플리케이션에 적용됩니다.<br/>
yaml 내 `${}` 정의된 변수명이 키 값이며 키 값에 해당하는 환경변수가 적용됩니다. 

`.env` 파일은 **Notion**과 **AWS S3**를 통해 관리됩니다.<br/>
(자세한 내용은 플랫폼 팀 BE 구성원에게 문의 부탁드립니다.)

<br/>

### Run Locally 🏃‍♂️‍➡️
> `local.env` 파일이 필요합니다.

1. Clone the project
2. Open the project
3. Install/Refresh dependencies
   ```shell
   ./gradlew dependencies --refresh-dependencies
   ```
4. Move `.env` file to un-versioned package
   ```ignorelang
   # 현재 gitignore 적용된 path
   **/src/main/resources/**/*.env
   ``` 
5. Set property files
    - Apple Key file : `./src/main/resources/key`
    - JWT Pem Key file : `./src/main/resources`
6. Run Application with `local.env`
    - Itellij **사용** 시, 아래와 같이 실행합니다.
        - 어플리케이션 실행 드롭다운 클릭 & [ **Edit Configurations...** ] 선택
        - [ Run ] 섹션의 [ **Modify options** ] 드룹다운 클릭
        - Operation System - [ **Environment variables** ] 선택
          ![스크린샷 2025-01-11 오후 5.28.47.png](..%2F..%2F%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-01-11%20%EC%98%A4%ED%9B%84%205.28.47.png)
        - env 파일을 선택
          ![스크린샷 2025-01-11 오후 5.26.57.png](..%2F..%2F%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-01-11%20%EC%98%A4%ED%9B%84%205.26.57.png)
    - Intellij **미사용** 시, 아래와 같이 실행합니다.
      ```shell
      # test 없이 build & Jar 생성
      ./gradlew clean build -x test
      
      # env 파일 내용을 실행 환경 변수(xargs)로 정의한 후, Jar 실행
      # `.env` path : 각자의 상황에 따른 반영 필요
      export $(cat ./src/main/resources/env/local.env | xargs) && java -jar ./build/libs/authentication.jar
      ```

<br/>

### Run Tests 🧪
```bash
# Run All Test
./gradlew test

# Run Specific Test Class
./gradlew test --tests "sopt.makers.**.XXXClass"

# Run Specific Test Method
./gradlew test --tests "sopt.makers.**.XXXClass.XXXMethod"
```
