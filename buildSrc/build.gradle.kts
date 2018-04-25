plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

apply<org.gradle.kotlin.dsl.plugins.precompiled.PrecompiledScriptPlugins>()

gradlePlugin {
    (plugins) {
        "StandardPlugin" {
            id = "plugins.standard"
            implementationClass = "plugins.StandardPlugin"
        }
    }
}