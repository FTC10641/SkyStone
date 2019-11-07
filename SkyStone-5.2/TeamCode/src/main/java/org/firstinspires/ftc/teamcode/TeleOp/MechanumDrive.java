/*
    this is the java class for our TestCode
 */

package org.firstinspires.ftc.teamcode.TeleOp;

//import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.SubSystems.*;


@TeleOp(name="TeleOp", group="Linear Opmode")
public class MechanumDrive extends LinearOpMode {

    Robot robot = new Robot();
    Sensors sensors = new Sensors();

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private boolean changed = false;
    private boolean changed1 = false;
    private boolean changed2 = false;

    public float liftPosition = 0;

    public float hsvValues[] = {0F, 0F, 0F};

//    //LED Patterns
//    RevBlinkinLedDriver.BlinkinPattern humanIndicator1;
//    RevBlinkinLedDriver.BlinkinPattern off;


    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        robot.initRobot(hardwareMap);
        sensors.initSensors(hardwareMap);

//        humanIndicator1 = RevBlinkinLedDriver.BlinkinPattern.YELLOW;
//        off = RevBlinkinLedDriver.BlinkinPattern.BLACK;

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
            if (RightTrigger()) {
                robot.rightIntake.setPower(-1);
                robot.leftIntake.setPower(1);
            } else if (LeftTrigger()) {
                robot.rightIntake.setPower(1);
                robot.leftIntake.setPower(-1);
            } else {
                robot.leftIntake.setPower(0);
                robot.rightIntake.setPower(0);
            }

            //block grabber this works bt checking if y button is pressed and if toggle is false
            if (gamepad1.y && !changed) {
                //if that be the case, then we check for position if it's smaller than 0.8
                if (robot.blockGrabber.getPosition() <= 0.8) {
                    //set the servor position to 0.9
                    robot.blockGrabber.setPosition(0.9);
                } else{
                    //if it is bigger than 0.8 then we set it to 0.7
                    robot.blockGrabber.setPosition(0.7);
                }
                //activate toggle
                changed = true;
                //if no buttons are pressed and toggle is activated
            } else if (!gamepad1.y && !gamepad2.a) {
                //reset changed variable deactivating toggle
                changed = false;
            }

            //lift the lift
            //if dpad up is activated and lift position is below the threshold and toggle is off
            if(gamepad1.dpad_up && liftPosition <= 5 && changed2) {
                //run method Up with speed of 0.9 and a target position that is 1 greater that current position
                robot.Up(0.9, liftPosition + 1);
                //increase liftPosition by one
                liftPosition++;
                //activate toggle
                changed2 = false;
                //if dpad down is activate and lift position is greater than the threshold and toggle is off
            }else if(gamepad1.dpad_up && liftPosition >= 0 && changed2) {
                //run Down method, speed of 0.9 and target be one less than position
                robot.Down(0.9, liftPosition - 1);
                //decrease liftPosition by one
                liftPosition--;
                //activate toggle
                changed2 = false;
                //if toggle be active and neither dpad is pressed
            }else if(!gamepad1.dpad_up && !gamepad1.dpad_down && !changed2) {
                //deactivate toggle
                changed2 = true;
            }

            //platform grabber
            //if x button is pressed and toggle is off
            if(gamepad1.x && changed1) {
                //if foundationGrabber position is greater than 0.5
                if(robot.foundationGrabber.getPosition() >= 0.5) {
                    //set position to 0
                    robot.foundationGrabber.setPosition(0);
                }else{
                    //set position to 1 if it is not greater than 0.5
                    robot.foundationGrabber.setPosition(1);
                }
                //activate toggle
                changed1 = false;
                //if button is not pressed and toggle be activated
            }else if(!gamepad1.x && !changed1) {
                //deactivate toggle
                changed1 = true;
            }

            //reset claw and lift
            //if a is pressed and toggle is off
            if(gamepad2.a && !changed) {
                //run Metod Retract with pullback = 0, speed = 0.9, and distance = 0
                robot.Retract(0,0.9, 0);
                //since this method affects the lift, reset liftPosition
                liftPosition = 0;
                //activate toggle
                changed = true;
                //if neither a nor y is pressed, turn off toggle
            } else if(!gamepad2.a && !gamepad1.y && changed) {
                changed = false;
            }

            // Setup a variable for each drive wheel to save power level for telemetry
//            double rightIntakePower;
//            double leftIntakePower;
            double frontRightPower;
            double backRightPower;
            double frontLeftPower;
            double backLeftPower;

            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            double strafe = -gamepad1.left_stick_x;
            double drive = gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;
            frontLeftPower = Range.clip(drive + turn - strafe, -1.0, 1.0);
            backLeftPower = Range.clip(drive + turn + strafe, -1.0, 1.0);
            backRightPower = Range.clip(drive - turn + strafe, -1.0, 1.0);
            frontRightPower = Range.clip(drive - turn - strafe, -1.0, 1.0);
//            rightIntakePower = Range.clip(-gamepad1.right_trigger + gamepad1.left_trigger,-1.0,1.0); //controls for the Intake motors
//            leftIntakePower = Range.clip(-gamepad1.right_trigger + gamepad1.left_trigger, -1.0, 1.0);
//

//            rightIntakePower = (float) scaleInput(rightIntakePower);
//            leftIntakePower = (float) scaleInput(leftIntakePower);
            frontLeftPower = (float) scaleInput(frontLeftPower);
            backLeftPower = (float) scaleInput(backLeftPower);
            frontRightPower = (float) scaleInput(frontRightPower);
            backRightPower =  (float) scaleInput(backRightPower);
            // Send calculated power to wheels and intake motors
            robot.frontLeft.setPower(frontLeftPower);
            robot.backLeft.setPower(backLeftPower);
            robot.frontRight.setPower(frontRightPower);
            robot.backRight.setPower(backRightPower);
//            robot.rightIntake.setPower(rightIntakePower);
//            robot.leftIntake.setPower(leftIntakePower);


            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
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

    //so trigger grazing doesn't activate the intake
    boolean RightTrigger() {
        if (gamepad1.right_trigger >= 0.1) {
            return(true); }
        else return(false); }

    boolean LeftTrigger() {
        if (gamepad1.left_trigger >= 0.1) {
            return(true); }
        else return(false); }
}