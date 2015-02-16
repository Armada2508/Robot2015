package org.usfirst.frc.team2508.robot.components;

import org.usfirst.frc.team2508.robot.Robot;
import org.usfirst.frc.team2508.robot.Variables;
import org.usfirst.frc.team2508.robot.autonomous.Task;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.Timer;

public class Chassis {

    Robot robot;
    RobotDrive drive;

    public double speedFactor = 0.5;
    public double rotationFactor = 0.5;
    
    public boolean isYoloDrift420ing;

    public Chassis(Robot robot) {
        this.robot = robot;
        this.drive = new RobotDrive(0, 1, 2, 3);
    }

    /**
     * Invert a motor.
     * @param motorType
     * @param inverted
     */
    public void setInvertedMotor(MotorType motorType, boolean inverted) {
        drive.setInvertedMotor(motorType, inverted);
    }
    
    /**
     * Set the expiration/timeout of wheels...
     * @param timeout The timeout.
     */
    public void setExpiration(double timeout) {
        drive.setExpiration(timeout);
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
        drive.mecanumDrive_Cartesian(strafe, -linear, rotation, 0);
    }

    /**
     * Stop all movement and rotation.
     */
    public void stop() {
        mecanumDrive(0, 0, 0);
    }
    
    public void toggleYoloDrift420() {
        isYoloDrift420ing = !isYoloDrift420ing;
        
        if (!isYoloDrift420ing)
            return;
        
        new Task("Yolo Drift 420", true) {

            @Override
            protected void run(Robot robot) {
                robot.lift.goHome();
                
                robot.lift.setSpeed(Variables.LIFT_SPEED);
                
                double time = 0;
                
                while (isYoloDrift420ing) {
                    if (time % 0.3 == 0)
                        robot.lift.toggleClamp();
                    
                    if (time % 3 == 0)
                        robot.lift.setSpeed(-robot.lift.getSpeed());

                    mecanumDrive(0.5, 0.5, 0.3);
                    
                    time += Variables.LOOP_DELAY;
                    Timer.delay(Variables.LOOP_DELAY);
                }
            }
            
        }.runTask(robot);
    }
    
}
