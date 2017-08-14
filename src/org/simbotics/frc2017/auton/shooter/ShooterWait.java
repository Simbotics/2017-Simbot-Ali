package org.simbotics.frc2017.auton.shooter;

import org.simbotics.frc2017.auton.AutonCommand;
import org.simbotics.frc2017.auton.RobotComponent;

public class ShooterWait extends AutonCommand{
	
	public ShooterWait() {
		super(RobotComponent.SHOOTER);
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
