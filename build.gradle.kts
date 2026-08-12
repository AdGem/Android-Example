plugins {
    // AGP 9 provides Kotlin support itself; the standalone org.jetbrains.kotlin.android
    // plugin must not be applied alongside it. See https://kotl.in/gradle/agp-built-in-kotlin
    id("com.android.application") version "9.3.1" apply false
}
