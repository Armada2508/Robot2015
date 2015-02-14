package org.usfirst.frc.team2508.robot.components;

import edu.wpi.first.wpilibj.RobotDrive;
import org.usfirst.frc.team2508.robot.Robot;

public class Chassis {

    Robot robot;
    RobotDrive drive;

    public double speedFactor = 0.5;
    public double rotationFactor = 0.5;

    public Chassis(Robot robot) {
        this.robot = robot;
        this.drive = new RobotDrive(0, 1, 2, 3);
    }

    /**
     * Set chassis safety mode.
     * @param enabled
     */
    public void setSafetyEnabled(boolean enabled) {
        drive.setSafetyEnabled(enabled);
    }

    /**
     * Move the mecanum wheels. All values [-1.0, 1.0].
     * @param linear Move backwards or forwards.
     * @param strafe Strafe left or right
     * @param rotation Rotate left or right.
     */
    public void mecanumDrive(double linear, double strafe, double rotation) {
        drive.mecanumDrive_Cartesian(strafe, linear, rotation, 0);
    }

    /**
     * Stop all movement and rotation.
     */
    public void stop() {
        mecanumDrive(0, 0, 0);
    }
}
