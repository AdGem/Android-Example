plugins {
    id("com.android.application")
}

android {
    namespace = "com.adgem.example"
    compileSdk = 37
    defaultConfig {
        applicationId = "com.adgem.android.example"
        minSdk = 23
        targetSdk = 36
        versionCode = 18
        versionName = "1.18"
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
}

dependencies {
    implementation("com.adgem:adgem-android:5.0.0")
    implementation("androidx.appcompat:appcompat:1.8.0")
    implementation("com.google.android.material:material:1.14.0")
    implementation("androidx.preference:preference-ktx:1.2.1")
}
