/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.type;

import com.jme3.export.*;
import java.io.IOException;
import jme3tools.savegame.SaveGame;
import de.jpenguin.type.basics.*;

/**
 *
 * @author Karsten
 */
public class SimpleUnitType extends UnitType{
    
    private float movementSpeed;
    private float collisionSize;
    

    public static SimpleUnitType getSimpleUnitType(String id)
    {
        return Type.getType(SimpleUnitType.class, id);
    }
    
   public void write(JmeExporter ex) throws IOException {
        super.write(ex);
    }
 
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule capsule = im.getCapsule(this);
        movementSpeed   = capsule.readFloat(   "movementSpeed",   1);
        collisionSize  = capsule.readFloat(   "collisionSize",   1.5f);
        
    }

    /**
     * @return the movementSpeed
     */
    public float getMovementSpeed() {
        return movementSpeed;
    }

    /**
     * @return the collisionSize
     */
    public float getCollisionSize() {
        return collisionSize;
    }
    

}
