/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.unit.event;

import de.jpenguin.unit.event.*;
import de.jpenguin.unit.Unit;

import java.util.EventObject;
/**
 *
 * @author Karsten
 */
public abstract class UnitEvent extends EventObject{
    
    public UnitEvent(Unit u)
    {
        super(u);
    }

    /**
     * @return the unit
     */
    public Unit getUnit() {
        return (Unit)getSource();
    }
}
