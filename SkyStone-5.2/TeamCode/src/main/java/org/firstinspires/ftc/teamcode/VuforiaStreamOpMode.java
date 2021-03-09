package org.firstinspires.ftc.teamcode.TestCode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

/*
 * This sample demonstrates how to stream frames from Vuforia to the dashboard. Make sure to fill in
 * your Vuforia key below and select the 'Camera' preset on top right of the dashboard. This sample
 * also works for UVCs with slight adjustments.
 */
@Autonomous
@Disabled

public class VuforiaStreamOpMode extends LinearOpMode {

    // TODO: fill in
    public static final String VUFORIA_LICENSE_KEY = "AT0lsGH/////AAABmWdeMnwGNEBKt9Q07ctS5kJx6GZjBc/WpGuN8MmlLDJR19MSfjSnECR8En3IENinlkTZMFKuBTPS+/l09Hhvzp60nZOSRQoedkj0rkwaOUFmw5ZjvAJHPztaEPJyLeZH1gfsLCe//CosN9awm07Xi5V41Z8hYmgAuEMSwdQ+fOToMSPt8zKDJW23SHQKm0ZXhg9kXGAV1ZwRUCZjtujXZSTELC/zIG/COgdf+z4r3Toqosp0xJxdYHoIZv0q8RSNrY0Go7HLgYJXOYzBPSKeB13fdws0a164dbfUkmLaZMxDtZWVeR03knp3uohWohjsINkEowEPmGG4oajdHQ5uJL50goDV3tVGlHqoFwJ/56Kv";

    @Override
    public void runOpMode() throws InterruptedException {
        // gives Vuforia more time to exit before the watchdog notices
        msStuckDetectStop = 2500;

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_LICENSE_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        VuforiaLocalizer vuforia = ClassFactory.getInstance().createVuforia(parameters);

        FtcDashboard.getInstance().startCameraStream(vuforia, 0);

        waitForStart();

        while (opModeIsActive());
    }
}