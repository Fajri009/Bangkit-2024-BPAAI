plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    alias(libs.plugins.googleAndroidLibrariesMapsplatformSecretsGradlePlugin)
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

        buildConfigField("String", "TESTING_API_KEY", "\"db874e166f4c473e9132d19a45135274\"")
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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    testOptions {
        animationsDisabled = true
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
    implementation(libs.play.services.maps)
    implementation(libs.androidx.junit.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Ktx Android
    implementation("androidx.core:core-ktx:1.0.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.activity:activity-ktx:1.7.2")
    implementation("androidx.fragment:fragment-ktx:1.7.1")
    implementation("androidx.lifecycle:lifecycle-extensions:2.0.0")

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

    // Google Play Service Location (Fused Location) / Location Tracker
    implementation("com.google.android.gms:play-services-maps:18.0.0")
    implementation("com.google.android.gms:play-services-location:18.0.0")

    // Geofencing
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // Room
    val roomVersion = "2.5.2"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // Desugaring
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    // Mockito
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)

    //special testing
    testImplementation(libs.androidx.core.testing) // InstantTaskExecutorRule
    testImplementation(libs.kotlinx.coroutines.test) //TestCoroutineDispatcher

    //special instrumentation testing
    androidTestImplementation(libs.androidx.core.testing) // InstantTaskExecutorRule
    androidTestImplementation(libs.kotlinx.coroutines.test) //TestDispatcher

    //TestCoroutineDispatcher
    debugImplementation(libs.androidx.fragment.testing) //launchFragmentInContainer

    //mock web server (server tiruan)
    androidTestImplementation(libs.mockwebserver)
    androidTestImplementation(libs.okhttp3.okhttp.tls)

    androidTestImplementation(libs.androidx.espresso.contrib) //RecyclerViewActions

    // Idling Resource
    implementation(libs.androidx.espresso.idling.resource)

    androidTestImplementation(libs.espresso.intents) //IntentsTestRule
}