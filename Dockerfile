FROM openjdk:11-jdk
VOLUME /tmp
ARG JAR_FILE=build/libs/*.jar
ARG ENV_FILE=/application.yml
ARG IDLE_PROFILE
ENV ENV_IDLE_PROFILE=$IDLE_PROFILE
ENV SPRING_PROFILES_ACTIVE=$IDLE_PROFILE

COPY ${JAR_FILE} app.jar
COPY ${ENV_FILE} application.yml
RUN echo $ENV_IDLE_PROFILE
ENTRYPOINT ["java","-jar","/app.jar","--spring.config.name=application","&" ]
