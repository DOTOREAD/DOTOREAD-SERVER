FROM eclipse-temurin:17-jdk-alpine

# 필요한 패키지 설치
RUN apk add --no-cache chromium chromium-chromedriver

# 환경 변수 설정
ENV CHROME_BIN=/usr/bin/chromium-browser \
    CHROME_PATH=/usr/lib/chromium/ \
    CHROMEDRIVER_PATH=/usr/bin/chromedriver

# 애플리케이션 JAR 파일 복사
COPY ./build/libs/*SNAPSHOT.jar project.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "project.jar"]