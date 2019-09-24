/*
This should just make the robot Drive Forward 10 inches
 */

package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.SubSystems.*;

@Autonomous (name = "FirstAuto")

public class FirstAuto extends OpMode {

    Motors motor = new Motors();
    Actions act = new Actions();
    Sensors sensor = new Sensors();
    ElapsedTime time = new ElapsedTime();

    enum State {
        DForward
    }

    State driveState;

    @Override   // Game initialized
    public void init(){
        motor.initMotors(hardwareMap);
        act.initActions(hardwareMap);
        sensor.initSensors(hardwareMap);
        time = new ElapsedTime();
        driveState = State.DForward;
    }

    @Override   // Game start
    public void loop() {

        double CurrentTime = time.time();
        telemetry.addData("time", CurrentTime);
        double gyroangle;

        switch (driveState) {

            case DForward:
                act.DForward(1,10);
                if (act.DriveDone(10)){
                    time.reset();
                    act.Kill();
                }
                break;

        }

    }
}
