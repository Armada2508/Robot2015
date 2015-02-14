package org.usfirst.frc.team2508.robot.autonomous;

import java.util.Date;

import edu.wpi.first.wpilibj.Timer;

/**
 * A task represents an activity that the robot
 * can do. It can be executed with runTask().
 */
public abstract class Task {
	
	private String name;
    private Date stopWaiting;
    private boolean running = false;
    
    public Task(String name) {
    	this.name = name;
    }
    
    /**
     * Get the task name.
     * @return The name.
     */
    public String getName() {
    	return name;
    }

    /**
     * Initiate and run the task!
     */
    public void runTask() {
    	System.out.println("Starting Task: " + name);
    	
        // Set running
        this.running = true;

        // Run the task (implemented in child classes).
        run();

        System.out.println("Finished Task: " + name);
        this.running = false;
    }

    /**
     * Locks the thread until an amount of seconds has passed by.
     * @param seconds The amount of time.
     */
    public void waitFor(double seconds) {
    	this.stopWaiting = new Date(new Date().getTime() + (int) (seconds * 1000.0));
    	
    	while (!new Date().after(stopWaiting)) {
    		// Locks thread!
    		Timer.delay(0.05);
    	}
    }
    
    /**
     * Is this task running right now?
     * @return If the task is running;
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Run the task (implemented in child classes).
     */
    protected abstract void run();

}
