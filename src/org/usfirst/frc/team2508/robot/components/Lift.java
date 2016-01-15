package org.usfirst.frc.team2508.robot.components;

import org.usfirst.frc.team2508.robot.Robot;
import org.usfirst.frc.team2508.robot.Variables;
import org.usfirst.frc.team2508.robot.autonomous.Task;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

public class Lift {
	
	private Robot robot;
    private Talon talon;
    public Encoder encoder;
    private Solenoid primarySolenoid;
    private Solenoid secondarySolenoid;

    public Lift(Robot robot) {
    	this.robot = robot;
        this.talon = new Talon(4);
        this.encoder = new Encoder(1, 2);
        this.primarySolenoid = new Solenoid(0);
        this.secondarySolenoid = new Solenoid(1);
        toggleClamp(false);
        encoder.reset();
    }
    
    private double time;
    private double lastTime;
    public int lastPosition = 0;
    public boolean counteracting = false;
    public int returnTo;
    
    public void tick(boolean teleop) {
    	time += Variables.LOOP_DELAY;
    	
    	boolean returnToHome = false;
    	
    	if (returnToHome)  {
	    	if (teleop) {
	    		returnTo = -50000;
	    		counteracting = false;
	    		return;
	    	}
	    	
	    	if (time - lastTime < 0.5)
	    		return;
	    	
	    	lastTime = time;
	    	
	    	if (counteracting)
	    		return;
	    	
	    	if (getPosition() < lastPosition && getSpeed() == 0) {
	    		System.out.println("Heavy load... Going back up!");
	    		returnTo = lastPosition;
	    		lastPosition = getPosition();
	    		
	    		counteracting = true;
	
	        	robot.dashboard.put("Return To", returnTo);
	    		
	    		new Task("Counteract Weight", true) {
					
					@Override
					protected void run(Robot robot) {
						while (counteracting && getPosition() < returnTo) {
							setSpeed(Variables.LIFT_SPEED);
							Timer.delay(0.3);
						}
						counteracting = false;
						setSpeed(0);
					}
					
				}.runTask(robot);
	    	}
	    	
	    	lastPosition = getPosition();
    	}
    }
    
    public int getPosition() {
    	return encoder.getRaw();
    }
    
    /**
     * Get the current speed of the lift.
     * @return The speed in [-1.0, 1.0].
     */
    public double getSpeed() {
    	// thanks Cory, speed is opposite.
        return -talon.getSpeed();
    }

    /**
     * Set the speed of the lift.
     * @param speed Value in [-1.0, 1.0].
     */
    public void setSpeed(double speed) {
    	// thanks Cory, speed is opposite.
        talon.set(-speed);
    }

    /**
     * Check if the mechanism is clamped.
     * @return Is it clamped?
     */
    public boolean isClamped() {
        return !primarySolenoid.get() && secondarySolenoid.get();
    }

    /**
     * Toggle the clamping mechanism.
     * Example: toggleClamp();
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
