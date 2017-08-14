package org.simbotics.frc2017.auton.drive;

import org.simbotics.frc2017.auton.AutonCommand;
import org.simbotics.frc2017.auton.RobotComponent;
import org.simbotics.frc2017.io.RobotOutput;

public class DriveSetHighGear extends AutonCommand {

	private RobotOutput robotOut;
	private boolean isHigh;
	
	public DriveSetHighGear(boolean isHigh) {
		super(RobotComponent.DRIVE);
		this.isHigh = isHigh;
		this.robotOut = RobotOutput.getInstance();
		
	}

	@Override
	public boolean calculate() {
		this.robotOut.setHighGear(this.isHigh);
		return true;
	}

	@Override
	public void override() {
		// TODO Auto-generated method stub
		
	}

}
