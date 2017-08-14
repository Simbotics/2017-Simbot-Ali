package org.simbotics.frc2017.util;

import org.opencv.core.Point;
import org.simbotics.frc2017.imaging.SimCamera;
import org.simbotics.frc2017.imaging.SimTargetingInfo;
import org.simbotics.frc2017.io.DriverInput;
import org.simbotics.frc2017.io.RobotOutput;
import org.simbotics.frc2017.io.SensorInput;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TurretControl {
	
	private static TurretControl instance;
	private RobotOutput robotOut;
	private SensorInput sensorIn;
	private DriverInput driverIn;
	private SimMotionProfile turretProfile;
	private SimCamera simCamera;
	
	private double cameraEps;
	private int maxImageCount;
	private int imageCount = 0;
	private SimTargetingInfo targetInfo;
	
	private TargetingMode targetingState = TargetingMode.WAITING_FOR_TARGET;
	private Point currentMidpoint;
	
	private double p;
	private double i;
	private double d;
	private double velFF;
	private double accelFF; 
	private double eps;
	private boolean firstCycle = true;
	private boolean cameraAfter = false;
	private double presetTarget = 90;
	private double offset = 0.0; //0.05
	
	public static TurretControl getInstance(){
		if(instance == null){
			instance = new TurretControl();
		}
		return instance;
	}
	
	
	private TurretControl() {
		this.sensorIn = SensorInput.getInstance();
		this.robotOut = RobotOutput.getInstance();
		this.driverIn = DriverInput.getInstance();
		this.targetInfo = new SimTargetingInfo();
		
		this.p = RobotConstants.turretP;
		this.i = RobotConstants.turretI;
		this.d = RobotConstants.turretD;
		this.velFF = RobotConstants.turretKv;
		this.accelFF = RobotConstants.turretKa;
		this.eps = RobotConstants.turretEps;
		
		this.turretProfile = new SimMotionProfile(this.p, this.i, this.d, this.velFF, this.accelFF,this.eps, 0.01);
		this.turretProfile.configureProfile(this.sensorIn.getDeltaTime(), RobotConstants.TURRET_MAX_VEL,
				RobotConstants.TURRET_MAX_ACCEL, RobotConstants.TURRET_MAX_DECEL);
		
		this.simCamera = sensorIn.getSimCamera();
		this.cameraEps = RobotConstants.TURRET_CAMERA_EPSILON;
		this.maxImageCount = RobotConstants.TURRET_MAX_IMAGE_COUNT;
	
	}
	
	public enum TargetingMode {
		WAITING_FOR_TARGET,
		HAS_IMAGE,
		MOVING_TO_CAMERA_TARGET,
		MOVING_TO_PRESET_TARGET,
		HOMING_DONE
	}
	
	//add motor outputs
	
	public void cameraAim(){
		this.reset();
		this.targetingState = TargetingMode.WAITING_FOR_TARGET;
	}
	
	
	public void setPresetTarget(double angle, boolean cameraAfter){
		this.cameraAfter = cameraAfter;
		this.reset();
		if(Math.abs(angle - this.sensorIn.getTurretAngle()) < 20.0){ // already withing eps
			if(this.cameraAfter){
				this.targetingState = TargetingMode.WAITING_FOR_TARGET;
			}else{
				if(Math.abs(angle - this.sensorIn.getTurretAngle()) < this.eps){ // already withing eps
					this.targetingState = TargetingMode.HOMING_DONE;
				}else{
					this.targetingState = TargetingMode.MOVING_TO_PRESET_TARGET;
				}
			}
		}else{
			this.targetingState = TargetingMode.MOVING_TO_PRESET_TARGET;
		}
		this.presetTarget = angle;
	}
	
	
	
	public boolean calculate() {
		this.cameraEps = SmartDashboard.getNumber("2_Turret Camera Eps: ", RobotConstants.TURRET_CAMERA_EPSILON);
		this.maxImageCount = (int) SmartDashboard.getNumber("2_Turret Max Image Count: ",RobotConstants.TURRET_MAX_IMAGE_COUNT);
		
		this.p = SmartDashboard.getNumber("2_Turret P: ", RobotConstants.turretP);
		this.i = SmartDashboard.getNumber("2_Turret I: ", RobotConstants.turretP);
		this.d = SmartDashboard.getNumber("2_Turret D: ", RobotConstants.turretP);
		this.velFF = SmartDashboard.getNumber("2_Turret VelFF: ", RobotConstants.turretP);
		this.accelFF = SmartDashboard.getNumber("2_Turret AcclFF: ", RobotConstants.turretP);
		this.eps = SmartDashboard.getNumber("2_Turret Eps: ", RobotConstants.turretEps);
		
		this.turretProfile.setConstants(this.p, this.i, this.d, this.velFF, this.accelFF);
		this.turretProfile.setFinishedRange(this.eps);
		this.turretProfile.setDebug(true);
	
		
		if(this.targetingState == TargetingMode.WAITING_FOR_TARGET) { // start looking for an image
			this.robotOut.setCameraLight(true); // turn light on
			this.sensorIn.setCheckingForMovement(true); // tell sensorin to check if we move from this point while processing
			this.simCamera.setProcessingOn(true); // start image processing 
			this.getImage(); // look for an image
		}else{
			if(this.driverIn.getShooterOffButton()){
				this.robotOut.setCameraLight(true);
			}else{
				if(this.sensorIn.getSpringLightSensor()){ // we detect a spring in the gear pouch 
					this.robotOut.setCameraLight(true);
				}else{
					this.robotOut.setCameraLight(false);
				}
			} 
		}
		
		if(this.targetingState == TargetingMode.HAS_IMAGE) { // once we have found a target
			if(Math.abs(targetInfo.getYawToTargetDegrees(this.currentMidpoint)) < this.cameraEps) { //if we are already pointing at it
				this.targetingState = TargetingMode.HOMING_DONE;
			} else {
				this.targetingState = TargetingMode.MOVING_TO_CAMERA_TARGET;
			}
		}
			
		if(this.targetingState == TargetingMode.MOVING_TO_CAMERA_TARGET || 
				this.targetingState == TargetingMode.MOVING_TO_PRESET_TARGET) {
			this.goToTarget();
		}
		
		SmartDashboard.putString("12_TURRET TARGETING STATE: ", this.targetingState.toString());
		
		if(this.targetingState == TargetingMode.HOMING_DONE && !this.sensorIn.getHasDriveMoved()) { // we have finished aiming and have not moved 
			this.imageCount = 0;
			this.sensorIn.setCheckingForMovement(false);
			this.setTurretSafe(0.0);
			return true;
		}else if(this.targetingState == TargetingMode.HOMING_DONE && this.sensorIn.getHasDriveMoved()){ // we have moved
			resetTarget();
			
		}
		return false;
	}
	
	public boolean calculate(double output){
		this.setTurretSafe(output);
		return true;
	}

	private void getImage() {
		this.currentMidpoint = this.simCamera.getMidPoint();
		if(this.currentMidpoint != null) {
			this.imageCount++;
			this.simCamera.setProcessingOn(false);
			this.sensorIn.setDistanceFromGoalInches(this.targetInfo.getDistanceToTargetInches(this.currentMidpoint));
			this.targetingState = TargetingMode.HAS_IMAGE;
		} 
	}
	
	private void goToTarget() {
		double turretAngle = this.sensorIn.getTurretAngle();
		double angleToTarget = 0;
		System.out.println("FRIST CYCLE: " + this.firstCycle);
		if(this.firstCycle){
			if(this.targetingState == TargetingMode.MOVING_TO_CAMERA_TARGET){
				double distance = this.sensorIn.getDistanceFromGoalInches();
				if(distance > 60){
					angleToTarget = turretAngle + this.targetInfo.getYawToTargetDegrees(this.currentMidpoint)
					+ (distance-60.0) * this.offset;
				}else{
					angleToTarget = turretAngle + this.targetInfo.getYawToTargetDegrees(this.currentMidpoint);
				}
			}else if(this.targetingState == TargetingMode.MOVING_TO_PRESET_TARGET){
				angleToTarget = this.presetTarget;
				this.simCamera.setProcessingOn(false);
				if(this.driverIn.getShooterOffButton()){
					this.robotOut.setCameraLight(true);
				}else{
					if(this.sensorIn.getSpringLightSensor()){ // we detect a spring in the gear pouch 
						this.robotOut.setCameraLight(true);
					}else{
						this.robotOut.setCameraLight(false);
					}
				}
				this.sensorIn.setCheckingForMovement(false);
			}
			this.turretProfile.setDesiredValue(this.sensorIn.getTurretPositionState(), this.sensorIn.getTurretVelocityState(), 
					this.sensorIn.getTurretAccelerationState(), angleToTarget);
			this.firstCycle = false;
		}
		
		
		double output = -this.turretProfile.calculate(turretAngle, this.sensorIn.getTurretVelocityState());
		this.setTurretSafe(output);

		if(this.turretProfile.isDone()) {
			if(!this.cameraAfter){
				this.targetingState = TargetingMode.HOMING_DONE;
			}else{
				if(this.imageCount > 1){ // make sure we take two pictures so that the distance calc is correct
					this.targetingState = TargetingMode.HOMING_DONE;
				}else{
					this.resetTarget();
				}
				
			}
		}
	}
	
	public void setTurretSafe(double output){
		this.simCamera.setProcessingOn(false);
		
		if(this.driverIn.getShooterOffButton()){
			this.robotOut.setCameraLight(true);
		}else{
			if(this.sensorIn.getSpringLightSensor()){ // we detect a spring in the gear pouch 
				this.robotOut.setCameraLight(true);
			}else{
				this.robotOut.setCameraLight(false);
			}
		}
		if(output > 0){ // going to the right
			if(this.sensorIn.getTurretPositionState() < -10){ // past the maximum
				this.robotOut.setTurret(0);
			}else{
				this.robotOut.setTurret(output);
			}
		}else{ // going to the left
			if(this.sensorIn.getTurretPositionState() > 298){ // past the maximum
				this.robotOut.setTurret(0);
			}else{
				this.robotOut.setTurret(output);
			}
		}
		
		
	}
	
	public void reset() {
		this.resetTarget();
		this.imageCount = 0;
	}
	
	private void resetTarget(){
		this.targetingState = TargetingMode.WAITING_FOR_TARGET;
		this.sensorIn.setCheckingForMovement(false);
		this.currentMidpoint = null;
		this.firstCycle = true;
		this.robotOut.setTurret(0.0);
	}

}

