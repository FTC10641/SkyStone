package org.firstinspires.ftc.teamcode.TestCode;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.SubSystems.Robot;
import org.firstinspires.ftc.teamcode.SubSystems.Sensors;


@TeleOp(name="Testing", group="Linear Opmode")
@Disabled
public class TestTele extends LinearOpMode {

    Robot robot = new Robot();
    Sensors sensors = new Sensors();

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    public boolean changed = false;
    public double liftHeight = 0;
    public int maxLiftHeight = 7;
    public double minLiftHeight = -.25;
    public double speed = 0.9;

    public double maxCarriage = 2.75;
    public int minCarriage = 0;
    public boolean changed2 = false;
    public double carriagePosition = 0;
    public double speed2 = 1;


    public float hsvValues[] = {0F, 0F, 0F};

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        robot.initRobot(hardwareMap);
        sensors.initSensors(hardwareMap);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            //sensors.ReadColor();

            /*
            gamepad 1 controls, includes:
            Intake
            the servo that is connected to the color sensor
            Drive Train
            Lift
             */

            if (gamepad1.dpad_up && !changed && liftHeight < maxLiftHeight) {
                liftHeight++;
                changed = true;
            } else if (gamepad1.dpad_down && !changed && liftHeight > minLiftHeight) {
                liftHeight--;
                changed = true;
            }
            else if (gamepad1.dpad_left && !changed && liftHeight > minLiftHeight) {
            liftHeight = minLiftHeight;
            changed = true;
        }
            else if (!gamepad1.dpad_up && !gamepad1.dpad_down && changed) {
                changed = false;
            }

            if (robot.lift.getCurrentPosition() != liftHeight*(5*robot.LiftCountsPerInch)){
                robot.lift.setTargetPosition((int) (liftHeight*(5*robot.LiftCountsPerInch)));
                robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.lift.setPower(speed);
            }


            double rightIntakePower;
            double leftIntakePower;
            double frontRightPower;
            double backRightPower;
            double frontLeftPower;
            double backLeftPower;

            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            double strafe = -gamepad1.left_stick_x;
            double drive = -gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;
            frontLeftPower = Range.clip(drive + turn - strafe, -1.0, 1.0);
            backLeftPower = Range.clip(drive + turn + strafe, -1.0, 1.0);
            backRightPower = Range.clip(drive - turn - strafe, -1.0, 1.0);
            frontRightPower = Range.clip(drive - turn + strafe, -1.0, 1.0);
            rightIntakePower = Range.clip(-gamepad1.right_trigger + gamepad1.left_trigger,-1.0,1.0); //controls for the Intake motors
            leftIntakePower = Range.clip(-gamepad1.right_trigger + gamepad1.left_trigger, -1.0, 1.0);


            rightIntakePower = (float) scaleInput(rightIntakePower);
            leftIntakePower = (float) scaleInput(leftIntakePower);
            frontLeftPower = (float) scaleInput(frontLeftPower);
            backLeftPower = (float) scaleInput(backLeftPower);
            frontRightPower = (float) scaleInput(frontRightPower);
            backRightPower =  (float) scaleInput(backRightPower);
            // Send calculated power to wheels and intake motors
            robot.frontLeft.setPower(frontLeftPower);
            robot.backLeft.setPower(backLeftPower);
            robot.frontRight.setPower(frontRightPower);
            robot.backRight.setPower(backRightPower);
            robot.rightIntake.setPower(rightIntakePower);
            robot.leftIntake.setPower(leftIntakePower);

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Robot", "Lift Height counter: " + liftHeight);
            telemetry.addData("VerticalLeft", robot.verticalLeft.getCurrentPosition());
            telemetry.addData("VerticalRight", robot.verticalRight.getCurrentPosition());
            telemetry.addData("Horizontal", robot.horizontal.getCurrentPosition());
            telemetry.addData("Robot", "left (%.2f), right (%.2f)",
                    backLeftPower, frontLeftPower, frontRightPower, backRightPower);
            telemetry.addData("hue", hsvValues[0]);
            telemetry.update();
        }
    }

    /** This Scale Array function establishes a set of increasing values
     that is used to control the speed of motors and servos. It allows them to exponentially increases their rotation.  **/

    public double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }
}