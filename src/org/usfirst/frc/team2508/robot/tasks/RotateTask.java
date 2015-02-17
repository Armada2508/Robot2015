package org.usfirst.frc.team2508.robot.tasks;

import org.usfirst.frc.team2508.robot.Robot;
import org.usfirst.frc.team2508.robot.autonomous.Task;

public class RotateTask extends Task {
	
	boolean right;
    double time;
    
    /**
     * Rotate x degrees.
     * @param degrees The number of degrees.
     * @param speed The speed to rotate.
     */
    public RotateTask(double time, boolean right, boolean async) {
        super("Rotate " + right + " for " + time + " Seconds", async);
        this.right = right;
        this.time = time;
    }

    @Override
    protected void run(Robot robot) {
        robot.chassis.mecanumDrive(0, 0, right ? 0.4 : -0.4);
        waitFor(time);
        robot.chassis.stop();
    }

}
