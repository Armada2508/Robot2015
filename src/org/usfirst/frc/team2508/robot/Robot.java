package org.usfirst.frc.team2508.robot;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team2508.robot.autonomous.Task;
import org.usfirst.frc.team2508.robot.components.Arms;
import org.usfirst.frc.team2508.robot.components.Chassis;
import org.usfirst.frc.team2508.robot.components.Dashboard;
import org.usfirst.frc.team2508.robot.components.Lift;
import org.usfirst.frc.team2508.robot.components.Vision;
import org.usfirst.frc.team2508.robot.lib.LogitechGamepad;

import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot {

    public LogitechGamepad gamepad = new LogitechGamepad();

    public Chassis chassis = new Chassis(this);
    public Arms arms = new Arms(this);
    public Lift lift = new Lift(this);
    public Vision vision = new Vision(this);
    public Dashboard dashboard = new Dashboard();

    @Override
    public void robotInit() {
        resetRobot();
    }
    
    public void resetRobot() {
        chassis.setInvertedMotor(MotorType.kFrontRight, true);
        chassis.setInvertedMotor(MotorType.kRearLeft, true);
        chassis.setInvertedMotor(MotorType.kRearRight, false);
        chassis.setExpiration(0.1);
        lift.toggleClamp(false);
    }

    @Override
    public void autonomous() {
        // Disable safety.
        chassis.setSafetyEnabled(false);

        // Reset robot state.
        resetRobot();

        //========================
        // Make the List of Tasks
        //========================
        final List<Task> tasks = new ArrayList<Task>();

        tasks.add(new Task("Revert Home") {

            @Override
            protected void run() {
                lift.goHome();
                lift.toggleClamp();
            }
            
        });


        //=================
        // Autonomous Loop
        //=================
        while (isAutonomous() && isEnabled()) {
            //=======
            // Tasks
            //=======
            for (final Task task : tasks) {
                SmartDashboard.putString("Current Task", task.getName());
                task.runTask();
            }
            
            //========
            // Vision
            //========
            vision.tick();
            
            updateDashboard();
            Timer.delay(0.05);
        }
    }

    public void operatorControl() {
        // Enable safety.
        chassis.setSafetyEnabled(true);
        
        // Reset robot state.
        resetRobot();

        //=======================
        // Operator Control Loop
        //=======================
        while (isOperatorControl() && isEnabled()) {
            //===============
            // Mecanum Drive
            //===============
            double xMovement = gamepad.getLeftStickX() * chassis.speedFactor;
            double yMovement = gamepad.getLeftStickY() * chassis.speedFactor;
            double rotation = gamepad.getRightStickX() * chassis.rotationFactor;
            chassis.mecanumDrive(xMovement, yMovement, rotation);
            

            //==============
            // Speed Factor
            //==============
            if (gamepad.getFirstPressLT())
                chassis.speedFactor -= 0.1;
            if (gamepad.getFirstPressRT())
                chassis.speedFactor += 0.1;
            chassis.speedFactor = Math.max(Variables.MIN_SPEED_FACTOR, Math.max(1, chassis.speedFactor));

            
            //=================
            // Rotation Factor
            //=================
            if (gamepad.getFirstPressLeftStickPress())
                chassis.rotationFactor -= 0.1;
            if (gamepad.getFirstPressRightStickPress())
                chassis.rotationFactor += 0.1;
            chassis.rotationFactor = Math.max(Variables.MIN_ROTATION_FACTOR, Math.max(1, chassis.rotationFactor));
            
            
            //======
            // Lift
            //======
            if (gamepad.getButtonLB())
                lift.set(-Variables.LIFT_SPEED);
            else if (gamepad.getButtonRB())
                lift.set(Variables.LIFT_SPEED);
            else
                lift.set(0);


            //============
            // Lift Clamp
            //============
            if (gamepad.getFirstPressY())
                lift.toggleClamp();

            
            //======
            // Arms
            //======
            if (gamepad.getFirstPressA())
                arms.reverse();

            if (gamepad.getFirstPressX())
                arms.toggle();
            
            
            //========
            // Vision
            //========
            vision.tick();

            updateDashboard();
            gamepad.updatePrevButtonStates();
            Timer.delay(0.05);
        }
        
    }

    public void updateDashboard() {
        // Gamepad
        dashboard.put("L-Stick X (strafe)", gamepad.getLeftStickX());
        dashboard.put("L-Stick Y (linear)", gamepad.getLeftStickY());
        dashboard.put("R-Stick X (rotation)", gamepad.getRightStickX());

        // Speed
        dashboard.put("Speed Factor", chassis.speedFactor);
        dashboard.put("Rotation Factor", chassis.rotationFactor);
        
        // Lift
        String liftStatus = "Stationary";
        if (lift.get() < 0)
            liftStatus = "Lowering";
        else if (lift.get() > 0)
            liftStatus = "Raising";
        dashboard.put("Lift Status", liftStatus);

        // Clamp
        String clampStatus = "Free";
        if (lift.isClamped())
            clampStatus = "Clamped";
        dashboard.put("Clamp Status", clampStatus);
        
        // Arms
        String armDirection = arms.isPulling() ? "Pulling" : "Pushing";
        boolean armEnabled = arms.isEnabled();
        dashboard.put("Arm Direction", armDirection);
        dashboard.put("Arm Enabled", armEnabled);
        
        // Camera
        dashboard.put("Camera Enabled?", Variables.VISION);
    }

}