package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

public class Vision {
    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";

    private static final String VUFORIA_KEY =
            "AT0lsGH/////AAABmWdeMnwGNEBKt9Q07ctS5kJx6GZjBc/WpGuN8MmlLDJR19MSfjSnECR8En3IENinlkTZMFKuBTPS+/l09Hhvzp60nZOSRQoedkj0rkwaOUFmw5ZjvAJHPztaEPJyLeZH1gfsLCe//CosN9awm07Xi5V41Z8hYmgAuEMSwdQ+fOToMSPt8zKDJW23SHQKm0ZXhg9kXGAV1ZwRUCZjtujXZSTELC/zIG/COgdf+z4r3Toqosp0xJxdYHoIZv0q8RSNrY0Go7HLgYJXOYzBPSKeB13fdws0a164dbfUkmLaZMxDtZWVeR03knp3uohWohjsINkEowEPmGG4oajdHQ5uJL50goDV3tVGlHqoFwJ/56Kv";

    public VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    public TFObjectDetector tfod;

    HardwareMap hwMap = null;

    public void initVision(HardwareMap ahwMap) {
        hwMap = ahwMap;

        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = ahwMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        int tfodMonitorViewId = ahwMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", ahwMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.27;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
        tfod.activate();
    }

    public void Disable() {
        tfod.shutdown();
    }
}
