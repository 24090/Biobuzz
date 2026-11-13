package org.firstinspires.ftc.teamcode.opmodes.utility

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.Utility
import org.firstinspires.ftc.teamcode.subsystems.intake.Intake
import org.firstinspires.ftc.teamcode.subsystems.intake.IntakeMode
import org.firstinspires.ftc.teamcode.subsystems.intake.Transfer
import org.firstinspires.ftc.teamcode.subsystems.intake.TransferMode
import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter

@Utility
@Config
class FullShootTesting: LinearOpMode() {
    companion object {
        @JvmField var targetVelocity = 0.0
    }
    override fun runOpMode() {
        val telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)
        val intake = Intake(hardwareMap)
        val transfer = Transfer(hardwareMap)
        val shooter = Shooter(hardwareMap)
        intake.mode = IntakeMode.Running
        transfer.mode = TransferMode.HalfPower
        shooter.setPollenTurretPos(0.0)
        shooter.setNectarTurretPos(0.0)
        shooter.closeNectarBlocker()
        shooter.closePollenBlocker()
        waitForStart()
        while (opModeIsActive()){
            intake.update()
            transfer.update()
            shooter.update()
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
            telemetry.update()
        }
    }

}