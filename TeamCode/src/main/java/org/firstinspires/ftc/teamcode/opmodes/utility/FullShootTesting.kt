package org.firstinspires.ftc.teamcode.opmodes.utility

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.Utility
import org.firstinspires.ftc.teamcode.opmodes.commands.RobotFunctionOpMode
import org.firstinspires.ftc.teamcode.subsystems.intake.Intake
import org.firstinspires.ftc.teamcode.subsystems.intake.IntakeMode
import org.firstinspires.ftc.teamcode.subsystems.intake.Transfer
import org.firstinspires.ftc.teamcode.subsystems.intake.TransferMode
import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter
import org.firstinspires.ftc.teamcode.subsystems.shooter.ShooterTesting.Companion.targetVelocity

@Utility
@Config
class FullShootTesting: RobotFunctionOpMode({opmode ->
    intake.mode = IntakeMode.Running
    transfer.mode = TransferMode.HalfPower
    shooter.closeNectarBlocker()
    shooter.closePollenBlocker()
    opmode.waitForStart()
    while (opmode.opModeIsActive()){
        reads.update()
        intake.update()
        transfer.update()
        shooter.update()
        trackHive()
        if (shooter.pollenMotor.velocity >= targetVelocity - 20.0) {
            shooter.openNectarBlocker()
            transfer.mode = TransferMode.FullPower
        } else {
            shooter.closeNectarBlocker()
            transfer.mode = TransferMode.HalfPower

        }

        if (shooter.nectarMotor.velocity >= targetVelocity - 20.0) {
            shooter.openPollenBlocker()
            transfer.mode = TransferMode.FullPower
        } else {
            shooter.closePollenBlocker()
            transfer.mode = TransferMode.HalfPower
        }
        shooter.targetVelocityPollen = targetVelocity
        shooter.targetVelocityNectar = targetVelocity
        telemetry.addData("pollen vel", shooter.pollenMotor.velocity)
        telemetry.addData("nectar vel", shooter.nectarMotor.velocity)
        telemetry.addData("hive distance pollen", (getHivePosition() - getPollenPosition()).length)
        telemetry.addData("hive distance pollen", (getHivePosition() - getNectarPosition()).length)


        telemetry.update()
    }
})