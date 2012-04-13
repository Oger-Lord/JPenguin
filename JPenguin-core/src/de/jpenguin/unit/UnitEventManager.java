/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.unit;

//import java.util.ArrayList;
import de.jpenguin.unit.event.*;
import javax.swing.event.EventListenerList; 
/**
 *
 * @author Karsten
 */
public class UnitEventManager {
    
    EventListenerList unitEventListener;
    EventListenerList unitPlayerEventListener;
    private UnitEventManager subUnitEventManager;
    
    public UnitEventManager()
    {
        unitEventListener = new EventListenerList(); 
        unitPlayerEventListener = new EventListenerList(); 
    }
    
    
    public void addEvent(UnitEvent ue)
    {
        
        if(ue instanceof UnitRemovedEvent)
        {
            UnitRemovedEvent uaue = (UnitRemovedEvent)ue;
            for ( UnitEventListener l : unitEventListener.getListeners( UnitEventListener.class ) ) 
                l.unitRemovedEvent(uaue);
        }else if(ue instanceof UnitDeathEvent)
        {
            UnitDeathEvent uaue = (UnitDeathEvent)ue;
            for ( UnitEventListener l : unitEventListener.getListeners( UnitEventListener.class ) ) 
                l.unitDeathEvent(uaue);
        }else if(ue instanceof UnitKillsEvent)
        {
            UnitKillsEvent uaue = (UnitKillsEvent)ue;
            for ( UnitEventListener l : unitEventListener.getListeners( UnitEventListener.class ) ) 
                l.unitKillsEvent(uaue);
        }else if(ue instanceof UnitDamagingEvent)
        {
            UnitDamagingEvent uaue = (UnitDamagingEvent)ue;
            for ( UnitEventListener l : unitEventListener.getListeners( UnitEventListener.class ) ) 
                l.unitDamagingEvent(uaue);
        }else if(ue instanceof UnitDamagedEvent)
        {
            UnitDamagedEvent uaue = (UnitDamagedEvent)ue;
            for ( UnitEventListener l : unitEventListener.getListeners( UnitEventListener.class ) ) 
                l.unitDamagedEvent(uaue);
        }else if(ue instanceof UnitExecuteOrderEvent)
        {
            UnitExecuteOrderEvent uaue = (UnitExecuteOrderEvent)ue;
            for ( UnitEventListener l : unitEventListener.getListeners( UnitEventListener.class ) ) 
                l.unitExecuteOrderEvent(uaue);
        }
        
        if(subUnitEventManager != null)
        {
            subUnitEventManager.addEvent(ue);
        }
        
    }
    
    
    public void addEvent(UnitPlayerEvent upe)
    {
        if(upe instanceof UnitPlayerSelectionEvent)
        {
            UnitPlayerSelectionEvent uaue = (UnitPlayerSelectionEvent)upe;
            for ( UnitPlayerEventListener l : unitEventListener.getListeners( UnitPlayerEventListener.class ) ) 
                l.unitPlayerSelectionEvent(uaue);
        }else if(upe instanceof UnitPlayerIssueOrderEvent)
        {
            UnitPlayerIssueOrderEvent uaue = (UnitPlayerIssueOrderEvent)upe;
            for ( UnitPlayerEventListener l : unitEventListener.getListeners( UnitPlayerEventListener.class ) ) 
                l.unitPlayerIssueOrderEvent(uaue);
        }
        
        if(subUnitEventManager != null)
        {
            subUnitEventManager.addEvent(upe);
        }
    }
    
    
    public void registerUnitEventListener(UnitEventListener uel)
    {
        unitEventListener.add(UnitEventListener.class,uel);
    }
    
    public void registerUnitPlayerEventListener(UnitPlayerEventListener upel)
    {
        unitPlayerEventListener.add(UnitPlayerEventListener.class,upel);
    } 
    
    public void removeListener(UnitEventListener uel)
    {
        unitEventListener.remove(UnitEventListener.class,uel);
    }

    public void removeListener(UnitPlayerEventListener upel)
    {
        unitPlayerEventListener.remove(UnitPlayerEventListener.class,upel);
    }

    /**
     * @param subUnitEventManager the subUnitEventManager to set dont use this!
     */
    public void setSubUnitEventManager(UnitEventManager subUnitEventManager) {
        this.subUnitEventManager = subUnitEventManager;
    }
    
}
