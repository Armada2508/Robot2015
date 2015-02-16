package org.usfirst.frc.team2508.robot.tasks;

import org.usfirst.frc.team2508.robot.Robot;
import org.usfirst.frc.team2508.robot.autonomous.Task;

public class LiftBox extends Task {

    double distance;
    
    public LiftBox(double distance, boolean async) {
        super("Lift " + distance + " Boxes", async);
        this.distance = distance;
    }

    @Override
    protected void run(Robot robot) {
        robot.lift.lift(distance);
    }

}
