package org.firstinspires.ftc.teamcode.opmodes.utility

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.subsystems.intake.Intake
import org.firstinspires.ftc.teamcode.subsystems.intake.Transfer

@TeleOp
class TransferAndIntakeTesting(): LinearOpMode() {
    override fun runOpMode() {
        val intake = Intake(hardwareMap)
        val transfer = Transfer(hardwareMap)
        waitForStart()
        while (opModeIsActive()){
            intake.update()
            transfer.update()
        }
    }
}