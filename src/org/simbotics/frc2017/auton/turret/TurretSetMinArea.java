package org.simbotics.frc2017.auton.turret;

import org.simbotics.frc2017.auton.AutonCommand;
import org.simbotics.frc2017.auton.RobotComponent;
import org.simbotics.frc2017.io.SensorInput;


public class TurretSetMinArea extends AutonCommand {

	private SensorInput sensorIn;
	private double size;
	
	public TurretSetMinArea(double size) {
		super(RobotComponent.TURRET);
		this.size = size;
		this.sensorIn = SensorInput.getInstance();
		
	}

	@Override
	public boolean calculate() {
		this.sensorIn.setMinArea(this.size);
		return true;
	}

	@Override
	public void override() {
		// TODO Auto-generated method stub
		
	}

}
