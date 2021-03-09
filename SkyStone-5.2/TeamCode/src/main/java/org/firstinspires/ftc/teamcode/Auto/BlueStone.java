package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.SubSystems.Robot;
import org.firstinspires.ftc.teamcode.SubSystems.Sensors;

import java.util.List;
import java.util.Locale;

@Autonomous (name = "BlueStone")

public class BlueStone extends OpMode {
    Robot robot = new Robot();
    Sensors sensors = new Sensors();

    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";
    String label;
    int count = 1;
    boolean skystoneFound = false;

    private static final String VUFORIA_KEY =
            "AT0lsGH/////AAABmWdeMnwGNEBKt9Q07ctS5kJx6GZjBc/WpGuN8MmlLDJR19MSfjSnECR8En3IENinlkTZMFKuBTPS+/l09Hhvzp60nZOSRQoedkj0rkwaOUFmw5ZjvAJHPztaEPJyLeZH1gfsLCe//CosN9awm07Xi5V41Z8hYmgAuEMSwdQ+fOToMSPt8zKDJW23SHQKm0ZXhg9kXGAV1ZwRUCZjtujXZSTELC/zIG/COgdf+z4r3Toqosp0xJxdYHoIZv0q8RSNrY0Go7HLgYJXOYzBPSKeB13fdws0a164dbfUkmLaZMxDtZWVeR03knp3uohWohjsINkEowEPmGG4oajdHQ5uJL50goDV3tVGlHqoFwJ/56Kv";

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;


    enum State {
        CenterForward, RightForward, LeftForward, MoveForward, Stop, TestStrafe, Straighten, Straighten2, Detection, LeftRaiseArm,LeftStrafeLeft, LeftReverse, LeftStraighten, p1StrafeLeft, p1Forward, p1Straighten, p2Forward, p3Forward, p1Deliver, p1Dispeanse, p1Return, p1ForwardIntake, p1ReverseToDeliver, p1Park, p2Straighten, p2angleIntake, p2StrafeLeft, p2ForwardIntake, p2Park, p2Dispeanse, p2Deliver, p2ReverseToDeliver, p2Reverse, p1angleIntake, p3Park, p3Dispeanse, p3Deliver, p3ReverseToDeliver, p3Reverse, p3ForwardIntake, p3StrafeLeft, p3angleIntake, p3Straighten, p1StrafeLeftbefore, p2StrafeLeftbefore, p1Reverse

    }

    State state;
    ElapsedTime time;

    @Override
    public void init() {
        robot.initRobot(hardwareMap);
        sensors.initSensors(hardwareMap);
        state = State.MoveForward;
        time = new ElapsedTime();
        initVuforia();
        initTfod();
        tfod.activate();
    }

    @Override
    public void loop() {
        double CurrentTime = time.time();
        telemetry.addData("Time", CurrentTime);
        double gyroangle;
        sensors.angles = sensors.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        gyroangle = Double.parseDouble(formatAngle(sensors.angles.angleUnit, sensors.angles.firstAngle));
        telemetry.addData("Heading", formatAngle(sensors.angles.angleUnit, sensors.angles.firstAngle));
        telemetry.addData("Current State",state.toString());
        telemetry.addData("Lift Servo Encoder", robot.stoneManager.getCurrentPosition());
        telemetry.update();

        switch (state){

            case MoveForward:
                robot.Forward(.75,17);
                if (robot.DriveDone(17)) {
                    state = State.Straighten;
                    Reset();
                }
                break;

            case Straighten:
                robot.TurnAbsolute(0, gyroangle);
                if (gyroangle >= -4 && gyroangle <= 4 && CurrentTime >= .25){
                    state = State.p1StrafeLeftbefore;
                    Reset();
                }
                break;

//            case TestStrafe:
//                robot.StrafeRight(.5,8);
//                if (robot.StrafeDriveDone(8)) {
//                    state = State.Straighten2;
//                    Reset();
//                }
//                break;
//
//            case Straighten2:
//                robot.TurnAbsolute(0, gyroangle);
//                if (gyroangle >= -2 && gyroangle <= 2 && CurrentTime >= .25){
//                    state = State.Stop;
//                    Reset();
//                }
//                break;

            case Detection:
                    if (tfod != null) {
                        // getUpdatedRecognitions() will return null if no new information is available since
                        // the last time that call was made.
                        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                        if (updatedRecognitions != null && CurrentTime >= .5 /*&& variable*/) {
                            telemetry.addData("# Object Detected", updatedRecognitions.size());
                            // step through the list of recognitions and display boundary info.
                            int i = 0;
                            for (Recognition recognition : updatedRecognitions) {
                                telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                                label = recognition.getLabel();
                                telemetry.addData(String.format("left,top (%d)", i), "%.03f , %.03f",
                                            recognition.getLeft(), recognition.getTop());
                                telemetry.addData(String.format("right,bottom (%d)", i), "%.03f , %.03f",
                                        recognition.getRight(), recognition.getBottom());
                            }
                            if (CurrentTime >= 1){
                                telemetry.addData("Found", ";-;");

                                robot.StrafeRight(.5, 4);
                                if (robot.StrafeDriveDone(4)) {
                                    count++;
                                    Reset();
                            }
                            else if (label == "Skystone" || count == 3) {
                                telemetry.addData("Found", "Skystone");
                                skystoneFound = true;
                                }
                            }
                            telemetry.update();
                        }
                        if (count == 1 && skystoneFound){
                            state = State.p1StrafeLeftbefore;
                        }
                        else if (count == 2 && skystoneFound){
                            state = State.CenterForward;
                        }
                        else if (count == 3 && skystoneFound){
                            state = State.RightForward;
                        }
                    }
                    break;

                    /// Position 1 branch

            case p1StrafeLeftbefore:
                robot.StrafeLeft(1, 7);
                if (robot.StrafeDriveDone(7)) {
                    state = State.p1Straighten;
                    Reset();
                }
                break;

            case p1Straighten:
                robot.TurnAbsolute(-30, gyroangle);
                if (gyroangle >= -34 && gyroangle <= -28 && CurrentTime >= .25){
                    state = State.p1ForwardIntake;
                    Reset();
                }
                break;

            case p1ForwardIntake:
                robot.IntakeBlock();
                robot.Forward(.55,7);
                if (robot.DriveDone(7)){
                    state = State.p1Reverse;
                    Reset();
                }
                break;

            case p1Reverse:
                robot.Reverse(.75, 5);
                if (robot.DriveDone(5)) {
                    state = State.p1angleIntake;
                    Reset();        
                }
                break;

            case p1angleIntake:
                robot.TurnAbsolute(0, gyroangle);
                if (gyroangle >= -4 && gyroangle <= 4){
                    state = State.p1ReverseToDeliver;
                    Reset();
                }
                break;

            case p1ReverseToDeliver:
                robot.Reverse(1,5);
                if (robot.DriveDone(5)) {
                    state = State.p1Deliver;
                    Reset();
                }
                break;
                
            case p1Deliver:
                robot.StrafeLeft(1, 35);
                if (robot.StrafeDriveDone(35)) {
                    robot.IntakeOff();

                    state = State.p1Dispeanse;
                    Reset();
                }
                break;
                
            case p1Dispeanse:
                robot.OuttakeBlock();
                if (CurrentTime == 2) {
                    state = State.p1Park;
                    Reset();
                }
                break;
                
            case p1Park:
                robot.StrafeRight(1, 10);
                if (robot.StrafeDriveDone(10)) {
                    state = State.Stop;
                    Reset();
                }
                break;

                /// Position 2 branch

            case Stop:
                robot.IntakeOff();
                robot.Death();
                break;

                }
            }

    private void Reset(){
        robot.StopUsingEncoders();
        robot.Kill();
        time.reset();
    }

    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.6;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }

    String formatAngle (AngleUnit angleUnit,double angle){
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees (double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }
}