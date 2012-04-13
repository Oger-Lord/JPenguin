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
public class UnitKillsEvent extends UnitEvent {
    
    private Unit affectUnit;
    
    public UnitKillsEvent(Unit u, Unit affectUnit)
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

}
