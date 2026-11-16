package org.firstinspires.ftc.teamcode.util

import org.firstinspires.ftc.teamcode.subsystems.drive.pathing.Pose
import org.firstinspires.ftc.teamcode.subsystems.drive.pathing.Vector
import kotlin.math.PI

val startPoseShooting = Pose(2*tileSize + robotWidth / 2.0, robotLength/2.0, PI/2.0)
val gardenPose = Pose(robotWidth / 2.0 + 1.0, robotLength / 2.0 + 4.0, -PI/2.0)
val hiveBasePosition = Vector.fromCartesian(57.91, 54.88)