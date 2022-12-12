import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.3"
	id("io.spring.dependency-management") version "1.0.13.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("plugin.serialization") version "1.6.10"
	kotlin("kapt") version "1.5.20"
	id("org.jetbrains.kotlin.plugin.jpa") version "1.5.21"
	jacoco
}

group = "com.leandoer"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11


repositories {
	maven {
		url = uri("https://maven.pkg.github.com/Leenocktopus/random-bit-sequence-test")
		credentials {
			username = ""
			password = ""
		}
	}
	mavenCentral()

}

dependencies {
	implementation("randombit:randombits-sequencetest:1.1.RELEASE")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.liquibase:liquibase-core")
	implementation("org.liquibase:liquibase-groovy-dsl:3.0.1")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.postgresql:postgresql")
	implementation("org.hibernate:hibernate-validator:7.0.4.Final")
	kapt("org.hibernate:hibernate-jpamodelgen:5.4.30.Final")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	testImplementation("org.springframework.boot:spring-boot-starter-test"){
		exclude(module = "mockito-core")
	}
	testImplementation("com.ninja-squad:springmockk:3.0.1")
	testImplementation("org.testcontainers:junit-jupiter:1.16.0")
	testImplementation( "org.testcontainers:postgresql:1.16.0")
	testImplementation("org.junit.jupiter:junit-jupiter-api")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

allOpen {
	annotation("javax.persistence.Entity")
	annotation("javax.persistence.MappedSuperclass")
	annotation("javax.persistence.Embeddable")
}

tasks.build {
	finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
	dependsOn(tasks.build)
	finalizedBy(tasks.jacocoTestCoverageVerification)
	reports{
		xml.required.set(true)
	}
	classDirectories.setFrom(
		sourceSets.main.get().output.asFileTree.matching {
			exclude(
				"com/leandoer/tracer/TraceApplication*",
				"com/leandoer/tracer/configuration/*",
				"com/leandoer/tracer/model/*",
				"com/leandoer/tracer/repository/specification/*"
			)
		}
	)
}

tasks.jacocoTestCoverageVerification {
	dependsOn(tasks.jacocoTestReport)
	violationRules {
		rule {
			limit {
				counter = "INSTRUCTION"
				value = "COVEREDRATIO"
				minimum = "0.50".toBigDecimal()
			}
		}
	}
	classDirectories.setFrom(
		sourceSets.main.get().output.asFileTree.matching {
			exclude(
				"com/leandoer/tracer/TraceApplication*",
				"com/leandoer/tracer/configuration/*",
				"com/leandoer/tracer/model/*",
				"com/leandoer/tracer/repository/specification/*"
			)
		}
	)
}
