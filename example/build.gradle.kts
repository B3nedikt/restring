plugins {
    id("com.android.application")
    alias(libs.plugins.kotlin.android)
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

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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

    implementation(libs.kotlin.stdlib)

    implementation(libs.appCompat)
    implementation(libs.constraintLayout)

    implementation(libs.appLocale)
    implementation(libs.viewPump)
    implementation(libs.reword)
}
