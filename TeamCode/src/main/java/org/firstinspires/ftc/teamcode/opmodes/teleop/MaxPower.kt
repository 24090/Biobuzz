package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor

@Config
@TeleOp
class PowerMotor: LinearOpMode() {
    companion object {
        @JvmField var power = 1.0;

    }
    override fun runOpMode() {
        val motor = hardwareMap.get(DcMotor::class.java, "flMotor")
        waitForStart()
        while (opModeIsActive()){
            motor.power = power
        }
    }
}