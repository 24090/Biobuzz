package org.firstinspires.ftc.teamcode.subsystems.intake

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.opmodes.commands.Robot

@Config
class flowerIntake(hardwareMap: HardwareMap) {
    companion object {
        @JvmField
        var flowerUp = 1.0 //temp number

        @JvmField
        var flowerCollect = 0.0 //temp number
    }

    val flowerServo: Servo = hardwareMap.get(Servo::class.java, "pollenTurret")

    fun flowerCollectUp() {
        flowerServo.position = flowerUp
    }

    fun flowerCollectDown() {
        flowerServo.position = flowerCollect
    }

}

@TeleOp
@Config
class FlowerTesting: LinearOpMode() {
    override fun runOpMode() {
        while (opModeIsActive()) {
            if (gamepad1.a) {
                flowerIntake.flowerUp
            }
            else {
                flowerIntake.flowerCollect
            }
        }
    }
}