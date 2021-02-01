import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.4.21"
  `maven-publish`
  id("maven-publish")
}

group = "dev.lukasl"
version = "1.0"

repositories {
  mavenCentral()
  maven("https://dl.bintray.com/kotlin/kotlin-eap")

}

dependencies {
  testImplementation(kotlin("test-junit5"))
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
  implementation(kotlin("stdlib-jdk8"))
  implementation("com.google.code.gson:gson:2.8.6")
}

tasks.test {
  useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
  kotlinOptions.jvmTarget = "13"
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
  jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
  jvmTarget = "1.8"
}

publishing {
  fun findProperty(s: String) = project.findProperty(s) as String?

  repositories {
    maven {
      name = "GitHubPackages"
      url = uri(
        "https://maven.pkg.github.com/${
          property("github.packages.owner")
        }/${
          property("github.packages.repository")
        }"
      )
      credentials {
        username = findProperty("github.username") ?: System.getenv("GITHUB_USERNAME")
        password = findProperty("github.token") ?: System.getenv("GITHUB_TOKEN")
      }
    }
  }
  publications {
    register("jar", MavenPublication::class) {
      from(components["java"])
      group = "dev.lukasl"
      artifactId = "clark4j"
      version = "0.1.4"
    }
  }
}
