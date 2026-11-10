package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.opmodes.commands.RobotFunctionOpMode
import org.firstinspires.ftc.teamcode.subsystems.drive.pathing.followers.TeleopFollower
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D
import org.firstinspires.ftc.teamcode.subsystems.drive.pathing.Pose
import org.firstinspires.ftc.teamcode.util.timeSeconds
import kotlin.math.PI

/**
 * This is an example implementation of a `RobotFunctionOpmode`
 * @see RobotFunctionOpMode
 * **/
@TeleOp // tells the driver station to treat this opmode as a teleop opmode (as opposed to an autonomous)
class ExampleTeleop: RobotFunctionOpMode({ opmode ->
    // this tells the drivetrain to move each update according to the function below
    val follower = TeleopFollower(opmode.gamepad1, red)
    drive.followFunction = follower.function
    opmode.waitForStart()
    drive.localizer.pose = Pose(Pose2D(DistanceUnit.INCH, 64.65, 5.9, AngleUnit.RADIANS, 0.0))
    var timer = timeSeconds()
    while (opmode.opModeIsActive()){ // loops until the stop button is pressed
        if (opmode.gamepad1.dpadUpWasPressed()) {
            intake.isRunning = true
        } else if (opmode.gamepad1.dpadDownWasPressed()) {
            intake.isRunning = false
        }
        if (opmode.gamepad1.dpadLeftWasPressed()){
            intake.isRunningBack = true
        } else if (opmode.gamepad1.dpadRightWasPressed()) {
            intake.isRunningBack = false
        }
        if (opmode.gamepad1.left_bumper) {
            shooter.setPollenTurretPos(0.0)
        } else if (opmode.gamepad1.right_bumper) {
            shooter.setPollenTurretPos(PI)
        }
        if (opmode.gamepad1.right_trigger > 0.5) {
            shooter.targetVelocity = 1140.0
            if (shooter.pollenMotor.velocity >= 1120.0){
                shooter.openPollenBlocker()
            }
        } else {
            shooter.targetVelocity = 0.0
            shooter.closePollenBlocker()
        }

        reads.update() // reads sensors
        drive.update() // updates the drivetrain subsystem
        shooter.update()
        shooter.trackHive()
        intake.update()
        transfer.update()

        telemetry.addData("heading lock", follower.headingLock)
        telemetry.addData("loop time", ((timeSeconds() - timer)*1000).toInt())
        timer = timeSeconds()
        telemetry.update() // sends telemetry to the driver station
    }
})