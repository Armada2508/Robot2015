package org.usfirst.frc.team2508.robot.tasks;

import org.usfirst.frc.team2508.robot.Robot;
import org.usfirst.frc.team2508.robot.autonomous.Task;

public class LiftTask extends Task {

    double distance;
    
    public LiftTask(double distance, boolean async) {
        super("Lift " + distance + " Boxes", async);
        this.distance = distance;
    }

    @Override
    protected void run(Robot robot) {
        robot.lift.lift(distance);
    }

}
