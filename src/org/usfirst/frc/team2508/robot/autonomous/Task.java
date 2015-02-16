package org.usfirst.frc.team2508.robot.autonomous;

import java.util.Date;

import org.usfirst.frc.team2508.robot.Robot;

import edu.wpi.first.wpilibj.Timer;

/**
 * A task represents an activity that the robot
 * can do. It can be executed with runTask().
 */
public abstract class Task {
    
    private String name;
    private boolean async;
    
    private Date stopWaiting;
    private boolean running = false;
    
    public Task(String name, boolean async) {
        this.name = name;
        this.async = async;
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
    public void runTask(Robot robot) {
        Thread thread = getThread(robot);
        if (async)
            thread.start();
        else
            thread.run();
    }

    public Thread getThread(final Robot robot) {
        return new Thread() {
            @Override
            public void run() {
                System.out.println("Starting Task: \"" + name + "\"");
                
                // Set running
                running = true;

                // Run the task (implemented in child classes).
                Task.this.run(robot);

                System.out.println("Finished Task: \"" + name + "\"");
                running = false;
            }
        };
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
    protected abstract void run(Robot robot);

}
