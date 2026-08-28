FROM eclipse-temurin:21-jdk-alpine

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

VOLUME /tmp
EXPOSE 8081
COPY --chown=appuser:appgroup ./target/msc-service-0.0.1-SNAPSHOT.jar /msc-service.jar
USER appuser
ENTRYPOINT ["java","-jar","/msc-service.jar"]