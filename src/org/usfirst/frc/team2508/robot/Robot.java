package org.usfirst.frc.team2508.robot;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team2508.robot.autonomous.AsyncTask;
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

public class Robot extends SampleRobot {

    public LogitechGamepad gamepad = new LogitechGamepad();

    public Chassis chassis = new Chassis(this);
    public Arms arms = new Arms(this);
    public Lift lift = new Lift(this);
    public Vision vision = new Vision(this);
    public Dashboard dashboard = new Dashboard();
    
    public List<Task> tasks = new ArrayList<Task>();

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
        
        tasks = new ArrayList<Task>();
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
        tasks.add(new Task("Home, Forward, Clamp") {

            @Override
            protected void run() {
                // Go to home position
                lift.goHome();
                
                // Drive forward at 0.3 speed
                chassis.mecanumDrive(0.3, 0, 0);
                
                // Wait 1 second, then stop movement.
                waitUntil(1);
                chassis.stop();
                
                // Clampp
                lift.toggleClamp(true);
            }
            
        });
        
        tasks.add(new AsyncTask("Lift") {
            
            @Override
            protected void run() {
                // Lift at a speed of 0.5.
                lift.set(0.5);
                
                // Wait until it reaches a good height, then stop.
                waitUntil(0.5);
                lift.set(0);
            }
        });
        
        tasks.add(new AsyncTask("Move Back, Move Right") {
            
            @Override
            protected void run() {
                // Move backwards.
                chassis.mecanumDrive(-0.3, 0, 0);
                
                // Wait until far enough back.
                waitUntil(1);
                
                // Strafe right
                chassis.mecanumDrive(0, 0.5, 0);
                
                // Wait until far enough right.
                waitUntil(1);
                
                // Stop
                chassis.stop();
            }
        });

        
        //=============
        // Async Stuff
        //=============
        new Thread() {
            
            @Override
            public void run() {
                while (isAutonomous() && isEnabled()) {
                    //========
                    // Vision
                    //========
                    vision.tick();
                    
                    updateDashboard();
                    Timer.delay(0.05);
                }
            }
            
        }.start();
        

        //=================
        // Tasks
        //=================
        for (final Task task : tasks) {
            // Break if not autonomous anymore.
            if (!isAutonomous() || !isEnabled())
                break;
            
            dashboard.put("Latest Task", task.getName() + " (#" + (tasks.indexOf(task) + 1) + ")");
            task.runTask();
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
        
        // Task
        if (!isAutonomous())
            dashboard.put("Latest Task", "N/A");
        
        int concurrent = 0;
        for (Task task : tasks)
            if (task.isRunning())
                concurrent += 1;
        dashboard.put("Concurrent Tasks", concurrent);
    }

}