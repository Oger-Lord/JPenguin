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
public class PlayerData implements Savable {
    
    private String name;
    private String id;
    private boolean visible;
    private ColorRGBA color;
    private boolean neutral;
    
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(name,   "name",   "test");
        capsule.write(id,   "id",   "test");
        capsule.write(visible,   "visible",   true);
        capsule.write(color,  "color",   ColorRGBA.Red);
        capsule.write(isNeutral(),  "neutral",   false);
    }
    
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        name  = capsule.readString( "name",   "test");
        id  = capsule.readString( "id",   "test");
        visible  = capsule.readBoolean( "visible",   true);
        setNeutral(capsule.readBoolean( "neutral",   false));
        color  = (ColorRGBA)capsule.readSavable("color", ColorRGBA.Red);
        
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

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * @param visible the visible to set
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * @return the color
     */
    public ColorRGBA getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(ColorRGBA color) {
        this.color = color;
    }

    /**
     * @return the neutral
     */
    public boolean isNeutral() {
        return neutral;
    }

    /**
     * @param neutral the neutral to set
     */
    public void setNeutral(boolean neutral) {
        this.neutral = neutral;
    }

}
