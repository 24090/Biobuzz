package org.firstinspires.ftc.teamcode.subsystems.drive.tuners

import com.qualcomm.robotcore.eventloop.opmode.Utility
import org.firstinspires.ftc.teamcode.subsystems.controlsystems.sysid.SysIDRoutine
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive
import org.firstinspires.ftc.teamcode.subsystems.drive.pathing.Pose

@Utility(name = "Drive SysID")
class DriveSysID() : SysIDRoutine<Drive>(
    "Drive",
    arrayOf("x", "y", "heading"),
    {
        val drive = Drive(hardwareMap)
        drive.localizer.pose = Pose(0.0, 0.0, 0.0)
        drive
    },
    { signal ->
        localizer.pinpoint.update()

        flMotor.power = signal
        frMotor.power = signal
        blMotor.power = signal
        brMotor.power = signal

        arrayOf(localizer.x, localizer.y, localizer.heading)
    },
    {
        signal, t -> t > 3.0
    },
    quasistaticSlope = 0.6,
    dynamicStep = 0.3,
    dynamicStepRate = 0.4
)