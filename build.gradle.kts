// Top-level build file where you can add configuration options common to all sub-projects/modules.

ext {
    val hiltVersion by extra("2.51.1")
    val navVersion by extra("2.8.5")
    val kotlinVersion by extra("2.1.0")
}

plugins {
    id("com.android.application") version "8.7.3" apply false
    id("com.android.library") version "8.7.3" apply false
    id("com.google.devtools.ksp") version "2.1.0-1.0.29" apply false
    id("org.jetbrains.kotlin.android") version "2.1.0" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    id("androidx.navigation.safeargs") version "2.8.5" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
