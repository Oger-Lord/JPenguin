/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.player.event;

import java.util.EventObject;
import de.jpenguin.player.Player;

/**
 *
 * @author Karsten
 */
public class PlayerEvent extends EventObject {
    
    public PlayerEvent(Player p)
    {
        super(p);
    }

    /**
     * @return the player
     */
    public Player getPlayer() {
        return (Player)source;
    }
}
