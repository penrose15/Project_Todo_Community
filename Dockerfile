FROM openjdk:11-jdk
VOLUME /tmp
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ARG IDLE_PROFILE
ENV ENV_IDLE_PROFILE=$IDLE_PROFILE

RUN mkdir -p /config
ENTRYPOINT ["java","-jar","/app.jar" , "-Dspring.profiles.active=${ENV_IDLE_PROFILE}"]
