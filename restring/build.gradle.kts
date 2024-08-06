plugins {
    id("com.android.library")
    id("jacoco-configuration")
    id("publication")
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.dokka)
}

android {
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        minSdk = AppConfig.minSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = false
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
    namespace = "dev.b3nedikt.restring"
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.appCompat)

    // Test libraries
    testImplementation(libs.junit)
    testImplementation(libs.kluent)
    testImplementation(libs.robolectric)
    testImplementation(libs.mockitoKotlin)
    testImplementation(libs.mockitoInline)
    testImplementation(libs.testCore)

    testImplementation(libs.material)

    testImplementation(libs.viewPump)
    testImplementation(libs.reword)
}

tasks.register<Jar>("dokkaHtmlJar") {
    dependsOn(tasks.dokkaHtml)
    from(tasks.dokkaHtml.flatMap { it.outputDirectory })
    archiveClassifier.set("html-docs")
}