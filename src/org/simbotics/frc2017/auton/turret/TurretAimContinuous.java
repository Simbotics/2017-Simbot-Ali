package org.simbotics.frc2017.auton.turret;

import org.simbotics.frc2017.auton.AutonCommand;
import org.simbotics.frc2017.auton.RobotComponent;
import org.simbotics.frc2017.util.TurretControl;


public class TurretAimContinuous extends AutonCommand {
	private double targetAngle;
	private boolean firstCycle = true;
	private TurretControl turretControl; 
	
	public TurretAimContinuous(double angle) {
		super(RobotComponent.TURRET,-1);
		this.targetAngle = angle;
		this.turretControl = TurretControl.getInstance();
		
	}

	@Override
	public boolean calculate() {
		if(this.firstCycle){
			this.turretControl.setPresetTarget(this.targetAngle, true); // goes to an angle then aims
			this.firstCycle = false;
		}
		if(this.turretControl.calculate()){ // has finished aiming
			this.turretControl.cameraAim(); // re aim
		}
		return false; // never stop re aiming
	}

	@Override
	public void override() {
		this.turretControl.reset();
		
	}

}
