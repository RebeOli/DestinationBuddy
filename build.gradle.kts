plugins {
    java
    application
    // IL PLUGIN MAGICO UFFICIALE DI JAVAFX!
    id("org.openjfx.javafxplugin") version "0.1.0"
}

repositories {
    mavenCentral()
}

dependencies {
    // Il driver per il database del professore
    implementation("com.mysql:mysql-connector-j:9.3.0")
}

// Facciamo configurare JavaFX direttamente al plugin
javafx {
    version = "21" // Usate Java 21, quindi diciamo a JavaFX di usare la stessa versione
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.graphics")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

application {
    // Il punto di partenza dell'app
    mainClass.set("it.unibo.destinationbuddy.App")
}