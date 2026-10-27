package org.firstinspires.ftc.teamcode.subsystems.intake

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

class Intake(hardwareMap: HardwareMap) {
    var isRunning = false
    val motor: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "intake")
    init {
        motor.direction = DcMotorSimple.Direction.REVERSE
    }
    fun update(){
        if (isRunning) {
            motor.power = (0.4 * 2300 - motor.velocity) * 0.001 + 0.4
        } else {
            motor.power = 0.0
        }
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