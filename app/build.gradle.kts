plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application

    // Add Spotless for linting and code style
    id("com.diffplug.spotless") version "7.2.1"

    // JavaFX plugin to install JavaFX library needed modules
    id("org.openjfx.javafxplugin") version "0.1.0"

    /*
    * Plugin to create fat Jar
    * To create the fat jar, run on a terminal "./gradlew shadowJar
    * The fat jar will be found in build/libs/projectname-all.jar
    */
    id("com.gradleup.shadow") version "9.0.2"
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation(libs.junit.jupiter)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Guava EventBus might be needed in the future.
    implementation(libs.guava)
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    // Define the main class for the application.
    mainClass = "com.zekecode.hakai.App"
}

spotless {
    java {
        importOrder()  // Using spotless default import order configuration
        removeUnusedImports()
        removeWildcardImports()
        googleJavaFormat()
    }
}

javafx {
    version = "24.0.2"
    modules = listOf(
        "javafx.controls",
        "javafx.base",
        "javafx.fxml",
        "javafx.swing",
        "javafx.graphics",
        "javafx.media"
    )
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
