package org.simbotics.frc2017.auton.mode.step2Modes;

import org.simbotics.frc2017.auton.AutonOverride;
import org.simbotics.frc2017.auton.RobotComponent;
import org.simbotics.frc2017.auton.drive.DriveSetOutput;
import org.simbotics.frc2017.auton.drive.DriveStraightProfile;
import org.simbotics.frc2017.auton.drive.DriveTurnToAnglePID;
import org.simbotics.frc2017.auton.drive.DriveWait;
import org.simbotics.frc2017.auton.mode.AutonBuilder;
import org.simbotics.frc2017.auton.mode.AutonMode;
import org.simbotics.frc2017.auton.shooter.ShooterSetRPM;
import org.simbotics.frc2017.auton.shooter.ShooterShootAndFeedTimer;
import org.simbotics.frc2017.auton.turret.TurretAim;
import org.simbotics.frc2017.auton.turret.TurretMoveToAngle;
import org.simbotics.frc2017.auton.turret.TurretWait;
import org.simbotics.frc2017.auton.util.AutonWait;
import org.simbotics.frc2017.auton.util.AutonWaitUntilAutoTime;
import org.simbotics.frc2017.util.RobotConstants;

public class FromMiddleShootLeft implements AutonMode {

	@Override
	public void addToMode(AutonBuilder ab) {
		ab.addCommand(new ShooterSetRPM(RobotConstants.ADJUSTABLE_RPM, 0));
		
		
		ab.addCommand(new DriveStraightProfile(-2.0,75,0.01,1200)); // drive to gear peg
		ab.addCommand(new DriveWait());
		
		ab.addCommand(new DriveTurnToAnglePID(30,0.75 ,1.0, 500));
		
		ab.addCommand(new DriveStraightProfile(-6.4,30,10,0.25,0.5,2150)); // drive to gear peg
		ab.addCommand(new DriveWait());
		
		ab.addCommand(new TurretAim(270, 3000));
		ab.addCommand(new TurretWait());
		
		ab.addCommand(new AutonOverride(RobotComponent.SHOOTER));
		ab.addCommand(new ShooterShootAndFeedTimer(RobotConstants.ADJUSTABLE_RPM,700,700,3500,true,-1));	
		ab.addCommand(new AutonWait(3000));
		
		ab.addCommand(new AutonOverride(RobotComponent.SHOOTER));	
		
		
		ab.addCommand(new TurretMoveToAngle(90, 2000));
		ab.addCommand(new DriveTurnToAnglePID(90,1.0, 500));
		
		ab.addCommand(new DriveStraightProfile(17.0, 90, 0.01, 2500)); 
		ab.addCommand(new AutonWaitUntilAutoTime(14500));
		ab.addCommand(new AutonOverride(RobotComponent.DRIVE));
		ab.addCommand(new DriveSetOutput(-0.3));
		
		
		
	}

}
