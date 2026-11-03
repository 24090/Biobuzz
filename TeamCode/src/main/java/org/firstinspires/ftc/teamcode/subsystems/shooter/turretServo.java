package org.firstinspires.ftc.teamcode.subsystems.shooter;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class turretServo extends OpMode{

    Servo turretservo = new Servo();
    @Override
    public void init() {
        turretservo.init(HardwareMap);
    }

    @Override
    public void loop() {
        if(gamepad1.a) {
            turretservo.setTurretServo(-1.0);
        }
        else {
            turretservo.setTurretServo(1.0);
        }
    }

}
