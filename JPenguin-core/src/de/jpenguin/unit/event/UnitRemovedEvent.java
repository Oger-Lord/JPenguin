/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.unit.event;

import de.jpenguin.unit.event.*;
import de.jpenguin.unit.Unit;
/**
 *
 * @author Karsten
 */
public class UnitRemovedEvent extends UnitEvent {
    
    public UnitRemovedEvent(Unit unit)
    {
        super(unit);
    }
}
