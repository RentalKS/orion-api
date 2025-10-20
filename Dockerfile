FROM openjdk:23-jdk

WORKDIR /app

COPY target/orion-0.0.1-SNAPSHOT.jar /app/orion.jar

EXPOSE 8080

CMD ["java", "-jar", "orion.jar"]

