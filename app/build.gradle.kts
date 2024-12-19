plugins {
    id("com.android.application")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.android.marvel"
    compileSdk = 34

    defaultConfig {
        applicationId = namespace
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        val baseMarvelUrl = "\"https://gateway.marvel.com/\""
        val localTs = "\"1\""
        val publicApiKey = "\"7e31776f7168945879071a4a3a851493\""
        val privateApiKey = "\"a3c2af3c86160c32d9184026643745cfe192d747\""

        getByName("debug") {
            isMinifyEnabled = false
            isDebuggable = true
            buildConfigField("String", "BASE_URL_MARVEL", baseMarvelUrl)
            buildConfigField("String", "LOCAL_TS", localTs)
            buildConfigField("String", "PUBLIC_API_KEY", publicApiKey)
            buildConfigField("String", "PRIVATE_API_KEY", privateApiKey)
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("String", "BASE_URL_MARVEL", baseMarvelUrl)
            buildConfigField("String", "LOCAL_TS", localTs)
            buildConfigField("String", "PUBLIC_API_KEY", publicApiKey)
            buildConfigField("String", "PRIVATE_API_KEY", privateApiKey)
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
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    val lifecycleVersion = "2.8.7"
    val retrofitVersion = "2.9.0"
    val moshiVersion = "1.15.1"

    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.activity:activity-ktx:1.9.3")
    implementation("androidx.fragment:fragment-ktx:1.8.5")
    implementation("androidx.navigation:navigation-fragment-ktx:${rootProject.ext["navVersion"]}")
    implementation("androidx.navigation:navigation-ui-ktx:${rootProject.ext["navVersion"]}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    implementation("com.google.dagger:hilt-android:${rootProject.ext["hiltVersion"]}")
    ksp("com.google.dagger:hilt-compiler:${rootProject.ext["hiltVersion"]}")

    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofitVersion")
    implementation("com.squareup.moshi:moshi-kotlin:$moshiVersion")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    implementation("com.github.bumptech.glide:glide:4.14.2")
    ksp("com.github.bumptech.glide:ksp:4.14.2")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
