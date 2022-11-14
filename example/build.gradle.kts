plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        applicationId = "dev.b3nedikt.restring.example"
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"))
        }
    }
    namespace = "dev.b3nedikt.restring.example"
}

dependencies {
    implementation(project(":restring"))

    implementation(Dependencies.kotlin)

    implementation(Dependencies.appCompat)
    implementation(Dependencies.constraintLayout)

    implementation(Dependencies.applocale)
    implementation(Dependencies.viewPump)
    implementation(Dependencies.reword)

    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.10")
}
