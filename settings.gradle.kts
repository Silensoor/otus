rootProject.name = "homeworkOtus"

include("hw06-reflection")
include("hw08-gc:homework")
include("hw10-aop")
include("hw12-atm")
include("hw15-pattern")
include("hw15-pattern:homework")
include("hw16-io")
include("hw16-io:homework")
include("hw18-jdbc")
include("hw18-jdbc:homework")
include("hw18-jdbc:demo")
include("hw21-cache")
include("hw21-cache:demo")
include("hw21-cache:homework")
include("hw22-jpql")
include("hw22-jpql:homework-template")
include("hw24-webServer")
include("hw25-injection")

pluginManagement {
  val jgitver: String by settings
  val dependencyManagement: String by settings
  val springframeworkBoot: String by settings
  val johnrengelmanShadow: String by settings
  val jib: String by settings
  val protobufVer: String by settings
  val sonarlint: String by settings
  val spotless: String by settings

  plugins {
    id("fr.brouillard.oss.gradle.jgitver") version jgitver
    id("io.spring.dependency-management") version dependencyManagement
    id("org.springframework.boot") version springframeworkBoot
    id("com.github.johnrengelman.shadow") version johnrengelmanShadow
    id("com.google.cloud.tools.jib") version jib
    id("com.google.protobuf") version protobufVer
    id("name.remal.sonarlint") version sonarlint
    id("com.diffplug.spotless") version spotless
  }
}