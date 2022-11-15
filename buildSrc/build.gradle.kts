plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:7.3.0")
    implementation(kotlin("gradle-plugin", "1.7.21"))
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.6.21")
}