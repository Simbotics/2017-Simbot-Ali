package org.simbotics.frc2017.auton.turret;

import org.simbotics.frc2017.auton.AutonCommand;
import org.simbotics.frc2017.auton.RobotComponent;
import org.simbotics.frc2017.io.RobotOutput;

public class TurretSetOutput extends AutonCommand {

	private RobotOutput robotOut;
	private double output;
	
	public TurretSetOutput(double output) {
		super(RobotComponent.TURRET);
		this.output = output;
		this.robotOut = RobotOutput.getInstance();
		
	}

	@Override
	public boolean calculate() {
		this.robotOut.setTurret(this.output);
		return true;
	}

	@Override
	public void override() {
		// TODO Auto-generated method stub
		
	}

}
