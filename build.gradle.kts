plugins {
    id("java")
    id("org.springframework.boot") version ("2.7.18")
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    id("io.github.paopaoyue.ypp-rpc-generator") version "0.0.18-jdk8"
}

group = "com.github.paopaoyue"
version = "1.1.2-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

rpcGenerator {
    serviceName = "wlj-service"
    serviceShortAlias = "wlj"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation(files("C:\\Users\\LENOVO\\Desktop\\projects\\mtsLib\\BaseMod.jar"))
    implementation(files("C:\\Users\\LENOVO\\Desktop\\projects\\mtsLib\\desktop-1.0.jar"))
    implementation(files("C:\\Users\\LENOVO\\Desktop\\projects\\mtsLib\\ModTheSpire.jar"))
    implementation(files("C:\\Users\\LENOVO\\Desktop\\projects\\mtsLib\\StSLib.jar"))
    implementation(files("C:\\Users\\LENOVO\\Desktop\\projects\\rpc-mod\\build\\libs\\rpc-mod-0.1.0-SNAPSHOT-stsMod.jar"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Delete>("cleanTestFolder") {
    delete(fileTree("E:\\Steam Games\\steamapps\\common\\SlayTheSpire\\mods") { include("*.jar") })
}

tasks.register<Delete>("cleanPublishFolder") {
    delete(fileTree("E:\\Steam Games\\steamapps\\common\\SlayTheSpire\\wlj-mod\\content") { include("*.jar") })
}

tasks.register<Copy>("deployToTestFolder") {
    dependsOn(tasks.clean, tasks.jar)
    from(layout.buildDirectory.dir("libs"))
    include("*.jar")
    into(layout.buildDirectory.dir("E:\\Steam Games\\steamapps\\common\\SlayTheSpire\\mods"))
}

tasks.register<Copy>("deployToPublishFolder") {
    dependsOn(tasks.clean, tasks.jar)
    from(layout.buildDirectory.dir("libs"))
    include("*.jar")
    into(layout.buildDirectory.dir("E:\\Steam Games\\steamapps\\common\\SlayTheSpire\\wlj-mod\\content"))
}

tasks.register<Exec>("runGame") {
    dependsOn(":cleanTestFolder", ":deployToTestFolder")
    workingDir("E:\\Steam Games\\steamapps\\common\\SlayTheSpire")
    commandLine("cmd", "/c", "java -jar mts-launcher.jar")
}

tasks.register<Exec>("publishMod") {
    dependsOn(":cleanPublishFolder", ":deployToPublishFolder")
    workingDir("E:\\Steam Games\\steamapps\\common\\SlayTheSpire")
    commandLine("cmd", "/c", "java -jar mod-uploader.jar upload -w wlj-mod")
}
