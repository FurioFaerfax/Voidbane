/**
 * NOTE: This is entirely optional and basics can be done in `settings.gradle.kts`
 */

repositories {
    mavenCentral()
    maven {
        name = "hytale"
        url = uri("https://maven.hytale.com/release") // Or "hytale-pre-release" for pre-release versions
    }
}
dependencies {
    // + here means the latest version, but you can hard-wire any known version!
    implementation("com.hypixel.hytale:Server:+")
}