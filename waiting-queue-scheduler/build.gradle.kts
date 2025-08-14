dependencies {
    implementation(project(mapOf("path" to ":waiting-queue-api")))
    testImplementation(testFixtures(project(":waiting-queue-api")))
}
plugins {
    id("org.springframework.boot")
}