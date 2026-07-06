FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
EXPOSE 8080
COPY ./target/msc-service-0.0.1-SNAPSHOT.jar msc-service.jar
ENTRYPOINT ["java","-jar","/msc-service.jar"]