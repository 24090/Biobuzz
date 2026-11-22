package org.firstinspires.ftc.teamcode.util

class KalmanFilter(var x: Double, val model_covariance: Double, val sensor_covariance: Double, var covariance: Double) {
    fun update(estimated_change: Double, measured_state: Double){
        var new_x = x + estimated_change
        var new_covariance = covariance + model_covariance
        val K = new_covariance/(new_covariance + sensor_covariance)
        new_x += K * (measured_state - x)
        new_covariance *= (1-K)

        x = new_x
        covariance = new_covariance
    }

    fun getEstimate(): Double{
        return x
    }

    fun getEstimate(estimated_change: Double, measured_state: Double): Double{
        update(estimated_change, measured_state)
        return x
    }
}