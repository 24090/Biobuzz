package org.firstinspires.ftc.teamcode.subsystems.intake

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.subsystems.controlsystems.VoltageCompensatedMotor

class Intake(hardwareMap: HardwareMap) {
    var isRunning = false
    val motor = VoltageCompensatedMotor(hardwareMap.get(DcMotorEx::class.java, "intake"), cachingTolerance = 0.02)
    init {
        motor.direction = DcMotorSimple.Direction.REVERSE
    }
    fun update(){
        if (isRunning) {
            motor.power = (800 - motor.velocity) * 0.0008 + 0.35
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
        intake.isRunning = true
        waitForStart()
        // running
        while (opModeIsActive()){
            intake.update()
            telemetry.addData("velocity", intake.motor.velocity)
            telemetry.update()
        }

    }
}