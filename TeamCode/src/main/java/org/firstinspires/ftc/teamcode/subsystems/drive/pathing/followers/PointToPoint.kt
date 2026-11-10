package org.firstinspires.ftc.teamcode.subsystems.drive.pathing.followers

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.teamcode.subsystems.controlsystems.PDLT
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive.DriveConstants.hD
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive.DriveConstants.hP
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive.DriveConstants.hS
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive.DriveConstants.hT
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive.DriveConstants.kS
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive.DriveConstants.xyD
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive.DriveConstants.xyP
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive.DriveConstants.xyT
import org.firstinspires.ftc.teamcode.subsystems.drive.DriveVectors
import org.firstinspires.ftc.teamcode.subsystems.drive.DriveVectors.Companion.processTurnDriveStrafe
import org.firstinspires.ftc.teamcode.subsystems.drive.Localizer
import org.firstinspires.ftc.teamcode.subsystems.drive.pathing.Pose
import org.firstinspires.ftc.teamcode.subsystems.drive.pathing.Vector
import org.firstinspires.ftc.teamcode.subsystems.drive.pathing.getRelativePose
import org.firstinspires.ftc.teamcode.subsystems.drive.pathing.getRelativeVelocity
import org.firstinspires.ftc.teamcode.util.Reference


fun getPointToPoint(targetPose: Reference<Pose>): Drive.() -> DriveVectors = { pointToPoint(targetPose.get(), localizer) }
fun pointToPoint(targetPose: Pose, localizer: Localizer, full: Boolean = true): DriveVectors {
    val error = getRelativePose(localizer.pose, targetPose)
    val dError = -getRelativeVelocity(localizer.pose, localizer.poseVel)
    val turn = PDLT(AngleUnit.normalizeRadians(error.heading), dError.heading, hP, hD, hS, hT)
    val translational =
        PDLT(Vector.fromPose(error), Vector.fromPose(dError), xyP, xyD, kS, xyT)
    val drive = if (translational.x.isNaN()) 0.0 else translational.x
    val strafe = if (translational.y.isNaN()) 0.0 else translational.y
    return if (!full) {
        DriveVectors.fromRotation(turn) + DriveVectors.fromTranslation(drive, strafe)
    } else {
        processTurnDriveStrafe(turn, drive, strafe)
    }
}