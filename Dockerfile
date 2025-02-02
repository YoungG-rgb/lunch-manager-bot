FROM maven:3-amazoncorretto-17 as builder
WORKDIR /opt/app
COPY mvnw pom.xml ./
COPY ./src ./src
RUN mvn clean install -DsipTests

FROM openjdk:17.0.2-slim-buster

ARG SERVER_PORT
ARG LUNCH_CHAT_ID
ARG BOT_NAME
ARG BOT_TOKEN

ENV TZ Asia/Bishkek
ENV SERVER_PORT=${SERVER_PORT}
ENV BOT_NAME=${BOT_NAME}
ENV BOT_TOKEN=${BOT_TOKEN}
ENV LUNCH_CHAT_ID=${LUNCH_CHAT_ID}

WORKDIR /opt/app

COPY --from=builder /opt/app/target/*.jar /opt/app/*.jar

ENTRYPOINT ["java", "-jar", "/opt/app/*.jar"]