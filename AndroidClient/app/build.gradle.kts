import java.util.Properties



plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21"
}

android {
    namespace = "com.example.jambubble_client"
    compileSdk = 36

    buildFeatures {
        compose = true
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.example.jambubble_client"
        minSdk = 33
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            properties.load(localPropertiesFile.inputStream())
        }

        val keys = listOf(
            "INIT_LOGIN_EMAIL_SPATH",
            "INIT_LOGIN_PASS_SPATH",
            "BASE_URL",
            "ACCESS_TOKEN",
            "SPOTIFY_CLIENT_ID",
            "SPOTIFY_ACCESS_TOKEN",
            "SPOTIFY_REFRESH_TOKEN"
        )

        keys.forEach { key ->
            val value = properties.getProperty(key) ?: ""
            buildConfigField("String", key, "\"$value\"")
        }

        manifestPlaceholders["redirectSchemeName"] = "jambubble"
        manifestPlaceholders["redirectHostName"] = "callback"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )


        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.ui.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    val composeBom = platform("androidx.compose:compose-bom:2025.01.00")
    implementation(composeBom)

    implementation(libs.ui)
    implementation(libs.material3)
    implementation(libs.ui.tooling.preview)

    debugImplementation(libs.ui.tooling)
    implementation(libs.androidx.activity.compose)

    implementation(libs.coil.compose)

    // Retrofit & OkHttp (マルチパート対応に必要)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.security.crypto)

    // DataStore & Serialization
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.serialization.json)


    //Spotify
    implementation(files("libs/spotify-app-remote-release-0.8.0.aar"))
    implementation(files("libs/spotify-auth-release-2.1.0.aar"))
    implementation(libs.gson)

    implementation(libs.androidx.browser)

    // CameraX
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

// ML Kit Barcode Scanner
    implementation(libs.barcode.scanning)

    implementation(libs.core)

    implementation(libs.coil.compose.v250)
    //コルーチン
    implementation(libs.kotlinx.coroutines.android.v173)
    //SignalR
    implementation(libs.signalr)

}