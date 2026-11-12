package org.firstinspires.ftc.teamcode.subsystems.intake

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.subsystems.controlsystems.VoltageCompensatedMotor

enum class IntakeMode{
    Stopped,
    Running,
    Reverse
}
class Intake(hardwareMap: HardwareMap) {
    var mode: IntakeMode = IntakeMode.Stopped
    val motor = VoltageCompensatedMotor(hardwareMap.get(DcMotorEx::class.java, "intake"), cachingTolerance = 0.02)
    init {
        motor.direction = DcMotorSimple.Direction.REVERSE
    }
    fun update(){
        when(mode) {
            IntakeMode.Stopped ->
                motor.power = (0 - motor.velocity) * 0.0008
            IntakeMode.Running ->
                motor.power = (800 - motor.velocity) * 0.0008 + 0.35
            IntakeMode.Reverse ->
                motor.power = (-800 - motor.velocity) * 0.0008 - 0.35
        }
    }
}

@TeleOp
class IntakeTesting(): LinearOpMode() {
    override fun runOpMode() {
        // initialisation
        val intake = Intake(hardwareMap)
        intake.mode = IntakeMode.Running
        waitForStart()
        // running
        while (opModeIsActive()){
            intake.update()
            telemetry.addData("velocity", intake.motor.velocity)
            telemetry.update()
        }

    }
}