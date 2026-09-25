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
class FirstAuto (red: Boolean): AutoOpMode(red, Pose(59.350637522768665, 8.773224043715848, Math.toRadians(0.0)), { Race(
//gotocircle means that it will go to a certain area. NOT the name of the path
    Sequence( // tells the drivetrain to go to these points in order
        drive.goToCircle(Pose(27.132969034608386, 21.30874316939891, Math.toRadians(0.0))), // move and shoot
        drive.goToCircle(Pose(8.505464480874313, 12.162169854280526, Math.toRadians(180.0))), //prepare to collect other balls
        drive.goToCircle(Pose(8.440801457194901, 8.080145719489995, Math.toRadians(180.0))), //collect other balls
        drive.goToCircle(Pose(42.98907103825137, 121.0009107468124,Math.toRadians(270.0))), //go to other side, shoot and prepare for flower collection
        drive.goToCircle(Pose(46.18488160291437, 128.37340619307832, Math.toRadians(0.0))) //collect flower
    ), // might have messed up the heading
    Forever { // updates the sensors and motors so that stuff actually happens
        reads.update()
        drive.update()
        telemetry.update()
    }// obv this is all just speculation. i dont know if theres going to be a method for collecting flowers, if its going to be a turret etc.
)})// this is just for learning purposes and is not final

class ExampleAutoRed: ExampleAutoOpMode(red = true) // creates the opmode above for red alliance