/*
This is where all of the robot's hardware that isn't a sensor and actions for autonomous lives and where we init them and declare the variables
 */

package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


public class Robot {

    //setting up variables for each motor
    public DcMotor backRight = null;
    public DcMotor backLeft = null;
    public DcMotor frontRight = null;
    public DcMotor frontLeft = null;
    public DcMotor rightIntake = null;
    public DcMotor leftIntake = null;
    public DcMotor lift = null;
    private DcMotor totallyRealMotor = null;

    public DcMotor verticalLeft;
    public DcMotor verticalRight;
    public DcMotor horizontal;

    //setting up variables for servos
    public Servo foundationServo1 =  null;
    public Servo foundationServo2 = null;
    public Servo blockGrabber = null;
    public CRServo liftServo1 = null;
    public CRServo liftServo2 = null;
    public Servo capstoneServo = null;

    public DigitalChannel insideSwitch = null;
    public DigitalChannel outsideSwitch = null;

    //REV blinkin for the LEDs
    public RevBlinkinLedDriver blinkinLedDriver = null;

    private RevBlinkinLedDriver.BlinkinPattern rainbowMode;
    private RevBlinkinLedDriver.BlinkinPattern yellowMode;
    private RevBlinkinLedDriver.BlinkinPattern off;


    public double liftPower;
    public double frontRightPower;
    public double backRightPower;
    public double frontLeftPower;
    public double backLeftPower;

    public StoneManager stoneManager = null;

    /*
    Lift Encoder Information
     */
    public final double COUNTS_PER_MOTOR_REV =  537.6;    // eg: Andy Mark Motor Encoder
    public final double DRIVE_GEAR_REDUCTION = 1;
    public final double PINION_DIAMETER_INCHES = 1.10236;     // For figuring circumference
    public final double LiftCountsPerInch = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (PINION_DIAMETER_INCHES * 3.1415);

    /*
    Odometry Encoder information
     */
    public final double COUNTS_PER_MOTOR_REV2 =  360;
    public final double DRIVE_GEAR_REDUCTION2 = 2;
    public final double WHEEL_DIAMETER_INCHES2 = 2;     // For figuring circumference
    public final double OdometryCountsPerInch = (COUNTS_PER_MOTOR_REV2 * DRIVE_GEAR_REDUCTION2) / (WHEEL_DIAMETER_INCHES2 * 3.1415);

    HardwareMap hwMap = null;

    public void initRobot(HardwareMap ahwMap) {
        hwMap = ahwMap;

        /*
        naming hardware for the configuration file on the Robot Controller
         */
        backRight = ahwMap.get(DcMotor.class, "rightB");
        backLeft = ahwMap.get(DcMotor.class, "leftB");
        frontRight = ahwMap.get(DcMotor.class, "rightF");
        frontLeft = ahwMap.get(DcMotor.class, "leftF");

        //odometry wheels
        String backR = "rightB";
        String backL = "leftB";
        String frontR = "rightF";
        String frontL = "leftF";

        String verticalLeftEncoderName = frontR; //frontR -
        String verticalRightEncoderName = frontL; //frontL - port 2
        String horizontalEncoderName = backL;  //backL
        //05-VR,06-VL,07-H

        verticalLeft = ahwMap.dcMotor.get(verticalLeftEncoderName);
        verticalRight = ahwMap.dcMotor.get(verticalRightEncoderName);
        horizontal = ahwMap.dcMotor.get(horizontalEncoderName);

        rightIntake = ahwMap.get(DcMotor.class,"rIntake");
        leftIntake = ahwMap.get(DcMotor.class,"lIntake");
        lift = ahwMap.get(DcMotor.class,"lift");
        totallyRealMotor = ahwMap.dcMotor.get("real");

        foundationServo1 = ahwMap.get(Servo.class, "found1");
        foundationServo2 = ahwMap.get(Servo.class, "found2");
        blockGrabber = ahwMap.get(Servo.class, "grabber");
        liftServo1 = ahwMap.get(CRServo.class, "liftS1");
        liftServo2 = ahwMap.get(CRServo.class, "liftS2");
        capstoneServo = ahwMap.get(Servo.class, "capstone");


        insideSwitch = ahwMap.get(DigitalChannel.class, "inSwitch");
        outsideSwitch = ahwMap.get(DigitalChannel.class, "outSwitch");


        blinkinLedDriver = ahwMap.get(RevBlinkinLedDriver.class, "LED");

        rainbowMode = RevBlinkinLedDriver.BlinkinPattern.RAINBOW_RAINBOW_PALETTE;
        yellowMode = RevBlinkinLedDriver.BlinkinPattern.YELLOW;
        off = RevBlinkinLedDriver.BlinkinPattern.BLACK;

        backRight.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        frontLeft.setPower(0);
        rightIntake.setPower(0);
        leftIntake.setPower(0);

        // reversing the right motors so it can drive straight
        backRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);

//        horizontal.setDirection(DcMotor.Direction.REVERSE);
//       verticalLeft.setDirection(DcMotor.Direction.REVERSE);
//        verticalRight.setDirection(DcMotor.Direction.REVERSE);
        //01-FL, 02-BL, 04-BR, 03-FR

        lift.setDirection(DcMotor.Direction.REVERSE);

        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        verticalLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        verticalRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        horizontal.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        verticalLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        verticalRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        horizontal.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        totallyRealMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        totallyRealMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        // adding brakes to the motor so they immediately stop when told
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftIntake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightIntake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeftPower = (float) scaleInput(frontLeftPower);
        backLeftPower = (float) scaleInput(backLeftPower);
        frontRightPower = (float) scaleInput(frontRightPower);
        backRightPower = (float) scaleInput(backRightPower);
        liftPower = (float) scaleInput(liftPower);

        stoneManager = new StoneManager(totallyRealMotor, blockGrabber, liftServo1, liftServo2);
    }

    /**
     * This Scale Array function establishes a set of increasing values
     * that is used to control the speed of motors and servos. It allows them to exponentially increases their rotation.
     **/
    public double scaleInput(double dVal) {
        double[] scaleArray = {0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00};

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



    public void Forward(double speed, double distance){
        frontRight.setTargetPosition((int) (-distance * OdometryCountsPerInch));
        backRight.setTargetPosition((int) (-distance * OdometryCountsPerInch));
        frontLeft.setTargetPosition((int) (-distance * OdometryCountsPerInch));
        backLeft.setTargetPosition((int) (-distance * OdometryCountsPerInch));

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontRight.setPower(-speed);
        backRight.setPower(-speed);
        frontLeft.setPower(-speed);
        backLeft.setPower(-speed);
    }

    public void Reverse(double speed, double distance){
        frontRight.setTargetPosition((int) (distance * OdometryCountsPerInch));
        backRight.setTargetPosition((int) (distance * OdometryCountsPerInch));
        frontLeft.setTargetPosition((int) (distance * OdometryCountsPerInch));
        backLeft.setTargetPosition((int) (distance * OdometryCountsPerInch));

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontRight.setPower(speed);
        backRight.setPower(speed);
        frontLeft.setPower(speed);
        backLeft.setPower(speed);
    }

    public void StrafeLeft(double speed, double distance){
        frontRight.setTargetPosition((int) (-distance * OdometryCountsPerInch));
        backRight.setTargetPosition((int) (distance * OdometryCountsPerInch));
        frontLeft.setTargetPosition((int) (distance * OdometryCountsPerInch));
        backLeft.setTargetPosition((int) (-distance * OdometryCountsPerInch));

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontRight.setPower(-speed);
        backRight.setPower(speed);
        frontLeft.setPower(speed);
        backLeft.setPower(-speed);
    }

    public void StrafeRight(double speed, double distance){
        frontRight.setTargetPosition((int) (distance * OdometryCountsPerInch));
        backRight.setTargetPosition((int) (-distance * OdometryCountsPerInch));
        frontLeft.setTargetPosition((int) (-distance * OdometryCountsPerInch));
        backLeft.setTargetPosition((int) (distance * OdometryCountsPerInch));

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontRight.setPower(speed);
        backRight.setPower(-speed);
        frontLeft.setPower(-speed);
        backLeft.setPower(speed);
    }

    public void TurnAbsolute(double target, double heading){

        verticalLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        verticalRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        horizontal.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        double Error = heading - target;
        double Kp = 0.017;
        double LFPower;
        double LRPower;
        double RFPower;
        double RRPower;
        double EPower;

        if ((Math.abs(Error)) > 2 ){
            LFPower = -Error * Kp;
            LRPower = -Error * Kp;
            RFPower = Error * Kp;
            RRPower = Error * Kp;
            EPower = Error * Kp;

            Range.clip(LFPower,-1,1);
            Range.clip(LRPower,-1,1);
            Range.clip(RFPower,-1,1);
            Range.clip(RRPower,-1,1);
            Range.clip(EPower,-1,1);

            frontLeft.setPower(LFPower);
            backLeft.setPower(LRPower);
            frontRight.setPower(RFPower);
            backRight.setPower(RRPower);
        }
        else {
            frontLeft.setPower(0);
            backLeft.setPower(0);
            frontRight.setPower(0);
            backRight.setPower(0);
        }
    }
    
    public void Kill(){
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        verticalLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        verticalRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        horizontal.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void Death(){
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);
    }

    //this method is very important, DON'T delete
    public void StopUsingEncoders(){
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        verticalLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        verticalRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        horizontal.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    public void ServosDown(){
       foundationServo1.setPosition(1);
       foundationServo2.setPosition(0);
    }

    public void ServosUp(){
        foundationServo1.setPosition(0);
        foundationServo2.setPosition(1);
    }

    public void IntakeBlock(){
        rightIntake.setPower(.75);
        leftIntake.setPower(-.75);
    }

    public void OuttakeBlock(){
        rightIntake.setPower(-1);
        leftIntake.setPower(1);
    }

    public void IntakeOff(){
        rightIntake.setPower(0);
        leftIntake.setPower(0);
    }

    public void SetDriveMotorsToFloat(){
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

    }

    public void SetDriveMotorToBrake(){
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void RainbowMode(){
        blinkinLedDriver.setPattern(rainbowMode);
    }

    public void YellowMode(){
        blinkinLedDriver.setPattern(yellowMode);
    }

    public void LEDOff(){
        blinkinLedDriver.setPattern(off);
    }


    /**
     Checks to see if the motors are running
     and if they're not it will return true
     */
    public boolean IsBusy(){
        if (!frontLeft.isBusy() || !backRight.isBusy())
        {
            return (true);
        } else return (false);
    }

    public boolean DriveDone(double distance){
        if (
            (Math.abs(verticalLeft.getCurrentPosition() / OdometryCountsPerInch) >= distance
            || Math.abs(verticalRight.getCurrentPosition() / OdometryCountsPerInch) >= distance)
        )
        {
            return (true);
        }
        else return (false);
    }

    public boolean StrafeDriveDone(double distance){
        if (
            (Math.abs(verticalLeft.getCurrentPosition() / OdometryCountsPerInch) >= 0
            && Math.abs(verticalRight.getCurrentPosition() / OdometryCountsPerInch) >= 0)
            && Math.abs(horizontal.getCurrentPosition() / OdometryCountsPerInch) >= distance
        )
        {
            return (true);
        }
        else return (false);
    }
}