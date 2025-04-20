# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/sonarqube-springboot-*.jar app.jar

# Set timezone
ENV TZ=UTC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Create a non-root user
RUN addgroup --system --gid 1001 spring && \
    adduser --system --uid 1001 --gid 1001 spring
USER spring

# Set network
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://springboot-postgres:5432/todo_db
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=postgres

EXPOSE 8000
ENTRYPOINT ["java", "-jar", "app.jar"] 