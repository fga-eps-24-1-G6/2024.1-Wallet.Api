# Build
FROM maven:3.8.4-openjdk-11 AS base

ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
# Add pom and install dependencies
ADD pom.xml $HOME
RUN mvn dependency:resolve
ADD . $HOME

# Intermediate build
FROM base as builder
RUN mvn clean package -DskipTests

# Run
FROM eclipse-temurin:11-jdk-alpine as runner

COPY --from=builder /usr/app/target/*.jar wallet-api.jar

ENTRYPOINT ["java", "-jar", "/wallet-api.jar"]
