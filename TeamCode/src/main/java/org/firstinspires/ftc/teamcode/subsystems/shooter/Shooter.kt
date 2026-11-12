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
import org.firstinspires.ftc.teamcode.subsystems.drive.Localizer
import org.firstinspires.ftc.teamcode.subsystems.drive.pathing.Pose
import org.firstinspires.ftc.teamcode.subsystems.drive.pathing.Vector
import org.firstinspires.ftc.teamcode.subsystems.controlsystems.VoltageCompensatedMotor
import kotlin.math.PI

@Config
class Shooter(hardwareMap: HardwareMap) {
    companion object {
        @JvmField
        var kF = 0.00044
        @JvmField
        var kP = 0.012

        @JvmField
        var pollenBlockerOpen = 0.4
        @JvmField
        var pollenBlockerClose = 0.26

        @JvmField
        var nectarBlockerOpen = 0.4
        @JvmField
        var nectarBlockerClose = 0.26


        @JvmField
        var turretOffsetNectar = 0.1
        @JvmField
        var turretOffsetPollen = 0.854
        @JvmField
        var turretScale = 0.151
    }
    var targetVelocity = 0.0
    val pollenMotor = VoltageCompensatedMotor(hardwareMap.get(DcMotorEx::class.java, "pollenShooter"))
    val pollenTurretServo: Servo = hardwareMap.get(Servo::class.java, "pollenTurret")
    val pollenBlockerServo: Servo =hardwareMap.get(Servo::class.java, "pollenBlocker")
    val nectarMotor = VoltageCompensatedMotor(hardwareMap.get(DcMotorEx::class.java, "nectarShooter"))
    val nectarTurretServo: Servo = hardwareMap.get(Servo::class.java, "nectarTurret")
    val nectarBLockerServo: Servo = hardwareMap.get(Servo::class.java, "nectarBlocker")
    val localizer = Localizer(hardwareMap)

    init {
        pollenMotor.direction = DcMotorSimple.Direction.FORWARD
        nectarMotor.direction = DcMotorSimple.Direction.REVERSE
    }

    fun openPollenBlocker() {
        pollenBlockerServo.position = pollenBlockerOpen
    }

    fun openNectarBlocker() {
        nectarBLockerServo.position = nectarBlockerOpen
    }

    fun closePollenBlocker() {
        pollenBlockerServo.position = pollenBlockerClose
    }

    fun closeNectarBlocker() {
        nectarBLockerServo.position = nectarBlockerClose
    }

    fun setPollenTurretPos(angle: Double) {
        pollenTurretServo.position = (((angle * turretScale + turretOffsetPollen)%(2*PI * turretScale)+(2*PI * turretScale))%(2*PI * turretScale))
    }

    fun setNectarTurretPos(angle: Double) {
        nectarTurretServo.position = (((angle * turretScale + turretOffsetNectar)%(2*PI * turretScale)+(2*PI * turretScale))%(2*PI * turretScale))
    }

    fun update() {
        pollenMotor.power = (targetVelocity - pollenMotor.velocity)*kP + targetVelocity * kF
//      nectarMotor.power = (targetVelocity - nectarMotor.velocity)*kP + targetVelocity * kF
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
            if (gamepad1.y) {
                shooter.openNectarBlocker()
            }
            else {
                shooter.closeNectarBlocker()
            }

            if (gamepad1.x) {
                shooter.setPollenTurretPos(PI/2)

            }else if (gamepad1.b) {
                shooter.setPollenTurretPos(-PI/2)
            } else {
                shooter.setPollenTurretPos(0.0)
            }
            if (gamepad1.dpad_down) {
                shooter.setNectarTurretPos(PI/2)
            } else if (gamepad1.dpad_up) {
                shooter.setNectarTurretPos(-PI/2)
            } else {
                shooter.setNectarTurretPos(0.0)
            }
        }
    }
}