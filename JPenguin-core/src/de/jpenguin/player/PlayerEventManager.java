/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.player;

//import java.util.ArrayList;
import de.jpenguin.player.event.*;
import javax.swing.event.EventListenerList; 
/**
 *
 * @author Karsten
 */
public class PlayerEventManager {
    
    EventListenerList playerEventListener;
    private PlayerEventManager subPlayerEventManager;
    
    public PlayerEventManager()
    {
        playerEventListener = new EventListenerList();  
    }
    
    
    public void addEvent(PlayerEvent pe)
    {
        
        if(pe instanceof PlayerChatEvent)
        {
            PlayerChatEvent uaue = (PlayerChatEvent)pe;
            for ( PlayerEventListener l : playerEventListener.getListeners( PlayerEventListener.class ) ) 
                l.playerChatEvent(uaue);
        }
        
        if(subPlayerEventManager != null)
        {
            subPlayerEventManager.addEvent(pe);
        }
        
    }
    
    
    public void registerPlayerEventListener(PlayerEventListener pel)
    {
        playerEventListener.add(PlayerEventListener.class,pel);
    }
    

    public void removeListener(PlayerEventListener pel)
    {
        playerEventListener.remove(PlayerEventListener.class,pel);
    }

    /**
     * @param subPlayerEventManager the subPlayerEventManager to set dont use this!
     */
    public void setSubUnitEventManager(PlayerEventManager subPlayerEventManager) {
        this.subPlayerEventManager = subPlayerEventManager;
    }
    
}
