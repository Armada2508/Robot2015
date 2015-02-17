package org.usfirst.frc.team2508.robot.tasks;

import org.usfirst.frc.team2508.robot.Robot;
import org.usfirst.frc.team2508.robot.Variables;
import org.usfirst.frc.team2508.robot.autonomous.Task;

public class LiftTimeTask extends Task {

    double time;
    
    public LiftTimeTask(double time, boolean async) {
        super("Lift for " + time + " Seconds", async);
        this.time = time;
    }

    @Override
    protected void run(Robot robot) {
        robot.lift.setSpeed(Variables.LIFT_SPEED);
        waitFor(time);
        robot.lift.setSpeed(0);
    }

}
