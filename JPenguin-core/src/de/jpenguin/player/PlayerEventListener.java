/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.player;


import de.jpenguin.player.event.*;
import java.util.EventListener; 
/**
 *
 * @author Karsten
 */
public interface PlayerEventListener extends EventListener{
    
    public void playerChatEvent(PlayerChatEvent e);

}
