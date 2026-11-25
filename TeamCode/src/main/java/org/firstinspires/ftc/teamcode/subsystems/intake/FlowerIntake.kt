package org.firstinspires.ftc.teamcode.subsystems.intake

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import java.util.concurrent.Flow

@Config
class FlowerIntake(hardwareMap: HardwareMap) {
    companion object {
        @JvmField
        var flowerUp = 0.38  //temp number

        @JvmField
        var flowerCollect = 0.70 //temp number
    }

    val flowerServo: Servo = hardwareMap.get(Servo::class.java, "flowerIntake")

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
        val flowerIntake = FlowerIntake(hardwareMap)
        waitForStart()
        while (opModeIsActive()) {
            if (gamepad1.a) {
                flowerIntake.flowerCollectUp()
            }
            else {
                flowerIntake.flowerCollectDown()
            }
        }
    }
}