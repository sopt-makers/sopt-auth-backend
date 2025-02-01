FROM openjdk:21-jdk-slim as production

# 빌드 시 전달된 PROFILE 인수를 받을 ARG 설정
ARG PROFILE
ENV SPRING_PROFILES_ACTIVE=${PROFILE}

# 작업 디렉토리 생성 및 이동
WORKDIR /app-run

# 빌드된 .jar 파일 복사
COPY build/libs/authentication.jar /app-run/authentication.jar

EXPOSE 8080
ENTRYPOINT ["java"]
CMD ["-Dspring.config.additional-location=file:/app-run/env/application.env", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "authentication.jar"]