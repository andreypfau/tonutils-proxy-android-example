plugins {
    id("com.android.application")
    id("org.jetbrains.compose")
    kotlin("android")
}

dependencies {
    implementation(compose.foundation)
    implementation(compose.material3)
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.compose.ui:ui-tooling-preview-android:1.5.4")
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.4")
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
    buildFeatures {
        compose = true
    }
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
}
