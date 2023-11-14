plugins {
    id("com.android.application")
    id("org.jetbrains.compose")
    kotlin("multiplatform")
}

kotlin {
    androidTarget()

    jvmToolchain(17)

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Use an Amper Module as a dependency
                implementation(project(":shared"))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation("androidx.activity:activity-compose:1.8.0")
                implementation("androidx.appcompat:appcompat:1.6.1")
            }
        }
    }
}

android {
    namespace = "org.ton.browser"
    compileSdkVersion = "android-34"
    defaultConfig {
        minSdkPreview = "21"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
        }
    }
}