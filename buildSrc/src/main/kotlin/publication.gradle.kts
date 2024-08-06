
import java.net.URI
import java.util.Properties

plugins {
    id("com.android.library")
    id("maven-publish")
    id("signing")
}

afterEvaluate {

    extensions.configure<PublishingExtension> {

        publications {
            register<MavenPublication>(name = "release") {

                from(components["release"])

                groupId = Pom.groupId
                artifactId = Pom.artifactId
                version = project.version.toString()

                artifact(tasks.named("dokkaHtmlJar"))

                pom {
                    name.set(Pom.libraryName)
                    description.set(Pom.libraryDescription)
                    url.set(Pom.siteUrl)

                    licenses {
                        license {
                            name.set(Pom.licenseName)
                            url.set(Pom.licenseUrl)
                        }
                    }
                    developers {
                        developer {
                            id.set(Pom.developerId)
                            name.set(Pom.developerName)
                            email.set(Pom.developerEmail)
                            organization.set(Pom.organization)
                            organizationUrl.set(Pom.siteUrl)
                        }
                    }
                    scm {
                        url.set(Pom.siteUrl)
                    }
                }

                extensions.configure<SigningExtension>("signing") {
                    val signingKeyId = propertyOrEnv("signingKeyId")
                    val signingKey = propertyOrEnv("signingKey")
                    val signingPassword = propertyOrEnv("signingPassword")
                    useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)

                    sign(this@register)
                }
            }

            repositories {
                maven {
                    name = "MavenCentral"
                    url = URI("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                    credentials {
                        username = propertyOrEnv("mavenUsername")
                        password = propertyOrEnv("mavenPassword")
                    }
                }
            }
        }
    }
}

fun propertyOrEnv(propertyName: String): String {
    val file = project.rootProject.file("local.properties")
    if (file.exists()) {
        val properties = Properties()
        file.inputStream().use {
            properties.load(it)
        }
        return if (properties.containsKey(propertyName)) {
            properties.getProperty(propertyName)
        } else ""
    }

    return System.getenv(propertyName) ?: ""
}
