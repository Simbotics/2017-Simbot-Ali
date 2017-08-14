package org.simbotics.frc2017.auton.mode.step2Modes;

import org.simbotics.frc2017.auton.AutonOverride;
import org.simbotics.frc2017.auton.RobotComponent;
import org.simbotics.frc2017.auton.drive.DriveSetOutput;
import org.simbotics.frc2017.auton.drive.DriveStraightProfile;
import org.simbotics.frc2017.auton.drive.DriveTurnToAnglePID;
import org.simbotics.frc2017.auton.drive.DriveTurnToAngleRightSide;
import org.simbotics.frc2017.auton.mode.AutonBuilder;
import org.simbotics.frc2017.auton.mode.AutonMode;
import org.simbotics.frc2017.auton.turret.TurretMoveToAngle;
import org.simbotics.frc2017.auton.util.AutonWaitUntilAutoTime;


public class MoveToOtherHopperRightHopperAuto implements AutonMode {

	@Override
	public void addToMode(AutonBuilder ab) {
		ab.addCommand(new AutonOverride(RobotComponent.SHOOTER));
		
		ab.addCommand(new TurretMoveToAngle(90, 1500));
		ab.addCommand(new DriveTurnToAngleRightSide(-45,5,1000));
		ab.addCommand(new DriveStraightProfile(-9.5, -45, 0.01, 1500));
		ab.addCommand(new DriveTurnToAnglePID(-135,2,1000));
		ab.addCommand(new DriveStraightProfile(16.0, -135, 0.01, 2000));
		ab.addCommand(new AutonWaitUntilAutoTime(14500));
		ab.addCommand(new AutonOverride(RobotComponent.DRIVE));
		ab.addCommand(new DriveSetOutput(-0.3));
	}

}
