plugins {
    alias(libs.plugins.taixu.android.library.compose)
}

android {
    namespace = "top.wkbin.taixu.feature.components"
    testOptions.unitTests.isIncludeAndroidResources = true
    defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":feature:theme"))
    implementation(libs.bundles.compose.ui)
    implementation(libs.androidx.activity.compose)
    implementation(libs.kotlinx.coroutines.core)
    // 澄明(液态玻璃)主题：底部导航毛玻璃折射
    implementation(libs.backdrop)
    implementation(libs.cupertino)
    implementation(libs.bundles.coil)
    testImplementation(libs.bundles.test.robolectric)
    testImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}
