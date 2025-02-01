FROM openjdk:21-jdk-slim as builder

# 기본값 : test
ARG PROFILE=test

# mkdir /app-build && cd /app-build
WORKDIR /app-build

# docker cp . gradle:app-build
COPY . /app-build

# create .jar
RUN echo "Build with PROFILE=${PROFILE}" && ./gradlew build -Pprofile=${PROFILE} --no-daemon

# Run-Time Image Setting
FROM openjdk:21-jdk-slim as production

# mkdir /app-run && cd /app-run
WORKDIR /app-run

# copy .jar to Run-Time Image
COPY --from=builder /app-build/build/libs/authentication.jar /app-run/authentication.jar


EXPOSE 8080
ENTRYPOINT ["java"]
CMD ["-Dspring.config.additional-location=file:/app-run/", "-jar", "authentication.jar"]