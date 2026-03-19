plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("com.android.tools.build:gradle:9.1.0")
    implementation("com.google.devtools.ksp:symbol-processing-gradle-plugin:2.3.6")
    implementation("com.google.dagger:hilt-android-gradle-plugin:2.59.2")
    implementation("org.jetbrains.kotlin:compose-compiler-gradle-plugin:2.2.10")
}

