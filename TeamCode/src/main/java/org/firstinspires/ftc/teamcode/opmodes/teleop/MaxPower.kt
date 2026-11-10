package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit

@Config
@TeleOp
class PowerMotor: LinearOpMode() {
    companion object {
        @JvmField var power = 1.0;

    }
    override fun runOpMode() {
        val motor = hardwareMap.get(DcMotorEx::class.java, "pollenShooter")
        val telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)
        waitForStart()
        while (opModeIsActive()){
            motor.power = power
            telemetry.addData("speed", motor.velocity)
            telemetry.addData("current", motor.getCurrent(CurrentUnit.MILLIAMPS))
            telemetry.update()
        }
    }
}