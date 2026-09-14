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

/**
 * This is an example implementation of an `AutoOpMode`
 * @see AutoOpMode
 * **/
open class ExampleAutoOpMode(red: Boolean): AutoOpMode(red, Pose(0.0, 0.0, 0.0), { Race(
    /*
     * the structure of this command is a `Race` between a `Forever` and a `Sequence`
     *
     * a `Race` command ends when it's first child to end does.
     *  Since a `Forever` command never ends, in this case it just ends when the `Sequence` does.
      */
    Sequence( // tells the drivetrain to go to these points in order
        drive.goToCircle(Pose(20.0, 0.0, PI/2)),
        drive.goToCircle(Pose(20.0, 20.0, -PI/2)),
        drive.goToCircle(Pose(0.0, 0.0, 0.0))
    ),
    Forever { // updates the sensors and motors so that stuff actually happens
        reads.update()
        drive.update()
        telemetry.update()
    }
) })

@Disabled // tells the driver station to ignore this opmode (it's only here as an example)
@Autonomous // tells the driver station to treat this opmode as an autonomous opmode (as opposed to a driver controlled opmode)
class ExampleAutoRed: ExampleAutoOpMode(red = true) // creates the opmode above for red alliance

@Disabled // tells the driver station to ignore this opmode (it's only here as an example)
@Autonomous // tells the driver station to treat this opmode as an autonomous opmode (as opposed to a driver controlled opmode)
class ExampleAutoBlue: ExampleAutoOpMode(red = false) // creates the opmode above for blue alliance