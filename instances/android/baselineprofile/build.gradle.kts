import com.android.build.api.dsl.ManagedVirtualDevice
import com.flipperdevices.buildlogic.ApkConfig

plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.baselineprofile)
}

@Suppress("VariableNaming")
val TARGET_APP_ID_KEY = "targetAppId"

android {
    commonAndroid(project)
    namespace = "${ApkConfig.APPLICATION_ID}.baselineprofile"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "TARGET_APP_ID_KEY", "\"${TARGET_APP_ID_KEY}\"")
    }

    targetProjectPath = projects.instances.android.app.identityPath.path

    testOptions.managedDevices.devices {
        create<ManagedVirtualDevice>("pixel7Api34") {
            device = "Pixel 7"
            apiLevel = 34
            systemImageSource = "aosp"
        }
    }
}

baselineProfile {
    managedDevices += "pixel7Api34"
    useConnectedDevices = false
}

dependencies {
    implementation(projects.components.core.log)
    implementation(libs.ktx.testing)
    implementation(libs.espresso.core)
    implementation(libs.uiautomator)
    implementation(libs.benchmark.macro.junit4)
}

androidComponents {
    onVariants { variant ->
        val artifactsLoader = variant.artifacts.getBuiltArtifactsLoader()
        variant.instrumentationRunnerArguments.put(
            TARGET_APP_ID_KEY,
            variant.testedApks.map { dir -> artifactsLoader.load(dir)?.applicationId }
        )
    }
}
