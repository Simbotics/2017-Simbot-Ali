package org.simbotics.frc2017.teleop;

import org.simbotics.frc2017.io.DriverInput;
import org.simbotics.frc2017.io.RobotOutput;
import org.simbotics.frc2017.io.SensorInput;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TeleopPouch implements TeleopComponent {

	private static TeleopPouch instance;
	
	private RobotOutput robotOut;
	private DriverInput driverIn;
	private SensorInput sensorIn;
	private IntakeState currentState = IntakeState.HAS_GEAR; 
	private int cyclesInScoringState = 0;
	private int cyclesInRetracting = 0;
	
	public static TeleopPouch getInstance(){
		if(instance == null){
			instance = new TeleopPouch();
		}
		return instance;
	}
	
	private TeleopPouch(){
		this.driverIn = DriverInput.getInstance();
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();
	}
	
	public enum IntakeState {
		WAITING_FOR_GEAR,
		GETTING_GEAR,
		HAS_GEAR,
		GETTING_BALLS,
		SCORING_GEAR,
		SCORING_GEAR_AUTO,
		RETRACTING
	}
	
	@Override
	public void calculate() {
		//Change to the correct state
		if(this.driverIn.getOperatorBallRampButton()){ // if we want to get balls
			this.currentState = IntakeState.GETTING_BALLS;
			resetCycles();
			resetRetractingCycles(); 
		}else if(this.driverIn.getDriverManualScoreGearButton()){ // manual gear scoring
			this.currentState = IntakeState.SCORING_GEAR;
			resetRetractingCycles();
		}else if(this.driverIn.getDriverScoreGearButton() && this.sensorIn.getSpringLightSensor()){ // pressing button and gear peg is detected 
			this.currentState = IntakeState.SCORING_GEAR_AUTO;
			resetRetractingCycles();
		}else if(this.driverIn.getDriverGetGearButton() && this.currentState != IntakeState.RETRACTING && this.currentState != IntakeState.SCORING_GEAR 
				&& this.currentState != IntakeState.SCORING_GEAR_AUTO){ // make sure we dont close the sides before the puncher is retracted 
			this.currentState = IntakeState.GETTING_GEAR;
			resetCycles();
		}else{ // not pressing any buttons
			if(this.currentState == IntakeState.SCORING_GEAR){ // if we were scoring but now we aren't pressing any buttons 
				this.currentState = IntakeState.RETRACTING;
			}else if(this.currentState == IntakeState.GETTING_GEAR){ // if we were getting a gear now we have one
				this.currentState = IntakeState.HAS_GEAR;
			}else if(this.currentState == IntakeState.GETTING_BALLS){
				this.currentState = IntakeState.HAS_GEAR;
			}else if(this.currentState == IntakeState.RETRACTING && this.cyclesInRetracting > 30){ // we are finished retracting 
				this.currentState = IntakeState.WAITING_FOR_GEAR;
				resetRetractingCycles();
			}
			if(this.currentState != IntakeState.SCORING_GEAR_AUTO){
				resetCycles();
			}
		}
		
		if(this.currentState == IntakeState.GETTING_GEAR){
			this.robotOut.setPouchClamp(true); // open
			this.robotOut.setPouchPunch(false); // inside
			this.robotOut.setPouchRamp(true); // outside
			this.robotOut.setBallRamp(false); // inside
		}else if(this.currentState == IntakeState.SCORING_GEAR){
			this.cyclesInScoringState++;
			if(this.cyclesInScoringState > 10){ // move the gear out
				this.robotOut.setPouchClamp(false); // open
				this.robotOut.setPouchPunch(true); // outside
				this.robotOut.setPouchRamp(false); // inside
				this.robotOut.setBallRamp(false); // inside
			}else{ // wait for the door to open
				this.robotOut.setPouchClamp(false); // open
				this.robotOut.setPouchPunch(false); // inside
				this.robotOut.setPouchRamp(false); // inside
				this.robotOut.setBallRamp(false); // inside
			}
		}else if(this.currentState == IntakeState.SCORING_GEAR_AUTO){
			this.cyclesInScoringState++;
			if(this.cyclesInScoringState > 30){ // now retract
				this.robotOut.setPouchClamp(false); // open
				this.robotOut.setPouchPunch(false); // inside
				this.robotOut.setPouchRamp(false); // inside
				this.robotOut.setBallRamp(false); // inside
				this.currentState = IntakeState.RETRACTING;
			}else if(this.cyclesInScoringState > 10){	// move the gear out
				this.robotOut.setPouchClamp(false); // open
				this.robotOut.setPouchPunch(true); // outside
				this.robotOut.setPouchRamp(false); // inside
				this.robotOut.setBallRamp(false); // inside	
			}else{ // wait for the door to open
				this.robotOut.setPouchClamp(false); // open
				this.robotOut.setPouchPunch(false); // inside
				this.robotOut.setPouchRamp(false); // inside
				this.robotOut.setBallRamp(false); // inside
			}
		}else if(this.currentState == IntakeState.HAS_GEAR){
			this.robotOut.setPouchClamp(true); // closed
			this.robotOut.setPouchPunch(false); // inside
			this.robotOut.setPouchRamp(false); // inside
			this.robotOut.setBallRamp(false); // inside
		}else if(this.currentState == IntakeState.WAITING_FOR_GEAR){
			this.robotOut.setPouchClamp(false); // open
			this.robotOut.setPouchPunch(false); // inside
			this.robotOut.setPouchRamp(false); // inside
			this.robotOut.setBallRamp(false); // inside
		}else if(this.currentState == IntakeState.RETRACTING){
			this.robotOut.setPouchClamp(false); // open
			this.robotOut.setPouchPunch(false); // inside
			this.robotOut.setPouchRamp(false); // inside
			this.robotOut.setBallRamp(false); // inside
			this.cyclesInRetracting++;
		}else if(this.currentState == IntakeState.GETTING_BALLS){
			this.robotOut.setPouchClamp(true); // closed
			this.robotOut.setPouchPunch(false); // inside
			this.robotOut.setPouchRamp(true); // outside
			this.robotOut.setBallRamp(true); // outside
			
		}
		
		SmartDashboard.putString("12_POUCH STATE: ", this.currentState.toString());
		
	}
	
	public IntakeState getPouchState(){
		return this.currentState;
	}
	
	private void resetCycles(){
		this.cyclesInScoringState = 0;
	}
	
	private void resetRetractingCycles(){
		this.cyclesInRetracting = 0;
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		resetCycles();
		resetRetractingCycles();
		
	}
	
	

}
