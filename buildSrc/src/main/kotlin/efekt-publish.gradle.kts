plugins {
    `maven-publish`
    signing
    id("org.jetbrains.dokka")
}


val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    archiveClassifier.set("javadoc")
    from(tasks.findByName("dokkaGfm"))
}

signing {
    sign(publishing.publications)
}

publishing {
    val ossrhUsername: String? by project
    val ossrhPassword: String? by project
    repositories {
        maven {
            name = "sonatype"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = ossrhUsername
                password = ossrhPassword
            }
        }
    }
    publications.withType<MavenPublication> {
        artifact(dokkaJar)
        pom {
            name.set(project.name)
            description.set(
                "Efekt is a Redux-like pattern implementation with effects!"
            )
            url.set(GitHttp)
            licenses {
                license {
                    name.set("MIT")
                    url.set("https://opensource.org/license/mit/")
                    distribution.set("repo")
                }
            }
            developers {
                developer {
                    id.set("DaftMobile")
                    name.set("DaftMobile")
                }
            }
            scm {
                url.set(GitHttp)
                connection.set(GitConnection)
                developerConnection.set(GitDevConnection)
            }
        }
    }
}

val GitBase = "github.com/DaftMobile/Efekt"
val GitHttp = "https://$GitBase"
val GitConnection = "scm:git:git://$GitBase.git"
val GitDevConnection = "scm:git:ssh://git@$GitBase.git"
