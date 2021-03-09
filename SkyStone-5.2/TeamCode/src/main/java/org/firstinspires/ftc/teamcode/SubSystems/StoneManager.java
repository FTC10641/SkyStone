package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

public class StoneManager {
    //insert comment here
    private DcMotor totallyRealMotor;

    private Servo blockGrabber;
    private CRServo liftServo1;
    private CRServo liftServo2;

    private boolean flagged = false;

    private static final int INTAKEPOSITION = 100;

    public StoneManager(DcMotor dcMotorIn, Servo blockGrabberIn, CRServo liftServo1In, CRServo liftServo2In){
        totallyRealMotor = dcMotorIn;
        blockGrabber = blockGrabberIn;
        liftServo1 = liftServo1In;
        liftServo2 = liftServo2In;
    }

    public int getCurrentPosition(){
        return totallyRealMotor.getCurrentPosition();
    }

    private void raiseArm(){
        liftServo1.setPower(-1);
        liftServo2.setPower(1);
    }

    private void lowerArm(){
        liftServo1.setPower(1);
        liftServo2.setPower(-1);
    }

    private void stopArm(){
        liftServo1.setPower(0);
        liftServo2.setPower(0);
    }

    public void raiseArmToIntakePosition(){
        if (this.getCurrentPosition() < INTAKEPOSITION){
            this.raiseArm();
        }
        else if (this.getCurrentPosition() >= INTAKEPOSITION){
            this.stopArm();
        }
    }


    public void toggleStoneGrabber(){
        if (blockGrabber.getPosition() == 0){
            blockGrabber.setPosition(1);
        }
        else if (blockGrabber.getPosition() == 1){
            blockGrabber.setPosition(0);
        }
    }

    public void run(Gamepad gamepad1){
        if (gamepad1.x){
            lowerArm();
        }
        else if (gamepad1.y){
            raiseArm();
        }
        else {
            stopArm();
        }
//
//        if (gamepad1.y){
//            this.raiseArmToIntakePosition();
//        }
//        else {
//            stopArm();
//        }

        if (!flagged && gamepad1.b){
            flagged = true;
            toggleStoneGrabber();
        }
        else if (flagged && !gamepad1.b){
            flagged = false;
        }
    }
}
