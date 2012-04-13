/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.unit;


import de.jpenguin.unit.event.*;
import java.util.EventListener; 
/**
 *
 * @author Karsten
 */
public interface UnitEventListener extends EventListener{
    
    public void unitDamagedEvent(UnitDamagedEvent e);
    public void unitDamagingEvent(UnitDamagingEvent e);
    public void unitDeathEvent(UnitDeathEvent e);
    public void unitKillsEvent(UnitKillsEvent e);
    public void unitRemovedEvent(UnitRemovedEvent e);
    public void unitExecuteOrderEvent(UnitExecuteOrderEvent e);
}
