package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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

@Autonomous (name = "OneSkystoneRed")


public class OneSkystoneRed extends OpMode {
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
        CenterForward, RightForward, LeftForward, MoveForward,
        Stop, TestStrafe, Straighten, Straighten2, Detection,
        LeftRaiseArm,LeftStrafeLeft, LeftReverse, LeftStraighten,
        p1StrafeLeft, p1Forward, p1Straighten, p2Forward,
        p3Forward, p1Deliver, p1Dispeanse, p1Return,
        p1ForwardIntake, p1ReverseToDeliver, p1Park, p2Straighten,
        p2angleIntake, p2StrafeLeft, p2ForwardIntake, p2Park,
        p2Dispeanse, p2Deliver, p2ReverseToDeliver, p2Reverse,
        p1angleIntake, p3Park, p3Dispeanse, p3Deliver,
        p3ReverseToDeliver, p3Reverse, p3ForwardIntake,
        p3StrafeLeft, p3angleIntake, p3Straighten,
        p1StrafeLeftbefore, p2StrafeLeftbefore, p3StrafeLeftbefore,
        p1ForwardToDeliver, p1Dispense, p1StraightenToDeliver,
        p2Dispense, p2ForwardToDeliver, p3Dispense, p3ForwardToDeliver,
        p1Straighten2, p1ForwardToFinishDeliver, p1ToCase, p1StrafeRightbefore, p1Reverse

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
                robot.Forward(.75,18);
                if (robot.DriveDone(18)) {
                    state = State.Straighten;
                    Reset();
                }
                break;

            case Straighten:
                robot.TurnAbsolute(0, gyroangle);
                if (gyroangle >= -4 && gyroangle <= 4 && CurrentTime >= .25){
                    state = State.Detection;
                    Reset();
                }
                break;

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
                            state = State.p1StrafeRightbefore;
                        }
                        else if (count == 2 && skystoneFound){
                            state = State.p2StrafeLeftbefore;
                        }
                        else if (count == 3 && skystoneFound){
                            state = State.p3StrafeLeftbefore;
                        }
                    }
                    break;

             /*
            this is the start of the first position
             */

            case p1StrafeRightbefore:
                robot.StrafeRight(1, 2);
                if (robot.StrafeDriveDone(2)) {
                    state = State.p1Straighten;
                    Reset();
                }
                break;

            case p1Straighten:
                robot.TurnAbsolute(25, gyroangle);
                if (gyroangle >= 23 && gyroangle <= 27 && CurrentTime >= .25){
                    state = State.p1ToCase;
                    Reset();
                }
                break;

            case p1ToCase:
                robot.Forward(.45,20);
                if (robot.DriveDone(20)){
                    state = State.p1ForwardIntake;
                    Reset();
                }
                break;

            case p1ForwardIntake:
                robot.IntakeBlock();
                robot.Forward(.5,4);
                if (robot.DriveDone(4)){
                    state = State.p1Reverse;
                    Reset();
                }
                break;

            case p1Reverse:
                robot.Reverse(.75, 8);
                robot.IntakeOff();
                if (robot.DriveDone(8)) {
                    state = State.Stop;
                    Reset();
                }
                break;

            case p1angleIntake:
                robot.TurnAbsolute(-80, gyroangle);
                if (gyroangle >= -82 && gyroangle <= -78 && CurrentTime >= .5){
                    state = State.p1ForwardToDeliver;
                    Reset();
                }
                break;

            case p1ForwardToDeliver:
                robot.Forward(1, 31);
                if (robot.DriveDone(31)) {
                    robot.IntakeOff();
                    state = State.p1Straighten2;
                    Reset();
                }
                break;

            case p1Straighten2:
                robot.TurnAbsolute(-80, gyroangle);
                if (gyroangle >= -82 && gyroangle <= -78 && CurrentTime >= .5){
                    state = State.p1ForwardToFinishDeliver;
                    Reset();
                }
                break;

            case p1ForwardToFinishDeliver:
                robot.Forward(1, 31);
                if (robot.DriveDone(31)) {
                    robot.IntakeOff();
                    state = State.p1Dispense;
                    Reset();
                }
                break;

            case p1Dispense:
                robot.OuttakeBlock();
                if (CurrentTime >= 1) {
                    state = State.p1Park;
                    robot.IntakeOff();
                    Reset();
                }
                break;

            case p1Park:
                robot.Reverse(1, 15);
                if (robot.DriveDone(15)) {
                    state = State.Stop;
                    Reset();
                }
                break;

            /*
            this is the start of the second position
             */

            case p2StrafeLeftbefore:
                robot.StrafeLeft(1, 10);
                if (robot.StrafeDriveDone(10)) {
                    state = State.p2Straighten;
                    Reset();
                }
                break;

            case p2Straighten:
                robot.TurnAbsolute(-30, gyroangle);
                if (gyroangle >= -34 && gyroangle <= -26 && CurrentTime >= .25){
                    state = State.p2ForwardIntake;
                    Reset();
                }
                break;

            case p2ForwardIntake:
                robot.IntakeBlock();
                robot.Forward(.45,15);
                if (robot.DriveDone(15)){
                    state = State.p2Reverse;
                    Reset();
                }
                break;

            case p2Reverse:
                robot.Reverse(.75, 8);
                if (robot.DriveDone(8)) {
                    state = State.p2angleIntake;
                    Reset();
                }
                break;

            case p2angleIntake:
                robot.TurnAbsolute(-80, gyroangle);
                if (gyroangle >= -82 && gyroangle <= -78 && CurrentTime >= .5){
                    state = State.p2ForwardToDeliver;
                    Reset();
                }
                break;

            case p2ForwardToDeliver:
                robot.Forward(1, 53);
                if (robot.DriveDone(53)) {
                    robot.IntakeOff();
                    state = State.p2Dispense;
                    Reset();
                }
                break;

            case p2Dispense:
                robot.OuttakeBlock();
                if (CurrentTime >= 1) {
                    state = State.p2Park;
                    robot.IntakeOff();
                    Reset();
                }
                break;

            case p2Park:
                robot.Reverse(1, 9);
                if (robot.DriveDone(9)) {
                    state = State.Stop;
                    Reset();
                }
                break;

             /*
            this is the start of the third position
             */

            case p3StrafeLeftbefore:
                robot.StrafeLeft(1, 4);
                if (robot.StrafeDriveDone(4)) {
                    state = State.p3Straighten;
                    Reset();
                }
                break;

            case p3Straighten:
                robot.TurnAbsolute(-30, gyroangle);
                if (gyroangle >= -34 && gyroangle <= -26 && CurrentTime >= .25){
                    state = State.p3ForwardIntake;
                    Reset();
                }
                break;

            case p3ForwardIntake:
                robot.IntakeBlock();
                robot.Forward(.45,15);
                if (robot.DriveDone(15)){
                    state = State.p3Reverse;
                    Reset();
                }
                break;

            case p3Reverse:
                robot.Reverse(.75, 8);
                if (robot.DriveDone(8)) {
                    state = State.p3angleIntake;
                    Reset();
                }
                break;

            case p3angleIntake:
                robot.TurnAbsolute(-80, gyroangle);
                if (gyroangle >= -82 && gyroangle <= -78 && CurrentTime >= .5){
                    state = State.p3ForwardToDeliver;
                    Reset();
                }
                break;

            case p3ForwardToDeliver:
                robot.Forward(1, 49);
                if (robot.DriveDone(49)) {
                    robot.IntakeOff();
                    state = State.p3Dispense;
                    Reset();
                }
                break;

            case p3Dispense:
                robot.OuttakeBlock();
                if (CurrentTime >= 1) {
                    state = State.p3Park;
                    robot.IntakeOff();
                    Reset();
                }
                break;

            case p3Park:
                robot.Reverse(1, 10);
                if (robot.DriveDone(10)) {
                    state = State.Stop;
                    Reset();
                }
                break;

            case Stop:
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