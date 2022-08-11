FROM openjdk:11-jre-slim
EXPOSE 8081

COPY target/*.jar /app/app.jar
WORKDIR /app

ENV JAVA_OPTIONS="-server -XshowSettings:vm"

ENTRYPOINT ["java", "-jar", "app.jar"]