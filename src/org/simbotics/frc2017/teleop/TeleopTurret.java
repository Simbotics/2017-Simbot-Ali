package org.simbotics.frc2017.teleop;

import org.simbotics.frc2017.io.DriverInput;
import org.simbotics.frc2017.io.RobotOutput;
import org.simbotics.frc2017.io.SensorInput;
import org.simbotics.frc2017.util.TurretControl;


public class TeleopTurret implements TeleopComponent{
	private static TeleopTurret instance;
	private DriverInput driverIn;
	private SensorInput sensorIn;
	private RobotOutput robotOut;
	private TurretControl turretControl; 
	private int turretPreset = 0;
	private int lastTurretPreset =0;
	private boolean hasFinishedAiming = false;
	private boolean shouldReAim = false;

	
	public static TeleopTurret getInstance() {
		if(instance == null) {
			instance = new TeleopTurret();
		}
		return instance;
	}
	
	private TeleopTurret() {
		this.driverIn = DriverInput.getInstance();
		this.sensorIn = SensorInput.getInstance();
		this.robotOut = RobotOutput.getInstance();
		this.turretControl = TurretControl.getInstance();
		
		this.sensorIn.setMinArea(300.0);
	}
	
	@Override
	public void calculate() {
		if(this.driverIn.getTurretLeftButton()){
			this.turretPreset = 1;
		}else if(this.driverIn.getTurretMidButton()){
			this.turretPreset = 2;
		}else if(this.driverIn.getTurretRightButton()){
			this.turretPreset = 3;
		}else if(this.driverIn.getTurretBackButton()){
			this.turretPreset = 5;
		}else if(Math.abs(this.driverIn.getTurretManualStick()) > 0.2){
			this.turretPreset = 4;
		}else if(this.driverIn.getTurretCameraAimButton()){
			this.turretPreset = 6;
		}else if(this.driverIn.getShooterOffButton()){ // go back to the middle and dont aim once the shooter is off 
			this.turretPreset = 7;
		}
		
		boolean hasChangedPreset = (this.turretPreset != this.lastTurretPreset);
		this.lastTurretPreset = this.turretPreset;
		
		boolean operatorButtons = (this.driverIn.getTurretLeftButton() || this.driverIn.getTurretMidButton()
				|| this.driverIn.getTurretRightButton() || this.driverIn.getTurretBackButton() || this.driverIn.getTurretCameraAimButton());
		
		if(this.hasFinishedAiming && (this.sensorIn.getIsDriveMoving() || operatorButtons)){ // we have finished aiming now reaim if we ever start moving or press another button  
			this.shouldReAim = true;
		}
		
		if(this.turretPreset == 1){
			if(hasChangedPreset){ // need to set a new target for the turret control
				this.turretControl.setPresetTarget(170,false);
				this.shouldReAim = true;
			}else if(this.shouldReAim && !this.sensorIn.getIsDriveMoving() && this.hasFinishedAiming){ // once we have finished aiming and are not moving start camera aiming 
				this.turretControl.cameraAim();
				this.shouldReAim = false;
			}
		}else if(this.turretPreset == 2){
			if(hasChangedPreset){
				this.turretControl.setPresetTarget(90,false);
				this.shouldReAim = true;
			}else if(this.shouldReAim && !this.sensorIn.getIsDriveMoving() && this.hasFinishedAiming){
				this.turretControl.cameraAim();
				this.shouldReAim = false;
			}
		}else if(this.turretPreset == 3){
			if(hasChangedPreset){
				this.turretControl.setPresetTarget(10,false);
				this.shouldReAim = true;
			}else if(this.shouldReAim && !this.sensorIn.getIsDriveMoving() && this.hasFinishedAiming){
				this.turretControl.cameraAim();
				this.shouldReAim = false;
			}
		}else if(this.turretPreset == 5){
			if(hasChangedPreset){
				this.turretControl.setPresetTarget(280,false);
				this.shouldReAim = true;
			}else if(this.shouldReAim && !this.sensorIn.getIsDriveMoving() && this.hasFinishedAiming){
				this.turretControl.cameraAim();
				this.shouldReAim = false;
			}
		}else if(this.turretPreset == 6){
			if(hasChangedPreset || (this.shouldReAim && !this.sensorIn.getIsDriveMoving()) && this.hasFinishedAiming){
				this.turretControl.cameraAim();
				this.shouldReAim = false;
			}
		}else if(this.turretPreset == 7){
			if(hasChangedPreset){
				this.turretControl.setPresetTarget(90,false);
			}
		}
			
		if(this.turretPreset != 4 && this.turretPreset != 0) { // if we aren't doing manual control
			this.hasFinishedAiming = this.turretControl.calculate(); // check if the turret control is finished 
		}else{ // manual control
			this.turretControl.setTurretSafe(this.driverIn.getTurretManualStick() / 1.5);
			if(this.driverIn.getShooterOffButton()){ // human player signaling 
				this.robotOut.setCameraLight(true);
			}else{
				if(this.sensorIn.getSpringLightSensor()){ // we detect a spring in the gear pouch 
					this.robotOut.setCameraLight(true);
				}else{
					this.robotOut.setCameraLight(false);
				}
			}
			
			this.hasFinishedAiming = false;
			this.shouldReAim = false;
		}
		
		if(this.driverIn.getOperatorStartButton()){
			this.sensorIn.reset();
		}
	}

	@Override
	public void disable() {
		//this.turretControl.reset();
		this.turretControl.setTurretSafe(0.0);
	}
}
