package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.SubSystems.NewHardwareMap;

@Autonomous (name = "FirstAuto")

public abstract class FirstAuto extends OpMode {

    NewHardwareMap robot = new NewHardwareMap();

    enum State {
        DForward

    }

    State driveState;
    ElapsedTime time;

    @Override   // Game initialized
    public void init(){
        robot.init(hardwareMap);
        driveState = State.DForward;
        time = new ElapsedTime();

    }

    @Override   // Game start
    public void loop() {

        double CurrentTime = time.time();
        telemetry.addData("time", CurrentTime);
        double gyroangle;

        switch (driveState) {

            case DForward:



        }

    }
}
