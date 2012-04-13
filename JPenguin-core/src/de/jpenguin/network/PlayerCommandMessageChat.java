/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.network;

import com.jme3.network.*;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;

import de.jpenguin.player.PlayerCommandChat;
import de.jpenguin.unit.Order;
import de.jpenguin.unit.Unit;
import de.jpenguin.game.Game;
/**
/**
 *
 * @author Karsten
 */
@Serializable
public class PlayerCommandMessageChat extends PlayerCommandMessage  {

    private String message;
    private String playerChat;
   
    public PlayerCommandMessageChat()
    {
        
    }
    
    public PlayerCommandMessageChat(PlayerCommandChat pcc, Game game)
    {
        message = pcc.getMessage();
        
        player=game.getControllerPlayer().getId();
  
        playerChat=pcc.getPlayer().getId();
    }
    
    @Override
    public PlayerCommandChat getPlayerCommand(Game game)
    {
        return new PlayerCommandChat(game.getPlayer().get(playerChat),message);
    }
}
