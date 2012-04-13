/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.unit.event;



import de.jpenguin.unit.Unit;
/**
 *
 * @author Karsten
 */
public class UnitDamagingEvent extends UnitEvent {
    
    private Unit affectUnit;
    private float damage;
    
    public UnitDamagingEvent(Unit u, float damage, Unit affectUnit)
    {
        super(u);
        this.affectUnit=affectUnit;
    }

    /**
     * @return the affectUnit
     */
    public Unit getTargetUnit() {
        return affectUnit;
    }

    /**
     * @return the damage
     */
    public float getDamage() {
        return damage;
    }

}
