import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp)
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            // Google Maps Compose
            implementation("com.google.maps.android:maps-compose:4.3.3")
            implementation("com.google.android.gms:play-services-maps:18.2.0")
            implementation("com.google.android.gms:play-services-location:21.2.0")
            // MVI orbit
            implementation("org.orbit-mvi:orbit-core:10.0.0")
            implementation("org.orbit-mvi:orbit-viewmodel:10.0.0")
            implementation("org.orbit-mvi:orbit-compose:10.0.0")
            // Compose Destinations (type-safe navigation)
            implementation(libs.compose.destinations.core)
            implementation("com.google.firebase:firebase-auth:23.1.0")
            implementation("com.google.firebase:firebase-database:21.0.0")

            // Android Credential Manager (The modern way to do Google Sign-In)
            implementation("androidx.credentials:credentials:1.2.1")
            implementation("androidx.credentials:credentials-play-services-auth:1.2.1")
            implementation("com.google.android.libraries.identity.googleid:googleid:1.1.0")
            implementation("com.google.dagger:hilt-android:2.57.1")
            implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
            // Coil for image loading
            implementation("io.coil-kt:coil-compose:2.6.0")
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(projects.shared)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}
android {
    namespace = "com.pinpoint"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.pinpoint"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        manifestPlaceholders["MAPS_API_KEY"] = localProperties["MAPS_API_KEY"] ?: ""
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
    add("kspAndroid", "com.google.dagger:hilt-compiler:2.57.1")
    add("kspAndroid", libs.compose.destinations.ksp)
}

