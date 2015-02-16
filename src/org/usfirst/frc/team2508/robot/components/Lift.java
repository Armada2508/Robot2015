package org.usfirst.frc.team2508.robot.components;

import org.usfirst.frc.team2508.robot.Robot;
import org.usfirst.frc.team2508.robot.Variables;
import org.usfirst.frc.team2508.robot.autonomous.Task;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

public class Lift {
    private Robot robot;
    private Talon talon;
    private Encoder encoder;
    private Solenoid primarySolenoid;
    private Solenoid secondarySolenoid;
    private DigitalInput homeInput;

    public Lift(Robot robot) {
        this.talon = new Talon(4);
        this.encoder = new Encoder(0, 1);
        this.primarySolenoid = new Solenoid(0);
        this.secondarySolenoid = new Solenoid(1);
        this.homeInput = new DigitalInput(2);
        this.robot = robot;
        toggleClamp(false);
    }
    
    public void taskClampLift() {
        new Task("Clamp And Lift", true) {
            
            @Override
            protected void run(Robot robot) {
                toggleClamp(true);
                
                waitFor(0.2);
                
                lift(1.0);
            }
            
        }.runTask(robot);
    }
    
    public void taskDownReleaseHome() {
        new Task("Down Release Home", true) {
            
            @Override
            protected void run(Robot robot) {
                encoder.reset();
                
                setSpeed(Variables.LIFT_SPEED + 0.01);
                
                double distance = 2000.0 * Variables.BOX_HEIGHT_ROTATIONS * 0.25;
                
                while (getSpeed() >= Variables.LIFT_SPEED + 0.01 && Math.abs(encoder.getRaw()) <= distance) {
                    Timer.delay(0.01);
                }
                
                encoder.reset();
                
                setSpeed(0);
                
                toggleClamp(false);
                
                goHome();
            }
            
        }.runTask(robot);
    }
    
    public boolean isAtHome() {
        return homeInput.get();
    }
    
    /**
     * Goes to home position, but locks thread
     * in the process until it reaches home.
     */
    public void goHome() {
        setSpeed(-Variables.LIFT_SPEED);
        
        double waitTime = 0.0;
        while (!isAtHome()) {
            // Lock thread, but if it's been stuck
            // in this loop for too long, stop!
            if (waitTime >= 3.0) {
                System.out.println("Error: Unable to detect home! Digital input broken?");
                break;
            }
            
            waitTime += 0.1;
            Timer.delay(0.1);
        }
    }
    
    /**
     * Get the current speed of the lift.
     * @return The speed in [-1.0, 1.0].
     */
    public double getSpeed() {
        return talon.getSpeed();
    }

    /**
     * Set the speed of the lift.
     * @param speed Value in [-1.0, 1.0].
     */
    public void setSpeed(double speed) {
        talon.set(speed);
    }
    
    /**
     * Lift to a certain height in boxes.
     * @param distance Distance in boxes.
     */
    public void lift(double distance) {
        if (distance == 0)
            return;
        
        double speed = distance < 0 ? -Variables.LIFT_SPEED + 0.01 : Variables.LIFT_SPEED - 0.01;

        encoder.reset();
        
        setSpeed(speed);
        
        double encoderDistance =  2000.0 * Variables.BOX_HEIGHT_ROTATIONS * distance;
        
        while (getSpeed() == speed && Math.abs(encoder.getRaw()) <= encoderDistance) {
            Timer.delay(0.01);
        }
        
        encoder.reset();
        
        setSpeed(0);
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
