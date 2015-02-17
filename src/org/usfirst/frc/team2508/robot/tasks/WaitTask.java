package org.usfirst.frc.team2508.robot.tasks;

import org.usfirst.frc.team2508.robot.Robot;
import org.usfirst.frc.team2508.robot.autonomous.Task;

public class WaitTask extends Task {

    double time;
    
    public WaitTask(double time, boolean async) {
        super("Wait " + time, async);
        this.time = time;
    }

    @Override
    protected void run(Robot robot) {
    	waitFor(time);
    }

}
