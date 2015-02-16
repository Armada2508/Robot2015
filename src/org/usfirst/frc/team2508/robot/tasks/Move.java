package org.usfirst.frc.team2508.robot.tasks;

import org.usfirst.frc.team2508.robot.Robot;
import org.usfirst.frc.team2508.robot.autonomous.Task;

public class Move extends Task {
    
    double linear;
    double strafe;
    double time;

    /**
     * Move in a linear direction.
     * @param linear The movement forward and backwards.
     * @param strafe Movement left and right.
     * @param time The amount of time to perform the movement.
     */
    public Move(double linear, double strafe, double time, boolean async) {
        super("Move Straight " + linear + ", Strafe " + strafe, async);
        this.linear = linear;
        this.strafe = strafe;
        this.time = time;
    }

    @Override
    protected void run(Robot robot) {
        robot.chassis.mecanumDrive(linear, strafe, 0);
        waitFor(time);
        robot.chassis.stop();
    }

}
