/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.player;

import de.jpenguin.player.event.PlayerChatEvent;
import de.jpenguin.unit.Order;
import de.jpenguin.game.Game;

import java.util.ArrayList;

import de.jpenguin.gui.chat.ChatOutputController;
/**
 *
 * @author Karsten
 */
public class PlayerCommandChat implements PlayerCommand {
     
    private Player player;
    private String message;
    
    public PlayerCommandChat(Player p,String message)
    {
        this.player=p;
        this.message=message;
    }
    
    public void run(Game game)
    {
        player.getPlayerEventManager().addEvent(new PlayerChatEvent(player,message));
        game.getChat().addMessage(player.getName() + ": "+message, false);
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    public Player getPlayer()
    {
        return player;
    }

    
}
