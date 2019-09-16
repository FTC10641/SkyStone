import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class HardwareMap {

    //setting up variables for each motor
    public DcMotor backRight;
    public DcMotor backLeft;
    public DcMotor frontRight;
    public DcMotor frontLeft;

    public void init(hardwareMap ahwMap) {

        //configing each motor to the actual motor on robot and in phone config, ya know?
        backRight = hardwareMap.get(DcMotor.class, "rightB");
        backLeft = hardwareMap.get(DcMotor.class, "rightB");
        frontRight = hardwareMap.get(DcMotor.class, "rightF");
        frontLeft = hardwareMap.get(DcMotor.class, "leftF");

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