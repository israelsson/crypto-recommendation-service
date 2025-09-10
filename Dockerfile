FROM openjdk:21-jdk-slim

 COPY target/crypto-recommendation-service-*.jar crypto-recommendation-service.jar

 EXPOSE 8080

 ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar crypto-recommendation-service.jar"]