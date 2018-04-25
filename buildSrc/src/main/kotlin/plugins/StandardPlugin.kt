package plugins

import org.gradle.api.Task
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*

@Suppress("UNUSED")
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