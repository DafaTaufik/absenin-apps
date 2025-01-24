import org.jetbrains.kotlin.storage.CacheResetOnProcessCanceled.enabled

plugins {
    id("com.google.gms.google-services")
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

}

android {

    namespace = "com.example.if570_lab_uts_dafataufikallatief_67476"
    compileSdk = 34


    defaultConfig {
        applicationId = "com.example.if570_lab_uts_dafataufikallatief_67476"
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
    buildFeatures{
        dataBinding = true
    }
}

dependencies {
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore")
    implementation ("com.google.firebase:firebase-firestore-ktx")
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation ("com.google.firebase:firebase-storage:20.0.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.2")
    implementation("com.github.qamarelsafadi:CurvedBottomNavigation:0.1.3")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.androidx.cardview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}