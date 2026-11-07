package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.teamcode.opmodes.commands.RobotFunctionOpMode
import org.firstinspires.ftc.teamcode.subsystems.controlsystems.PDLT
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive.DriveConstants.hD
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive.DriveConstants.hP
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive.DriveConstants.hS
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive.DriveConstants.hT
import org.firstinspires.ftc.teamcode.subsystems.drive.DriveVectors.Companion.processTurnDriveStrafe
import org.firstinspires.ftc.teamcode.subsystems.drive.pathing.getRelativePose
import org.firstinspires.ftc.teamcode.subsystems.drive.pathing.getRelativeVelocity
import kotlin.math.PI
import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter
import org.firstinspires.ftc.teamcode.subsystems.shooter.ShooterTesting.Companion.TargetVelocity
import java.util.Optional

/**
 * This is an example implementation of a `RobotFunctionOpmode`
 * @see RobotFunctionOpMode
 * **/
 // tells the driver station to ignore this opmode (it's only here as an example)
@TeleOp // tells the driver station to treat this opmode as a teleop opmode (as opposed to an autonomous)
class ExampleTeleop: RobotFunctionOpMode({ opmode ->
    // this tells the drivetrain to move each update according to the function below
    var headingLock: Optional<Double> = Optional.empty<Double>();
    drive.follow = {
        if (opmode.gamepad1.right_stick_x.toDouble() == 0.0 && (drive.localizer.headingVel == 0.0)){
            if (!headingLock.isPresent) {
                headingLock = Optional.of(drive.localizer.heading)
            }
        }
        processTurnDriveStrafe(
            if (!headingLock.isPresent) {
                -opmode.gamepad1.right_stick_x.toDouble() * 1.6
            } else {
                val error = AngleUnit.normalizeRadians(headingLock.get() - drive.localizer.heading)
                val dError = -drive.localizer.headingVel
                PDLT(error, dError, hP, hD, hS, hT)
            },
            -opmode.gamepad1.left_stick_y.toDouble() * 1.6,
            -opmode.gamepad1.left_stick_x.toDouble() * 1.6,
        )
    }
    val shooter = Shooter(opmode.hardwareMap)
    opmode.waitForStart()
    shooter.update()
    while (opmode.opModeIsActive()){ // loops until the stop button is pressed
        shooter.targetVelocity = TargetVelocity
        shooter.update()
        if (opmode.gamepad1.dpadUpWasPressed()) {
            intake.isRunning = true
        } else if (opmode.gamepad1.dpadDownWasPressed()) {
            intake.isRunning = false
        }
        if (opmode.gamepad1.left_bumper) {
            shooter.setPollenTurretPos(PI/2)
        }
        if (opmode.gamepad1.right_bumper) {
            shooter.setPollenTurretPos(3*PI/2)
        }
        if (opmode.gamepad1.right_trigger > 0.5) {
            shooter.targetVelocity = 1300.0
            if (shooter.pollenMotor.velocity > 1280.0){
                shooter.openPollenBlocker()
            }
        } else {
            shooter.targetVelocity = 0.0
            shooter.closePollenBlocker()
        }

        reads.update() // reads sensors
        drive.update() // updates the drivetrain subsystem
        shooter.update()
        intake.update()
        transfer.update()
        telemetry.update() // sends telemetry to the driver station
    }


})