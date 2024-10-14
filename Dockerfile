FROM eclipse-temurin:17-jdk-alpine
RUN apk add --no-cache chromium chromium-chromedriver
COPY ./build/libs/*SNAPSHOT.jar project.jar
ENV CHROMEDRIVER_PATH=/usr/bin/chromedriver
ENTRYPOINT ["java", "-jar", "project.jar"]