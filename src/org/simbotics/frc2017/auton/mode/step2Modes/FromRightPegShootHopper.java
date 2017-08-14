package org.simbotics.frc2017.auton.mode.step2Modes;

import org.simbotics.frc2017.auton.AutonOverride;
import org.simbotics.frc2017.auton.RobotComponent;
import org.simbotics.frc2017.auton.drive.Drive2dProfile;
import org.simbotics.frc2017.auton.drive.DriveSetHighGear;
import org.simbotics.frc2017.auton.drive.DriveSetOutputSides;
import org.simbotics.frc2017.auton.drive.DriveStraightProfile;
import org.simbotics.frc2017.auton.drive.DriveTurnLeftUntilAngle;
import org.simbotics.frc2017.auton.drive.DriveWait;
import org.simbotics.frc2017.auton.mode.AutonBuilder;
import org.simbotics.frc2017.auton.mode.AutonMode;
import org.simbotics.frc2017.auton.pouch.SetBallRamp;
import org.simbotics.frc2017.auton.shooter.ShooterSetRPM;
import org.simbotics.frc2017.auton.shooter.ShooterShootAndFeedTimer;
import org.simbotics.frc2017.auton.turret.TurretAim;
import org.simbotics.frc2017.auton.turret.TurretMoveToAngle;
import org.simbotics.frc2017.auton.turret.TurretWait;
import org.simbotics.frc2017.auton.util.AutonWait;
import org.simbotics.frc2017.auton.util.AutonWaitUntilAutoTime;
import org.simbotics.frc2017.util.RobotConstants;

public class FromRightPegShootHopper implements AutonMode {

	@Override
	public void addToMode(AutonBuilder ab) {
		ab.addCommand(new TurretMoveToAngle(10, 1500));
		
		ab.addCommand(new Drive2dProfile("RightSideGearCurveHopperReverse",true, 14, -1,-1,1.0,4.0, 4.2,0.012,1800));
		
		ab.addCommand(new DriveTurnLeftUntilAngle(1.0,330,600));
		ab.addCommand(new ShooterSetRPM(RobotConstants.ADJUSTABLE_RPM, 0.0));
		ab.addCommand(new SetBallRamp(true));
		ab.addCommand(new DriveWait());
		
		ab.addCommand(new DriveStraightProfile(28 / 12.0, 360, 0.01, 800));
		ab.addCommand(new DriveSetHighGear(false));
		ab.addCommand(new TurretAim(10, 3000));
		ab.addCommand(new DriveStraightProfile(12 / 12.0, 360, 0.01, 600));
		
		
		ab.addCommand(new AutonOverride(RobotComponent.SHOOTER));
		ab.addCommand(new ShooterShootAndFeedTimer(RobotConstants.ADJUSTABLE_RPM,1200,700,12000,false,-1));	
		
		
		ab.addCommand(new DriveSetOutputSides(0.20,0.2));
		
		ab.addCommand(new TurretWait());
		//ab.addCommand(new DriveSetOutputSides(0.0,0.0));
		ab.addCommand(new AutonOverride(RobotComponent.SHOOTER));
		
		
		ab.addCommand(new ShooterShootAndFeedTimer(RobotConstants.ADJUSTABLE_RPM,1200,700,12000,true,-1));		
		ab.addCommand(new AutonWaitUntilAutoTime(14800));
		ab.addCommand(new DriveSetHighGear(true));
		ab.addCommand(new AutonWait(200));
		
	}

}
