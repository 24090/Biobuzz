package org.firstinspires.ftc.teamcode.opmodes.utility

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.Utility
import org.firstinspires.ftc.teamcode.subsystems.intake.Intake
import org.firstinspires.ftc.teamcode.subsystems.intake.Transfer
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
        intake.isRunning = true
        waitForStart()
        while (opModeIsActive()){
            intake.update()
            transfer.update()
            shooter.update()
            shooter.targetVelocity = targetVelocity
            telemetry.addData("shooter vel", shooter.pollenMotor.velocity)
            telemetry.update()
        }
    }

}