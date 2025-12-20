plugins {
    id("java-library")
    id("application")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

sourceSets {
    main {
        java {
            srcDir("src")
        }
        resources {
            srcDir("src") // In case there are non-java files in src being used as resources
        }
    }
}

application {
    mainClass.set("Main")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
    workingDir = project.projectDir // Ensure it runs in core dir to find CSVs if relative paths are used
}
