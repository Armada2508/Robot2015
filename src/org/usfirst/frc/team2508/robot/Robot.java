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
    	System.out.println("Robot Modular Running!");
        resetRobot();
    }
    
    public void resetRobot() {
        chassis.setInvertedMotor(MotorType.kFrontRight, true);
        chassis.setInvertedMotor(MotorType.kRearLeft, true);
        chassis.setInvertedMotor(MotorType.kRearRight, false);
        
        lift.toggleClamp(false);
        
        tasks = new ArrayList<Task>();

        dashboard.put("Latest Task", "N/A");
    }

    @Override
    public void autonomous() {
    	System.out.println("Autonomous started.");
    	
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
                // Drive forward
                chassis.mecanumDrive(-0.5, 0, 0);
                
                // Wait 1 second, then stop movement.
                waitUntil(0.95);
                chassis.stop();
            }
            
        });
        
        tasks.add(new Task("Lift") {
            
            @Override
            protected void run() {
            	lift.toggleClamp(true);
            	
            	waitUntil(0.6);
            	
                // Lift at a speed of 0.5.
                lift.set(0.9);
                
                // Wait until it reaches a good height, then stop.
                waitUntil(2.8);
                lift.set(0);
            }
        });
        
        
        tasks.add(new Task("Rotate, Move Forward") {
            
            @Override
            protected void run() {
            	chassis.mecanumDrive(0, 0, 0.5);
            	
            	waitUntil(1);
            	
            	chassis.mecanumDrive(-0.8, 0, 0);
            	
            	waitUntil(3);
            	
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
    	System.out.println("Operator control started");
    	
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
            double strafe = gamepad.getLeftStickX() * chassis.speedFactor * 1.7;
            double linear = gamepad.getLeftStickY() * chassis.speedFactor;
            double rotation = gamepad.getRightStickX() * chassis.rotationFactor;
            chassis.mecanumDrive(linear, strafe, rotation);
            

            //==============
            // Speed Factor
            //==============
            if (gamepad.getFirstPressLT())
                chassis.speedFactor -= 0.1;
            if (gamepad.getFirstPressRT())
                chassis.speedFactor += 0.1;
            if (gamepad.getFirstPressLT() || gamepad.getFirstPressRT())
            	chassis.speedFactor = Math.max(Variables.MIN_SPEED_FACTOR, Math.min(1, chassis.speedFactor));

            
            //=================
            // Rotation Factor
            //=================
            if (gamepad.getFirstPressLeftStickPress())
                chassis.rotationFactor -= 0.1;
            if (gamepad.getFirstPressRightStickPress())
                chassis.rotationFactor += 0.1;
            if (gamepad.getFirstPressLeftStickPress() || gamepad.getFirstPressRightStickPress())
            	chassis.rotationFactor = Math.max(Variables.MIN_ROTATION_FACTOR, Math.min(1, chassis.rotationFactor));
            
            
            //======
            // Lift
            //======
            if (gamepad.getButtonLB())
                lift.set(-Variables.LIFT_SPEED);
            else if (gamepad.getButtonRB())
                lift.set(Variables.LIFT_SPEED);
            else if (lift.get() != 0)
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
    
    private String liftStatus;
    private String clampStatus;
    private String armDirection;

    public void updateDashboard() {
        // Gamepad
        dashboard.put("L-Stick X (strafe)", gamepad.getLeftStickX());
        dashboard.put("L-Stick Y (linear)", gamepad.getLeftStickY());
        dashboard.put("R-Stick X (rotation)", gamepad.getRightStickX());

        // Speed
        dashboard.put("Speed Factor", chassis.speedFactor);
        dashboard.put("Rotation Factor", chassis.rotationFactor);
        
        // Lift
        if (lift.get() < 0)
            liftStatus = "Lowering";
        else if (lift.get() > 0)
            liftStatus = "Raising";
        else
        	liftStatus = "Stationary";
        dashboard.put("Lift Status", liftStatus);

        // Clamp
        if (lift.isClamped())
            clampStatus = "Clamped";
        else
        	clampStatus = "Free";
        dashboard.put("Clamp Status", clampStatus);
        
        // Arms
        armDirection = arms.isPulling() ? "Pulling" : "Pushing";
        boolean armEnabled = arms.isEnabled();
        dashboard.put("Arm Direction", armDirection);
        dashboard.put("Arm Enabled", armEnabled);

        // Camera
        dashboard.put("Camera Enabled?", Variables.VISION);
        dashboard.put("Advanced Vision?", Variables.VISION_ADVANCED);
        
        // Autonomous
        if (isAutonomous()) {
            int concurrent = 0;
            for (Task task : tasks)
                if (task.isRunning())
                    concurrent += 1;
            dashboard.put("Concurrent Tasks", concurrent);	
        }
    }

}