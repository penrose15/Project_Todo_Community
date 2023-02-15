FROM openjdk:11-jdk
VOLUME /tmp
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
RUN mkdir -p /config
ENTRYPOINT ["java","-jar","/app.jar", \
    "--spring.config.location=/config/application.yml"]
