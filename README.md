# precompiled-script-plugin-sample

It is suggested that you open this project with Intellij IDEA 2018.1.2, Java 1.8, and
Gradle 4.7.  To run just use `gradle --console=plain clean build`.  There are two
custom tasks defined precompiled and standard to demonstrate the differences between
the plugin types.

Example showing two different ways to generate buildSrc plugins using kotlin-dsl.
One uses the new org.gradle.kotlin.dsl.plugins.precompiled.PrecompiledScriptPlugins
feature and a standard kotlin-dsl plugin.

There are two issues with the new PrecompiledScriptPlugins feature when used in a 
buildSrc context:

* When navigating in Intellij IDEA from the top level build.gradle.kts to the
recompiled script plugin in buildSrc/src/main/kotlin/plugins/precompiled.gradle.kts
we navigate the following class that uses reflection to get to the actual plugin
code which is not visible in the IDE.  When using a plugin from a binary repository
we would expect that the plugin source would be in a sources jar.  When running from
the buildSrc project we would expect to navigate to the code that generated the plugin.
However we see:

```
class PrecompiledPlugin : org.gradle.api.Plugin<org.gradle.api.Project> {
       override fun apply(target: org.gradle.api.Project) {
           try {
               Class
                   .forName("plugins.Precompiled_gradle")
                   .getDeclaredConstructor(org.gradle.api.Project::class.java)
                   .newInstance(target)
           } catch (e: java.lang.reflect.InvocationTargetException) {
               throw e.targetException
           }
       }
   }
```

This contrasts with the standard plugin buildSrc/src/main/kotlin/plugins/StandardPlugin.kt
where we navigate directly to the source code that implements the plugin:

```
class StandardPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = target.run {
        println("Configuring StandardPlugin.kt")

        val standard by tasks.creating(Task::class) {
            group = "Sample"
            description = "Sample standard script plugin task"
            doLast {
                println("Running standard script plugin task")
            }
        }
        val build by tasks.getting(Task::class)
        build.dependsOn(standard)
    }
}
```

* The standard plugin does not have access to all the kotlin-dsl.  For example the
generated Kotlin accessors are not available in a standard kotlin-dsl plugin.  It
is unclear what needs to be done so that he the code inside the apply block has
access to the generated accessors.

* The standard plugin requires more per plugin configuration in the
buildSrc/build.gradle.kts file:

```
gradlePlugin {
    (plugins) {
        "StandardPlugin" {
            id = "plugins.standard"
            implementationClass = "plugins.StandardPlugin"
        }
    }
}
```