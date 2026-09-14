package org.firstinspires.ftc.teamcode.commands

/**
 * A command which waits until a condition turns true
 * @param f the condition which ends the command once it's true
 */
class WaitUntil(val f: () -> Boolean, name: String = "WaitUntil"): OverrideButtonCommand(name) {
    override fun nextInstant() = f()
    override fun getButtons(): ArrayList<Pair<Int, String>> {
        val buttons = super.getButtons()
        return buttons
    }
    override fun run(): CommandResult {
        return if (f()) {
            CommandResult.End(Result.success("End condition satisfied"))
        } else {
            CommandResult.Continue
        }
    }
}
fun WaitUntil(f: () -> Boolean) = WaitUntil(f, "WaitUntil")