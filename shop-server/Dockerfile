FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package


FROM eclipse-temurin:21-jdk-alpine
COPY --from=build /app/target/shop-app-0.0.1-SNAPSHOT.jar shop-app.jar
ENTRYPOINT ["java","-jar","shop-app.jar"]
EXPOSE 8080
