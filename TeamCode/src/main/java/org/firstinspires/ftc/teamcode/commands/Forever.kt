package org.firstinspires.ftc.teamcode.commands

/**
 * A Command which generates a new child, runs it until it stops, regenerates the child, and repeats
 * @param f the child generator
 */
class ForeverCommand(f: () -> Command, name: String): RepeatCommandUntil(f, {false}, name)

fun ForeverCommand(f: () -> Command) = ForeverCommand(f, "Forever")
class Forever(val f: () -> Unit, name: String = "Forever"): OverrideButtonCommand(name) {
    override fun run(): CommandResult {
        f.invoke()
        return CommandResult.Continue
    }
}

/**
 * A Command which runs a function forever
 * @param f the function to run
 */
fun Forever(f: () -> Unit) = Forever(f, "Forever")
