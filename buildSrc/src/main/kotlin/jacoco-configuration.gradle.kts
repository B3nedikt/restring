plugins {
    jacoco
}

jacoco {
    toolVersion = "0.8.8"
}

tasks.withType<Test>().configureEach {

    extensions.configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

tasks.register<JacocoReport>(name = "jacocoTestReport") {

    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val fileFilter = listOf("**/R.class",
                      "**/R$*.class",
                      "**/BuildConfig.*",
                      "**/Manifest*.*",
                      "**/*Test*.*",
                      "android/**/*.*"
    )

    sourceDirectories.setFrom(files("${project.projectDir}/src/main/java"))

    classDirectories.setFrom(
        fileTree("${project.buildDir}/tmp/kotlin-classes/debug") {
            exclude(fileFilter)
        }
    )

    executionData.setFrom(
        fileTree(project.buildDir) {

            include(
                "jacoco/testDebugUnitTest.exec",
                "outputs/code_coverage/debugAndroidTest/connected/*coverage.ec"
            )
        }
    )
}