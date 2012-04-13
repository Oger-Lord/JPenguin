/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.unit;

import de.jpenguin.unit.event.UnitPlayerSelectionEvent;
import de.jpenguin.unit.event.UnitPlayerIssueOrderEvent;

import java.util.EventListener; 
/**
 *
 * @author Karsten
 */
public interface UnitPlayerEventListener extends EventListener {
    
    public void unitPlayerSelectionEvent(UnitPlayerSelectionEvent e);
    public void unitPlayerIssueOrderEvent(UnitPlayerIssueOrderEvent e);
}
