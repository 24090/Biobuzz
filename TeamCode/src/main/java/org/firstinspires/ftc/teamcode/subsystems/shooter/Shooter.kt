package org.firstinspires.ftc.teamcode.subsystems.shooter

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
@Config
class Shooter(hardwareMap: HardwareMap) {
    companion object {
        @JvmField
        var KF = 0.00043
        @JvmField
        var KP = 0.02
    }
    var TargetVelocity = 0.0
    val motor: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "flMotor")

    fun update() {
        if (motor.velocity < TargetVelocity){
            motor.power = (TargetVelocity - motor.velocity)*KP + TargetVelocity * KF
        }
        else {
            motor.power = 0.0
        }
    }

    fun GetEncoderSpeed(): Double{
        return motor.velocity
    }
}
@TeleOp
@Config
class ShooterTesting: LinearOpMode() {
    companion object {
        @JvmField var TargetVelocity = 0.0
    }
    override fun runOpMode() {
        val telemetry: MultipleTelemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)
        val shooter = Shooter(hardwareMap)
        waitForStart()
        shooter.update()
        while (opModeIsActive()) {
            shooter.TargetVelocity = TargetVelocity
            shooter.update()
            telemetry.addData("encoder speed", "shooter.GetEncoderSpeed")
            telemetry.update()
        }
    }

}