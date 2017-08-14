package org.simbotics.frc2017.auton.turret;

import org.simbotics.frc2017.auton.AutonCommand;
import org.simbotics.frc2017.auton.RobotComponent;

public class TurretWait extends AutonCommand{
	
	public TurretWait() {
		super(RobotComponent.TURRET);
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
