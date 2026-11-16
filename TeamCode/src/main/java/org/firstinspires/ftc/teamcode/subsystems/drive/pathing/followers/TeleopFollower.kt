package org.firstinspires.ftc.teamcode.subsystems.drive.pathing.followers

import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.teamcode.subsystems.controlsystems.PDLT
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive.DriveConstants.hD
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive.DriveConstants.hP
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive.DriveConstants.hS
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive.DriveConstants.hT
import org.firstinspires.ftc.teamcode.subsystems.drive.DriveVectors
import org.firstinspires.ftc.teamcode.subsystems.drive.DriveVectors.Companion.processTurnTranslational
import org.firstinspires.ftc.teamcode.subsystems.drive.Localizer
import org.firstinspires.ftc.teamcode.subsystems.drive.pathing.Pose
import org.firstinspires.ftc.teamcode.subsystems.drive.pathing.Vector
import org.firstinspires.ftc.teamcode.subsystems.drive.pathing.getRelativeVelocity
import org.firstinspires.ftc.teamcode.util.robotLength
import java.util.Optional
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.pow

class TeleopFollower(val gamepad: Gamepad, val gamepad2: Gamepad, val red: Boolean = true) {
    var headingLock: Optional<Double> = Optional.empty<Double>();

    val adjustPose: Drive.(Vector) -> Unit = { direction: Vector ->
        localizer.pose = (localizer.pose).let { Pose(it.x + direction.x, it.y + direction.y, it.heading) }
    }
    val function: Drive.() -> DriveVectors = {

        if (gamepad2.dpadUpWasPressed()) adjustPose(Vector.fromCartesian(-1.0, 0.0))
        if (gamepad2.dpadDownWasPressed()) adjustPose(Vector.fromCartesian(1.0, 0.0))
        if (gamepad2.dpadLeftWasPressed()) adjustPose(Vector.fromCartesian(0.0, -1.0))
        if (gamepad2.dpadRightWasPressed()) adjustPose(Vector.fromCartesian(0.0, 1.0))
        if (gamepad2.guide) {
            localizer.pose = Pose(robotLength/2.0, 50.0 + robotLength/2.0, 0.0)
            if (headingLock.isPresent) {
                headingLock = Optional.of(0.0)
            }
        }

        if (gamepad.right_stick_x.toDouble() == 0.0 && (localizer.headingVel.absoluteValue <= 0.05)){
            if (!headingLock.isPresent) {
                headingLock = Optional.of(localizer.heading)
            }
        } else if (gamepad.right_stick_x.toDouble() != 0.0) {
            headingLock = Optional.empty()
        }
        val leftStick = Vector.fromCartesian(-gamepad.left_stick_y.toDouble(), -gamepad.left_stick_x.toDouble())

        if (gamepad.left_stick_button) {
            headingLock = Optional.of(leftStick.angle)
        }

        processTurnTranslational(
            if (!headingLock.isPresent) {
                -gamepad.right_stick_x.toDouble() * gamepad.right_stick_x.toDouble().absoluteValue * 1.6
            } else {
                val error = AngleUnit.normalizeRadians(headingLock.get() - localizer.heading)
                val dError = -localizer.headingVel
                PDLT(error, dError, hP, hD, hS, hT)
            },
            leftStick.rotated(-localizer.heading) * 1.6
        )
    }
}