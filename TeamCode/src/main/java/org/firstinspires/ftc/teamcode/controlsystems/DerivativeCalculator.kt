package org.firstinspires.ftc.teamcode.subsystems.controlsystems

import java.util.LinkedList

class DerivativeCalculator(val storeCount: Int) {
    private var pastStates = LinkedList<Pair<Double, Double>>()

    fun updateLength() = pastStates.last().first - pastStates.first().first

    fun update(newX: Number, newY: Number) = update(Pair(newX, newY))

    fun update(newState: Pair<Number, Number>){
        if (pastStates.size >= storeCount){
            pastStates.removeAt(0)
        }
        pastStates.add(Pair(newState.first.toDouble(), newState.second.toDouble()))
    }
    fun get() = if (pastStates.size >= 2) {
        (pastStates.first().second - pastStates.last().second) / (pastStates.first().first - pastStates.last().first)
    } else {
        null
    }
}