package org.simbotics.frc2017.teleop;

import java.util.ArrayList;

/**
 *
 * @author Michael
 */
public class TeleopControl {
    
    private ArrayList<TeleopComponent> components; 
    private static TeleopControl instance;
    
    public static TeleopControl getInstance() {
        if(instance == null) {
            instance = new TeleopControl();
        }
        return instance;
    }

    private TeleopControl() {
        // GOTCHA: remember to add teleop components here!
        this.components = new ArrayList<>();
        
        // add teleop components here
        this.components.add(TeleopDrive.getInstance());
        this.components.add(TeleopShooter.getInstance());
        this.components.add(TeleopHanger.getInstance());
        this.components.add(TeleopPouch.getInstance());
        this.components.add(TeleopTurret.getInstance());
    }
    
    public void runCycle() {
    	for(TeleopComponent t : this.components) {
    		t.calculate();
    	}
    }
    
    public void disable() {
    	for(TeleopComponent t : this.components) {
    		t.disable();
    	}
    }
            
}
