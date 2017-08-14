package org.simbotics.frc2017.auton;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.simbotics.frc2017.auton.mode.AutonBuilder;
import org.simbotics.frc2017.auton.mode.AutonMode;
import org.simbotics.frc2017.auton.mode.DefaultMode;
import org.simbotics.frc2017.auton.mode.step1Modes.Left2dCloseHopperShoot;
import org.simbotics.frc2017.auton.mode.step1Modes.Left2dFarHopperShoot;
import org.simbotics.frc2017.auton.mode.step1Modes.LeftScoreSideGear;
import org.simbotics.frc2017.auton.mode.step1Modes.LeftShootThenSideGear;
import org.simbotics.frc2017.auton.mode.step1Modes.Right2dCloseHopperShoot;
import org.simbotics.frc2017.auton.mode.step1Modes.Right2dFarHopperShoot;
import org.simbotics.frc2017.auton.mode.step1Modes.RightScoreSideGear;
import org.simbotics.frc2017.auton.mode.step1Modes.RightShootThenSideGear;
import org.simbotics.frc2017.auton.mode.step1Modes.ScoreMiddleGear;
import org.simbotics.frc2017.auton.mode.step1Modes.ShootingTest;
import org.simbotics.frc2017.auton.mode.step2Modes.BackUp;
import org.simbotics.frc2017.auton.mode.step2Modes.FromLeftPegShootHopper;
import org.simbotics.frc2017.auton.mode.step2Modes.FromMiddleShootLeft;
import org.simbotics.frc2017.auton.mode.step2Modes.FromMiddleShootRight;
import org.simbotics.frc2017.auton.mode.step2Modes.FromRightPegShootHopper;
import org.simbotics.frc2017.auton.mode.step2Modes.KeepShooting;
import org.simbotics.frc2017.auton.mode.step2Modes.KeepShootingFar;
import org.simbotics.frc2017.auton.mode.step2Modes.MoveToHopperLeftGearHopper;
import org.simbotics.frc2017.auton.mode.step2Modes.MoveToHopperLeftHopperAuto;
import org.simbotics.frc2017.auton.mode.step2Modes.MoveToHopperRightGearHopper;
import org.simbotics.frc2017.auton.mode.step2Modes.MoveToHopperRightHopperAuto;
import org.simbotics.frc2017.auton.mode.step2Modes.MoveToOtherHopperLeftGearHopper;
import org.simbotics.frc2017.auton.mode.step2Modes.MoveToOtherHopperLeftHopperAuto;
import org.simbotics.frc2017.auton.mode.step2Modes.MoveToOtherHopperRightGearHopper;
import org.simbotics.frc2017.auton.mode.step2Modes.MoveToOtherHopperRightHopperAuto;
import org.simbotics.frc2017.auton.mode.step2Modes.MoveToPegLeftHopperAuto;
import org.simbotics.frc2017.auton.mode.step2Modes.MoveToPegRightHopperAuto;
import org.simbotics.frc2017.io.DriverInput;
import org.simbotics.frc2017.io.RobotOutput;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 
 * @author Programmers
 */
public class AutonControl {

    private static AutonControl instance;
    
    public static final int NUM_ARRAY_MODE_STEPS = 4;
   
    
    private int autonDelay;
    private long autonStartTime;
    
    private boolean running;
    
    private int curAutonStepToSet = 0;
    private int[] autonSubmodeSelections = new int[NUM_ARRAY_MODE_STEPS];
    private ArrayList<ArrayList<AutonMode>> autonSteps = new ArrayList<>();
    
    private int currIndex;
    private AutonCommand[] commands;
        
    private String autoSelectError = "NO ERROR";
    
    public static AutonControl getInstance() {
        if(instance == null) {
            instance = new AutonControl();
        }
        return instance;
    }

    private AutonControl() {
        this.autonDelay = 0;
        this.currIndex = 0;
        
        for(int i = 0; i < NUM_ARRAY_MODE_STEPS; i++) {
        	this.autonSteps.add(new ArrayList<AutonMode>());
        	this.autonSubmodeSelections[i] = 0; // default to default auto modes 
        }
        	
        // GOTCHA: remember to put all auton modes here
        
        // --- STEP 1 SUBMODES
        ArrayList<AutonMode> step1 = this.autonSteps.get(0);
        
        step1.add(new DefaultMode());      //0
       
        //step1.add(new Drive25feetTest());
        
        step1.add(new ScoreMiddleGear());
        step1.add(new RightScoreSideGear());
        step1.add(new LeftScoreSideGear());
        //step1.add(new RightCloseHopperShoot());
        step1.add(new Right2dCloseHopperShoot());
        step1.add(new Right2dFarHopperShoot());
        step1.add(new Left2dCloseHopperShoot());
        step1.add(new Left2dFarHopperShoot());
        step1.add(new RightShootThenSideGear());
        step1.add(new LeftShootThenSideGear());
        step1.add(new ShootingTest());
        
        
        
        // --- STEP 2 SUBMODES
        ArrayList<AutonMode> step2 = this.autonSteps.get(1);
        step2.add(new DefaultMode()); //0
        step2.add(new BackUp());
        step2.add(new KeepShooting());
        step2.add(new KeepShootingFar());
        step2.add(new FromMiddleShootLeft());
        step2.add(new FromMiddleShootRight());
        step2.add(new FromRightPegShootHopper());
        step2.add(new FromLeftPegShootHopper());
        step2.add(new MoveToPegLeftHopperAuto());
        step2.add(new MoveToPegRightHopperAuto());
        step2.add(new MoveToHopperLeftHopperAuto());
        step2.add(new MoveToOtherHopperLeftHopperAuto());
        //step2.add(new MoveToHopperLeftOldAuto());
        step2.add(new MoveToHopperRightHopperAuto());
        step2.add(new MoveToOtherHopperRightHopperAuto());
        //step2.add(new MoveToHopperRightOldAuto());
        
        
        // --- STEP 3 SUBMODES
        ArrayList<AutonMode> step3 = this.autonSteps.get(2);
        step3.add(new DefaultMode()); //0
        step3.add(new MoveToHopperLeftGearHopper());
        step3.add(new MoveToHopperRightGearHopper());
        step3.add(new MoveToOtherHopperLeftGearHopper());
        step3.add(new MoveToOtherHopperRightGearHopper());
        step3.add(new KeepShooting());
        
        
        // --- STEP 4 SUBMODES
        ArrayList<AutonMode> step4 = this.autonSteps.get(3);
        step4.add(new DefaultMode());
       
        
       
    }

    public void initialize() {
        System.out.println("START AUTO");
        
        this.currIndex = 0;
        this.running = true;

        // initialize auton in runCycle
        AutonBuilder ab = new AutonBuilder();

        // add auton commands from all the different steps
        for(int i = 0; i < this.autonSteps.size(); i++) {
        	this.autonSteps.get(i).get(this.autonSubmodeSelections[i]).addToMode(ab);
        }
        
        // get the full auton mode
        this.commands = ab.getAutonList();

        this.autonStartTime = System.currentTimeMillis();
        
        // clear out each components "run seat"
        AutonCommand.reset();
    }
    
    public void runCycle() {
        // haven't initialized list yet
        long timeElapsed = System.currentTimeMillis() - this.autonStartTime;
        if(timeElapsed > this.getAutonDelayLength() && this.running) {
            System.out.println("Current index " + this.currIndex);
            
            
                // start waiting commands
                while(this.currIndex < this.commands.length &&
                        this.commands[this.currIndex].checkAndRun()) {
                    this.currIndex++;
               
            }
            // calculate call for all running commands
            AutonCommand.execute();
        } else {
            RobotOutput.getInstance().stopAll();
        }

    
    }
    
    public void stop() {
        this.running = false;
    }
    
    public long getAutonDelayLength() {
        return this.autonDelay * 500;
    }

    public void updateModes() {
        DriverInput driverIn = DriverInput.getInstance();
        
        if(driverIn.getAutonStepIncrease()) {
        	this.curAutonStepToSet++;
        	this.curAutonStepToSet = Math.min(this.curAutonStepToSet, this.autonSteps.size() - 1);
        }
        
        if(driverIn.getAutonStepDecrease()) {
        	this.curAutonStepToSet--;
        	this.curAutonStepToSet = Math.max(this.curAutonStepToSet, 0);
        }
         
       	boolean updatingAutoMode = false;

        try {
        
        	int val = 0;
             if(driverIn.getAutonModeIncrease()){
             	val = 1;
             }else if(driverIn.getAutonModeIncreaseBy10()){
             	val = 10;
             }else if(driverIn.getAutonModeDecrease()){
             	val = -1;
             }else if(driverIn.getAutonModeDecreaseBy10()){
             	val = -10; 
             }
             
        	
        	
        if(val != 0) {
            updatingAutoMode = true;
            
            
           
            // figure out which auton mode is being selected
            int autonMode = this.autonSubmodeSelections[this.curAutonStepToSet] + val;
            
            
            
            // make sure we didn't go off the end of the list
            autonMode = Math.min(autonMode, this.autonSteps.get(this.curAutonStepToSet).size() - 1);          
            if(autonMode < 0 ){
            	autonMode = 0;
            }
            
            
            this.autonSubmodeSelections[this.curAutonStepToSet] = autonMode;
            

           
            
            /*
            if(val < 0) { this.autonMode = 0; }
            else { this.autonMode = 1; }
         */   
        } else if(driverIn.getAutonSetDelayButton()) {
            this.autonDelay = (int)((driverIn.getAutonDelayStick() + 1) * 5.0);
            if(this.autonDelay < 0 ) {
            	this.autonDelay =0;
            }
        }
        
        } catch(Exception e) {
        	//this.autonMode = 0;
        	// TODO: some kind of error catching
        	
        	
        	StringWriter sw = new StringWriter();
        	e.printStackTrace(new PrintWriter(sw));
        	
        	
        	this.autoSelectError = sw.toString();
        
        }
        
        // display steps of auto
        for(int i = 0; i < autonSteps.size(); i++) {
	        // name of the current auton mode
	        String name = this.autonSteps.get(i).get(this.autonSubmodeSelections[i]).getClass().getName();
	
	        // make sure there is a '.'
	        if(name.lastIndexOf('.') >= 0) {
	            // get just the last bit of the name
	            name = name.substring(name.lastIndexOf('.'));
	        }
	        
	        String outputString = "" + autonSubmodeSelections[i] + name + "";
	        
	        SmartDashboard.putString("12_Auton Step " + (i+1) + ": ", outputString);
	       
	        if(updatingAutoMode) {
            	//System.out.print(this.autonSubmodeSelections[i] + "-");
	        	System.out.println("Step " + (i + 1) + ": " + outputString);
	        }
	        	
	        	// System.out.println();
	        
	        //SmartDashboard.putString("Auton Error: ", this.autoSelectError);
        }
        
        if(updatingAutoMode) {
        	System.out.println("----------------------------------");
        }
        
        // step we are currently modifying
        SmartDashboard.putNumber("12_SETTING AUTON STEP: ", this.curAutonStepToSet+1);
        
        // delay 
        String delayAmt = "";
        if(this.autonDelay < 10) {
            // pad in a blank space for single digit delay
            delayAmt = " " + this.autonDelay;
        } else {
            delayAmt = "" + this.autonDelay;
        }
        SmartDashboard.putNumber("12_Auton Delay: ", this.autonDelay);


    }

}
