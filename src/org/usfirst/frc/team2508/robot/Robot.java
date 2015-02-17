package org.usfirst.frc.team2508.robot;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team2508.robot.autonomous.Task;
import org.usfirst.frc.team2508.robot.components.Chassis;
import org.usfirst.frc.team2508.robot.components.Dashboard;
import org.usfirst.frc.team2508.robot.components.Lift;
import org.usfirst.frc.team2508.robot.components.Vision;
import org.usfirst.frc.team2508.robot.lib.LogitechGamepad;
import org.usfirst.frc.team2508.robot.tasks.ClampTask;
import org.usfirst.frc.team2508.robot.tasks.LiftTimeTask;
import org.usfirst.frc.team2508.robot.tasks.MoveTask;
import org.usfirst.frc.team2508.robot.tasks.RotateTask;
import org.usfirst.frc.team2508.robot.tasks.WaitTask;

import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends SampleRobot {

    public LogitechGamepad gamepad = new LogitechGamepad();

    public Chassis chassis = new Chassis(this);
    public Lift lift = new Lift(this);
    public Vision vision = new Vision(this);
    public Dashboard dashboard = new Dashboard();
    
    public List<Task> tasks = new ArrayList<Task>();
    
    @Override
    public void robotInit() {
        System.out.println("Robot Modular Initialized!");
        resetRobot();
    }
    
    public void resetRobot() {
        chassis.setInvertedMotor(MotorType.kFrontRight, true);
        chassis.setInvertedMotor(MotorType.kRearLeft, true);
        chassis.setInvertedMotor(MotorType.kRearRight, false);
        
        // lift.toggleClamp(false);
        
        tasks = new ArrayList<Task>();

        dashboard.put("Latest Task", "N/A");
    }

    @Override
    public void autonomous() {
        System.out.println("Autonomous started!");
        chassis.setSafetyEnabled(false);
        resetRobot();
        
        //========================
        // Make the List of Tasks
        //========================
        
        int mode = 1;
        
        if (mode == 1) {
	        tasks.add(new ClampTask(true, false));
	        
	        tasks.add(new WaitTask(1.5, false));
	        
	        tasks.add(new LiftTimeTask(1.3, false));
	       
	        tasks.add(new WaitTask(1.0, false));
	        
	        tasks.add(new RotateTask(1.2, true, false));
	        
	        tasks.add(new WaitTask(1.0, false));
	        
	        tasks.add(new MoveTask(0.7, 0, 2.2, false));
        }
        
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
                    Timer.delay(Variables.LOOP_DELAY);
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
            task.runTask(this);
        }
        
    }

    public void operatorControl() {
        System.out.println("Operator control started!");
        chassis.setSafetyEnabled(true);
        resetRobot();

        //=======================
        // Operator Control Loop
        //=======================
        while (isOperatorControl() && isEnabled()) {
            //===============
            // Mecanum Drive
            //===============
            double strafe = gamepad.getLeftStickX() * chassis.speedFactor * 1.5;
            double linear = -gamepad.getLeftStickY() * chassis.speedFactor;
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
                lift.setSpeed(-Variables.LIFT_SPEED);
            else if (gamepad.getButtonRB())
                lift.setSpeed(Variables.LIFT_SPEED);
            else if (lift.getSpeed() != 0)
                lift.setSpeed(0);


            //============
            // Lift Clamp
            //============
            if (gamepad.getFirstPressY())
                lift.toggleClamp();

            
            /*======
            // Arms
            //======
            if (gamepad.getFirstPressA())
                arms.reverse();

            if (gamepad.getFirstPressX())
                arms.toggle();
            */
            
            //==============
            // Clamp & Lift
            //==============
            if (gamepad.getFirstPressX())
                lift.taskClampLift();
            else if (gamepad.getFirstPressA())
                lift.taskDownReleaseHome();
            
            //===============
            // #yoloDrift420
            //===============
            if (gamepad.getFirstPressStart())
                chassis.toggleYoloDrift420();
            
            
            //========
            // Vision
            //========
            vision.tick();

            updateDashboard();
            gamepad.updatePrevButtonStates();
            Timer.delay(Variables.LOOP_DELAY);
        }
    }
    
    private double dashTime;
    private double lastDashUpdate;

    public void updateDashboard() {
        dashTime += Variables.LOOP_DELAY;
        
        if (dashTime - lastDashUpdate >= 0.21)
            return;
        
        lastDashUpdate = dashTime;
        
        
        // Diagnostics
        dashboard.put("Diagnostics", chassis.isYoloDrift420ing);
        
        // Gamepad
        dashboard.put("L-Stick Y (linear)", -gamepad.getLeftStickY());
        dashboard.put("L-Stick X (strafe)", gamepad.getLeftStickX());
        dashboard.put("R-Stick X (rotation)", gamepad.getRightStickX());

        // Speed
        dashboard.put("Speed Factor", chassis.speedFactor);
        dashboard.put("Rotation Factor", chassis.rotationFactor);
        
        // Lift
        String liftStatus = "Stationary";
        if (lift.getSpeed() < 0)
            liftStatus = "Lowering";
        else if (lift.getSpeed() > 0)
            liftStatus = "Raising";
        dashboard.put("Lift Status", liftStatus);
        
        dashboard.put("At Home?", lift.isAtHome());

        // Clamp
        dashboard.put("Clamp Status", lift.isClamped() ? "Clamped" : "Free");

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