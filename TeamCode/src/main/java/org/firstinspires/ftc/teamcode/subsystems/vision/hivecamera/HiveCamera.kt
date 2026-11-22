package org.firstinspires.ftc.teamcode.subsystems.vision.hivecamera

import android.util.Size
import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.Utility
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference
import org.firstinspires.ftc.teamcode.util.timeSeconds
import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor
import org.openftc.easyopencv.OpenCvCameraFactory
import kotlin.math.PI
import kotlin.math.sin

enum class HiveStatus {
    Audience,
    MovingAudience,
    MovingScoring,
    Scoring,
    Unknown
}

class HiveTracker {
    val lookback = 1.0
    var hiveStatus = HiveStatus.Unknown
    var audienceSamples: List<Pair<Double, Double>> = arrayListOf()
    var scoringSamples: List<Pair<Double, Double>> = arrayListOf()

    fun update(roll: Double, time: Long, isAudienceTag: Boolean) {
        val roll = if (roll > 0.0) roll - 180.0 else roll
        val isAudienceTag = if (roll > 0.0) !isAudienceTag else isAudienceTag

        var samples = if (isAudienceTag) audienceSamples else scoringSamples + Pair(roll, time/1e9)
        samples = samples.filter { sample ->
            sample.second > timeSeconds() - 1.0
        }
        if (isAudienceTag) audienceSamples = samples else scoringSamples = samples

        hiveStatus = if (roll > -25) {
            if (isAudienceTag) HiveStatus.Audience else HiveStatus.Scoring
        } else {
            when (hiveStatus) {
                HiveStatus.Audience -> HiveStatus.MovingScoring
                HiveStatus.MovingAudience -> HiveStatus.MovingAudience
                HiveStatus.MovingScoring -> HiveStatus.MovingScoring
                HiveStatus.Scoring -> HiveStatus.MovingAudience
                HiveStatus.Unknown -> if (isAudienceTag) HiveStatus.Audience else HiveStatus.Scoring
            }
        }
    }
}
class HiveCamera(hardwareMap: HardwareMap) {
    val cameraAngleUp = PI/2
    val hiveZeroOffset = 0.0
    val aprilTagProcessor = AprilTagProcessor.Builder().build()
    val visionPortal = VisionPortal.Builder()
        .setCamera(hardwareMap.get(WebcamName::class.java, "hiveCamera"))
        .setCameraResolution(Size(640, 480))
        .addProcessor(aprilTagProcessor)
        .enableLiveView(true)
        .build()

    val blueHiveTracker = HiveTracker()
    val redHiveTracker = HiveTracker()

    fun enable(){
        visionPortal.setProcessorEnabled(aprilTagProcessor, true)
    }

    fun disable(){
        visionPortal.setProcessorEnabled(aprilTagProcessor, false)
    }

    init {
        enable()
        //val cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("hiveCamera", "id", hardwareMap.appContext.getPackageName())
        val camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName::class.java, "hiveCamera"), 0)
        FtcDashboard.getInstance().startCameraStream(camera, 10.0)
    }

    fun update(){
        aprilTagProcessor.detections
            ?.filterIsInstance<AprilTagClusterDetection>()
            ?.forEach { detection ->
                val totalAngle = detection.metadata.fieldOrientation.toOrientation(
                    AxesReference.INTRINSIC,
                    AxesOrder.XYZ,
                    AngleUnit.DEGREES
                ).firstAngle.toDouble()
                when (detection.metadata.name){
                    "RED AUDIENCE" -> redHiveTracker.update(totalAngle, detection.frameAcquisitionNanoTime, true)
                    "RED SCORING" -> redHiveTracker.update(totalAngle, detection.frameAcquisitionNanoTime, false)
                    "BLUE AUDIENCE" -> blueHiveTracker.update(totalAngle, detection.frameAcquisitionNanoTime, true)
                    "BLUE SCORING" ->blueHiveTracker.update(totalAngle, detection.frameAcquisitionNanoTime, false)
                }
            }
    }

    val redHiveStatus
        get() = redHiveTracker.hiveStatus
    val blueHiveStatus
        get() = blueHiveTracker.hiveStatus

    private fun getHiveHeightFromCluster(detection: AprilTagClusterDetection){
        detection.ftcPose.z * sin(cameraAngleUp) + detection.ftcPose.y * sin(cameraAngleUp)
    }

    fun getData(): List<Triple<String, Double, Long>>{
        return aprilTagProcessor.detections
            ?.filterIsInstance<AprilTagClusterDetection>()
            ?.map { detection ->
                Triple(
                    detection.metadata.name!!,
                    detection.metadata.fieldOrientation.toOrientation(
                        AxesReference.INTRINSIC,
                        AxesOrder.XYZ,
                        AngleUnit.DEGREES
                    ).thirdAngle.toDouble(),
                    detection.frameAcquisitionNanoTime
                )
            } ?: listOf()
    }
}

@Utility
class HiveCameraTesting: LinearOpMode() {
    override fun runOpMode() {
        val telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)
        val camera = HiveCamera(hardwareMap)
        camera.enable()
        waitForStart()
        while (opModeIsActive()){
            camera.update()
            camera.getData().forEach {
                telemetry.addData(it.first, it.second)
            }
            telemetry.addData("blue hive status:", camera.blueHiveStatus)
            telemetry.addData("red hive status:", camera.redHiveStatus)
            telemetry.update()
        }
    }
}