package org.usfirst.frc.team2508.robot.autonomous;

import java.util.Date;

public abstract class Task {

    private Date startTime;
    private boolean completed = false;

    /**
     * Initiate and run the task!
     */
    public void runTask() {
        // Record the start time.
        this.startTime = new Date();

        // Run the task (implemented in child classes).
        run();

        this.completed = true;
    }

    /**
     * Locks the thread until an amount of seconds has passed by.
     * @param seconds The amount of time.
     */
    public void waitUntil(double seconds) {
        while (!hasBeen(seconds)) {
            // locks thread
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
     * Run the task (implemented in child classes).
     */
    protected abstract void run();

    /**
     * Check if task is completed.
     * @return If the task is over.
     */
    public boolean isCompleted() {
        return completed;
    }

}
