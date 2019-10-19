package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.SubSystems.*;

@Autonomous(name = "FirstAuto2")

public class FirstAuto2 extends OpMode {

    Motors motor = new Motors();

    enum State {

        DForward

    }

    State DriveForward;
    ElapsedTime time;

    @Override
    public void init() {
        motor.initMotors(hardwareMap);
        time = new ElapsedTime();
        DriveForward = State.DForward;
    }

    @Override
    public void loop() {
        double CurrentTime = time.time();
        telemetry.addData("time", CurrentTime);
        telemetry.addLine("get ready for this to fail!!");
        telemetry.update();

        switch (DriveForward) {

            case DForward: {

             motor.DForward(1, 10);
             if(motor.DriveDone(10)){
                 motor.Kill();
                 time.reset();
                   }
                }
                break;

        }

    }
}