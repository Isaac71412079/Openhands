plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp") version "2.0.21-1.0.25"
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.openhands"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.openhands"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    buildFeatures {
        compose = true
        viewBinding = true   // ← CORRECCIÓN
    }
}

dependencies {
    // Firebase BOM
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))

    // Firebase KTX
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")

    // Coroutines Firebase
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")

    // AndroidX / Compose
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.material3)

    // Room
    implementation(libs.bundles.local)
    ksp(libs.room.compiler)
    testImplementation(libs.room.testing)

    // Retrofit + Koin
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.navigation)
    implementation(libs.koin.androidx.compose)

    // Otros
    implementation(libs.coil.compose)
    implementation(libs.coil.network)
    implementation(libs.datastore)
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended-android:1.6.8")
    implementation(libs.bundles.camerax)
    implementation(libs.google.accompanist.permissions)
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}