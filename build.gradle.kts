plugins {
    id("java")
}

group = "com.github.paopaoyue"
version = "1.0.2-SNAPSHOT"

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

tasks.register<Copy>("deployToMODFolder") {
    dependsOn(tasks.clean, tasks.jar)
    from(layout.buildDirectory.dir("libs"))
    include("*.jar")
    into(layout.buildDirectory.dir("E:\\Steam Games\\steamapps\\common\\SlayTheSpire\\mods"))
}
