plugins {
    id("com.android.application")
}

android {
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        applicationId = "dev.b3nedikt.restring.example_java"
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

    implementation(libs.appCompat)
    implementation(libs.constraintLayout)

    implementation(libs.appLocale)
    implementation(libs.viewPump)
    implementation(libs.reword)
}
