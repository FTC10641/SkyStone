/*
This is where all of the robot's sensors live and where we init them and declare the variables
 */

package org.firstinspires.ftc.teamcode.SubSystems;

import android.graphics.Color;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class Sensors extends Robot {

    public BNO055IMU imu;

//    public ColorSensor colorSensor = null;
//
//    public float hsvValues[] = {0F, 0F, 0F};
//
//    public final float values[] = hsvValues;
//
//    public final double SCALE_FACTOR = 255;

    public Orientation angles;

    HardwareMap hwMap = null;


    public void initSensors(HardwareMap ahwMap) {
        hwMap = ahwMap;

        //colorSensor = ahwMap.get(ColorSensor.class, "colorS");

        imu = hwMap.get(BNO055IMU.class, "imu");

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu.initialize(parameters);

    }
}
//    public void ReadColor(){
//        // convert the RGB values to HSV values.
//        // multiply by the SCALE_FACTOR.
//        // then cast it back to int (SCALE_FACTOR is a double)
//        Color.RGBToHSV((int) (colorSensor.red() * SCALE_FACTOR),
//                (int) (colorSensor.green() * SCALE_FACTOR),
//                (int) (colorSensor.blue() * SCALE_FACTOR),
//                hsvValues);
//    }
//
//    public boolean red(){
//        if (hsvValues[0] >= 340 && hsvValues[0] <= 370 || hsvValues[0] >= 0 && hsvValues[0] <= 40){
//            return(true);}
//        else return(false);
//    }
//    public boolean blue(){
//        if (hsvValues[0] >= 190 && hsvValues[0] <= 300){
//            return(true);}
//        else return(false);
//    }
//}