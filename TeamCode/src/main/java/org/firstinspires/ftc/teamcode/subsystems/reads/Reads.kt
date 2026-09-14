package org.firstinspires.ftc.teamcode.subsystems.reads

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver
import com.qualcomm.robotcore.hardware.HardwareMap

class Reads(hwMap: HardwareMap, var pinpointReadInterval: Int? = 1, var voltageReadInterval: Int? = 10, var bulkReadInterval: Int? = 1) {
    val bulkReads = BulkReads(hwMap)
    val updateVoltages = VoltageReader.updater(hwMap)
    val pinpoint: GoBildaPinpointDriver = hwMap.get(GoBildaPinpointDriver::class.java, "pinpoint")
    var loopNumber = 0
    fun doRead(loopNumber: Int, interval: Int?) = (interval != null && loopNumber%interval == 0)
    fun update(){
        loopNumber += 1
        if (doRead(loopNumber, pinpointReadInterval)) pinpoint.update()
        if (doRead(loopNumber, bulkReadInterval)) bulkReads.update()
        if (doRead(loopNumber, voltageReadInterval)) updateVoltages()
    }

}