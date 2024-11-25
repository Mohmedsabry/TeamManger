plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}
dependencies {
    testImplementation(libs.junit.junit)
    testImplementation(libs.truth)
    testImplementation(libs.junit.junit)
}
