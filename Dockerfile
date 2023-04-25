FROM openjdk:17.0.1-jdk-slim
WORKDIR /
ADD build/libs/cryptoservice-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
COPY data /data
ENTRYPOINT ["java","-jar","/app.jar"]