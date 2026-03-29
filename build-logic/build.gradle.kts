import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    `kotlin-dsl`
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
fun version(name: String) = libs.findVersion(name).get().requiredVersion

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("com.android.tools.build:gradle:${version("agp")}")
    implementation("com.google.devtools.ksp:symbol-processing-gradle-plugin:${version("ksp")}")
    implementation("com.google.dagger:hilt-android-gradle-plugin:${version("hilt")}")
    implementation("org.jetbrains.kotlin:compose-compiler-gradle-plugin:${version("kotlin-compose-plugin")}")
}

