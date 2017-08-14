package org.simbotics.frc2017.auton.pouch;

import org.simbotics.frc2017.auton.AutonCommand;
import org.simbotics.frc2017.auton.RobotComponent;
import org.simbotics.frc2017.io.RobotOutput;

public class SetBallRamp extends AutonCommand {
	private RobotOutput robotOut;
	private boolean isOut;
	
	public SetBallRamp(boolean isOut) {
		super(RobotComponent.POUCH);
		this.robotOut = RobotOutput.getInstance();
		this.isOut = isOut;
		
	}

	@Override
	public boolean calculate() {
		this.robotOut.setBallRamp(this.isOut);
		this.robotOut.setPouchRamp(this.isOut);
		return true;
	}

	@Override
	public void override() {
		// TODO Auto-generated method stub
		
	}

}
