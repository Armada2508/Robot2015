package org.usfirst.frc.team2508.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import org.usfirst.frc.team2508.robot.autonomous.AsyncTask;
import org.usfirst.frc.team2508.robot.autonomous.Task;
import org.usfirst.frc.team2508.robot.components.Arms;
import org.usfirst.frc.team2508.robot.components.Chassis;
import org.usfirst.frc.team2508.robot.components.Lift;
import org.usfirst.frc.team2508.robot.components.Vision;
import org.usfirst.frc.team2508.robot.lib.LogitechGamepad;

import java.util.ArrayList;
import java.util.List;

public class Robot extends SampleRobot {

    public LogitechGamepad gamepad = new LogitechGamepad();

    public Chassis chassis = new Chassis(this);
    public Arms arms = new Arms(this);
    public Lift lift = new Lift(this);
    public Vision vision = new Vision(this);

    @Override
    public void robotInit() {

    }

    @Override
    public void autonomous() {
        chassis.setSafetyEnabled(false);

        final List<Task> tasks = new ArrayList<Task>();

        tasks.add(new Task() {
            @Override
            protected void run() {
                chassis.mecanumDrive(5, 0, 0);

                waitUntil(0.5);
                chassis.stop();
            }
        });

        tasks.add(new Task() {
            @Override
            protected void run() {
                lift.toggleClamp(true);
            }
        });

        tasks.add(new AsyncTask() {
            @Override
            protected void run() {
                lift.set(0.5);

                waitUntil(1);
                lift.set(0);
            }
        });

        tasks.add(new AsyncTask() {
            @Override
            protected void run() {
                chassis.mecanumDrive(-5, 0, 0);
                waitUntil(3);
                chassis.mecanumDrive(0, 5, 0);
            }
        });


        while (isAutonomous() && isEnabled()) {
            for (final Task task : tasks) {
                task.runTask();
                Timer.delay(0.10);
            }
        }
    }

    public void operatorControl() {
        chassis.setSafetyEnabled(true);

        while (isOperatorControl() && isEnabled()) {

            // Lift clamp
            if (gamepad.getFirstPressY())
                lift.toggleClamp();

            // Mecanum Drive
            double xMovement = gamepad.getLeftStickX() * chassis.speedFactor;
            double yMovement = gamepad.getLeftStickY() * chassis.speedFactor;
            double rotation = gamepad.getRightStickX() * chassis.speedFactor;
            chassis.mecanumDrive(xMovement, yMovement, rotation);

            // Speed Factor
            if (gamepad.getFirstPressLT())
                chassis.speedFactor -= 0.1;
            if (gamepad.getFirstPressRT())
                chassis.speedFactor += 0.1;
            chassis.speedFactor = Math.max(Variables.MIN_SPEED_FACTOR, Math.max(1, chassis.speedFactor));

            // Rotation Factor
            if (gamepad.getFirstPressLeftStickPress())
                chassis.rotationFactor -= 0.1;
            if (gamepad.getFirstPressRightStickPress())
                chassis.rotationFactor += 0.1;
            chassis.rotationFactor = Math.max(Variables.MIN_ROTATION_FACTOR, Math.max(1, chassis.rotationFactor));

            // Lift
            if (gamepad.getButtonLB())
                lift.set(-Variables.LIFT_SPEED);
            else if (gamepad.getButtonRB())
                lift.set(Variables.LIFT_SPEED);
            else
                lift.set(0);

            // Arms
            if (gamepad.getFirstPressA())
                arms.reverse();

            if (gamepad.getFirstPressX())
                arms.toggle();



            Timer.delay(0.10);
        }
    }

}