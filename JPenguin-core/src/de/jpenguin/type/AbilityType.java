/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class AbilityType extends Type {
    
    private String name;
    private String description;
    private String orderId;
    private String icon;
    private float manaCosts;
    private float cooldown;
    private int position;
    private int target; 
    
    public static AbilityType getAbilityType(String id)
    {
        return Type.getType(AbilityType.class, id);
    }
    
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule capsule = im.getCapsule(this);
        name   = capsule.readString(    "name",   "");
        description   = capsule.readString(    "description",   "");
        orderId  = capsule.readString(    "orderId",   "");
        icon   = capsule.readString(    "icon",   "");
        position   = capsule.readInt(   "position",   1);
        target   = capsule.readInt(   "target",   0);
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
     * @return the orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @return the manaCosts
     */
    public float getManaCosts() {
        return manaCosts;
    }

    /**
     * @return the cooldown
     */
    public float getCooldown() {
        return cooldown;
    }

    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * @return the target
     */
    public int getTarget() {
        return target;
    }


}
