package org.firstinspires.ftc.teamcode.subsystems.shooter

import android.util.Log
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
        var pollenBlockerOpen = 0.4
        @JvmField
        var pollenBlockerClose = 0.3

        @JvmField
        var turretOffset = 0.053
        @JvmField
        var turretScale = 0.15


    }
    var targetVelocity = 0.0
    val pollenMotor: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "pollenShooter")
    val pollenTurretServo: Servo = hardwareMap.get(Servo::class.java, "pollenTurret")
    val pollenBlockerServo: Servo =hardwareMap.get(Servo::class.java, "pollenBlocker")


    init {
        pollenMotor.direction = DcMotorSimple.Direction.FORWARD
    }

    fun openPollenBlocker() {
        pollenBlockerServo.position = pollenBlockerOpen
    }

    fun closePollenBlocker() {
        pollenBlockerServo.position = pollenBlockerClose
    }

    fun setPollenTurretPos(angle: Double) {
        Log.i("angle",
            (((angle * turretScale + turretOffset)%(2*PI * turretScale)+(2*PI * turretScale))%(2*PI * turretScale)).toString()
        )
        pollenTurretServo.position = ((angle * turretScale + turretOffset)%(2*PI * turretScale)+(2*PI * turretScale))%(2*PI * turretScale)
    }


    fun update() {
        if (pollenMotor.velocity < targetVelocity){
            pollenMotor.power = (targetVelocity - pollenMotor.velocity)*kP + targetVelocity * kF
        }
        else {
            pollenMotor.power = 0.0
        }
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
            telemetry.addData("encoder speed", shooter.pollenMotor.velocity)
            telemetry.update()
            if (gamepad1.a) {
                shooter.openPollenBlocker()
            }
            else {
                shooter.closePollenBlocker()
            }

            if (gamepad1.x) {
                shooter.setPollenTurretPos(PI/2)

            }else if (gamepad1.b) {
                shooter.setPollenTurretPos(-PI/2)
            } else {
                shooter.setPollenTurretPos(0.0)
            }
        }
    }
}