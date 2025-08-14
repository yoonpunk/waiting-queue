plugins {
    id("org.springframework.boot")
    id("java-test-fixtures")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    testFixturesImplementation("org.springframework.boot:spring-boot-starter")
}
