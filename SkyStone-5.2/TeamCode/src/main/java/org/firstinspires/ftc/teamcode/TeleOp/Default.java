/*
    This is the java class for our Tele op
 */

package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.SubSystems.Robot;
import org.firstinspires.ftc.teamcode.SubSystems.Sensors;

@TeleOp(name="TeleOp", group="Linear Opmode")
public class Default extends LinearOpMode {

    //Gives this code access to the Hardware Maps
    private Robot robot = new Robot();
    private Sensors sensors = new Sensors();

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private boolean toggle = true;
    private boolean toggle2 = false;


    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        robot.initRobot(hardwareMap);
        sensors.initSensors(hardwareMap);

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
                Lift Servos
                Capstone Servo
             */

            //this runs the Stone Manager controls which include the Lift Servos and the Block Grabber Servo
            robot.stoneManager.run(gamepad1);

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

            /*
            Gamepad 2 controls include:
                Capstone Servo
                Foundation Servos
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
                    robot.foundationServo1.setPosition(.9);
                    robot.foundationServo2.setPosition(0);
                }
            } else if (!gamepad2.a) {
                toggle = true; // Button has been released, so this allows a re-press to activate the code above.
            }

            if (gamepad2.dpad_up){
                robot.capstoneServo.setPosition(.5);
            }

            if (gamepad2.right_bumper){
                robot.RainbowMode();
            }
            else {
                robot.YellowMode();
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
            robot.frontLeft.setPower(gamepad1.right_stick_button ? frontLeftPower/2 : frontLeftPower);
            robot.backLeft.setPower(gamepad1.right_stick_button ? backLeftPower/2 : backLeftPower);
            robot.frontRight.setPower(gamepad1.right_stick_button ? frontRightPower/2 : frontRightPower);
            robot.backRight.setPower(gamepad1.right_stick_button ? backRightPower/2 : backRightPower);

            if (robot.insideSwitch.getState()) {
                robot.lift.setPower(liftPower);
            }
            else if (LeftTrigger()){
                robot.lift.setPower(liftPower);
            }
            else {
                robot.lift.setPower(0);
            }

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Robot", "left (%.2f), right (%.2f)",
                    backLeftPower, frontLeftPower, frontRightPower, backRightPower);
            telemetry.addData("Lift Servo Encoder", robot.stoneManager.getCurrentPosition());
            telemetry.update();
        }
    }
    //Makes the variable "left_trigger" into a boolean variable
    boolean LeftTrigger(){
        if (gamepad1.left_trigger >= 0.1){
            return (true);}
        else return (false);}
}