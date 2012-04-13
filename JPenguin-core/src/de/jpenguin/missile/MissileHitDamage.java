/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.missile;

import de.jpenguin.unit.Unit;
/**
 *
 * @author Karsten
 */
public class MissileHitDamage implements MissileHit{

    public float damage;
    
    public MissileHitDamage(float damage)
    {
        this.damage=damage;
    }
    
    public void missileHit(Unit u)
     {
         if(u != null && u.getLife() > 0)
         {
            u.damage(damage);
         }
     }
     
    public void missileHit(float x, float y, float z)
    { 
    }
}
