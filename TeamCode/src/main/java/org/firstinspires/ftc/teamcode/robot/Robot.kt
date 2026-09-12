package org.firstinspires.ftc.teamcode.opmodes.commands

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.commands.Command
import org.firstinspires.ftc.teamcode.commands.runBlocking
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive
import org.firstinspires.ftc.teamcode.subsystems.drive.pathing.Pose
import org.firstinspires.ftc.teamcode.subsystems.reads.Reads
import org.firstinspires.ftc.teamcode.util.Reference
import org.firstinspires.ftc.teamcode.util.storedRed

open class Robot(hwMap: HardwareMap, telemetry: Telemetry) {
    val telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)
    var red
        get() = storedRed.get()
        set(v) = storedRed.set(v)
    val reads = Reads(hwMap)
    val drive = Drive(hwMap)

    var time = System.currentTimeMillis()
    fun recordTime(name: String) {
        val newTime = System.currentTimeMillis()
        telemetry.addData("$name (ms)", newTime - time)
        time = newTime
    }
}

open class Auto(val red: Boolean, val startPose: Pose, val command: Robot.() -> Command) : LinearOpMode(){
    override fun runOpMode() {
        storedRed = Reference(red)
        val robot = Robot(hardwareMap, telemetry)
        val command = robot.command()
        while (opModeInInit()){
            telemetry.update()
        }

        robot.drive.localizer.pose = startPose.mirroredIf(storedRed.get())

        waitForStart()

        if (!opModeIsActive()){
            return
        }

        robot.drive.startP2PWithTargetPose(startPose)
        runBlocking(command)
    }

}

open class Teleop(val function: Robot.(opmode: LinearOpMode) -> Unit): LinearOpMode(){
    override fun runOpMode() {
        val robot = Robot(hardwareMap, telemetry)
        robot.function(this)
    }
}
