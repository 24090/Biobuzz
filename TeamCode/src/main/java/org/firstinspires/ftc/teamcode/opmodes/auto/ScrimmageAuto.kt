package org.firstinspires.ftc.teamcode.opmodes.auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.commands.Forever
import org.firstinspires.ftc.teamcode.commands.Instant
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

@Autonomous(name = "partner auto")
class PartnerAutio: AutoOpMode(true, Pose(robotWidth / 2.0, fieldSize - robotLength/2.0, 0.0), {Race(
    Sequence(
        drive.goToCircle(Pose(robotLength / 2.0, fieldSize - robotWidth/2.0 + 12.0, 0.0))
    ),
    Forever {
        drive.update()
    }
)})

@Autonomous(name = "Scrimmage Auto")
class ScrimmageAuto : AutoOpMode(true, startPoseShooting, command = { Race(
        Forever {
            reads.update()
        },
        Sequence(
            Instant {
                shooter.closePollenBlocker()
                trackHive()
                shooter.targetVelocityPollen = 1180.0
                intake.mode = IntakeMode.Running
                transfer.mode = TransferMode.HalfPower
            },
            shootPollen(),
            Instant {
                shooter.targetVelocityPollen = 0.0
                intake.mode = IntakeMode.Running
                transfer.mode = TransferMode.HalfPower
            },
            drive.goToCircle(gardenPose + Pose(0.0, 12.0, 0.0)),
            Race(
                Sequence(
                    drive.goToCircle(gardenPose),
                    Sleep(100.0)
                ),
                Sleep(4.0)
            ),
            Instant {
                shooter.targetVelocityPollen = 1180.0
                intake.mode = IntakeMode.Running
                transfer.mode = TransferMode.HalfPower
            },
            drive.goToCircle(startPoseShooting),
            shootPollen(),
            drive.goToCircle(startPoseShooting - Pose(36.0, 0.0, 0.0)),
            drive.goToCircle(gardenPose + Pose(3.5, 28.0, PI)),
            drive.goToCircle(gardenPose + Pose(3.5, 32.0, PI)),
            drive.goToCircle(gardenPose + Pose(3.5, 28.0, PI)),
            drive.goToCircle(gardenPose + Pose(3.5, 32.0, PI)),
            drive.goToCircle(gardenPose + Pose(3.5, 28.0, PI)),
            drive.goToCircle(gardenPose + Pose(3.5, 32.0, PI)),
            drive.goToCircle(gardenPose + Pose(3.5, 28.0, PI)),
            drive.goToCircle(gardenPose + Pose(3.5, 32.0, PI)),
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