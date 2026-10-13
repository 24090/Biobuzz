package org.firstinspires.ftc.teamcode.subsystems.intake

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

class Intake(hardwareMap: HardwareMap) {
    val motor: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "intake")
    init {
        motor.direction = DcMotorSimple.Direction.REVERSE
    }
    fun update(){
        motor.power = 1.0
    }
}

@TeleOp
class IntakeTesting(): LinearOpMode() {
    override fun runOpMode() {
        // initialisation
        val intake = Intake(hardwareMap)
        waitForStart()
        // running
        while (opModeIsActive()){
            intake.update()
        }

    }
}