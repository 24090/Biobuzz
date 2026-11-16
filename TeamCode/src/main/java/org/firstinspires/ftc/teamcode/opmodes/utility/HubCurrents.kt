package org.firstinspires.ftc.teamcode.opmodes.utility

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import org.firstinspires.ftc.teamcode.opmodes.commands.Robot

@TeleOp
class HubCurrents: LinearOpMode() {
    override fun runOpMode() {
        val robot = Robot(this)
        val telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)
        val exhub = hardwareMap.get(LynxModule::class.java, "Expansion Hub 2")
        val chub = hardwareMap.get(LynxModule::class.java, "Control Hub")
        waitForStart()
        while (opModeIsActive()) {
            telemetry.addData("exhub", exhub.getCurrent(CurrentUnit.MILLIAMPS))
            telemetry.addData("chub", chub.getCurrent(CurrentUnit.MILLIAMPS))
            telemetry.update()
        }
    }
}