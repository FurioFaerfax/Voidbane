rootProject.name = "com.github.furiofaerfax.voidbane"

plugins {
    // See documentation on https://scaffoldit.dev
    id("dev.scaffoldit") version "0.2.+"
}

// Would you like to do a split project?
// Create a folder named "common", then configure details with `common { }`

hytale {
    usePatchline("release")
    useVersion("latest")

    repositories {
        // Any external repositories besides: MavenLocal, MavenCentral, HytaleMaven, and CurseMaven
    }

    dependencies {
    }

    manifest {
        Group = "FurioFaerfax"
        Name = "Voidbane"
        Main = "com.github.furiofaerfax.voidbane.Voidbane"
    }
}