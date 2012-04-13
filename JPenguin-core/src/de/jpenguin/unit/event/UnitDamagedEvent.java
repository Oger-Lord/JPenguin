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
public class UnitDamagedEvent extends UnitEvent {
    
    private Unit affectUnit;
    private float damage;
    
    public UnitDamagedEvent(Unit u, float damage, Unit affectUnit)
    {
        super(u);
        this.affectUnit=affectUnit;
    }

    /**
     * @return the affectUnit
     */
    public Unit getDamagingUnit() {
        return affectUnit;
    }

    /**
     * @return the damage
     */
    public float getDamage() {
        return damage;
    }

}
