FROM openjdk:11-jdk
VOLUME /tmp
ARG JAR_FILE=build/libs/*.jar
ARG IDLE_PROFILE
ENV ENV_IDLE_PROFILE=$IDLE_PROFILE
ENV SPRING_PROFILES_ACTIVE=$IDLE_PROFILE

COPY ${JAR_FILE} app.jar
RUN echo $ENV_IDLE_PROFILE
ENTRYPOINT ["java","-jar","-Dspring.config.location=file:src/main/resources/application.yml","-Dspring.profiles.active=${ENV_IDLE_PROFILE}","/app.jar" ]
