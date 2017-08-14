package org.simbotics.frc2017.auton.util;

import org.simbotics.frc2017.auton.AutonCommand;
import org.simbotics.frc2017.auton.RobotComponent;
import org.simbotics.frc2017.io.SensorInput;

/**
 *
 * @author Michael
 */
public class AutonWaitUntilAutoTime extends AutonCommand {

    private long whenToStopWaiting;
    private boolean firstCycle;
    private SensorInput sensorIn;
    
    public AutonWaitUntilAutoTime(long whenToStopWaiting) {
        super(RobotComponent.UTIL);
        this.whenToStopWaiting = whenToStopWaiting;
        this.firstCycle = true;
        this.sensorIn = SensorInput.getInstance();
    }

    /*
     * need to override checkAndRun so that it
     * blocks even before going in to its "run seat"
     */
    @Override
	public boolean checkAndRun(){
        if(this.sensorIn.getTimeSinceAutoStarted() < this.whenToStopWaiting) {
            // haven't reached time limit yet
            return false;
        } else {
            // if reached time, use the normal checkAndRun
            return super.checkAndRun();
        }
    }
    
    @Override
	public boolean calculate() {
        return true;
    }

	@Override
	public void override() {
		// nothing to do
		
	}
    
    
    
}
