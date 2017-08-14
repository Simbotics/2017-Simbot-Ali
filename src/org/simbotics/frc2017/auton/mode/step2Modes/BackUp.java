package org.simbotics.frc2017.auton.mode.step2Modes;

import org.simbotics.frc2017.auton.drive.DriveSetOutputSides;
import org.simbotics.frc2017.auton.mode.AutonBuilder;
import org.simbotics.frc2017.auton.mode.AutonMode;
import org.simbotics.frc2017.auton.util.AutonWait;


public class BackUp implements AutonMode {

	@Override
	public void addToMode(AutonBuilder ab) {
		
		
		ab.addCommand(new DriveSetOutputSides(-0.50,-0.5));
		ab.addCommand(new AutonWait(800));
		ab.addCommand(new DriveSetOutputSides(0,0));
		
	
		
	}

}
