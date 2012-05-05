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
public class UnitType extends Type{
    
    private String name;
    private float life;
    private float size;
    private String model;
    
    private String attackMissile;
    private float attackSpeed;
    private float attackDamage;
    private float attackDistance;
    private String attackSound;
    private String deathSound; 
    private float armor;
    private float hpRegeneration;
    private float mpRegeneration;
    private float mana;
    private String[] abilities;
    private float sightRadius;
    private float selectionSize;
    private int pathingType;
    
    public UnitType()
   {
                
   }
    
    
   public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(getName(),   "name",   "test");
        capsule.write(getLife(),   "life",   1);
        capsule.write(getSize(),   "size",   1);
        capsule.write(model,   "model",   "test");
        capsule.write(getAttackSpeed(),   "attackSpeed",   1);
        capsule.write(getAttackDamage(),   "attackDamage",   0);
        capsule.write(getAttackDistance(),   "attackDistance",   100);
        capsule.write(getAttackSound(),   "attackSound",   "");
        capsule.write(getDeathSound(),   "deathSound",   "");
        capsule.write(attackMissile,   "attackMissile",   "");
    }
 
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule capsule = im.getCapsule(this);
        name   = capsule.readString(    "name",   "test");
        life   = capsule.readFloat(    "life",   1);
        size   = capsule.readFloat(    "size",   1);
        model   = capsule.readString(    "model",   "test");
        attackSpeed   = capsule.readFloat(   "attackSpeed",   1);
        attackDamage   = capsule.readFloat(   "attackDamage",   1);
        attackDistance   = capsule.readFloat(   "attackDistance",   100);
        attackSound   = capsule.readString(    "attackSound",   "");
        deathSound   = capsule.readString(    "deathSound",   "");
        attackMissile = capsule.readString(    "attackMissile",   "");
        armor   = capsule.readFloat(   "armor",   0);
        hpRegeneration   = capsule.readFloat(   "hpRegeneration",   0);
        mpRegeneration   = capsule.readFloat(   "mpRegeneration",   0);
        mana   = capsule.readFloat(   "mana",   0);
        sightRadius  = capsule.readFloat(   "sightRadius",   20);
        selectionSize  = capsule.readFloat(   "selectionSize",   1);
        pathingType  = capsule.readInt(   "pathingType",   0);
        
        String test = capsule.readString(   "abilities",   null);
        if(test != null)
        {
            abilities = test.split(" ");
        }
    }
    

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the life
     */
    public float getLife() {
        return life;
    }

    /**
     * @return the size
     */
    public float getSize() {
        return size;
    }

    /**
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * @return the attackSpeed
     */
    public float getAttackSpeed() {
        return attackSpeed;
    }

    /**
     * @return the attackDamage
     */
    public float getAttackDamage() {
        return attackDamage;
    }
    
     /**
     * @return the attackDistance
     */
    public float getAttackDistance() {
        return attackDistance;
    }

    /**
     * @return the attackSound
     */
    public String getAttackSound() {
        return attackSound;
    }

    /**
     * @return the deathSound
     */
    public String getDeathSound() {
        return deathSound;
    }
    
    /**
     * @return the attackMissile
     */
    public String getAttackMissile() {
        return attackMissile;
    }
    
     /**
     * @return the armor
     */
    public float getArmor() {
        return armor;
    }

    /**
     * @return the hpRegeneration
     */
    public float getHpRegeneration() {
        return hpRegeneration;
    }

    /**
     * @return the mpRegeneration
     */
    public float getMpRegeneration() {
        return mpRegeneration;
    }

    /**
     * @return the mana
     */
    public float getMana() {
        return mana;
    }

    /**
     * @return the abilities
     */
    public String[] getAbilities() {
        return abilities;
    }

    /**
     * @return the sightRadius
     */
    public float getSightRadius() {
        return sightRadius;
    }

    /**
     * @return the selectionSize
     */
    public float getSelectionSize() {
        return selectionSize;
    }
}
