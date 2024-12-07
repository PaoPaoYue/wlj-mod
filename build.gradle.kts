plugins {
    id("java")
}

group = "com.github.paopaoyue"
version = "1.3.3"

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
