/*
    This is the java class for our Tele op
 */

package org.firstinspires.ftc.teamcode.TestCode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.SubSystems.Robot;
import org.firstinspires.ftc.teamcode.SubSystems.Sensors;

@TeleOp(name="TestingLift", group="Linear Opmode")
@Disabled
public class TestLift extends LinearOpMode {

    //Gives this code access to the Hardware Maps
    Robot robot = new Robot();
    Sensors sensors = new Sensors();

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private boolean toggle = true;
    private boolean toggle2 = false;

    private boolean toggle3 = true;
    private boolean toggle4 = false;


    //    lift variables
    private double maxLiftHeight = 5;
    private double minLiftHeight = 0;

    RevBlinkinLedDriver.BlinkinPattern humanPlayerIndicator;
    RevBlinkinLedDriver.BlinkinPattern noStone;


    private int maxCarriage = 19;
    private int minCarriage = 0;
    private boolean carriageToggle = false;
    private boolean carriageOn = false;
    private int carriagePosition = 0;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        robot.initRobot(hardwareMap);
        sensors.initSensors(hardwareMap);

        humanPlayerIndicator = RevBlinkinLedDriver.BlinkinPattern.BEATS_PER_MINUTE_RAINBOW_PALETTE;
        noStone = RevBlinkinLedDriver.BlinkinPattern.YELLOW;

        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            /*
            Gamepad 1 controls include:
                Intake
                Block Grabber
                Drive Train
                Lift
             */

            //Intake
            if (gamepad1.right_bumper) {
                robot.rightIntake.setPower(1);
                robot.leftIntake.setPower(-1);
            }
            else if (gamepad1.left_bumper) {
                robot.rightIntake.setPower(-1);
                robot.leftIntake.setPower(1);
            }
            else {
                robot.leftIntake.setPower(0);
                robot.rightIntake.setPower(0);
            }

            if (toggle3 && gamepad1.a) {  // Only execute once per Button push
                toggle3 = false;  // Prevents this section of code from being called again until the Button is released and re-pressed
                if (toggle4) {
                    toggle4 = false;
                    robot.blockGrabber.setPosition(0);
                }
                else {
                    toggle4 = true;
                    robot.blockGrabber.setPosition(1);
                }
            } else if (!gamepad1.a) {
                toggle3 = true; // Button has been released, so this allows a re-press to activate the code above.
            }

            if (gamepad1.x){
                robot.liftServo1.setPower(1);
                robot.liftServo2.setPower(-1);
            }
            else if (gamepad1.y){
                robot.liftServo1.setPower(-1);
                robot.liftServo2.setPower(1);
            }
            else {
                robot.liftServo1.setPower(0);
                robot.liftServo2.setPower(0);
            }


            /*
            Gamepad 2 controls include:
                Foundation Servos
                Indexer Servo
             */

            //foundation grabber
            if (toggle && gamepad2.a) {  // Only execute once per Button push
                toggle = false;  // Prevents this section of code from being called again until the Button is released and re-pressed
                if (toggle2) {
                    toggle2 = false;
                    robot.foundationServo1.setPosition(0);
                    robot.foundationServo2.setPosition(1);
                }
                else {
                    toggle2 = true;
                    robot.foundationServo1.setPosition(1);
                    robot.foundationServo2.setPosition(0);
                }
            } else if (!gamepad2.a) {
                toggle = true; // Button has been released, so this allows a re-press to activate the code above.
            }

            double liftPower;
            double frontRightPower;
            double backRightPower;
            double frontLeftPower;
            double backLeftPower;

            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            double strafe = gamepad1.left_stick_x;
            double drive = gamepad1.left_stick_y;
            double turn = -gamepad1.right_stick_x;

            frontLeftPower = Range.clip(drive + turn - strafe, -1.0, 1.0);
            backLeftPower = Range.clip(drive + turn + strafe, -1.0, 1.0);
            backRightPower = Range.clip(drive - turn - strafe, -1.0, 1.0);
            frontRightPower = Range.clip(drive - turn + strafe, -1.0, 1.0);

            liftPower = Range.clip(gamepad1.right_trigger - gamepad1.left_trigger, -1.0, 1.0);

            //Drive Controls, Adds a AntiTurbo Button else it will drive full speed
            robot.frontLeft.setPower(gamepad2.y ? frontLeftPower/2 : frontLeftPower);
            robot.backLeft.setPower(gamepad2.y ? backLeftPower/2 : backLeftPower);
            robot.frontRight.setPower(gamepad2.y ? frontRightPower/2 : frontRightPower);
            robot.backRight.setPower(gamepad2.y ? backRightPower/2 : backRightPower);

            robot.lift.setPower(liftPower);

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Lift", robot.lift.getCurrentPosition());
            telemetry.addData("Robot", "left (%.2f), right (%.2f)",
                    backLeftPower, frontLeftPower, frontRightPower, backRightPower);
            telemetry.update();
        }
    }
}