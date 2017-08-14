package org.simbotics.frc2017.auton.shooter;

import org.simbotics.frc2017.auton.AutonCommand;
import org.simbotics.frc2017.auton.RobotComponent;
import org.simbotics.frc2017.io.RobotOutput;

public class ShooterSetRPM extends AutonCommand {

	private RobotOutput robotOut;
	
	private double targetRPM;
	private double blenderOutput;
	
	public ShooterSetRPM(double targetRPM, double blenderOutput) {
		super(RobotComponent.SHOOTER);
		this.targetRPM = targetRPM;
		this.blenderOutput = blenderOutput;
		this.robotOut = RobotOutput.getInstance();
	}

	@Override
	public boolean calculate() {
		this.robotOut.setShooterTarget(this.targetRPM);
		this.robotOut.setBlender(this.blenderOutput);
		return false;
	}

	@Override
	public void override() {
		// TODO Auto-generated method stub
		
		this.robotOut.setBlender(0.0);
	}

}
