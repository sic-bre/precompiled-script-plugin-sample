package plugins 

println("Configuring precompiled.gradle.kts")

val precompiled by tasks.creating(Task::class) {
	group = "Sample"
	description = "Sample precompiled script plugin task"
	doLast {
		println("Running precompiled script plugin task")
	}
}
val build by tasks.getting(Task::class)
build.dependsOn(precompiled)