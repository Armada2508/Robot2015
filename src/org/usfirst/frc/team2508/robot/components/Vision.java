package org.usfirst.frc.team2508.robot.components;

import org.usfirst.frc.team2508.robot.Robot;
import org.usfirst.frc.team2508.robot.Variables;

import com.ni.vision.VisionException;

import edu.wpi.first.wpilibj.CameraServer;

public class Vision {

    private CameraServer camera;

    public Vision(Robot robot) {
    	if (Variables.VISION)
    		setupCamera();
    }
    
    public void setupCamera() {
        try {
            this.camera = CameraServer.getInstance();
            this.camera.setQuality(30);
            this.camera.startAutomaticCapture(Variables.CAMERA);
        } catch (VisionException e) {
            System.out.println("Error: Camera not plugged in?");
            Variables.VISION = false;
        }
    }

}
