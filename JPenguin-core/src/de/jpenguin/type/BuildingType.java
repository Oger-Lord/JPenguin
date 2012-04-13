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
public class BuildingType extends UnitType{
    
    private int pathingSize;

    public static BuildingType getBuildingType(String id)
    {
        return Type.getType(BuildingType.class, id);
    }
    
   public void write(JmeExporter ex) throws IOException {
        super.write(ex);
    }
 
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule capsule = im.getCapsule(this);
        pathingSize   = capsule.readInt(   "pathingSize",   1);
        
    }

    /**
     * @return the pathingSize
     */
    public int getPathingSize() {
        return pathingSize;
    }


    

}
