package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Odometry.*;
import org.firstinspires.ftc.teamcode.SubSystems.*;

@Autonomous (name = "FirstAuto")

public class FirstAuto extends OpMode {

    ElapsedTime time = new ElapsedTime();


    enum State {
        DForward
    }

    State driveState;

    @Override   // Game initialized
    public void init(){
        time = new ElapsedTime();
        driveState = State.DForward;
    }

    @Override   // Game start
    public void loop() {

        double CurrentTime = time.time();
        telemetry.addData("time", CurrentTime);
        double gyroangle;
        telemetry.addLine("get ready for this to fail!!");

        switch (driveState) {

            case DForward:
                telemetry.addLine("your dad str8");
        }

    }
}
