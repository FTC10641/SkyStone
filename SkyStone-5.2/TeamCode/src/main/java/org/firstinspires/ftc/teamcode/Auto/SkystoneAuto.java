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

@Autonomous (name = "SkystoneAuto")

public class SkystoneAuto extends OpMode {
    Robot robot = new Robot();
    Sensors sensors = new Sensors();

    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";

    private static final String VUFORIA_KEY =
            "AT0lsGH/////AAABmWdeMnwGNEBKt9Q07ctS5kJx6GZjBc/WpGuN8MmlLDJR19MSfjSnECR8En3IENinlkTZMFKuBTPS+/l09Hhvzp60nZOSRQoedkj0rkwaOUFmw5ZjvAJHPztaEPJyLeZH1gfsLCe//CosN9awm07Xi5V41Z8hYmgAuEMSwdQ+fOToMSPt8zKDJW23SHQKm0ZXhg9kXGAV1ZwRUCZjtujXZSTELC/zIG/COgdf+z4r3Toqosp0xJxdYHoIZv0q8RSNrY0Go7HLgYJXOYzBPSKeB13fdws0a164dbfUkmLaZMxDtZWVeR03knp3uohWohjsINkEowEPmGG4oajdHQ5uJL50goDV3tVGlHqoFwJ/56Kv";

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;


    enum State {
        CenterForward, RightForward, LeftForward, Detection

    }

    State state;
    ElapsedTime time;

    @Override
    public void init() {
        robot.initRobot(hardwareMap);
        sensors.initSensors(hardwareMap);
        state = State.Detection;
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
        telemetry.update();

        switch (state){

            case Detection:
                    if (tfod != null) {
                        // getUpdatedRecognitions() will return null if no new information is available since
                        // the last time that call was made.
                        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                        if (updatedRecognitions != null) {
                            telemetry.addData("# Object Detected", updatedRecognitions.size());
                            // step through the list of recognitions and display boundary info.
                            int i = 0;
                            for (Recognition recognition : updatedRecognitions) {
                                telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                                telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                            recognition.getLeft(), recognition.getTop());
                                telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                        recognition.getRight(), recognition.getBottom());

                                if (recognition.getLeft() <= 271){
                                    state = State.LeftForward;
                                }
                                else if(recognition.getLeft() >= 403){
                                    state = State.RightForward;

                                }
                                else{
                                    state = State.CenterForward;
                                }

                            }
                            telemetry.update();
                        }
                    }
                    break;

            case LeftForward:
                break;

            case CenterForward:
                break;

            case RightForward:
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
        tfodParameters.minimumConfidence = 0.8;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }

    String formatAngle (AngleUnit angleUnit,double angle){
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees ( double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }
}