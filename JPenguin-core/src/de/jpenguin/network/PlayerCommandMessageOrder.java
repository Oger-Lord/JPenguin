/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.network;

import com.jme3.network.*;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;

import de.jpenguin.player.PlayerCommandOrder;
import de.jpenguin.unit.Order;
import de.jpenguin.unit.Unit;
import de.jpenguin.game.Game;
/**
 *
 * @author Karsten
 */

@Serializable
public class PlayerCommandMessageOrder extends PlayerCommandMessage  {
    
   // private String player;
    private int unitFrom;
    private String orderString;
    private float targetX;
    private float targetY;
    private int targetUnit=-1;
    private int type;
    
    public PlayerCommandMessageOrder()
    {
        
    }
    
    public PlayerCommandMessageOrder(PlayerCommandOrder pc, Game game)
    {
        unitFrom = idFromUnit(game, pc.getUnit());
        
        orderString = pc.getOrder().getOrderString();
        targetX = pc.getOrder().getTargetX();
        targetY = pc.getOrder().getTargetY();
        
        targetUnit = idFromUnit(game, pc.getOrder().getTargetUnit());
        
        type = pc.getOrder().getType();
        
        player=game.getControllerPlayer().getId();
        
    }
    
    @Override
    public PlayerCommandOrder getPlayerCommand(Game game)
    {
        Order o;
        
        if(type == Order.TARGET_POINT)
        {
            o =new Order(orderString,targetX,targetY);
        }else if(type == Order.TARGET_UNIT)
        {
            o =new Order(orderString,unitFromId(game,targetUnit));
        }else{
            o =new Order(orderString);
        }
        
        return new PlayerCommandOrder(unitFromId(game,unitFrom),o);
    }
    
    public static Unit unitFromId(Game game, int id)
    {
        return game.getUnits().get(id);
    }
    
    public static int idFromUnit(Game game,Unit u)
    {
        return game.getUnits().indexOf(u);
    }
    
    /*
    public String getPlayerId()
    {
        return player;
    }
     * 
     */
    
}
