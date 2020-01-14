package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.SubSystems.Robot;
import org.firstinspires.ftc.teamcode.SubSystems.Sensors;

import java.util.Locale;

@Autonomous (name = "Park Only")

public class ParkOnly extends OpMode {
    Robot robot = new Robot();
    Sensors sensors = new Sensors();

    enum State {
        Park, StrafeToPark, Turn, DummyCase

    }

    State state;
    ElapsedTime time;

    @Override
    public void init() {
        robot.initRobot(hardwareMap);
        sensors.initSensors(hardwareMap);
        state = State.Park;
        time = new ElapsedTime();
    }

    @Override
    public void loop() {
        double CurrentTime = time.time();
        telemetry.addData("Time", CurrentTime);
        double gyroangle;
        sensors.angles = sensors.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        gyroangle = Double.parseDouble(formatAngle(sensors.angles.angleUnit, sensors.angles.firstAngle));
        telemetry.addData("Heading", formatAngle(sensors.angles.angleUnit, sensors.angles.firstAngle));
        telemetry.addData("Current State",state.toString());
        telemetry.update();

        switch (state){
            case Park:
                robot.Forward(1,36);
                if (robot.DriveDone(34)) {
                    robot.Death();
                    Reset();
                }
                break;
        }
    }
    private void Reset(){
        robot.StopUsingEncoders();
        robot.Kill();
        time.reset();
    }

    String formatAngle (AngleUnit angleUnit,double angle){
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees ( double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }
}
