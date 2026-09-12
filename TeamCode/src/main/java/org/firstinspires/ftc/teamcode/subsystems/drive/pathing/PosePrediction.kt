package org.firstinspires.ftc.teamcode.subsystems.drive.pathing

import org.firstinspires.ftc.teamcode.subsystems.drive.Drive.DriveConstants.kA
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive.DriveConstants.kS
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive.DriveConstants.kV
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive.DriveConstants.tipAccelBackward
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive.DriveConstants.tipAccelForward
import org.firstinspires.ftc.teamcode.subsystems.drive.DriveVectors
import org.firstinspires.ftc.teamcode.subsystems.reads.VoltageReader.controlHubVoltage
import kotlin.math.absoluteValue
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sign

fun minStopDistance(heading: Double, errorAngle: Double, velocity: Vector, minAccelX: Double, maxAccelX: Double): Double{
    val errorNorm = Vector.fromPolar(errorAngle, 1.0)
    val v = errorNorm.scalarProjection(velocity)
    val vX = velocity.rotated(-heading).x
    val uncorrectedPower = DriveVectors
        .fromTranslation(errorNorm * -100)
        .trimmed(controlHubVoltage / 14.0)
        .left.length

    val uncorrectedAccel = (uncorrectedPower - kV * v - kS)/kA

    val minAccel = errorNorm.scalarInverseProjection(Vector.fromPolar(0.0, minAccelX))
    val minPower = minAccel * kA + vX * kV + kS * sign(v)

    val maxAccel = errorNorm.scalarInverseProjection(Vector.fromPolar(0.0, maxAccelX))
    val maxPower = maxAccel * kA + vX * kV + kS * sign(v)


    val constantAccel = if (minPower > uncorrectedPower) minAccel else if (maxPower < uncorrectedPower) maxAccel else uncorrectedAccel
    val t = max(
        (uncorrectedPower - (constantAccel * kA + v * kV + kS * sign(v)))/(constantAccel * kV),
        0.0
    )
    val s = 0 + t * v + t.pow(2)/2 * constantAccel
    val newVelocity = v + constantAccel * t
    return (s + minStopDistanceWithoutTipCorrection(errorAngle, newVelocity))
}

fun minStopDistanceWithoutTipCorrection(errorAngle: Double, velocity: Double): Double{
    val maxStopPower = DriveVectors
        .fromTranslation(Vector.fromPolar(errorAngle, -100.0))
        .trimmed(controlHubVoltage / 14.0).left.length
    val initialAcceleration = (maxStopPower - kS * velocity.absoluteValue - kV * velocity)/kA
    val k1 = (kA/kV) * initialAcceleration
    return  kA/kV * ((velocity - k1)*ln((k1 - velocity)/k1) - velocity)
}

fun getStopPosition(pose: Pose, velocity: Vector): Vector{
    return pose.vector() + velocity.norm() * minStopDistance(pose.heading, velocity.angle, velocity, tipAccelBackward, tipAccelForward)
}
