package org.simbotics.frc2017.auton.shooter;

import org.simbotics.frc2017.auton.AutonCommand;
import org.simbotics.frc2017.auton.RobotComponent;
import org.simbotics.frc2017.io.RobotOutput;

public class ShooterSetOutput extends AutonCommand {
	private RobotOutput robotOut;
	private double output;
	
	public ShooterSetOutput(double output) {
		super(RobotComponent.SHOOTER);
		this.output = output;
		this.robotOut = RobotOutput.getInstance();
	}

	@Override
	public boolean calculate() {
		this.robotOut.setShooterOutput(this.output);
		return false;
	}

	@Override
	public void override() {
		// TODO Auto-generated method stub
		this.robotOut.setShooterOutput(0);
	}

}
