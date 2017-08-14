package org.simbotics.frc2017.auton.pouch;

import org.simbotics.frc2017.auton.AutonCommand;
import org.simbotics.frc2017.auton.RobotComponent;
import org.simbotics.frc2017.io.RobotOutput;

public class PouchScoreGear extends AutonCommand {
	private RobotOutput robotOut;
	private int cycleCount = 0;
	
	public PouchScoreGear() {
		super(RobotComponent.POUCH);
		this.robotOut = RobotOutput.getInstance();
	}

	@Override
	public boolean calculate() {
		this.cycleCount++;
		if(this.cycleCount > 10){ // move the gear out
			this.robotOut.setPouchClamp(false); // open
			this.robotOut.setPouchPunch(true); // outside
			this.robotOut.setPouchRamp(false); // inside
			if(this.cycleCount > 40){ // return to normal state
				this.robotOut.setPouchClamp(false); // close
				this.robotOut.setPouchPunch(false); // inside
				this.robotOut.setPouchRamp(false); // inside
				return true;
			}
		}else{ // wait for the door to open
			this.robotOut.setPouchClamp(false); // open
			this.robotOut.setPouchPunch(false); // inside
			this.robotOut.setPouchRamp(false); // inside
			
		}
		return false;
	}

	@Override
	public void override() {
		// TODO Auto-generated method stub
		
	}

}
