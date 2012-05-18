/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.game;

import java.util.ArrayList;
import de.jpenguin.game.Game;

import de.jpenguin.player.PlayerCommandChat;

/**
 *
 * @author Karsten
 */
public class Chat {
    
    private Game game;
    
    public Chat(Game game)
    {
        this.game=game;
    }
         
    public void addMessage(String s)
    {
        addMessage(s, false);
    }
    
    public void addMessage(String s, boolean send)
    {
        if(send)
        {
            game.addPlayerCommandChat(new PlayerCommandChat(game.getControllerPlayer(),s));
        }else{
            game.getGameApplication().getGUI().chatMessage(s);
        }
    }
    
    
}
