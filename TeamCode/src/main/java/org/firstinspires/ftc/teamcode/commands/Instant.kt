package org.firstinspires.ftc.teamcode.commands

/**
 * A Command which runs instantly
 *
 * This command does not even take up a loop, since the command runner knows to automatically run another loop when all commands finish
 * @param f the child generator
 */
class Instant(val f: () -> Unit, name: String): Command(name){
    override fun nextInstant() = true
    override fun run(): CommandResult {
        f.invoke()
        return CommandResult.End(Result.success("Instant function ran"))
    }
}

fun Instant(f: () -> Unit) = Instant(f, "Instant")