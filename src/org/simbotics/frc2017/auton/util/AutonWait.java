package org.simbotics.frc2017.auton.util;

import org.simbotics.frc2017.auton.AutonCommand;
import org.simbotics.frc2017.auton.RobotComponent;

/**
 *
 * @author Michael
 */
public class AutonWait extends AutonCommand {

    private long startTime;
    private long delayTime;
    private boolean firstCycle;
    
    public AutonWait(long howLong) {
        super(RobotComponent.UTIL);
        this.delayTime = howLong;
        this.firstCycle = true;
    }

    /*
     * need to override checkAndRun so that it
     * blocks even before going in to its "run seat"
     */
    @Override
	public boolean checkAndRun() {
        if(this.firstCycle) {
            this.firstCycle = false;
            this.startTime = System.currentTimeMillis();
        }
        
        long timeElapsed = System.currentTimeMillis() - this.startTime;
        
        if(timeElapsed < this.delayTime) {
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
