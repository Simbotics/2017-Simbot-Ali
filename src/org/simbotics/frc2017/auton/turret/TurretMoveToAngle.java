package org.simbotics.frc2017.auton.turret;

import org.simbotics.frc2017.auton.AutonCommand;
import org.simbotics.frc2017.auton.RobotComponent;
import org.simbotics.frc2017.util.TurretControl;


public class TurretMoveToAngle extends AutonCommand {
	private double targetAngle;
	private boolean firstCycle = true;
	private TurretControl turretControl; 
	
	public TurretMoveToAngle(double angle, long timeout) {
		super(RobotComponent.TURRET,timeout);
		this.targetAngle = angle;
		this.turretControl = TurretControl.getInstance();
		
	}

	@Override
	public boolean calculate() {
		if(this.firstCycle){
			this.turretControl.setPresetTarget(this.targetAngle, false);
			this.firstCycle = false;
		}
		return this.turretControl.calculate(); // will be true once targeting is done
	}

	@Override
	public void override() {
		this.turretControl.reset();
		
	}

}
