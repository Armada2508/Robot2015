package org.usfirst.frc.team2508.robot.components;

import org.usfirst.frc.team2508.robot.Robot;
import org.usfirst.frc.team2508.robot.Variables;

import edu.wpi.first.wpilibj.Talon;

public class Arms {
    
    private Talon talon;

    private boolean enabled = false;
    private boolean pulling = true;

    public Arms(Robot robot) {
        this.talon = new Talon(5);
    }
    
    /**
     * Check if the arms are running.
     * @return If arm motors are running.
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * Check if arms are set to intaking (regardless if enabled).
     * @return True if intaking, false if not intaking.
     */
    public boolean isPulling() {
        return pulling;
    }

    /**
     * Set the speed of both arms.
     * @param speed The speed [-1.0, 1.0]. Positive is intake.
     */
    public void set(double speed) {
        talon.set(speed);
    }

    /**
     * Toggle the arm motors. Retains forward
     */
    public void toggle() {
        enabled = !enabled;
        run();
    }

    /**
     * Reverse the direction on the arms.
     * This will not activate the arms.
     */
    public void reverse() {
        pulling = !pulling;
        if (enabled)
            run();
    }
    
    /**
     * Activate the motors at a speed determined in variables
     * in the direction that is saved.
     */
    private void run() {
        double speed = Variables.ARM_SPEED;

        // If not intaking, reverse the speed.
        if (!pulling)
            speed = -speed;

        set(speed);
    }

}
