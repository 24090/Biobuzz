package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import org.firstinspires.ftc.teamcode.opmodes.commands.RobotFunctionOpMode
import org.firstinspires.ftc.teamcode.subsystems.drive.pathing.followers.TeleopFollower
import org.firstinspires.ftc.teamcode.subsystems.intake.FlowerIntake
import org.firstinspires.ftc.teamcode.subsystems.intake.IntakeMode
import org.firstinspires.ftc.teamcode.subsystems.intake.TransferMode
import org.firstinspires.ftc.teamcode.util.timeSeconds
import kotlin.math.abs

/**
 * This is an example implementation of a `RobotFunctionOpmode`
 * @see RobotFunctionOpMode
 * **/
@TeleOp // tells the driver station to treat this opmode as a teleop opmode (as opposed to an autonomous)
class ExampleTeleop: RobotFunctionOpMode({ opmode ->
    // this tells the drivetrain to move each update according to the function below
    val follower = TeleopFollower(opmode.gamepad1, opmode.gamepad2, red)
    drive.followFunction = follower.function
    opmode.waitForStart()
    val exhub = opmode.hardwareMap.get(LynxModule::class.java, "Expansion Hub 2")
    val chub = opmode.hardwareMap.get(LynxModule::class.java, "Control Hub")
    val flowerIntake = FlowerIntake(opmode.hardwareMap)

    var timer = timeSeconds()
    while (opmode.opModeIsActive()){ // loops until the stop button is pressed
        if (opmode.gamepad1.dpadUpWasPressed()) {
            intake.mode = IntakeMode.Running
        } else if (opmode.gamepad1.dpadDownWasPressed()) {
            intake.mode = IntakeMode.Reverse
        } else if (opmode.gamepad1.dpadLeftWasPressed()){
            intake.mode = IntakeMode.Stopped
        }
        if (opmode.gamepad1.left_trigger>0.5){
            flowerIntake.flowerCollectDown()
        } else {
            flowerIntake.flowerCollectUp()
        }


        if (opmode.gamepad1.right_trigger > 0.5) {
            if (abs(shooter.pollenMotor.velocity - shooter.targetVelocityPollen) <= 40.0){
                transfer.mode = TransferMode.FullPower
                shooter.openPollenBlocker()
            }
            if (abs(shooter.nectarMotor.velocity - shooter.targetVelocityNectar) <= 40.0){
                shooter.openNectarBlocker()
                transfer.mode = TransferMode.FullPower
            }
        } else {
            transfer.mode = TransferMode.HalfPower
            shooter.closePollenBlocker()
            shooter.closeNectarBlocker()
        }

        reads.update() // reads sensors
        drive.update() // updates the drivetrain subsystem
        shooter.update()
        trackHive()
        setTargetVelocitiesFromDistance()
        intake.update()
        transfer.update()
        telemetry.addData("loop time (dms)", ((timeSeconds() - timer)*10000).toInt())
        timer = timeSeconds()
        telemetry.addData("pose", drive.localizer.pose)
        telemetry.addLine("-------AMPERAGE-------")
        telemetry.addData("total exhub", exhub.getCurrent(CurrentUnit.AMPS))
        telemetry.addData("total chub", chub.getCurrent(CurrentUnit.AMPS))

        telemetry.addData("transfer", transfer.motor.getCurrent(CurrentUnit.AMPS))
        telemetry.addData("intake", intake.motor.getCurrent(CurrentUnit.AMPS))
        telemetry.addLine("drivetrain:")
        telemetry.addData("\tfl", drive.flMotor.getCurrent(CurrentUnit.AMPS))
        telemetry.addData("\tfr", drive.frMotor.getCurrent(CurrentUnit.AMPS))
        telemetry.addData("\tbl", drive.blMotor.getCurrent(CurrentUnit.AMPS))
        telemetry.addData("\tbr", drive.brMotor.getCurrent(CurrentUnit.AMPS))
        telemetry.addLine("shooter:")
        telemetry.addData("\tpollen", shooter.pollenMotor.getCurrent(CurrentUnit.AMPS))
        telemetry.addData("\tnectar", shooter.nectarMotor.getCurrent(CurrentUnit.AMPS))
        telemetry.update() // sends telemetry to the driver station
    }
})