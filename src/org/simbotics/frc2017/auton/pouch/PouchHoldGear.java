package org.simbotics.frc2017.auton.pouch;

import org.simbotics.frc2017.auton.AutonCommand;
import org.simbotics.frc2017.auton.RobotComponent;
import org.simbotics.frc2017.io.RobotOutput;

public class PouchHoldGear extends AutonCommand {
	private RobotOutput robotOut;
	
	public PouchHoldGear() {
		super(RobotComponent.POUCH);
		this.robotOut = RobotOutput.getInstance();
	}

	@Override
	public boolean calculate() {
		this.robotOut.setPouchClamp(true); // closed
		this.robotOut.setPouchPunch(false); // inside
		this.robotOut.setPouchRamp(false); // inside
		this.robotOut.setBallRamp(false); // inside
		return true;
	}

	@Override
	public void override() {
		// TODO Auto-generated method stub
		
	}

}
