package org.simbotics.frc2017.util.path.generation;

/**
 * A WaypointSequence is a sequence of Waypoints. #whatdidyouexpect
 *
 * @author Art Kalb
 * @author Stephen Pinkerton
 * @author Jared341
 */
public class WaypointSequence {

	public static class Waypoint {
		public Waypoint(double x, double y, double thetaInDegrees) {
			this.x = x;
			this.y = y;
			this.theta = (thetaInDegrees);
		}

		public Waypoint(Waypoint tocopy) {
			this.x = tocopy.x;
			this.y = tocopy.y;
			this.theta = tocopy.theta;
		}

		public double x;
		public double y;
		public double theta;
	}

	Waypoint[] waypoints_;
	int num_waypoints_;
	String pathName;

	public WaypointSequence(int max_size, String pathName) {
		waypoints_ = new Waypoint[max_size];
		this.pathName = pathName;

	}
	
	public double getWaypointSequenceDistance(){
		double totalDist = 0;
		
		for(int i = 1; i < num_waypoints_; i++){
			double xDist = waypoints_[i].x - waypoints_[i-1].x;
			double yDist = waypoints_[i].y - waypoints_[i-1].y;
			
			totalDist += Math.abs(Math.sqrt((xDist*xDist) + (yDist*yDist)));
		}
		return totalDist;
	}
	
	public double getDistanceToWaypoint(double x, double y, int index){
		double xDist = waypoints_[index].x - x;
		double yDist = waypoints_[index].y - y;
		
		return Math.abs(Math.sqrt((xDist*xDist) + (yDist*yDist)));
	}
	
	public double getAngleBetweenWaypoint(double x, double y,int index2){
		return Math.toDegrees(Math.atan2(waypoints_[index2].y - y ,waypoints_[index2].x - x));
	}

	public void addWaypoint(Waypoint w) {
		if (num_waypoints_ < waypoints_.length) {
			waypoints_[num_waypoints_] = w;
			++num_waypoints_;
		}
	}

	public String getPathName() {
		return this.pathName;
	}

	public int getNumWaypoints() {
		return num_waypoints_;
	}

	public Waypoint getWaypoint(int index) {
		if (index >= 0 && index < getNumWaypoints()) {
			return waypoints_[index];
		} else {
			return null;
		}
	}

	
}
