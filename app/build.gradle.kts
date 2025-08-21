plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    // Add Spotless for linting and code style
    id("com.diffplug.spotless") version "7.2.1"
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation(libs.junit.jupiter)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // This dependency is used by the application.
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
    mainClass = "org.example.App"
}

spotless {
    java {
        importOrder()  // Using spotless default import order configuration
        removeUnusedImports()
        removeWildcardImports()
        googleJavaFormat()
    }
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
