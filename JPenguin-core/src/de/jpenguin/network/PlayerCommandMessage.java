/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.network;

import com.jme3.network.*;

import de.jpenguin.game.Game;
import de.jpenguin.player.PlayerCommand;
/**
 *
 * @author Karsten
 */
public abstract class PlayerCommandMessage extends AbstractMessage {
    
    protected String player;
    
    public abstract PlayerCommand getPlayerCommand(Game game);
    
    public String getPlayerId()
    {
        return player;
    }
}
