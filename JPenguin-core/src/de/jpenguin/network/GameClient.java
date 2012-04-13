/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.network;

/**
 *
 * @author Karsten
 */
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.IOException;

import de.jpenguin.game.Game;
import de.jpenguin.player.Player;
import de.jpenguin.player.PlayerCommandOrder;
import de.jpenguin.player.PlayerCommandChat;
import de.jpenguin.player.PlayerCommandManager;

import java.util.Hashtable;
import java.util.Enumeration;

public class GameClient {

    private Client client;
    private Game game;
    
    private int waitForPlayersJoin;
    private int waitForPlayersLoad;
    private int round=0;
    
    private String playerId;
    
    private Hashtable<String,Integer> lastRecievedRound;
    
    private static final int futureSteps=4;
    

    public GameClient(Game game,String host, String playerId, int waitForPlayers) throws IOException {
        GameServer.initializeClasses();
        
        this.game=game;
        this.waitForPlayersJoin=waitForPlayers;
        this.waitForPlayersLoad=waitForPlayers;
        this.playerId=playerId;
        
        lastRecievedRound = new Hashtable();
        
        client = Network.connectToServer(GameServer.NAME, GameServer.VERSION,
                host, GameServer.PORT, GameServer.UDP_PORT);
        client.addMessageListener(new ChatHandler(), GameMessage.class);
        client.addMessageListener(new ChatHandler(), PlayerCommandMessageOrder.class);
        client.addMessageListener(new ChatHandler(), PlayerCommandMessageChat.class);
        client.start();
        
        client.send(new GameMessage(playerId,GameMessage.Join));
    }

    private class ChatHandler implements MessageListener<Client> {

        public void messageReceived(Client source, Message m) {
            
            if(m instanceof PlayerCommandMessage)
            {
                PlayerCommandMessage pcm = (PlayerCommandMessage)m;
                
                PlayerCommandManager pManager=game.getPlayerCommandManager(pcm.getPlayerId());
                
                pManager.addCommand(pcm.getPlayerCommand(game), lastRecievedRound.get(pcm.getPlayerId())-round);
            }
            
            
            if(m instanceof GameMessage)
            {
                GameMessage gm= (GameMessage)m;
                
                if(gm.getValue() == GameMessage.NextRound)
                {
                    lastRecievedRound.put(gm.getPlayerId(),gm.getRound());
                }
                
                if(gm.getValue() == GameMessage.Loaded)
                {
                    waitForPlayersLoad--;
                    System.out.println("Multiplayer:" + playerId + " "+ waitForPlayersLoad);
                    lastRecievedRound.put(gm.getPlayerId(),futureSteps);
                }
                
                if(gm.getValue() == GameMessage.Join)
                {
                    waitForPlayersJoin--;
                   
                }
            }
            
            
        }
    }
    
    public void update()
    {
        round++;
        client.send(new GameMessage(playerId,GameMessage.NextRound,round+futureSteps));
    }
    
    public boolean canContinue()
    {
        if(round+1>getLastRecievedRound())
        {
            return false;
        }
        
        return true;
    }
    
    private int getLastRecievedRound()
    {
        int smallest = -1;
        
        Enumeration<Integer> e= lastRecievedRound.elements();
        
        while(e.hasMoreElements())
        {
            int i = e.nextElement();
            
            if(smallest == -1 || smallest > i)
            {
                smallest=i;
            }
        }
        
        return smallest;
    }
    
    public boolean allLoaded()
    {
        if(waitForPlayersLoad<=0)
        {
            return true;
        }
        return false;
    }
    
    public boolean allConnected()
    {
        if(waitForPlayersJoin<=0)
        {
            return true;
        }
        return false;
    }
    
    public void close()
    {
        client.close();
    }

    public void addPlayerCommand(PlayerCommandOrder pc)
    {
        client.send(new PlayerCommandMessageOrder(pc,game));
    }
    
    public void addPlayerCommandChat(PlayerCommandChat pc)
    {
        client.send(new PlayerCommandMessageChat(pc,game));
    }
    
    public void finishedLoading()
    {
        client.send(new GameMessage(playerId,GameMessage.Loaded));
    }
}
