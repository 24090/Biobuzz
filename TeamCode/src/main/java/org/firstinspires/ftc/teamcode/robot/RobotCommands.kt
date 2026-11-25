package org.firstinspires.ftc.teamcode.robot

import android.util.Log
import org.firstinspires.ftc.teamcode.commands.*
import org.firstinspires.ftc.teamcode.opmodes.commands.Robot
import org.firstinspires.ftc.teamcode.subsystems.intake.TransferMode
import kotlin.math.abs

fun Robot.shootPollen() = Sequence(
    WaitUntil{
        abs(shooter.pollenMotor.velocity - shooter.targetVelocityPollen) <= 20.0
    },
    Instant{
        Log.i("shoot pollen A", "shoot pollen")
        shooter.openPollenBlocker()
        transfer.mode = TransferMode.FullPower
    },
    Sleep(1.0),
    Instant{
        Log.i("shoot pollen B", "shoot pollen")
        shooter.closePollenBlocker()
        transfer.mode = TransferMode.HalfPower
    }
)