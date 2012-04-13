/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.network;

import com.jme3.network.*;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;

import java.util.ArrayList;



public class GameServer {
    // Normally these and the initialized method would
    // be in shared constants or something.

    public static final String NAME = "Test Game Server";
    public static final int VERSION = 1;
    public static final int PORT = 5111;
    public static final int UDP_PORT = 5111;
    
    public Server server;
    
    private static boolean initializedClasses=false;
    
    private static ArrayList<Message> missedMessages;

    public static void initializeClasses() {
        if(!initializedClasses)
        {
            Serializer.registerClass(GameMessage.class);
            Serializer.registerClass(PlayerCommandMessageOrder.class);
            Serializer.registerClass(PlayerCommandMessageChat.class);
            initializedClasses=true;
        }
    }
    
    public GameServer()
    {
        initializeClasses();
        
        missedMessages = new ArrayList();

        try{
            // Use this to test the client/server name version check
            server = Network.createServer(NAME, VERSION, PORT, UDP_PORT);
            server.start();

            ChatHandler handler = new ChatHandler();
            server.addMessageListener(handler, GameMessage.class);
            server.addMessageListener(handler, PlayerCommandMessageOrder.class);
            server.addMessageListener(handler, PlayerCommandMessageChat.class);
        }catch(Exception e){}
    }



    private static class ChatHandler implements MessageListener<HostedConnection>,ConnectionListener {

        public void messageReceived(HostedConnection source, Message m) {
            
            if(m instanceof GameMessage)
            {
                GameMessage gm = (GameMessage)m;
                if(gm.getValue() == GameMessage.Join)
                {
                     source.setAttribute("player", gm.getPlayerId());
                     
                     for(int i=0;i<missedMessages.size();i++)
                     {
                         source.getServer().broadcast( Filters.equalTo( source ), missedMessages.get(i));
                     }
                     
                     missedMessages.add(m);
                }
                
            }else if(m instanceof PlayerCommandMessage)
            {
                PlayerCommandMessage pcm = (PlayerCommandMessage)m;
                if(source.getAttribute("player").equals(pcm.getPlayerId())==false)
                {
                    return;
                }
            }
            
            
            source.getServer().broadcast(m);
        }
        
        public void connectionAdded( Server server, HostedConnection conn )
        {
           
        }
    
  
        public void connectionRemoved( Server server, HostedConnection conn )
        {
          //   server.broadcast(new GameMessage(GameMessage.Leave));
        }
    }
    
    public void close()
    {
        server.close();
    }
    
    public boolean isStarted()
    {
        if(server==null)
        {
            return false;
        }
        return true;
    }
}
