plugins {
    application
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.h2database:h2:2.2.224")
    implementation("org.postgresql:postgresql:42.7.1")
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("gg.jte:jte:3.2.0")
    implementation("io.javalin:javalin:6.4.0")
    implementation("io.javalin:javalin-bundle:6.4.0")
    implementation("io.javalin:javalin-rendering:6.4.0")
    implementation("org.slf4j:slf4j-simple:2.0.16")
    implementation ("org.jsoup:jsoup:1.19.1")
    implementation ("com.konghq:unirest-java:3.13.0")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.27.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation ("com.squareup.okhttp3:mockwebserver:4.12.0")

}

application {
    mainClass.value("hexlet.code.App")
}

tasks.test {
    useJUnitPlatform()
}