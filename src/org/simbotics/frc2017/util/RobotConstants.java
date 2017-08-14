package org.simbotics.frc2017.util;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RobotConstants {
	
	public static final double DRIVE_MAX_VEL_HIGH = 14.0; // fps // 14
	public static final double DRIVE_MAX_ACCEL_HIGH = 9.0;//8.5
	public static final double DRIVE_MAX_DECEL_HIGH = 9.0; //8.5
	
	public static final double DRIVE_MAX_JERK_HIGH = 8000000.0;
	
	public static final double GYRO_MAX_VEL_HIGH = 140; // DPS
	public static final double GYRO_MAX_ACCEL_HIGH = 170.0;
	public static final double GYRO_MAX_DECEL_HIGH = 170.0;
	
	public static final double DRIVE_MAX_TURN_RATE_HIGH = 5.5; // fps
	public static final double DRIVE_MAX_TURN_RATE_LOW = 2.0; // fps
	
	public static final double DRIVE_MAX_VEL_LOW = 6.5; // fps
	public static final double DRIVE_MAX_ACCEL_LOW = 6;
	public static final double DRIVE_MAX_DECEL_LOW = 6;
	
	public static final double GYRO_MAX_VEL_LOW = 200; // DPS
	public static final double GYRO_MAX_ACCEL_LOW = 100.0;
	public static final double GYRO_MAX_DECEL_LOW = 100.0;
	
	public static final double TURRET_MAX_VEL = 240; // DPS should be around 1300 ish 
	public static final double TURRET_MAX_ACCEL = 480; 
	public static final double TURRET_MAX_DECEL = 480;
	
	public static final double ELEVATOR_MAX_VEL = 1500; // RPM //860
	
	public static final double elevatorP = 0.0015;//0.002
	public static final double elevatorI = 0.0;
	public static final double elevatorD = 0.00001;//0.0002
	public static final double elevatorFF = 1.0 / ELEVATOR_MAX_VEL;
	public static final double elevatorEps = 0;
	public static final double elevatorAdjustableRPM = 700; // 1200 for auto, 700 for teleop 
	
	public static final double gyroPHigh = 0.040;
	public static final double gyroIHigh = 0.005;
	public static final double gyroDHigh = 0.6;
	public static final double gyroKvHigh = 0.006;//1.0 / (GYRO_MAX_VEL_HIGH*2); 
	public static final double gyroKaHigh = 0.001;
	
	public static final double gyroEps = 0.5;
	
	public static final double pathTurnP =  0.2;
	
	
	public static final double encPHigh= 0.45;
	public static final double encIHigh = 0.000;
	public static final double encDHigh = 0.002;
	public static final double encKvHigh = 1.0 / 14.0;// DRIVE_MAX_VEL_HIGH;
	public static final double encKvLow = 1.0 / 6.5;// DRIVE_MAX_VEL_LOW;
	public static final double encKaHigh = 0.027;//0.025;
	public static final double encEps = 0.01;
	
	public static final double splinePHigh= 0.45;
	public static final double splineIHigh = 0.000;
	public static final double splineDHigh = 0.002;
	public static final double splineKvHigh = 0.07;// DRIVE_MAX_VEL_HIGH;
	public static final double splineKvLow = 1.0 / 6.5;// DRIVE_MAX_VEL_LOW;
	public static final double splineKaHigh = 0.027;//0.025;
	
	
	public static final double SHOOTER_MAX_RPM = 5200;
	public static final double ADJUSTABLE_RPM = 2900;//3147
	
	public static final double SHOOTER_X_2 = 0.1588;//-0.2869
	public static final double SHOOTER_X = -14.97;//59.687
	public static final double SHOOTER_SHIFT = 3000.8;//-126.105; //1740.8487; 
	
	public static final double SHOOTER_X_2_FAST = -0.0117;
	public static final double SHOOTER_X_FAST = 15.3814;
	public static final double SHOOTER_SHIFT_FAST = 1620; //1400; 
	
	
	// Talons use native speed in encoder counts per 100ms so there needs to be a conversion from RPM 
	public static final int shooterCountsPerRev = 1024; 
	public static final double shooterMaxSpeedNative = ((SHOOTER_MAX_RPM / 60.0) * (1.0/10.0) * (shooterCountsPerRev)); // convert from RPM to native units
	
	public static final double shooterP = 0.2;//.18
	public static final double shooterI = 0.03;
	public static final double shooterD = 0.0;//3.0;//0.0
	public static final double shooterFF = 0.029; // 1023 is full power on a talons speed control
	public static final double shooterEps = 250;
	public static final double shooterDistanceP= 8; // lel
	
 	public static final double turretP = 0.06;
	public static final double turretI = 0.01;
	public static final double turretD = 0.0;
	public static final double turretKv = 1.0 / 260.0; // max vel of turret is much higher than what we actually want it to go
	public static final double turretKa = 0.0002;
	public static final double turretEps = 0.15;
	
	public static final double TURRET_CAMERA_EPSILON = 0.5;
	public static final int TURRET_MAX_IMAGE_COUNT = 5; 
	public static final double WHEEL_BASE_WIDTH =  21.25/12.0;


	
	public static void pushValues(){
		
		SmartDashboard.putNumber("2_Shooter X^2: ", SHOOTER_X_2);
		SmartDashboard.putNumber("2_Shooter X: ", SHOOTER_X);
		SmartDashboard.putNumber("2_Shooter Shift: ", SHOOTER_SHIFT);
		
		SmartDashboard.putNumber("2_Shooter X^2 Fast:", SHOOTER_X_2_FAST);
		SmartDashboard.putNumber("2_Shooter X Fast: ", SHOOTER_X_FAST);
		SmartDashboard.putNumber("2_Shooter Shift Fast: ", SHOOTER_SHIFT_FAST);
		
		
		SmartDashboard.putNumber("2_Turret Camera Eps: ", TURRET_CAMERA_EPSILON);
		SmartDashboard.putNumber("2_Turret Max Image Count: ", TURRET_MAX_IMAGE_COUNT);

		SmartDashboard.putNumber("2_Gyro P HIGH: ", gyroPHigh);
		SmartDashboard.putNumber("2_Gyro I HIGH: ", gyroIHigh);
		SmartDashboard.putNumber("2_Gyro D HIGH: ", gyroDHigh);
		SmartDashboard.putNumber("2_Gyro VelFF HIGH: ", gyroKvHigh);
		SmartDashboard.putNumber("2_Gyro AcclFF HIGH: ", gyroKaHigh);
		
		SmartDashboard.putNumber("2_Gyro Eps: ", gyroEps);
		
		SmartDashboard.putNumber("2_Enc P HIGH: ", encPHigh);
		SmartDashboard.putNumber("2_Enc I HIGH: ", encIHigh);
		SmartDashboard.putNumber("2_Enc D HIGH: ", encDHigh);
		SmartDashboard.putNumber("2_Enc VelFF HIGH: ", encKvHigh);
		SmartDashboard.putNumber("2_Enc AcclFF HIGH: ", encKaHigh);

		SmartDashboard.putNumber("2_Spline P HIGH: ", splinePHigh);
		SmartDashboard.putNumber("2_Spline I HIGH: ", splineIHigh);
		SmartDashboard.putNumber("2_Spline D HIGH: ", splineDHigh);
		SmartDashboard.putNumber("2_Spline VelFF HIGH: ", splineKvHigh);
		SmartDashboard.putNumber("2_Spline AcclFF HIGH: ", splineKaHigh);
		
		SmartDashboard.putNumber("2_Path Turn P: ", pathTurnP);
		SmartDashboard.putNumber("2_Enc Eps: ", encEps);
		
		SmartDashboard.putNumber("2_Shooter Adjustable Shot RPM: ", ADJUSTABLE_RPM);
		SmartDashboard.putNumber("2_Shooter P: ", shooterP);
		SmartDashboard.putNumber("2_Shooter I: ", shooterI);
		SmartDashboard.putNumber("2_Shooter D: ", shooterD);
		SmartDashboard.putNumber("2_Shooter FF: ",shooterFF);
		SmartDashboard.putNumber("2_Shooter Eps: ", shooterEps);
		SmartDashboard.putNumber("2_Shooter Distance P: ", shooterDistanceP);
		
		SmartDashboard.putNumber("2_Elevator Adjustable RPM: ", elevatorAdjustableRPM);
		SmartDashboard.putNumber("2_Elevator P: ", elevatorP);
		SmartDashboard.putNumber("2_Elevator I: ", elevatorI);
		SmartDashboard.putNumber("2_Elevator D: ", elevatorD);
		SmartDashboard.putNumber("2_Elevator FF: ",elevatorFF);
		SmartDashboard.putNumber("2_Elevator Eps: ", elevatorEps);
		
		SmartDashboard.putNumber("2_Turret P: ", turretP);
		SmartDashboard.putNumber("2_Turret I: ", turretI);
		SmartDashboard.putNumber("2_Turret D: ", turretD);
		SmartDashboard.putNumber("2_Turret VelFF: ", turretKv);
		SmartDashboard.putNumber("2_Turret AcclFF: ", turretKa);
		SmartDashboard.putNumber("2_Turret Eps: ", turretEps);
		
		
	}
	
	
	
	
	
}
