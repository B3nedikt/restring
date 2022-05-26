plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:7.2.0")
    implementation(kotlin("gradle-plugin", "1.6.20"))
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.6.21")
}