package org.simbotics.frc2017.auton.drive;

import org.simbotics.frc2017.auton.AutonCommand;
import org.simbotics.frc2017.auton.RobotComponent;
import org.simbotics.frc2017.io.RobotOutput;

public class DriveSetOutput extends AutonCommand {

	private RobotOutput robotOut;
	private double output;
	
	public DriveSetOutput(double output) {
		super(RobotComponent.DRIVE);
		this.output = output;
		this.robotOut = RobotOutput.getInstance();
		
	}

	@Override
	public boolean calculate() {
		this.robotOut.setDriveLeft(this.output);
		this.robotOut.setDriveRight(this.output);
		return true;
	}

	@Override
	public void override() {
		// TODO Auto-generated method stub
		
	}

}
