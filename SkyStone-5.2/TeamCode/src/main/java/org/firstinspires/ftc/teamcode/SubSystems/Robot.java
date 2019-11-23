/*
This is where all of the robot's hardware that isn't a sensor and actions for autonomous lives and where we init them and declare the variables
 */

package org.firstinspires.ftc.teamcode.SubSystems;

//import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Robot {

    //setting up variables for each motor
    public DcMotor backRight = null;
    public DcMotor backLeft = null;
    public DcMotor frontRight = null;
    public DcMotor frontLeft = null;
    public DcMotor rightIntake = null;
    public DcMotor leftIntake = null;
    public DcMotor lift = null;
    public DcMotor liftSlide = null;

    //setting up variables for servos
    public Servo blockGrabber = null;
    public Servo foundationGrabber = null;

    //limitI --> lift limit switch, limitII --> block detector
    public DigitalChannel limitI = null;
    public DigitalChannel limitII = null;

    //REV blinkin for the LEDs
//    public RevBlinkinLedDriver blinkinLedDriver = null;


    //most important part of the code:
    //;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

    //information for the encoders in the motor
    public static final double COUNTS_PER_MOTOR_REV =  537.6;    // eg: Andy Mark Motor Encoder
    public static final double DRIVE_GEAR_REDUCTION = 1;
    public static final double WHEEL_DIAMETER_INCHES = 1.10236;     // For figuring circumference
    public static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    public static final double COUNTS_PER_MOTOR_REV1 =  1478.4 ;    // eg: Andy Mark Motor Encoder
    public static final double DRIVE_GEAR_REDUCTION1 = 1;
    public static final double WHEEL_DIAMETER_INCHES1 = 1.10236;     // For figuring circumference
    public static final double COUNTS_PER_INCH1 = (COUNTS_PER_MOTOR_REV1 * DRIVE_GEAR_REDUCTION1) /
            (WHEEL_DIAMETER_INCHES1 * 3.1415);

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

        rightIntake = ahwMap.get(DcMotor.class,"rIntake");
        leftIntake = ahwMap.get(DcMotor.class,"lIntake");
        lift = ahwMap.get(DcMotor.class,"lift");
        liftSlide = ahwMap.get(DcMotor.class,"liftSlide");

        blockGrabber = ahwMap.get(Servo.class, "csServo");
        foundationGrabber= ahwMap.get(Servo.class, "grabber");

        limitI = ahwMap.get(DigitalChannel.class, "limitI");
        limitII = ahwMap.get(DigitalChannel.class, "limitII");

//        blinkinLedDriver = ahwMap.get(RevBlinkinLedDriver.class, "blinkin");

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

        lift.setDirection(DcMotor.Direction.REVERSE);

        // adding brakes to the motor so they immediately stop when told
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftIntake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightIntake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


    }
    public void DForward (double speed, double distance){
        backLeft.setTargetPosition((int) (-distance * COUNTS_PER_INCH));
        frontLeft.setTargetPosition((int) (-distance * COUNTS_PER_INCH));
        backRight.setTargetPosition((int) (-distance * COUNTS_PER_INCH));
        frontRight.setTargetPosition((int) (-distance * COUNTS_PER_INCH));

        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        backLeft.setPower(speed);
        frontLeft.setPower(speed);
        backRight.setPower(speed);
        frontRight.setPower(speed);
    }
    public void Up(double speed, double distance){
        lift.setTargetPosition((int) (distance * COUNTS_PER_INCH));

        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        lift.setPower(speed);
        }
    public void Down(double speed, double distance){
        lift.setTargetPosition((int) (distance * COUNTS_PER_INCH));

        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        lift.setPower(-speed);
    }

    public void Retract(double pullBack, double speed, double distance) {
        blockGrabber.setPosition(0.7);

        liftSlide.setTargetPosition((int) (pullBack * COUNTS_PER_INCH1));
        liftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftSlide.setPower(-speed);

        lift.setTargetPosition((int) (distance * COUNTS_PER_INCH));
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lift.setPower(-speed);
    }

    enum state{
        PULLUP,
        GRAB,
        PICKUP
    };

    public void Grab(double extended, double speed, double avoidanceHeight, double grabHeight, double pickupHeight) {
        state myVar = state.PULLUP;
        for (int i = 0; i < 4; i++){
            switch (myVar) {
                case PULLUP:
                    blockGrabber.setPosition(0.7);
                    lift.setTargetPosition((int) (avoidanceHeight * COUNTS_PER_INCH));
                    lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    lift.setPower(speed);
                    leftIntake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    while (limitII.getState() == false) {
                        rightIntake.setPower(-1);
                        leftIntake.setPower(1);
                    }
                    myVar = state.GRAB;
                    break;
                case GRAB:
                    lift.setTargetPosition((int) (grabHeight * COUNTS_PER_INCH));
                    lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    lift.setPower(speed);
                    myVar = state.PICKUP;

                    blockGrabber.setPosition(0.9);
                    myVar = state.PICKUP;
                    break;
                case PICKUP:
                    liftSlide.setTargetPosition((int) (extended * COUNTS_PER_INCH1));
                    liftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    liftSlide.setPower(speed / 1.5);

                    lift.setTargetPosition((int) (pickupHeight * COUNTS_PER_INCH));
                    lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    lift.setPower(speed);
                    break;
            }
            //Skylar, have a nice day! Elizabeth, DON'T TOUCH THIS CODE!
        }
    }



    public void Kill(){
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
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
        if (Math.abs(frontLeft.getCurrentPosition() / COUNTS_PER_INCH) >= distance
                || Math.abs(backRight.getCurrentPosition() / COUNTS_PER_INCH) >= distance)
        {
            return (true);
        } else return (false);
    }
}