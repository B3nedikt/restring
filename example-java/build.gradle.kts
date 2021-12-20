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
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(project(":restring"))

    implementation(Dependencies.appCompat)
    implementation(Dependencies.constraintLayout)

    implementation(Dependencies.applocale)
    implementation(Dependencies.viewPump)
    implementation(Dependencies.reword)
}
