package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.opmodes.commands.RobotFunctionOpMode
import org.firstinspires.ftc.teamcode.subsystems.drive.DriveVectors.Companion.processTurnDriveStrafe

/**
 * This is an example implementation of a `RobotFunctionOpmode`
 * @see RobotFunctionOpMode
 * **/
@Disabled // tells the driver station to ignore this opmode (it's only here as an example)
@TeleOp // tells the driver station to treat this opmode as a teleop opmode (as opposed to an autonomous)
class ExampleTeleop: RobotFunctionOpMode({ opmode ->
    // this tells the drivetrain to move each update according to the function below
    drive.follow = {
        processTurnDriveStrafe(
            -opmode.gamepad1.right_stick_x.toDouble(),
            -opmode.gamepad1.left_stick_y.toDouble(),
            -opmode.gamepad1.left_stick_x.toDouble(),
        )
    }

    opmode.waitForStart()
    while (opmode.opModeIsActive()){ // loops until the stop button is pressed
        if (opmode.gamepad1.x) {
            telemetry.addLine("x is pressed!")
        } else {
            telemetry.addLine("x is not pressed :(")
        }

        reads.update() // reads sensors
        drive.update() // updates the drivetrain subsystem
        telemetry.update() // sends telemetry to the driver station
    }
})