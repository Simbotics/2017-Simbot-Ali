package org.simbotics.frc2017.auton.mode.step2Modes;

import org.simbotics.frc2017.auton.AutonOverride;
import org.simbotics.frc2017.auton.RobotComponent;
import org.simbotics.frc2017.auton.mode.AutonBuilder;
import org.simbotics.frc2017.auton.mode.AutonMode;
import org.simbotics.frc2017.auton.shooter.ShooterShootAndFeedTimer;
import org.simbotics.frc2017.auton.shooter.ShooterWait;
import org.simbotics.frc2017.util.RobotConstants;


public class KeepShooting implements AutonMode {

	@Override
	public void addToMode(AutonBuilder ab) {
		
		
		ab.addCommand(new AutonOverride(RobotComponent.SHOOTER));
		ab.addCommand(new ShooterShootAndFeedTimer(RobotConstants.ADJUSTABLE_RPM, 700,700, 12000, true, -1));
		ab.addCommand(new ShooterWait());
	
		
	}

}
