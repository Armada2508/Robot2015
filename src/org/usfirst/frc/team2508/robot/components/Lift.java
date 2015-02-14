package org.usfirst.frc.team2508.robot.components;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import org.usfirst.frc.team2508.robot.Robot;

public class Lift {

    private Robot robot;
    private Talon lift;
    private Solenoid primarySolenoid;
    private Solenoid secondarySolenoid;

    public Lift(Robot robot) {
        this.robot = robot;
        this.lift = new Talon(4);
        this.primarySolenoid = new Solenoid(0);
        this.secondarySolenoid = new Solenoid(1);

        toggleClamp(false);
    }

    /**
     * Set the speed of the lift.
     * @param speed Value in [-1.0, 1.0].
     */
    public void set(double speed) {
        lift.set(speed);
    }

    /**
     * Check if the mechanism is clampd.
     * @return Is it clamped?
     */
    public boolean isClamped() {
        return primarySolenoid.get() && !secondarySolenoid.get();
    }

    /**
     * Toggle the clamping mechanism.
     */
    public void toggleClamp() {
        toggleClamp(!isClamped());
    }

    /**
     * Set the clamping mechanism.
     * @param clamp True for clamped, false for released.
     */
    public void toggleClamp(boolean clamp) {
        primarySolenoid.set(clamp);
        secondarySolenoid.set(!clamp);
    }

}
