import org.gradle.kotlin.dsl.implementation
import java.io.File
import java.net.URL
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.openhands"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.openhands"
        minSdk = 24
        targetSdk = 36
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
    }
}

fun getLocalProperty(key: String): String {
    val localProperties = Properties()
    val localPropertiesFile = project.rootProject.file("local.properties")

    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { input ->
            localProperties.load(input)
        }
    } else {
        error("Error: El archivo 'local.properties' no se encuentra en la raíz del proyecto. Asegúrese de crearlo.")
    }
    return localProperties.getProperty(key) ?: error("Error: La clave '$key' no se encuentra en 'local.properties'. Por favor, añádala con su valor.")
}

val localeMapping = mapOf(
    "en-US" to "values",
    "es-ES" to "values-es",
    "es-BO" to "values-es-rBO"
)
tasks.register("downloadLocoStrings") {
    group = "localization"
    description = "Downloads strings.xml files from Localise.biz via API"

    val locoApiKey = getLocalProperty("LOCO_API_KEY")

    val resDir = file("src/main/res")

    doLast {
        localeMapping.forEach { (apiCode, resFolder) ->
            downloadFile(
                apiKey = locoApiKey,
                apiCode = apiCode,
                resFolder = resFolder,
                resDir = resDir
            )
        }
    }
}
fun downloadFile(apiKey: String, apiCode: String, resFolder: String, resDir: File) {
    println("-> Descargando [$apiCode] en $resFolder")

    val outputFile = file("$resDir/$resFolder/strings.xml")

    outputFile.parentFile.mkdirs()

    val exportUrl = "https://localise.biz/api/export/locale/$apiCode.xml?key=$apiKey&format=android"

    try {
        URL(exportUrl).openStream().use { input ->
            outputFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        println("✅ Descarga de $apiCode exitosa en $resFolder/strings.xml.")
    } catch (e: Exception) {
        println("❌ ERROR al descargar $apiCode. Verifique que la clave '$apiCode' tenga contenido en Localise.biz y que la API Key sea correcta: ${e.message}")
    }
}

tasks.named("preBuild").configure {
    dependsOn("downloadLocoStrings")
}


dependencies {
    // --- Firebase (Correct Setup) ---
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-common-ktx") // The key fix

    // --- AndroidX & Compose ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3) // Single Material3 dependency
    implementation(libs.androidx.navigation.compose)
    implementation("androidx.compose.material:material-icons-extended-android:1.6.8")
    implementation(libs.androidx.runtime) // Single runtime dependency

    // --- Dependency Injection (Koin) ---
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.navigation)
    implementation(libs.koin.androidx.compose)

    // --- Networking & Data ---
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.datastore)
    implementation(libs.coil.compose)
    implementation(libs.coil.network)

    // --- Room (Local Database) ---
    implementation(libs.bundles.local)
    implementation(libs.androidx.compose.foundation)
    ksp(libs.room.compiler)
    // annotationProcessor(libs.room.compiler) // You can remove this; KSP is the modern replacement.

    // --- Media & Camera ---
    implementation(libs.bundles.camerax)
    implementation("androidx.media3:media3-exoplayer:1.4.0")
    implementation("androidx.media3:media3-ui:1.4.0")

    // --- Other Utilities ---
    implementation(libs.socketio)
    implementation(libs.google.accompanist.permissions)

    // --- Testing ---
    testImplementation(libs.junit)
    testImplementation(libs.room.testing)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}