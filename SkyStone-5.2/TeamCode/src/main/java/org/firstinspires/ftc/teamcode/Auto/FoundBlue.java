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

@Autonomous(name = "WallFoundationBlue")

public class FoundBlue extends OpMode {

    private Robot robot = new Robot();
    private Sensors sensors = new Sensors();

    enum State {
        DriveBackwards2, Turn, DriveForwards,
        DriveBackwards, DriveForwards2,
        Foundation, Straighten, StrafeRight, StrafeLeft, DriveBackwards3, StraightenToGrab, Stop, FoundationGrab, StrafeLeft2, FinalStraighten, StrafeRight2, StraightenFound

    }

    private State state;
    private ElapsedTime time;

    @Override
    public void init() {
        robot.initRobot(hardwareMap);
        sensors.initSensors(hardwareMap);
        state = State.DriveBackwards;
        time = new ElapsedTime();
        robot.ServosUp();
    }

    @Override
    public void loop() {
        double CurrentTime = time.time();
        telemetry.addData("Time", CurrentTime);
        double gyroangle;
        sensors.angles = sensors.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        gyroangle = Double.parseDouble(formatAngle(sensors.angles.angleUnit, sensors.angles.firstAngle));
        telemetry.addData("Heading", formatAngle(sensors.angles.angleUnit, sensors.angles.firstAngle));
        telemetry.addData("Current State", state.toString());
        telemetry.update();

        switch (state) {

            case DriveBackwards:
                robot.Reverse(.65, 24);
                if (robot.DriveDone(24)){
                    state = State.StraightenToGrab;
                    Reset();
                }
                break;

            case StraightenToGrab:
                robot.TurnAbsolute(0, gyroangle);
                if (gyroangle <= 4 && gyroangle >= -4 && CurrentTime >= .25) {
                    state = State.DriveBackwards2;
                    Reset();
                }
                break;

            case DriveBackwards2:
                robot.Reverse(.5, 4);
                if (robot.DriveDone(4)){
                    state = State.Foundation;
                    Reset();
                }
                break;

            case Foundation:
                robot.ServosDown();
                if (robot.foundationServo1.getPosition() >= 1 && robot.foundationServo2.getPosition() <= 0 && CurrentTime >= 2.0) {
                    state = State.DriveForwards;
                    Reset();
                }
                break;

//            case StrafeRight1:
//                robot.StrafeRight(.75,5);
//                if (robot.StrafeDriveDone(5)){
//                    state = State.DriveForwards;
//                    Reset();
//                }
//                break;

            case DriveForwards:
                robot.Forward(.75, 30);
                if (robot.DriveDone(30)) {
                    state = State.Turn;
                    Reset();
                }
                break;

            case Turn:
                robot.TurnAbsolute(90, gyroangle);
                if (gyroangle <= 94 && gyroangle >= 86 && CurrentTime >= .25) {
                    robot.ServosUp();
                    state = State.StrafeRight;
                    Reset();
                }
                break;

            case StrafeRight:
                robot.StrafeRight(.75, 2);
                if (robot.StrafeDriveDone(2) || CurrentTime >= 2){
                    state = State.DriveBackwards3;
                    Reset();
                }
                break;

            case DriveBackwards3:
                robot.Reverse(.5, 15);
                if (robot.DriveDone(15) || CurrentTime >= 2){
                    state = State.DriveForwards2;
                    Reset();
                }
                break;

            case DriveForwards2:
                robot.Forward(.75, 33);
                if (robot.DriveDone(33)) {
                    state = State.FinalStraighten;
                    Reset();
                }
                break;

            case FinalStraighten:
                robot.TurnAbsolute(90, gyroangle);
                if (gyroangle <= 94 && gyroangle >= 86 && CurrentTime >= 1) {
                    state = State.StrafeRight2;
                    Reset();
                }
                break;

            case StrafeRight2:
                robot.StrafeRight(.75, 3);
                if (robot.StrafeDriveDone(3) || CurrentTime >= 2){
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

    private String formatAngle (AngleUnit angleUnit,double angle){
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    private String formatDegrees (double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }
}

