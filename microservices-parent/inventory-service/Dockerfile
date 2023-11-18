# --------------------
# Dockerfile for product-service
# --------------------

# --------------------
# BUILD stage
# --------------------

# base image
FROM gradle:8.3-jdk17 AS builder

# Copy the application files from the host machine to the image filesystem
COPY --chown=gradle:gradle . /home/gradle/src

# set the directory within the image, for executing furure commands
WORKDIR /home/gradle/src

# Run gradle build within the container - NOT the host machine
RUN gradle build -x test


# --------------------
# PACKAGE stage
# --------------------

FROM openjdk:17

RUN mkdir /app

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/inventory-service.jar

ENV POSTGRES_USER=admin \
    POSTGRES_PASSWORD=password

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "/app/inventory-service.jar"]

# Okay, so eureka server and api-gateway are working, now
#   the "issue" here for running this using IDE is that,
#   you don't have any database running.
#   So is not possible to run it successfully from IDE, make sense? :c
#   Yes, Can You create database for me?
#   yes sure, we can create some databases to test