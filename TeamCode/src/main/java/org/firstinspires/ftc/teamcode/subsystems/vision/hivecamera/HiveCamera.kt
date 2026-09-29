package org.firstinspires.ftc.teamcode.subsystems.vision.hivecamera

import android.util.Size
import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.Utility
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor
import org.openftc.easyopencv.OpenCvCameraFactory


class HiveCamera(hardwareMap: HardwareMap) {
    val aprilTagProcessor = AprilTagProcessor.Builder().build()
    val visionPortal = VisionPortal.Builder()
        .setCamera(hardwareMap.get(WebcamName::class.java, "hiveCamera"))
        .setCameraResolution(Size(640, 480))
        .addProcessor(aprilTagProcessor)
        .build()

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

    fun getData(): List<Triple<String, Double, Long>>{
        return aprilTagProcessor.freshDetections
            ?.filterIsInstance<AprilTagClusterDetection>()
            ?.map { detection ->
                Triple(detection.metadata.name!!, detection.rawPose.z, detection.frameAcquisitionNanoTime)
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
            camera.getData().forEach {
                telemetry.addLine("name: ${it.first}, z: (${it.second}), timestamp: ${it.third.toDouble()/1.0e9}")
            }
        }
    }
}