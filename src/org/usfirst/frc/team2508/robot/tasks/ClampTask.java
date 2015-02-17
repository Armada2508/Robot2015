package org.usfirst.frc.team2508.robot.tasks;

import org.usfirst.frc.team2508.robot.Robot;
import org.usfirst.frc.team2508.robot.autonomous.Task;

public class ClampTask extends Task {

    boolean clamp;
    
    public ClampTask(boolean clamp, boolean async) {
        super(clamp ? "Clamp" : "Unclamp", async);
        this.clamp = clamp;
    }

    @Override
    protected void run(Robot robot) {
        robot.lift.toggleClamp(clamp);
    }

}
