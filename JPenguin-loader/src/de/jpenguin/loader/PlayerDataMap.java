/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.loader;

import com.jme3.export.*;
import com.jme3.math.ColorRGBA;

import java.io.IOException;
/**
 *
 * @author Karsten
 */
public class PlayerDataMap implements Savable {
    
    public static final int none=0;
    public static final int computer=1;
    public static final int user=2;
    
    private int controller;
    private String name;
    private String team;
    
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(getName(),   "name",   "test");
        capsule.write(team,   "team",   "");
        capsule.write(getController(),   "controller",   0);
    }
    
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        team = capsule.readString( "team",   "");
        setName(capsule.readString( "name",   "test"));
        setController(capsule.readInt( "controller",   0));
     }

    /**
     * @return the controller
     */
    public int getController() {
        return controller;
    }

    /**
     * @param controller the controller to set
     */
    public void setController(int controller) {
        this.controller = controller;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }


}
