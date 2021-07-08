plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdkVersion(AppConfig.compileSdk)

    defaultConfig {
        applicationId = "dev.b3nedikt.restring.example"
        minSdkVersion(AppConfig.minSdk)
        targetSdkVersion(AppConfig.targetSdk)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(project(":restring"))

    implementation(Dependencies.kotlin)

    implementation(Dependencies.appCompat)
    implementation(Dependencies.constraintLayout)

    implementation(Dependencies.applocale)
    implementation(Dependencies.viewPump)
    implementation(Dependencies.reword)
}
