/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.network;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
/**
 *
 * @author Karsten
 */

@Serializable
public class GameMessage extends AbstractMessage {
    
    public static final int NextRound=0;
    public static final int Leave=1;
    public static final int Join=2;
    public static final int Loaded=3;
    
    private int value;
    private int round;
    
    private String playerId;
    
    public GameMessage()
    {
        
    }
    
    public GameMessage(String playerId,int v)
    {
        this.playerId=playerId;
        value=v;
    }
    
     public GameMessage(String playerId,int v, int round)
    {
        this.playerId=playerId;
        this.round=round;
        value=v;
    }
    
    public int getValue()
    {
        return value;
    }
    
    public String getPlayerId()
    {
        return playerId;
    }
    
    public int getRound()
    {
        return round;
    }
    
}
