plugins {
	java
	id("org.springframework.boot") version "3.2.6"
	id("io.spring.dependency-management") version "1.1.5"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	val auth0Version = "4.4.0"
	val mapstructVersion = "1.5.5.Final"
	val lombokMapstructBindingVersion = "0.2.0"
	val flywayVersion = "10.15.0"
	val springDocAPIVersion = "2.5.0"

	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-amqp")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${springDocAPIVersion}")
	implementation("org.flywaydb:flyway-core:${flywayVersion}")
	implementation("com.auth0:java-jwt:${auth0Version}")
	implementation("org.mapstruct:mapstruct:${mapstructVersion}")

	compileOnly("org.projectlombok:lombok")
	runtimeOnly("org.postgresql:postgresql")
	runtimeOnly("org.flywaydb:flyway-database-postgresql:${flywayVersion}")

	annotationProcessor("org.projectlombok:lombok")
	annotationProcessor("org.mapstruct:mapstruct-processor:${mapstructVersion}")
	annotationProcessor("org.projectlombok:lombok-mapstruct-binding:${lombokMapstructBindingVersion}")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
