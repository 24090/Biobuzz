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
import org.firstinspires.ftc.teamcode.subsystems.drive.pathing.Vector
import java.util.Optional
import kotlin.math.absoluteValue
import kotlin.math.pow

class TeleopFollower(val gamepad: Gamepad, val red: Boolean = true) {
    var headingLock: Optional<Double> = Optional.empty<Double>();

    val function: Drive.() -> DriveVectors = {
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