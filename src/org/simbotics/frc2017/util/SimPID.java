package org.simbotics.frc2017.util;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SimPID {
	private double pConst;
	private double iConst;
	private double dConst;
	private double desiredVal;
	protected double previousError;
	private double errorSum;
	protected double finishedRange;
	private double maxOutput;
	private int minCycleCount;
	private int currentCycleCount;
	private boolean firstCycle;
	private boolean resetI;
	protected boolean debug;
	
	public SimPID(double p, double i, double d, double epsRange){
		this.pConst = p;
		this.iConst = i;
		this.dConst = d;
		this.finishedRange = epsRange; 
		this.resetI = true;
		this.desiredVal = 0.0;
		this.firstCycle = true;
		this.maxOutput = 1.0;
		this.currentCycleCount = 0;
		this.minCycleCount = 5;
		this.debug = false;
		
	}
	
	public void setConstants(double p,double i, double d){
		this.pConst = p;
		this.iConst = i;
		this.dConst = d;
	}
	
	public void setDesiredValue(double val) {
        this.desiredVal = val;
    }
	
	public void setFinishedRange(double range){
		this.finishedRange = range;
	}
	
	public void disableIReset(){
		this.resetI = false;
	}
	
	public void enableIReset(){
		this.resetI = true;
	}
	
	public void enableDebug(){
		this.debug = true;
	}
	
	public void disableDebug(){
		this.debug = false;
	}
	
	public void setMaxOutput(double max) {
        if(max < 0.0) {
            this.maxOutput = 0.0;
        } else if(max > 1.0) {
            this.maxOutput = 1.0;
        } else {
            this.maxOutput = max;
        }
    }
    
    public void setMinDoneCycles(int num) {
        this.minCycleCount = num;
    }
    
    public void resetErrorSum() {
        this.errorSum = 0.0;
    }
    
    public double getDesiredVal() {
        return this.desiredVal;
    }
    
        
    public double calcPID(double current) {
    	return calcPIDError(this.desiredVal - current);
    }
	
    private double calcPIDError (double error){
    	double pVal = 0.0;
        double iVal = 0.0;
        double dVal = 0.0;
        
        if(this.firstCycle) {
            this.previousError = error;
            this.firstCycle = false;
        }
        
        ///////P Calc///////
        pVal = this.pConst * error;
        
        ///////I Calc///////
        if(Math.abs(pVal) >= 1.0){ // P output is >= 1.0 which means we are very far away
        	this.errorSum = 0.0;
        }else if(Math.abs(error) <= this.finishedRange){ // within range
        	if(this.resetI){
        		this.errorSum = 0.0;
        	}else{
        		//this.errorSum += error; //maybe we need this? 
        	}
        }else if(pVal > 0.0){ // going forward
        	if(this.errorSum < 0.0){ // we were going backwards
        		this.errorSum = 0.0;
        	}
        	 this.errorSum += error; 
        }else{ // going backwards
        	if(this.errorSum > 0.0){ // we were going forward
        		this.errorSum = 0.0;
        	}
        	 this.errorSum += error; 
        }
       
        
        iVal = this.iConst * this.errorSum;
        
        ///////D Calc///////
        double deriv = error - this.previousError;
        dVal = this.dConst * deriv;
        
        //overal PID calc
        double output = pVal + iVal + dVal;
        
        //limit the output
        output = SimLib.limitValue(output, this.maxOutput);
        
        //store current value as previous for next cycle
        this.previousError = error;
        
        if(this.debug) {
        	SmartDashboard.putNumber("P out", pVal);
        	SmartDashboard.putNumber("I out", iVal);
        	SmartDashboard.putNumber("D out", dVal);
        	SmartDashboard.putNumber("PID OutPut", output);
        }
        
        return output;
    }
    
    public boolean isDone() {
        double currError = Math.abs(this.previousError);
        
        //close enough to target
        if(currError <= this.finishedRange) {
            this.currentCycleCount++;
        }
        //not close enough to target
        else {
            this.currentCycleCount = 0;
        }
        
        return this.currentCycleCount > this.minCycleCount;
    }
    
    public boolean getFirstCycle(){
    	return this.firstCycle;
    }
    
    public void resetPreviousVal() {
        this.firstCycle = true;
    } 
	
	
}
