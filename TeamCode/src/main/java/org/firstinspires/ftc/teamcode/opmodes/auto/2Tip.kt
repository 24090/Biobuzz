package org.firstinspires.ftc.teamcode.opmodes.auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.commands.Forever
import org.firstinspires.ftc.teamcode.commands.*
import org.firstinspires.ftc.teamcode.commands.Race
import org.firstinspires.ftc.teamcode.commands.Sequence
import org.firstinspires.ftc.teamcode.commands.Sleep
import org.firstinspires.ftc.teamcode.opmodes.commands.AutoOpMode
import org.firstinspires.ftc.teamcode.robot.shootPollen
import org.firstinspires.ftc.teamcode.subsystems.drive.pathing.Pose
import org.firstinspires.ftc.teamcode.subsystems.intake.IntakeMode
import org.firstinspires.ftc.teamcode.subsystems.intake.TransferMode
import org.firstinspires.ftc.teamcode.util.fieldSize
import org.firstinspires.ftc.teamcode.util.gardenPose
import org.firstinspires.ftc.teamcode.util.robotLength
import org.firstinspires.ftc.teamcode.util.robotWidth
import org.firstinspires.ftc.teamcode.util.startPoseShooting
import org.firstinspires.ftc.teamcode.util.tileSize
import kotlin.math.PI


@Autonomous(name = "2tip")
class TwoTip : AutoOpMode(true, startPoseShooting, command = { Race(
        Forever {
            reads.update()
        },
        Sequence(
            // setup for shooting
            Instant {
                shooter.closePollenBlocker()
                trackHive()
                shooter.targetVelocityPollen = 1560.0
                intake.mode = IntakeMode.Running
                transfer.mode = TransferMode.HalfPower
            },
            shootPollen(), // actually shoot
            Instant { shooter.targetVelocityPollen = 0.0 }, // post shooting turn off the shooter for energy


            // getting balls from the garden
            drive.goToCircle(gardenPose + Pose(0.0, 12.0, 0.0)), // go right in front of the garden

            Race( // a race command runs all of its subcommands at once and ends when any of its subcommands stop
                Sequence( // sequence does all of its subcommands in order, so this goes to the garden position and then waits two seconds
                    drive.goToCircle(gardenPose),
                    Sleep(2.0)
                ),
                Sleep(4.0) // this just waits four seconds
            ), // the race command is used here in case the bot can't reach the garden position, it will only spend four seconds waiting

            // setup for shooting again
            Instant { shooter.targetVelocityPollen = 1560.0 },
            drive.goToCircle(startPoseShooting),
            shootPollen(), // actually shoot
            Instant { shooter.targetVelocityPollen = 0.0 }, // post shooting turn off the shooter for energy

            drive.goToCircle(15,47, Math.toRadians(180)),
            Instant { flowerIntake.flowerCollectDown() },
            drive.goToCircle(11.5, 47, Math.toRadians(180)),
            Parallel(
                Instant { flowerIntake.flowerCollectUp() },
                drive.goToCircle(46.5, 125, Math.toRadians(90))
            ),
            shootPollen(),
            Instant { flowerIntake.flowerCollectDown() },
            drive.gotToCircle(46.5, 129.5, Math.toRadians(90)),
            shootPollen(),
            Parallel(
                Instant { flowerIntake.flowerCollectUp()},

            ),
            
            // TODO:
            //  - intake the first flower
            //  - shoot into the hive (on the other side this time)
            //  - intake from the second flower
            //  - shoot into the hive again
            //  - make sure parking path works (it will be a little diffent since the robot doesnt need to cross the field)

            // parking path
            drive.goToCircle(startPoseShooting - Pose(36.0, 0.0, 0.0)),
            drive.goToCircle(Pose(robotWidth / 2.0, fieldSize - robotLength/2.0 - 41.0, PI/2.0)),
        ),
        Forever {
            drive.update()
            shooter.update()
            intake.update()
            transfer.update()
        },
    )
})