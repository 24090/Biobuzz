package org.firstinspires.ftc.teamcode.opmodes.commands

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.teamcode.commands.Command
import org.firstinspires.ftc.teamcode.commands.runBlocking
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive
import org.firstinspires.ftc.teamcode.subsystems.drive.pathing.Pose
import org.firstinspires.ftc.teamcode.subsystems.reads.Reads
import org.firstinspires.ftc.teamcode.util.Reference
import org.firstinspires.ftc.teamcode.util.storedRed

open class Robot(opMode: OpMode) {
    var red
        get() = storedRed.get()
        set(v) = storedRed.set(v)

    val telemetry = MultipleTelemetry(opMode.telemetry, FtcDashboard.getInstance().telemetry)

    val reads = Reads(opMode.hardwareMap)
    val drive = Drive(opMode.hardwareMap)

    var time = System.currentTimeMillis()
    fun recordTime(name: String) {
        val newTime = System.currentTimeMillis()
        telemetry.addData("$name (ms)", newTime - time)
        time = newTime
    }
}

/**
 * An opmode which fully initializes the robot and then runs a command
 * @param red the color of the alliance
 * @param startPose the pose to initialize the robot in
 * @param command an extension function on the `Robot` class, which should return a command
 * **/
open class AutoOpMode(val red: Boolean, val startPose: Pose, val command: Robot.() -> Command) : RobotFunctionOpMode( { opmode ->
    storedRed = Reference(red)
    val robot = Robot(opmode)
    val command = robot.command()
    while (opmode.opModeInInit()){
        telemetry.update()
    }

    robot.drive.localizer.pose = startPose.mirroredIf(storedRed.get())

    opmode.waitForStart()

    if (opmode.opModeIsActive()){
        robot.drive.startP2PWithTargetPose(startPose)
        runBlocking(command)
    }


})

/**
 * An opmode which runs a function which has access to an initialized robot and opmode
 * @param function an extension function on the `Robot` class, which takes in a `LinearOpMode`
 *  **/
open class RobotFunctionOpMode(val function: Robot.(opmode: LinearOpMode) -> Unit): LinearOpMode(){
    override fun runOpMode() {
        val robot = Robot(this)
        robot.function(this)
    }
}
