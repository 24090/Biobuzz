package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor

class EncoderTest: LinearOpMode() {
    override fun runOpMode() {
        val encoder = hardwareMap.get(DcMotor::class.java, "flMotor")
        val telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)
        waitForStart()
        while (opModeIsActive()){
            telemetry.addData("encoder", encoder.currentPosition)
            telemetry.update()
        }
    }
}