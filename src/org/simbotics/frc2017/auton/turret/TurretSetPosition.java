package org.simbotics.frc2017.auton.turret;

import org.simbotics.frc2017.auton.AutonCommand;
import org.simbotics.frc2017.auton.RobotComponent;
import org.simbotics.frc2017.io.SensorInput;


public class TurretSetPosition extends AutonCommand {

	private SensorInput sensorIn;
	private double angle;
	
	public TurretSetPosition(double angle) {
		super(RobotComponent.TURRET);
		this.angle = angle;
		this.sensorIn = SensorInput.getInstance();
		
	}

	@Override
	public boolean calculate() {
		this.sensorIn.setTurretPosition(this.angle);
		return true;
	}

	@Override
	public void override() {
		// TODO Auto-generated method stub
		
	}

}
