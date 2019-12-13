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

@Autonomous (name = "NewAutoPleaseWork")

public class NewAuto extends OpMode {
    Robot robot = new Robot();
    Sensors sensors = new Sensors();

    private double leftEncoder = (robot.verticalLeft.getCurrentPosition()*robot.OdometryCountsPerInch);

    enum State {
        Turn, NotBackwards, Forward, Backwards, DriveBackwards

    }

    State state;
    ElapsedTime time;

    @Override
    public void init() {
        robot.initRobot(hardwareMap);
        sensors.initSensors(hardwareMap);
        state = State.DriveBackwards;
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
            case DriveBackwards:
                robot.Reverse(.75, 43);
                robot.foundationServo1.setPosition(1);
                robot.foundationServo2.setPosition(0);
                if (robot.DriveDone(43) && robot.foundationServo1.getPosition() == 1 && robot.foundationServo2.getPosition() == 0){
                    state = State.Backwards;
                    robot.Kill();
                    time.reset();
                }
                break;

//            case Turn:
//                if (CurrentTime >= 1.0){
//                    robot.TurnAbsolute(105, gyroangle);
//                    if (gyroangle >= 103 && gyroangle <= 107){
//                        state = State.Backwards;
//                        robot.Kill();
//                        time.reset();
//                    }
//                }
//                break;

            case Backwards:
                if (CurrentTime >= 1.0){
                    robot.StrafeLeft(.75,20);
                    if (robot.DriveDone(20)){
                        robot.Kill();
                        time.reset();
                    }
                }
                break;
        }
    }
    String formatAngle (AngleUnit angleUnit,double angle){
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees ( double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }
}
