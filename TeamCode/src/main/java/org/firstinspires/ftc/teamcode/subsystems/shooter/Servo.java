package org.firstinspires.ftc.teamcode.subsystems.shooter;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Servo {
    private Servo turretServo; // positional

    public void init(HardwareMap hwMap) {
        turretServo = hwMap.get(Servo.class, "turret_servo");
    }

    public void setTurretServo(double angle) {
        turretServo.setTurretServo(angle);
    }
}
