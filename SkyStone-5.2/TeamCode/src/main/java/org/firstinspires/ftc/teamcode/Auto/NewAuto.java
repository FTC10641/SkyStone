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

@Autonomous (name = "NewAuto")

public class NewAuto extends OpMode {
    Robot robot = new Robot();
    Sensors sensors = new Sensors();

    enum State {
        DriveForwards2, Straighten, PushFound, StrafeRight, DriveBackwards2, Forward, DriveForwards, Turn, DriveBackwards, Stop, DriveForwards3, DriveBackwards3, DriveForwards4, StrafeRight2, StrafeLeft, Foundation

    }

    State state;
    ElapsedTime time;

    @Override
    public void init() {
        robot.initRobot(hardwareMap);
        sensors.initSensors(hardwareMap);
        state = State.Forward;
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
        telemetry.addData("VertLeft Encoder ticks", robot.verticalLeft.getCurrentPosition());
        telemetry.addData("VertRight Encoder ticks", robot.verticalRight.getCurrentPosition());
        telemetry.addData("Horizontal Encoder ticks", robot.horizontal.getCurrentPosition());
        telemetry.update();

        switch (state){
            case Forward:
            case DriveBackwards:
                robot.Reverse(.55, 27);
                if (robot.DriveDone(27)){
                    state = State.Foundation;
                    Reset();
                }
                break;

            case Foundation:
                robot.ServosDown();
                if (robot.foundationServo1.getPosition() >= 1 && robot.foundationServo2.getPosition() <= 0 && CurrentTime >= 1.0) {
                    state = State.DriveForwards;
                    Reset();
                }
                break;

            case DriveForwards:
                robot.Forward(.75, 10);
                if (robot.DriveDone(10)) {
                    state = State.Turn;
                    Reset();
                }
                break;

            case Turn:
                robot.TurnAbsolute(90, gyroangle);
                if (gyroangle >= 86 && gyroangle <= 94 && CurrentTime >= 1) {
                    robot.ServosUp();
                    state = State.DriveBackwards2;
                    Reset();
                }
                break;

            case DriveBackwards2:
                robot.Reverse(.75,12);
                if (robot.DriveDone(12)){
                    state = State.StrafeRight;
                    Reset();
                }
                break;

            case StrafeRight:
                robot.StrafeRight(.75,10);
                if (robot.DriveDone(10)){
                    state = State.PushFound;
                    Reset();
                }
                break;

            case PushFound:
                robot.Reverse(1,10);
                if (robot.DriveDone(5)){
                    state = State.Straighten;
                    Reset();
                }
                break;

            case Straighten:
                robot.TurnAbsolute(90, gyroangle);
                if (gyroangle >= 88 && gyroangle <= 92 && CurrentTime >= .25){
                    state = State.DriveForwards2;
                    Reset();
                }
                break;

            case DriveForwards2:
                robot.Forward(.75,60);
                if (robot.DriveDone(60)){
                    state = State.StrafeLeft;
                    Reset();
                }
                break;

            case StrafeLeft:
                robot.StrafeLeft(1,15);
                if (robot.DriveDone(15)){
                    state = State.DriveForwards3;
                    Reset();
                }
                break;

            case DriveForwards3:
                robot.Forward(1,5);
                if (robot.DriveDone(5)){
                    state = State.Forward.StrafeRight2;
                    Reset();
                }
                break;

            case StrafeRight2:
                robot.StrafeRight(1,15);
                if (robot.DriveDone(15)){
                    state = State.DriveBackwards3;
                    Reset();
                }
                break;

            case DriveBackwards3:
                robot.Reverse(1, 68);
                if (robot.DriveDone(68)){
                    state = State.DriveForwards4;
                    Reset();
                }
                break;

            case DriveForwards4:
                robot.Forward(1, 30);
                if (robot.DriveDone(30)){
                    state = State.Stop;
                    Reset();
                }
                break;

            case Stop:
                robot.Death();
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
