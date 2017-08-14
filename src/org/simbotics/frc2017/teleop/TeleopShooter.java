package org.simbotics.frc2017.teleop;

import org.simbotics.frc2017.io.DriverInput;
import org.simbotics.frc2017.io.RobotOutput;
import org.simbotics.frc2017.io.SensorInput;
import org.simbotics.frc2017.teleop.TeleopPouch.IntakeState;
import org.simbotics.frc2017.util.RobotConstants;
import org.simbotics.frc2017.util.SimPIDF;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TeleopShooter implements TeleopComponent  {
	
	private static TeleopShooter instance;
	private RobotOutput robotOut;
	private DriverInput driverIn;
	private SensorInput sensorIn;
	private TeleopPouch teleopPouch;
	
	private SimPIDF elevatorControl;
	
	private int targetRPM = 0;
	private double elevatorTargetRPM = 0;
	private int preset = 0;
	private int defaultSpeed;
	private FeederMode feederState = FeederMode.FEEDER_OFF;
	
	
	public static TeleopShooter getInstance(){
		if(instance == null){
			instance = new TeleopShooter();
		}
		return instance;
	}
	
	private TeleopShooter(){
		this.driverIn = DriverInput.getInstance();
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();
		this.teleopPouch = TeleopPouch.getInstance();
		
		this.elevatorControl = new SimPIDF(0, 0, 0, 0, 0);
		
	}
	
	public enum FeederMode {
		FEEDER_BOOPING,
		FEEDER_SHOOTING,
		FEEDER_REVERSE,
		FEEDER_OFF
	}
	
	@Override
	public void calculate() {
		double p = SmartDashboard.getNumber("2_Shooter P: ", RobotConstants.shooterP);
		double i = SmartDashboard.getNumber("2_Shooter I: ", RobotConstants.shooterI);
		double d = SmartDashboard.getNumber("2_Shooter D: ", RobotConstants.shooterD);
		double ff = SmartDashboard.getNumber("2_Shooter FF: ", RobotConstants.shooterFF);
		double eps = SmartDashboard.getNumber("2_Shooter Eps: " , RobotConstants.shooterEps);
		this.defaultSpeed = (int) SmartDashboard.getNumber("2_Shooter Adjustable Shot RPM: ", RobotConstants.ADJUSTABLE_RPM);
		this.robotOut.configureShooterPID(p, i, d, ff, (int) eps);
		
		double eP = SmartDashboard.getNumber("2_Elevator P: ", RobotConstants.elevatorP);
		double eI = SmartDashboard.getNumber("2_Elevator I: ", RobotConstants.elevatorI);
		double eD = SmartDashboard.getNumber("2_Elevator D: ", RobotConstants.elevatorD);
		double eFF = SmartDashboard.getNumber("2_Elevator FF: ", RobotConstants.elevatorFF);
		double eEps = SmartDashboard.getNumber("2_Elevator Eps: ", RobotConstants.elevatorEps);
		this.elevatorTargetRPM = SmartDashboard.getNumber("2_Elevator Adjustable RPM: ", RobotConstants.elevatorAdjustableRPM);
		

		this.elevatorControl.setConstants(eP, eI, eD, eFF);
		this.elevatorControl.setFinishedRange(eEps);
		
		
		if(this.driverIn.getTurretLeftButton() ||  this.driverIn.getTurretBackButton()|| this.driverIn.getTurretMidButton() || this.driverIn.getTurretRightButton()){
			this.preset = 1;
		}else if(this.driverIn.getShooterOffButton()){
			this.preset = 0;
		}
		
		
		double dist = this.sensorIn.getDistanceFromGoalInches();
		if(this.preset == 1){
				double x2;
				double x;
				double shift;
			if(this.elevatorTargetRPM > 800){ // use fast constants
				x2 = SmartDashboard.getNumber("2_Shooter X^2 Fast: ", RobotConstants.SHOOTER_X_2_FAST);
				x = SmartDashboard.getNumber("2_Shooter X Fast: ", RobotConstants.SHOOTER_X_FAST);
				shift = SmartDashboard.getNumber("2_Shooter Shift Fast: ", RobotConstants.SHOOTER_SHIFT_FAST);
			}else{
				x2 = SmartDashboard.getNumber("2_Shooter X^2: ", RobotConstants.SHOOTER_X_2);
				x = SmartDashboard.getNumber("2_Shooter X: ", RobotConstants.SHOOTER_X);
				shift = SmartDashboard.getNumber("2_Shooter Shift: ", RobotConstants.SHOOTER_SHIFT);
			}
			this.targetRPM = (int) (x2*(dist*dist)+x*(dist)+shift); // try 0.02x^2 for better far shot
			
		}else if(this.preset == 2){
			this.targetRPM = this.defaultSpeed;
		}else{
			this.targetRPM = 0;
		}
		
	
		
		if(this.preset != 0){
			this.robotOut.setShooterTarget(this.targetRPM);
			//this.robotOut.setShooterOutput(1.0);
		}else{
			this.robotOut.setShooterOutput(0.0);
			        
		}
		
		if(this.driverIn.getFeederShootButton()){
			this.feederState = FeederMode.FEEDER_SHOOTING;
		}else if(this.driverIn.getFeedReverseButton()){
			this.feederState = FeederMode.FEEDER_REVERSE;
		}else if(this.teleopPouch.getPouchState() == IntakeState.GETTING_BALLS){
			this.feederState = FeederMode.FEEDER_BOOPING;
		}else{
			this.feederState = FeederMode.FEEDER_OFF;
		}
		
		
		if(this.feederState == FeederMode.FEEDER_SHOOTING){
			this.elevatorControl.setDesiredValue(this.elevatorTargetRPM);
			double output = this.elevatorControl.calcPID(this.sensorIn.getElevatorRPM());
			this.robotOut.setElevator(output);
			this.robotOut.setBlender(1.0);
		}else if (this.feederState == FeederMode.FEEDER_REVERSE){
			this.elevatorControl.resetErrorSum();
			this.robotOut.setElevator(-1.0);
			this.robotOut.setBlender(-1.0);
		}else if(this.feederState == FeederMode.FEEDER_BOOPING){ // spin wheel to move balls around
			this.elevatorControl.resetErrorSum();
			this.robotOut.setElevator(0.0);
			this.robotOut.setBlender(1.0);
		}else if(this.feederState == FeederMode.FEEDER_OFF){
			this.elevatorControl.resetErrorSum();
			this.robotOut.setElevator(0);
			this.robotOut.setBlender(0);
		}
		
		SmartDashboard.putBoolean("12_ShooterAtSpeed: ", this.robotOut.isShooterWithinEps(200));
		SmartDashboard.putNumber("12_Target RPM: ", this.targetRPM);
		SmartDashboard.putString("12_FEEDER STATE: ", this.feederState.toString());
		
		
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		this.robotOut.setElevator(0.0);
		this.robotOut.setBlender(0);
		this.robotOut.setShooterOutput(0.0);
		this.preset = 0;
		this.feederState = FeederMode.FEEDER_OFF;
		
	}

	

}
