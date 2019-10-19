/*
This is where all of the robot's actions that we use in our autonomous live and where we init them and declare the variables
 */
package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Actions extends Robot {

    public static final double COUNTS_PER_MOTOR_REV = 537.6;
    public static final double DRIVE_GEAR_REDUCTION = 1.333333333333333;
    public static final double WHEEL_DIAMETER_INCHES = 4.0;
    public static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    HardwareMap hwMap = null;

    public void initActions(HardwareMap ahwMap) {
        hwMap = ahwMap;
    }

        //    public static final double COUNTS_PER_MOTOR_REV =  360; //the counts per revolution of the encoder
//    public static final double DRIVE_GEAR_REDUCTION = 2; //the gear ratio
//    public static final double WHEEL_DIAMETER_INCHES = ;   //the diameter of the odometry wheel goes here
//    public static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
//            (WHEEL_DIAMETER_INCHES * 3.1415);

        public void DForward (double speed, double distance){
            backLeft.setTargetPosition((int) (distance * COUNTS_PER_INCH));
            frontLeft.setTargetPosition((int) (distance * COUNTS_PER_INCH));
            backRight.setTargetPosition((int) (distance * COUNTS_PER_INCH));
            frontRight.setTargetPosition((int) (distance * COUNTS_PER_INCH));

            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            backLeft.setPower(speed);
            frontLeft.setPower(speed);
            backRight.setPower(speed);
            frontRight.setPower(speed);
        }

        public void Kill () {
            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }

        public boolean DriveDone ( double distance){
            if ((Math.abs(frontLeft.getCurrentPosition() / COUNTS_PER_INCH) >= distance) ||
                    (Math.abs(backLeft.getCurrentPosition() / COUNTS_PER_INCH) >= distance)
                    || (Math.abs(frontRight.getCurrentPosition() / COUNTS_PER_INCH) >= distance) ||
                    (Math.abs(backRight.getCurrentPosition() / COUNTS_PER_INCH) >= distance)
            )
            {
                return (true);
            }
            else return (false);
        }
    }