plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.assignment_6"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.assignment_6"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    //Additional dependencies
    implementation("com.squareup.picasso:picasso:2.8") //Picasso - Images 2.71828
    implementation("com.android.volley:volley:1.2.1") //Volley - JSON parsing 1.1.1
    implementation("org.json:json:20211205") // Json parsing
    implementation("com.google.android.gms:play-services-location:21.0.1") //Location
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0") //Graph

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}