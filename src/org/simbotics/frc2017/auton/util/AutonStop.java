package org.simbotics.frc2017.auton.util;

import org.simbotics.frc2017.auton.AutonCommand;
import org.simbotics.frc2017.auton.AutonControl;
import org.simbotics.frc2017.auton.RobotComponent;

/**
 *
 * @author Michael
 */
public class AutonStop extends AutonCommand {

    public AutonStop() {
        super(RobotComponent.UTIL);
    }
    
    @Override
	public boolean calculate() {
        AutonControl.getInstance().stop();
        return true;
    }

	@Override
	public void override() {
		// nothing to do
		
	}
    
}
