/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TowerDefense;

import de.jpenguin.unit.UnitEventListener;
import de.jpenguin.game.Chat;
import de.jpenguin.unit.event.*;
/**
 *
 * @author Karsten
 */
public class TestUnitListener implements UnitEventListener {

    private Chat chat;
    
    public TestUnitListener(Chat chat)
    {
        this.chat=chat;
    }

    public void unitDamagedEvent(UnitDamagedEvent e) {
        
    }

    public void unitDamagingEvent(UnitDamagingEvent e) {
         chat.addMessage(e.getUnit().getUnitType().getName()+" is attacking " + e.getTargetUnit().getUnitType().getName());
    }

    public void unitDeathEvent(UnitDeathEvent e) {
       
    }

    public void unitKillsEvent(UnitKillsEvent e) {
        
    }

    public void unitRemovedEvent(UnitRemovedEvent e) {
        
    }

    public void unitExecuteOrderEvent(UnitExecuteOrderEvent e) {
        
    }
    

    
}
