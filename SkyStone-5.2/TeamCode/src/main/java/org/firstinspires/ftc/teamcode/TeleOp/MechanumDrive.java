package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.SubSystems.*;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a Mechanum Drive Teleop for a fouro wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="MechanumDrive", group="Linear Opmode")
public class MechanumDrive extends LinearOpMode {

    Motors motor = new Motors();
    Sensors sensors = new Sensors();

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private boolean changed = false;

    public float hsvValues[] = {0F, 0F, 0F};


    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        motor.initMotors(hardwareMap);
        sensors.initSensors(hardwareMap);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            sensors.ReadColor();

            if (RightTrigger()) {
                motor.rightIntake.setPower(-1);
                motor.leftIntake.setPower(1);
            } else if (LeftTrigger()) {
                motor.rightIntake.setPower(1);
                motor.leftIntake.setPower(-1);
            } else {
                motor.leftIntake.setPower(0);
                motor.rightIntake.setPower(0);
            }

            if (gamepad1.y && !changed) {
                if (motor.sensorServo.getPosition() <= .3) {
                    motor.sensorServo.setPosition(.85);
                } else motor.sensorServo.setPosition(.2);
                changed = true;
            } else if (!gamepad1.y) changed = false;

            if (sensors.blue()){
                telemetry.addLine("bruh thats pretty blue");
            }

            if (sensors.red()) {
                telemetry.addLine("really, red, thats pretty rad ");
            }


            // Setup a variable for each drive wheel to save power level for telemetry
            double frontRightPower;
            double backRightPower;
            double frontLeftPower;
            double backLeftPower;

            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            double strafe = gamepad1.left_stick_x;
            double drive = gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;
            frontLeftPower = Range.clip(drive + turn + strafe, -1.0, 1.0);
            backLeftPower = Range.clip(drive + turn - strafe, -1.0, 1.0);
            backRightPower = Range.clip(drive - turn + strafe, -1.0, 1.0);
            frontRightPower = Range.clip(drive - turn - strafe, -1.0, 1.0);


            // Send calculated power to wheels
            motor.frontLeft.setPower(frontLeftPower);
            motor.backLeft.setPower(backLeftPower);
            motor.frontRight.setPower(frontRightPower);
            motor.backRight.setPower(backRightPower);


            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)",
                    backLeftPower, frontLeftPower, frontRightPower, backRightPower);
            telemetry.addData("hue", hsvValues[0]);
            telemetry.update();
        }
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