package org.simbotics.frc2017.auton.drive;

import org.simbotics.frc2017.auton.AutonCommand;
import org.simbotics.frc2017.auton.RobotComponent;
import org.simbotics.frc2017.io.RobotOutput;

public class DriveSetOutputSides extends AutonCommand {

	private RobotOutput robotOut;
	private double left;
	private double right;
	
	public DriveSetOutputSides(double left,double right) {
		super(RobotComponent.DRIVE);
		this.left = left;
		this.right = right;
		this.robotOut = RobotOutput.getInstance();
		
	}

	@Override
	public boolean calculate() {
		this.robotOut.setDriveLeft(this.left);
		this.robotOut.setDriveRight(this.right);
		return true;
	}

	@Override
	public void override() {
		// TODO Auto-generated method stub
		
	}

}
