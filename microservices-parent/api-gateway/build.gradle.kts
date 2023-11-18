
plugins {
	java
	id("org.springframework.boot") version "3.1.5"
	id("io.spring.dependency-management") version "1.1.3"
}

group = "ca.gbc"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.springframework.cloud:spring-cloud-starter-gateway:4.0.8")
	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:4.0.3")
	implementation("com.fasterxml.woodstox:woodstox-core:6.5.1")
	implementation("com.google.guava:guava:32.1.3-jre")
	implementation("com.thoughtworks.xstream:xstream:1.4.20")
	implementation("org.apache.httpcomponents.client5:httpclient5:5.3-alpha1")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.bootBuildImage {
	builder.set("paketobuildpacks/builder-jammy-base:latest")
}
