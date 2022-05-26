plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.jetbrains.dokka")
    id("jacoco-configuration")
    id("publication")
}

android {
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
    namespace = "dev.b3nedikt.restring"
}

dependencies {
    implementation(Dependencies.kotlin)
    implementation(Dependencies.appCompat)

    // Test libraries
    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.kluent)
    testImplementation(Dependencies.robolectric)
    testImplementation(Dependencies.mockitoKotlin)
    testImplementation(Dependencies.mockitoInline)
    testImplementation(Dependencies.testCore)

    testImplementation(Dependencies.material)

    testImplementation(Dependencies.viewPump)
    testImplementation(Dependencies.reword)
}
