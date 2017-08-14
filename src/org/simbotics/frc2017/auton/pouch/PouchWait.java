package org.simbotics.frc2017.auton.pouch;

import org.simbotics.frc2017.auton.AutonCommand;
import org.simbotics.frc2017.auton.RobotComponent;

public class PouchWait extends AutonCommand{

	public PouchWait() {
		super(RobotComponent.POUCH);
	}

	@Override
	public boolean calculate() {
		
		return true;
	}

	@Override
	public void override() {
		// TODO Auto-generated method stub
		
	}

}
