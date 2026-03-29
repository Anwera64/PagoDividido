import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension

plugins {
    id("org.jetbrains.kotlin.plugin.compose")
}

extensions.findByType(ApplicationExtension::class.java)?.buildFeatures?.compose = true
extensions.findByType(LibraryExtension::class.java)?.buildFeatures?.compose = true



