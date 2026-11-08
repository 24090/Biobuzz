package org.firstinspires.ftc.teamcode.subsystems.intake

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import org.firstinspires.ftc.teamcode.subsystems.controlsystems.VoltageCompensatedMotor

class Transfer(hardwareMap: HardwareMap) {
    val motor: DcMotorEx = VoltageCompensatedMotor(hardwareMap.get(DcMotorEx::class.java, "transfer"), cachingTolerance = 0.02)
    init {
        motor.direction = DcMotorSimple.Direction.REVERSE
    }
    fun update(){
        motor.power = 1.0
    }
}

@TeleOp
class TransferTesting(): LinearOpMode() {
    override fun runOpMode() {
        val telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)
        // initialisation
        val transfer = Transfer(hardwareMap)
        waitForStart()
        // running
        while (opModeIsActive()){
            transfer.update()
            telemetry.addData("current", transfer.motor.getCurrent(CurrentUnit.AMPS))
            telemetry.update()
        }

    }
}