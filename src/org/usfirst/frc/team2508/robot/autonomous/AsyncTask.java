package org.usfirst.frc.team2508.robot.autonomous;

import org.usfirst.frc.team2508.robot.Robot;

/**
 * A asynchronous task represents an activity that 
 * the robot can do at the same time other things
 * are going on. It can be executed with runTask().
 */
public abstract class AsyncTask extends Task {

	/**
	 * Instantiate the task.
	 * @param name The name of the task.
	 */
    public AsyncTask(String name) {
		super(name);
	}

	/**
     * Run the task asynchronously.
     */
    @Override
    public void runTask(Robot robot) {
        new Thread() {
            @Override
            public void run() {
                AsyncTask.super.runTask(robot);
            }
        }.start();
    }

}
