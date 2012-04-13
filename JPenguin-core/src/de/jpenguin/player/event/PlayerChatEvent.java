/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.player.event;

import de.jpenguin.player.Player;
/**
 *
 * @author Karsten
 */
public class PlayerChatEvent extends PlayerEvent {
    
    private String message;
    
    public PlayerChatEvent(Player p, String message)
    {
        super(p);
        this.message=message;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }
    
}
