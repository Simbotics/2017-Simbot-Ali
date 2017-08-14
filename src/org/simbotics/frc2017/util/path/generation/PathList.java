package org.simbotics.frc2017.util.path.generation;

import java.util.ArrayList;

import org.simbotics.frc2017.util.Debugger;

public class PathList {

	private static ArrayList<WaypointSequence> paths;
	private static PathList instance;

	private PathList() {
		paths = new ArrayList<>();
		paths.add(driveStraightTest());
		paths.add(rightHopperCurve());
		paths.add(rightFarHopperCurve());
		paths.add(rightSideGearCurve());
		paths.add(leftSideGearCurve());
		paths.add(leftHopperCurve());
		paths.add(leftFarHopperCurve());
		paths.add(rightSideGearCurveHopperReverse());
		paths.add(leftSideGearCurveHopperReverse());
	}

	public static ArrayList<WaypointSequence> getPaths() {
		if (instance == null) {
			instance = new PathList();
		}
		return paths;
	}

	public static WaypointSequence getPathByName(String name) {

		for (WaypointSequence p : paths) {
			if (p.getPathName().equals(name)) {
				return p;
			}
		}
		Debugger.println("Can't find path in PathList.java");
		return null;

	}

	// GOTCHA: pathClassName variable must be the a valid class name!!!
	
	private static WaypointSequence driveStraightTest() {
		String pathClassName = "DriveStraightTest";
		WaypointSequence p = new WaypointSequence(10, pathClassName);

		p.addWaypoint(new WaypointSequence.Waypoint(0, 0, 90));
		p.addWaypoint(new WaypointSequence.Waypoint(0, 5, 90));

		return p;
	}
	
	private static WaypointSequence rightHopperCurve(){
		String pathClassName = "RightHopperCurve";
		WaypointSequence p = new WaypointSequence(10, pathClassName);
		
		p.addWaypoint(new WaypointSequence.Waypoint(0, 0, 90));
		p.addWaypoint(new WaypointSequence.Waypoint(0, 4.15, 90));
		p.addWaypoint(new WaypointSequence.Waypoint(3.0, 7.16, 90));
		p.addWaypoint(new WaypointSequence.Waypoint(4.85,7.16, 0));

		return p;
		
		
	}
	
	private static WaypointSequence leftHopperCurve(){
		String pathClassName = "LeftHopperCurve";
		WaypointSequence p = new WaypointSequence(10, pathClassName);
		
		p.addWaypoint(new WaypointSequence.Waypoint(0, 0, 90));
		p.addWaypoint(new WaypointSequence.Waypoint(0, 4.1, 90));
		p.addWaypoint(new WaypointSequence.Waypoint(-3.0, 7.16, 90));
		p.addWaypoint(new WaypointSequence.Waypoint(-4.85,7.16, 180));

		return p;
		
		
	}
	
	private static WaypointSequence rightFarHopperCurve(){
		String pathClassName = "RightFarHopperCurve";
		WaypointSequence p = new WaypointSequence(10, pathClassName);
		
		p.addWaypoint(new WaypointSequence.Waypoint(0, 0, 90));
		p.addWaypoint(new WaypointSequence.Waypoint(0, 3.85, 90));
		
		p.addWaypoint(new WaypointSequence.Waypoint(6.66, 11.25, 90));
		p.addWaypoint(new WaypointSequence.Waypoint(7.55,11.35, 0));

		return p;
		
		
	}
	
	private static WaypointSequence leftFarHopperCurve(){
		String pathClassName = "LeftFarHopperCurve";
		WaypointSequence p = new WaypointSequence(10, pathClassName);
		
		p.addWaypoint(new WaypointSequence.Waypoint(0, 0, 90));
		p.addWaypoint(new WaypointSequence.Waypoint(0, 3.85, 90));
		
		p.addWaypoint(new WaypointSequence.Waypoint(-6.66, 11.25, 90));
		p.addWaypoint(new WaypointSequence.Waypoint(-7.55,11.35, 180));

		return p;
		
		
	}
	
	private static WaypointSequence rightSideGearCurve(){
		String pathClassName = "RightSideGearCurve";
		WaypointSequence p = new WaypointSequence(10, pathClassName);
		
		p.addWaypoint(new WaypointSequence.Waypoint(0, 0, 90));
		p.addWaypoint(new WaypointSequence.Waypoint(0, 1.83, 90));
		p.addWaypoint(new WaypointSequence.Waypoint(-0.5,3.83, 150));
		//p.addWaypoint(new WaypointSequence.Waypoint(-0.5, 4.83, 90));
		p.addWaypoint(new WaypointSequence.Waypoint(-2.0, 7.23, 90));
		
		
		//p.addWaypoint(new WaypointSequence.Waypoint(-5.3,9.10, 150));
		p.addWaypoint(new WaypointSequence.Waypoint(-6.0,9.8, 150));

		return p;
		
		
	}
	
	
	private static WaypointSequence rightSideGearCurveHopperReverse(){
		String pathClassName = "RightSideGearCurveHopperReverse";
		WaypointSequence p = new WaypointSequence(10, pathClassName);
		
		//p.addWaypoint(new WaypointSequence.Waypoint(-6.0,9.8, 150));
		//p.addWaypoint(new WaypointSequence.Waypoint(-5.09,8.11, 150));
		
		p.addWaypoint(new WaypointSequence.Waypoint(-1.48, 8.2, 90));
		p.addWaypoint(new WaypointSequence.Waypoint(1.37, 8.5, 180));

		return p;
	}
	
	private static WaypointSequence leftSideGearCurve(){
		String pathClassName = "LeftSideGearCurve";
		WaypointSequence p = new WaypointSequence(10, pathClassName);
		
		p.addWaypoint(new WaypointSequence.Waypoint(0, 0, 90));
		p.addWaypoint(new WaypointSequence.Waypoint(0, 1.83, 90));
		p.addWaypoint(new WaypointSequence.Waypoint(0.5,3.83, 150));
		//p.addWaypoint(new WaypointSequence.Waypoint(-0.5, 4.83, 90));
		p.addWaypoint(new WaypointSequence.Waypoint(2.0, 7.23, 90));
		
		
		//p.addWaypoint(new WaypointSequence.Waypoint(-5.3,9.10, 150));
		p.addWaypoint(new WaypointSequence.Waypoint(6.0,9.8, 30));

		return p;
		
		
	}
	
	
	private static WaypointSequence leftSideGearCurveHopperReverse(){
		String pathClassName = "LeftSideGearCurveHopperReverse";
		WaypointSequence p = new WaypointSequence(10, pathClassName);
		
		//p.addWaypoint(new WaypointSequence.Waypoint(6.0,9.8, 150));
		//p.addWaypoint(new WaypointSequence.Waypoint(-5.09,8.11, 150));
		
		p.addWaypoint(new WaypointSequence.Waypoint(1.48, 8.2, 90));
		p.addWaypoint(new WaypointSequence.Waypoint(-1.37, 8.5, 0));

		return p;
	}
	
	

	

}
