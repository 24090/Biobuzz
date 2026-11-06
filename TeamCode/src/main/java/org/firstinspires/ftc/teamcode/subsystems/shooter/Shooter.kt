package org.firstinspires.ftc.teamcode.subsystems.shooter

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import kotlin.math.PI

@Config
class Shooter(hardwareMap: HardwareMap) {
    companion object {
        @JvmField
        var kF = 0.0003
        @JvmField
        var kP = 0.02

        @JvmField
        var blockerOpen = 0.0
        @JvmField
        var blockerClose = 0.1

        @JvmField
        var turretOffset = 0.0
        @JvmField
        var turretScale = 5.0/6.0 * 2 * PI


    }
    var targetVelocity = 0.0
    val motor: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "pollenShooter")
    val turretServo: Servo = hardwareMap.get(Servo::class.java, "rotateTurret")
    val blockerServo: Servo =hardwareMap.get(Servo::class.java, "pollenBlocker")

    init {
        motor.direction = DcMotorSimple.Direction.REVERSE
    }

    fun openBlocker() {
        blockerServo.position =blockerOpen
    }

    fun closeBlocker() {
        blockerServo.position =blockerClose
    }

    fun setTurretPos(angle: Double) {
        turretServo.position = ((angle * turretScale + turretOffset)%(2*PI/turretScale)+(2*PI/turretScale))%(2*PI/turretScale)
    }


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
            if (gamepad1.a) {
                shooter.openBlocker()
            }
            else {
                shooter.closeBlocker()
            }

            if (gamepad1.b) {
                shooter.setTurretPos(PI/2)
            }
            if (gamepad1.x) {
                shooter.setTurretPos(3*PI/2)
            }
        }
    }
}