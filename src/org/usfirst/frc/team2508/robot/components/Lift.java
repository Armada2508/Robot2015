package org.usfirst.frc.team2508.robot.components;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

import org.usfirst.frc.team2508.robot.Robot;
import org.usfirst.frc.team2508.robot.Variables;

public class Lift {
	
    private Talon talon;
    private Solenoid primarySolenoid;
    private Solenoid secondarySolenoid;
    private DigitalInput homeInput;

    public Lift(Robot robot) {
        this.talon = new Talon(4);
        this.primarySolenoid = new Solenoid(0);
        this.secondarySolenoid = new Solenoid(1);
        this.homeInput = new DigitalInput(2);

        toggleClamp(false);
    }
    
    public boolean isAtHome() {
    	return homeInput.get();
    }
    
    /**
     * Goes to home position, but locks thread
     * in the process until it reaches home.
     */
    public void goHome() {
    	set(-Variables.LIFT_SPEED);
    	
    	double waitTime = 0.0;
    	while (!isAtHome()) {
    		// Lock thread, but if it's been stuck
    		// in this loop for too long, stop!
    		if (waitTime >= 5.0) {
    			System.out.println("Error: Unable to detect home! Digital input broken?");
    			break;
    		}
    		
    		waitTime += 0.1;
    		Timer.delay(0.1);
    	}
    }
    
    /**
     * Get the current speed of the lift.
     * @return The speed.
     */
    public double get() {
    	return talon.get();
    }

    /**
     * Set the speed of the lift.
     * @param speed Value in [-1.0, 1.0].
     */
    public void set(double speed) {
        talon.set(speed);
    }

    /**
     * Check if the mechanism is clampd.
     * @return Is it clamped?
     */
    public boolean isClamped() {
        return !primarySolenoid.get() && secondarySolenoid.get();
    }

    /**
     * Toggle the clamping mechanism.
     */
    public void toggleClamp() {
        toggleClamp(!isClamped());
    }

    /**
     * Set the clamping mechanism.
     * @param clamp True for clamped, false for released.
     */
    public void toggleClamp(boolean clamp) {
        primarySolenoid.set(!clamp);
        secondarySolenoid.set(clamp);
    }

}
