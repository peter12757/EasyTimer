
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.eathemeat.easytimer"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.eathemeat.easytimer"
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
        compose =true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.ui.tooling.preview.android)
    implementation(libs.navigation.compose)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.material3.adaptive.navigation.suite.android)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.ui.tooling.preview)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui.ui)
    implementation(libs.androidx.compose.ui.ui.graphics)
    implementation(libs.androidx.compose.bom)
    implementation(libs.constraintlayout.compose)
    androidTestImplementation(libs.androidx.compose.bom)
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}