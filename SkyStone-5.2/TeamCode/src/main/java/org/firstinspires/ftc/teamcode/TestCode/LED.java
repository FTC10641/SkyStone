package org.firstinspires.ftc.teamcode.TestCode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SubSystems.Robot;
import org.firstinspires.ftc.teamcode.SubSystems.Sensors;

@TeleOp(name = "Testing LEDs")
public class LED extends LinearOpMode {

    Robot robot = new Robot();
    Sensors sensors = new Sensors();

    RevBlinkinLedDriver.BlinkinPattern stinkyPattern;

    @Override
    public void runOpMode() {
        robot.initRobot(hardwareMap);
        sensors.initSensors(hardwareMap);

        stinkyPattern = RevBlinkinLedDriver.BlinkinPattern.SKY_BLUE;


                waitForStart();
        while (opModeIsActive()) {

            if (gamepad1.a){
                robot.blinkinLedDriver.setPattern(stinkyPattern);
            }

        }
    }
}