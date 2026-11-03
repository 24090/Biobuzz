package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.commands.Servo;

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
