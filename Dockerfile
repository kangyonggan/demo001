FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} demo001.jar
ENTRYPOINT ["java","-jar","/demo001.jar"]