package org.usfirst.frc.team2508.robot.tasks;

import org.usfirst.frc.team2508.robot.Robot;
import org.usfirst.frc.team2508.robot.autonomous.Task;

public class Rotate extends Task {

	double degrees;
	double speed;
	double time;
	
	public Rotate(double degrees, double speed) {
		super("Rotate " + degrees + " Degrees");
		this.degrees = degrees;
		this.speed = speed;
		this.time = speed / degrees;
	}

	@Override
	protected void run(Robot robot) {
		robot.chassis.mecanumDrive(0, 0, speed);
		waitFor(time);
		robot.chassis.stop();
	}

}
