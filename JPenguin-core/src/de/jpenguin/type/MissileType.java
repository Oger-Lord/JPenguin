/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.type;

import com.jme3.export.*;
import java.io.IOException;
import de.jpenguin.type.basics.*;
/**
 *
 * @author Karsten
 */
public class MissileType extends Type {
    
    private String model;
    private String deathSound;
    private float speed;
    private float size;
    private float arc;
    private float collisionSize;
    
    public static MissileType getMissileType(String id)
    {
        return Type.getType(MissileType.class, id);
    }
    
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule capsule = im.getCapsule(this);
        size   = capsule.readFloat(    "size",   1);
        model   = capsule.readString(    "model",   "");
        deathSound   = capsule.readString(    "deathSound",   "");
        speed   = capsule.readFloat(   "speed",   1);
        arc   = capsule.readFloat(   "arc",   0);
        collisionSize   = capsule.readFloat(   "collisionSize",   1);
    }

    /**
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * @return the speed
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * @return the size
     */
    public float getSize() {
        return size;
    }

    /**
     * @return the arc
     */
    public float getArc() {
        return arc;
    }

    /**
     * @return the collisionSize
     */
    public float getCollisionSize() {
        return collisionSize;
    }
    
    /**
     * @return the deathSound
     */
    public String getDeathSound() {
        return deathSound;
    }
}
