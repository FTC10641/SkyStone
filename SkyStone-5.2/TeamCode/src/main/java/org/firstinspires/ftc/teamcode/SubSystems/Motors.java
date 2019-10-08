/*
This is where all of the robot's motors live and where we init them and declare the variables
 */

package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Motors {

    //setting up variables for each motor
    public DcMotor backRight = null;
    public DcMotor backLeft = null;
    public DcMotor frontRight = null;
    public DcMotor frontLeft = null;
    public DcMotor rightIntake = null;
    public DcMotor leftIntake = null;

    //setting up variables for servos
    public Servo sensorServo = null;

    //most important part of the code:
    //;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

    public static final double COUNTS_PER_MOTOR_REV =  537.6;    //the amount of ticks per 1 revolution of the motor
    public static final double DRIVE_GEAR_REDUCTION = 1.333333333333333;
    public static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    public static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    HardwareMap hwMap = null;

    public void initMotors(HardwareMap ahwMap) {
        hwMap = ahwMap;


        //configing each motor to the actual motor on robot and in phone config, ya know?
        backRight = ahwMap.get(DcMotor.class, "rightB");
        backLeft = ahwMap.get(DcMotor.class, "leftB");
        frontRight = ahwMap.get(DcMotor.class, "rightF");
        frontLeft = ahwMap.get(DcMotor.class, "leftF");

        //for the 2 intake motors
        rightIntake = ahwMap.get(DcMotor.class,"rIntake");
        leftIntake = ahwMap.get(DcMotor.class,"lIntake");

        sensorServo = ahwMap.get(Servo.class, "csServo");


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

        // adding breaks to the motor so they immediately stop when told
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftIntake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightIntake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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
    public void Kill(){
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
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