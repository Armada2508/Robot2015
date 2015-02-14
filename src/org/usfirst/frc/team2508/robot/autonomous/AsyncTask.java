package org.usfirst.frc.team2508.robot.autonomous;

public abstract class AsyncTask extends Task {

    /**
     * Run the task asynchronously.
     */
    @Override
    public void runTask() {
        new Thread() {
            @Override
            public void run() {
                AsyncTask.super.runTask();
            }
        }.start();
    }

}
