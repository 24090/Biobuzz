package org.firstinspires.ftc.teamcode.util

/*
 SCRATCHBOARD:
    for running random tests and stuff
 */

import org.firstinspires.ftc.teamcode.commands.Forever
import org.firstinspires.ftc.teamcode.commands.ForeverCommand
import org.firstinspires.ftc.teamcode.commands.Instant
import org.firstinspires.ftc.teamcode.commands.Parallel
import org.firstinspires.ftc.teamcode.commands.Race
import org.firstinspires.ftc.teamcode.commands.Sequence
import org.firstinspires.ftc.teamcode.commands.Sleep
import org.firstinspires.ftc.teamcode.commands.runBlocking

fun main(){
    var t: Long = 0
    runBlocking(Race(
        Forever {println("loop")},
        ForeverCommand{
            Sequence(
                Instant{  t = System.currentTimeMillis() },
                Instant{},
                Sleep(0.0),
                Sequence(
                    Instant{},
                    Instant{},
                    Sleep(1e-12),
                ),
                Parallel(
                    Instant{},
                    Race(
                        Instant{},
                        Sequence(
                            Instant{}
                        ),
                        Sleep(1e-12),
                    ),
                    Sleep(1e-12),
                ),
                Race(
                    Instant{}
                ),
                Instant { println( System.currentTimeMillis() - t) },
            )
        }
    ))

}