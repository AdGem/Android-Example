plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.adgem.example"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.adgem.android.example"
        minSdk = 21
        targetSdk = 34
        versionCode = 18
        versionName = "1.18"
    }

    viewBinding.isEnabled = true

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("com.adgem:adgem-android:4.0.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.preference:preference-ktx:1.2.1")
}
