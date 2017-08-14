/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simbotics.frc2017.auton.mode;

import java.util.ArrayList;

import org.simbotics.frc2017.auton.AutonCommand;
import org.simbotics.frc2017.auton.util.AutonStop;

/**
 *
 * @author Programmers
 */
public class AutonBuilder {
    
    private ArrayList<AutonCommand> cmds;
    
    public AutonBuilder() {
        this.cmds = new ArrayList<>();
    }
    
    public void addCommand(AutonCommand cmd) {
        this.cmds.add(cmd);
    }
    
    public AutonCommand[] getAutonList() {
        // add a stop at the end of every auton mode
        this.cmds.add(new AutonStop());
        
        AutonCommand[] result = new AutonCommand[this.cmds.size()];
        result = this.cmds.toArray(result);
        
        return result;
    }
    
}
