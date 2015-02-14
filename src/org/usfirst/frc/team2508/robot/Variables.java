package org.usfirst.frc.team2508.robot;

import com.ni.vision.NIVision.Range;

/**
 * An easy location for global variables.
 */
public class Variables {
	
	/**
	 * The Timer.delay() number to use in 
	 * autonomous and tele-op loops.
	 */
	public static double LOOP_DELAY = 0.01;
	
    /**
     * Controls the speed the speed that
     * the arm motors should go at.
     */
    public static double ARM_SPEED = 0.5;

    
    /**
     * Set the minimum speed the robot should
     * drive and strafe at. Setting this too
     * low could result in no movement.
     */
    public static double MIN_SPEED_FACTOR = 0.1;
    
    /**
     * Set the minimum rotation factor. Setting
     * this too low could result in no rotational
     * movement.
     */
    public static double MIN_ROTATION_FACTOR = 0.2;

    /**
     * Control the speed of the lift.
     */
    public static double LIFT_SPEED = 0.9;
    
    /**
     * Either cam0 or cam1 depending on what USB
     * port is being used in the RoboRIO.
     */
    public static String CAMERA = "cam1";
    
    /**
     * Receive camera input and log to the dashboard?
     */
    public static boolean VISION = true;

    /**
     * Set if we should interpret camera input and
     * detect bright objects in the image.
     */
    public static boolean VISION_ADVANCED = false;
    
    /**
     * RGB Red Range
     */
    public static Range VISION_RED = new Range(120, 250);
    
    /**
     * RGB Green Range
     */
    public static Range VISION_GREEN = new Range(170, 255);
    
    /**
     * RGB Blue Range
     */
    public static Range VISION_BLUE = new Range(235, 255);

}
