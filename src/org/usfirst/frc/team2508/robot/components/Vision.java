package org.usfirst.frc.team2508.robot.components;

import org.usfirst.frc.team2508.robot.Robot;
import org.usfirst.frc.team2508.robot.Variables;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.ColorMode;
import com.ni.vision.NIVision.IMAQdxCameraControlMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.VisionException;

import edu.wpi.first.wpilibj.CameraServer;

public class Vision {

    private CameraServer camera;
    private Image image;
    private int session;

    public Vision(Robot robot) {
        setupCamera();
    }
    
    public void setupCamera() {
        try {
            this.camera = CameraServer.getInstance();
            this.camera.setQuality(75);
            this.image = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
            this.session = NIVision.IMAQdxOpenCamera(Variables.CAMERA, IMAQdxCameraControlMode.CameraControlModeListener);
            
            NIVision.IMAQdxConfigureGrab(session);
            NIVision.IMAQdxStartAcquisition(session);    
        } catch (VisionException e) {
            System.out.println("Error: Camera not plugged in?");
            Variables.VISION = false;
        }
    }

    private double currentTime = 0;
    private double lastCapture = 0;
    
    /**
     * Called every time we go through the autonomous
     * or tele-op loop (each "tick").
     */
    public void tick() {
        currentTime += Variables.LOOP_DELAY;
        
        if (!Variables.VISION)
            return;
        
        if (currentTime - lastCapture < 0.07)
            return;

        lastCapture = currentTime;
        
        // Load new camera data into "image" variable.
        NIVision.IMAQdxGrab(session, image, 1);
        
        if (Variables.VISION_ADVANCED)
            NIVision.imaqColorThreshold(image, image, 255, ColorMode.RGB, Variables.VISION_RED, Variables.VISION_GREEN, Variables.VISION_BLUE);
        
        camera.setImage(image);
    }

}
