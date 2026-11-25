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
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import org.firstinspires.ftc.teamcode.opmodes.commands.Robot
import org.firstinspires.ftc.teamcode.subsystems.controlsystems.DerivativeCalculator
import org.firstinspires.ftc.teamcode.subsystems.drive.Localizer
import org.firstinspires.ftc.teamcode.subsystems.controlsystems.VoltageCompensatedMotor
import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter.Companion.velocityFromDistance
import org.firstinspires.ftc.teamcode.util.Polynomial
import org.firstinspires.ftc.teamcode.util.timeSeconds
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.sign

@Config
class Shooter(hardwareMap: HardwareMap) {
    companion object {
        @JvmField
        var kS = 0.06

        @JvmField
        var kV = 0.000293

        @JvmField
        var kP = 0.0008

        @JvmField
        var kP2 = 0.000035

        @JvmField
        var pollenBlockerOpen = 0.4
        @JvmField
        var pollenBlockerClose = 0.26

        @JvmField
        var nectarBlockerOpen = 0.85
        @JvmField
        var nectarBlockerClose = 0.94


        @JvmField
        var turretOffsetNectar = 0.21
        @JvmField
        var turretOffsetPollen = 0.71
        @JvmField
        var turretScale = 0.151

        @JvmField
        var velThreshold = 20.0

        @JvmStatic fun velocityFromDistance(distance: Double): Double = Polynomial(1472.6562, -1.71791, 0.0559552).evaluate(distance)
    }
    val pollenVelocityTracker = DerivativeCalculator(10)
    val nectarVelocityTracker = DerivativeCalculator(10)

    fun setTargetVelocities(pollen: Double, nectar: Double) {
        targetVelocityPollen = pollen
        targetVelocityNectar = nectar
    }
    fun setTargetVelocities(velocity: Double) {
        targetVelocityPollen = velocity
        targetVelocityNectar = velocity
    }
    var targetVelocityPollen = 0.0
    var targetVelocityNectar = 0.0

    val pollenMotor = VoltageCompensatedMotor(hardwareMap.get(DcMotorEx::class.java, "pollenShooter"))
    val pollenTurretServo: Servo = hardwareMap.get(Servo::class.java, "pollenTurret")
    val pollenBlockerServo: Servo =hardwareMap.get(Servo::class.java, "pollenBlocker")
    val nectarMotor = VoltageCompensatedMotor(hardwareMap.get(DcMotorEx::class.java, "nectarShooter"))
    val nectarTurretServo: Servo = hardwareMap.get(Servo::class.java, "nectarTurret")
    val nectarBLockerServo: Servo = hardwareMap.get(Servo::class.java, "nectarBlocker")
    val localizer = Localizer(hardwareMap)

    init {
        pollenMotor.direction = DcMotorSimple.Direction.REVERSE
        nectarMotor.direction = DcMotorSimple.Direction.FORWARD
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
    fun turretWrap(angle: Double, turretOffset: Double, servoPosition: Double) =
        (((angle * turretScale + turretOffset)%(2*PI * turretScale)+(2*PI * turretScale))%(2*PI * turretScale)).let {
            if (it +2.0*PI*turretScale <= 1.0 && servoPosition > it + PI*turretScale) {
                it + 2.0*PI*turretScale
            } else if (it-2*PI*turretScale >= 0.0 && servoPosition > it - PI*turretScale) {
                it - 2.0*PI*turretScale
            } else {
                it
            }
        }

    fun setPollenTurretPos(angle: Double) {
        pollenTurretServo.position = turretWrap(
            angle,
            turretOffsetPollen,
            pollenTurretServo.position
        )
    }

    fun setNectarTurretPos(angle: Double) {
        nectarTurretServo.position = turretWrap(
            angle,
            turretOffsetNectar,
            nectarTurretServo.position
        )
    }

    fun update() {
        pollenVelocityTracker.update(timeSeconds(), pollenMotor.currentPosition)
        nectarVelocityTracker.update(timeSeconds(), nectarMotor.currentPosition)

//        val pollenVelocity = (if (pollenVelocityTracker.updateLength() < 0.15) pollenVelocityTracker.get() else null) ?: pollenMotor.velocity
//        val nectarVelocity = (if (nectarVelocityTracker.updateLength() < 0.15) nectarVelocityTracker.get() else null) ?: pollenMotor.velocity

        if (targetVelocityPollen != 0.0) {
            val error = (targetVelocityPollen - pollenMotor.velocity)
            pollenMotor.power = error*error.absoluteValue*kP2 + error*kP + targetVelocityPollen * kV + kS * sign(targetVelocityPollen)
        } else {
            pollenMotor.power = 0.0
        }

        if (targetVelocityNectar != 0.0) {
            val error = (targetVelocityNectar - nectarMotor.velocity)
            nectarMotor.power = error*error.absoluteValue*kP2 + error*kP + targetVelocityNectar * kV + kS * sign(targetVelocityNectar)
        } else {
            nectarMotor.power = 0.0
        }
    }
}

@TeleOp
@Config
class ShooterTesting: LinearOpMode() {
    companion object {
        @JvmField var targetVelocity = velocityFromDistance(48.83)
        @JvmField var targetPos = 0.0

    }
    override fun runOpMode() {
        val telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)
        val robot = Robot(this)
        val shooter = robot.shooter
        val startTime = timeSeconds()
        var endTime = timeSeconds()

        waitForStart()
        shooter.update()
        while (opModeIsActive()) {
            if (shooter.pollenMotor.velocity < shooter.targetVelocityPollen - 20) endTime = timeSeconds()
            targetPos += 0.00
            shooter.targetVelocityPollen = targetVelocity
            shooter.targetVelocityNectar = 0.0
            robot.reads.update()
            shooter.update()

            telemetry.addData("current", shooter.pollenMotor.getCurrent(CurrentUnit.AMPS))
            telemetry.addData("raw speed", shooter.pollenMotor.velocity)
            telemetry.addData("calculated speed", shooter.pollenVelocityTracker.get())
            telemetry.addData("update length", shooter.pollenVelocityTracker.updateLength())
            telemetry.addData("spinup time", endTime - startTime)

            telemetry.update()
            if (gamepad1.a) {
                shooter.closePollenBlocker()
            }
            else {
                shooter.openPollenBlocker()
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
                shooter.setPollenTurretPos(targetPos)
            }
            if (gamepad1.dpad_down) {
                shooter.setNectarTurretPos(PI/2)
            } else if (gamepad1.dpad_up) {
                shooter.setNectarTurretPos(-PI/2)
            } else {
                shooter.setNectarTurretPos(targetPos)
            }
        }
    }
}