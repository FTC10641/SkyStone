/*
This is where all of the robot's motors live and where we init them and declare the variables
 */

package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Motors {

    //setting up variables for each motor
    public DcMotor backRight;
    public DcMotor backLeft;
    public DcMotor frontRight;
    public DcMotor frontLeft;

    HardwareMap hwMap = null;



    public void initMotors(HardwareMap ahwMap) {
        hwMap = ahwMap;


        //configing each motor to the actual motor on robot and in phone config, ya know?
        backRight = ahwMap.get(DcMotor.class, "rightB");
        backLeft = ahwMap.get(DcMotor.class, "leftB");
        frontRight = ahwMap.get(DcMotor.class, "rightF");
        frontLeft = ahwMap.get(DcMotor.class, "leftF");

        // reversing the right motors to be not gei
        backRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);

        // so breaks instant good
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


    }
}