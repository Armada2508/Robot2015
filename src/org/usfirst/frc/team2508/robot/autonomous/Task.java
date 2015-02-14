package org.usfirst.frc.team2508.robot.autonomous;

import java.util.Date;

import edu.wpi.first.wpilibj.Timer;

/**
 * A task represents an activity that the robot
 * can do. It can be executed with runTask().
 */
public abstract class Task {

	private final String name;
    private Date startTime;
    private boolean completed = false;
    
    /**
     * Instantiate a task.
     * @param name The name of the task.
     */
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
    	
        // Record the start time.
        this.startTime = new Date();

        // Run the task (implemented in child classes).
        run();

        System.out.println("Finished Task: " + name);
        this.completed = true;
    }

    /**
     * Locks the thread until an amount of seconds has passed by.
     * @param seconds The amount of time.
     */
    public void waitUntil(double seconds) {
        while (!hasBeen(seconds)) {
            // Locks thread! Warning!
        	Timer.delay(0.05);
        }
    }

    /**
     * Check if the task has been running for a certain amount of time.
     * @param seconds The duration.
     * @return If it's been running for that much time yo.
     */
    public boolean hasBeen(double seconds) {
        return getTime() >= seconds;
    }

    /**
     * Get the amount of time this task has been running.
     * @return The time in seconds.
     */
    public double getTime() {
        return ((double) (new Date().getTime() - startTime.getTime())) / 1000.0;
    }

    /**
     * Check if task is completed.
     * @return If the task is over.
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Run the task (implemented in child classes).
     */
    protected abstract void run();

}
