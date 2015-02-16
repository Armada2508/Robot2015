package org.usfirst.frc.team2508.robot.tasks;

import org.usfirst.frc.team2508.robot.Robot;
import org.usfirst.frc.team2508.robot.autonomous.Task;

public class Rotate extends Task {

    double degrees;
    double speed;
    
    /**
     * Rotate x degrees.
     * @param degrees The number of degrees.
     * @param speed The speed to rotate.
     */
    public Rotate(double degrees, double speed, boolean async) {
        super("Rotate " + degrees + " Degrees", async);
        this.degrees = degrees;
        this.speed = degrees < 0 ? -speed : speed;
    }

    @Override
    protected void run(Robot robot) {
        robot.chassis.mecanumDrive(0, 0, speed);
        waitFor((degrees / 90.0) * (0.5 / speed));
        robot.chassis.stop();
    }

}
