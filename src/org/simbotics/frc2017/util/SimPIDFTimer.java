package org.simbotics.frc2017.util;

public class SimPIDFTimer extends SimPIDF {
	private long startTime =-1;
	private long timeout;
	
	public SimPIDFTimer(double p,double i,double d,double f,double eps,long timeout) {
		super(p, i, d, f,eps);
		this.timeout = timeout;
	}
	
	@Override
	public double calcPID(double current){
		if(this.startTime == -1 || this.getFirstCycle()){
			this.startTime = System.currentTimeMillis();
		}
		return super.calcPID(current);
	}
	@Override
	public boolean isDone() {
		long currentTime = System.currentTimeMillis();
		if((currentTime - this.startTime) >= this.timeout){
			System.out.println("SimPIDTimer TIMEOUT");
		}
		return super.isDone() || ((currentTime - this.startTime) >= this.timeout);
	}
		
	public void setTimeOut(long timeout){
		this.timeout = timeout;
	}
}
