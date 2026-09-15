package org.firstinspires.ftc.teamcode.opmodes.auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.commands.Forever
import org.firstinspires.ftc.teamcode.commands.Race
import org.firstinspires.ftc.teamcode.commands.Sequence
import org.firstinspires.ftc.teamcode.opmodes.commands.AutoOpMode
import org.firstinspires.ftc.teamcode.subsystems.drive.DriveVectors.Companion.processTurnDriveStrafe
import org.firstinspires.ftc.teamcode.subsystems.drive.pathing.Pose
import kotlin.math.PI

@Disabled
@Autonomous
class FirstAuto (red: Boolean): AutoOpMode(red, Pose(132.7367941712204, 34.58566712204007, Math.toRadians(90.0)), { Race(

    Sequence( // tells the drivetrain to go to these points in order
        drive.goToCircle(Pose(132.29143897996357, 58.16575591985431, Math.toRadians(90.0))), //gotocircle means that it will go to a certain area. NOT the name of the path
        drive.goToCircle(Pose(124.66211293260474, 8.821493624772316, Math.toRadians(270.0))), //prepare to collect other balls
        drive.goToCircle(Pose(133.19763205828778, 9.255009107468126, Math.toRadians(270.0))), //collect other balls
        drive.goToCircle(Pose(132.0646630236794, 58.68032786885246,Math.toRadians(90.0))) //go back and shoot
    ),
    Forever { // updates the sensors and motors so that stuff actually happens
        reads.update()
        drive.update()
        telemetry.update()
    }
)})

class ExampleAutoBlue: ExampleAutoOpMode(red = false) // creates the opmode above for blue alliance