plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.bangkit_2024_bpaai"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.bangkit_2024_bpaai"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.activity:activity-ktx:1.7.2")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1")

    // ExoPlayer
    // media3-exoplayer: Digunakan sebagai fungsi utama (Core functionality) dan sifatnya wajib  ada (required)
    implementation("androidx.media3:media3-exoplayer:1.1.0")
    // media3-ui: Digunakan untuk menyediakan berbagai komponen UI dan resource yang bisa digunakan oleh ExoPlayer
    implementation("androidx.media3:media3-ui:1.1.0")
    // media3-session: Digunakan untuk membuat dan mengontrol media session
    implementation("androidx.media3:media3-session:1.1.0")

    // CameraX
    val cameraxVersion = "1.2.3"
    implementation("androidx.camera:camera-camera2:$cameraxVersion") // menampilkan kamera dalam aplikasi serta mengambil gambar
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion") // mengatur daur hidup atau lifecycle dari CameraX
    implementation("androidx.camera:camera-view:$cameraxVersion") // sebagai View Camera

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1") // untuk lifecycleScope

    // untuk membaca EXIF
    implementation("androidx.exifinterface:exifinterface:1.3.6")
}