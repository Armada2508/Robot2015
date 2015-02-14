package org.usfirst.frc.team2508.robot.components;

import edu.wpi.first.wpilibj.Talon;
import org.usfirst.frc.team2508.robot.Robot;
import org.usfirst.frc.team2508.robot.Variables;

public class Arms {

    private Robot robot;
    private Talon leftArm;
    private Talon rightArm;

    private boolean enabled = false;
    private boolean intake = true;

    public Arms(Robot robot) {
        this.robot = robot;
        this.leftArm = new Talon(5);
        this.rightArm = new Talon(6);
    }

    /**
     * Set the speed of both arms.
     * @param speed The speed [-1.0, 1.0]. Positive is intake.
     */
    public void set(double speed) {
        leftArm.set(speed);
        rightArm.set(-speed);
    }

    /**
     * Reverse the direction on the arms.
     * This will not activate the arms.
     */
    public void reverse() {
        intake = !intake;
        if (enabled)
            run();
    }

    /**
     * Toggle the arm motors. Retains forward
     */
    public void toggle() {
        enabled = !enabled;
        run();
    }

    /**
     * Activate the motors at a speed determined in variables
     * in the direction that is saved.
     */
    public void run() {
        double speed = Variables.ARM_SPEED;

        if (!intake)
            speed = -speed;

        set(speed);
    }

}
