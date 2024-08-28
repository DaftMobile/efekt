import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("com.vanniktech.maven.publish")
    id("org.jetbrains.dokka")
}

mavenPublishing {
    coordinates(project.group.toString(), project.name, project.version.toString())
    signAllPublications()
    publishToMavenCentral(SonatypeHost.S01, automaticRelease = false)
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

val GitBase = "github.com/DaftMobile/Efekt"
val GitHttp = "https://$GitBase"
val GitConnection = "scm:git:git://$GitBase.git"
val GitDevConnection = "scm:git:ssh://git@$GitBase.git"
