/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.type;

/**
 *
 * @author Karsten
 */

import com.jme3.export.*;
import java.io.IOException;
import de.jpenguin.type.basics.*;
/**
 *
 * @author Karsten
 */
public class BuffType extends Type {
    
    private String name;
    private String description;
    private String model;
    private String[] attachmentPoints;
    private String icon;
    
    public static BuffType getBuffType(String id)
    {
        return Type.getType(BuffType.class, id);
    }
    
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule capsule = im.getCapsule(this);
        name   = capsule.readString(    "name",   "");
        description   = capsule.readString(    "description",   "");
        icon   = capsule.readString(    "icon",   "");
        model   = capsule.readString(    "model",   "");
        
        String test = capsule.readString(   "attachmentPoints",   null);
        if(test != null)
        {
            attachmentPoints = test.split(" ");
        }
    }


    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * @return the attachmentPoints
     */
    public String[] getAttachmentPoints() {
        return attachmentPoints;
    }




}
