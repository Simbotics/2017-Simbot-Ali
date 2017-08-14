package org.simbotics.frc2017.auton.drive;

import org.simbotics.frc2017.auton.AutonCommand;
import org.simbotics.frc2017.auton.RobotComponent;
import org.simbotics.frc2017.io.RobotOutput;
import org.simbotics.frc2017.io.SensorInput;

public class DriveTurnRightUntilAngle extends AutonCommand {

	private RobotOutput robotOut;
	private SensorInput sensorIn;
	private double output;
	private double targetAngle;
	private boolean firstCycle = true;
	private boolean turnLeft = false;
	
	public DriveTurnRightUntilAngle(double output, double angle, long timeout) {
		super(RobotComponent.DRIVE,timeout);
		this.output = output;
		this.targetAngle = angle;
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();
		
	}

	@Override
	public boolean calculate() {
		this.robotOut.setDriveLeft(this.output);
		this.robotOut.setDriveRight(-this.output);
		if(this.sensorIn.getGyroPositionState() <= this.targetAngle){
			return true;
		}
		
		return false;
	}

	@Override
	public void override() {
		this.robotOut.setDriveLeft(0);
		this.robotOut.setDriveRight(0);
	}

}
