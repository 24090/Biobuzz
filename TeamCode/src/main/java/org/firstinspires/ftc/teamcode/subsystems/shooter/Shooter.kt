package org.firstinspires.ftc.teamcode.subsystems.shooter

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
@Config
class Shooter(hardwareMap: HardwareMap) {
    companion object {
        @JvmField
        var kF = 0.00043
        @JvmField
        var kP = 0.02
    }
    var targetVelocity = 0.0
    val motor: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "flMotor")

    fun update() {
        if (motor.velocity < targetVelocity){
            motor.power = (targetVelocity - motor.velocity)*kP + targetVelocity * kF
        }
        else {
            motor.power = 0.0
        }
    }

    fun getEncoderSpeed(): Double{
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
            shooter.targetVelocity = TargetVelocity
            shooter.update()
            telemetry.addData("encoder speed", shooter.getEncoderSpeed())
            telemetry.update()
        }
    }

}