package org.firstinspires.ftc.teamcode.subsystems.drive.pathing.followers

import org.firstinspires.ftc.teamcode.subsystems.drive.Drive
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive.DriveConstants.kA
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive.DriveConstants.kS
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive.DriveConstants.kV
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive.DriveConstants.tipAccelBackward
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive.DriveConstants.tipAccelForward
import org.firstinspires.ftc.teamcode.subsystems.drive.DriveVectors
import org.firstinspires.ftc.teamcode.subsystems.drive.DriveVectors.Companion.getTranslationalVectors
import org.firstinspires.ftc.teamcode.subsystems.drive.Localizer
import org.firstinspires.ftc.teamcode.subsystems.drive.pathing.BezierCurve
import org.firstinspires.ftc.teamcode.subsystems.drive.pathing.Pose
import org.firstinspires.ftc.teamcode.subsystems.drive.pathing.PurePursuitPath
import org.firstinspires.ftc.teamcode.subsystems.drive.pathing.Vector
import org.firstinspires.ftc.teamcode.subsystems.drive.pathing.getRelativeVelocity
import org.firstinspires.ftc.teamcode.subsystems.reads.VoltageReader.controlHubVoltage
import kotlin.math.max

sealed class HeadingBehaviour{
    class Tangent(val angle: Double): HeadingBehaviour()
    object Snap: HeadingBehaviour()
    object Interpolate: HeadingBehaviour()
}

fun getPurePursuit(path: PurePursuitPath): Drive.() -> DriveVectors{
    return {
        purePursuit(path, localizer)
    }
}

fun purePursuit(path: PurePursuitPath, localizer: Localizer): DriveVectors{
    val followPoint = path.getFollowPose(localizer.pose.vector(), 48.0)
    return pointToPoint(followPoint, localizer)
}

fun getFollowCurve(curve: BezierCurve): Drive.() -> DriveVectors = { followCurve(curve, localizer) }
fun followCurve(curve: BezierCurve, localizer: Localizer): DriveVectors{
    // 1. corrective position/heading
    // 2. static friction feedforward
    // 3. accelvelstuff

    val t = curve.closestT(localizer.pose.vector())
    println(t)
    val curvePose = curve.getPose(t)
    val curveVelocity = curve.getVelocity(t).vector()
    val curveAccel = curve.getAcceleration(t).vector()
    val corrective = pointToPoint(curvePose, localizer, false)
    val static = curveVelocity.normalized() * (kS + 0.4)
    val goodVelocityLength = max((localizer.poseVel.vector() dot curveVelocity.norm()), 0.0)
    val accelvel = (Vector.fromPolar(
        angle = curveAccel.angle,
        length = goodVelocityLength * (curveAccel.length / curveVelocity.length.let { if (it == 0.0) 0.001 else it} )
    ) + Vector.fromPolar(
        angle = curveVelocity.angle,
        goodVelocityLength,
    )) * 2.0

    return corrective
        .addWithoutPriority(getTranslationalVectors(static.x, static.y), controlHubVoltage / 14.0)
}
