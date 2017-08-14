package org.simbotics.frc2017.auton.pouch;

import org.simbotics.frc2017.auton.AutonCommand;
import org.simbotics.frc2017.auton.RobotComponent;
import org.simbotics.frc2017.io.RobotOutput;
import org.simbotics.frc2017.io.SensorInput;

public class PouchScoreGearWhenSensor extends AutonCommand {
	private RobotOutput robotOut;
	private SensorInput sensorIn;
	private int cycleCount = 0;
	private int cyclesWithOutSensor = 0;
	private int cyclesToWait;
	private boolean sensorFlag = false;
	
	public PouchScoreGearWhenSensor(int cyclesToWait){
		super(RobotComponent.POUCH);
		this.cyclesToWait = (int)(cyclesToWait/20.0);
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();
	}

	@Override
	public boolean calculate(){
		if(this.sensorFlag){ // light sensor has been triggered
			this.cycleCount++;
			if(this.cycleCount > 7){ // move the gear out
				this.robotOut.setPouchClamp(false); // open
				this.robotOut.setPouchPunch(true); // outside
				this.robotOut.setPouchRamp(false); // inside
				if(this.cycleCount > 25){ // return to normal state
					this.robotOut.setPouchClamp(false); // open
					this.robotOut.setPouchPunch(false); // inside
					this.robotOut.setPouchRamp(false); // inside
					this.robotOut.setCameraLight(false);
					return true;
				}
			}else{ // wait for the door to open
				this.robotOut.setPouchClamp(false); // open
				this.robotOut.setPouchPunch(false); // inside
				this.robotOut.setPouchRamp(false); // inside
				
			}
			return false;
		}else{ // light sensor has not been triggered yet
			if(this.sensorIn.getSpringLightSensor()){ // it has been triggered now 
				this.sensorFlag = true;
				this.robotOut.setCameraLight(true);
			}
			this.cyclesWithOutSensor++;
			if(this.cyclesWithOutSensor > this.cyclesToWait){
				return true;
			}
			return false;
		}
		
		
		
		
	}

	@Override
	public void override() {
		// TODO Auto-generated method stub
		this.robotOut.setPouchClamp(true); // close
		this.robotOut.setPouchPunch(false); // inside
		this.robotOut.setPouchRamp(false); // inside
		this.robotOut.setCameraLight(false);
		
	}

}
