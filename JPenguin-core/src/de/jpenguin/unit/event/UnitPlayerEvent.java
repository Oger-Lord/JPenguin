/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.unit.event;

import de.jpenguin.unit.event.*;
import de.jpenguin.unit.Unit;

import de.jpenguin.player.Player;

import java.util.EventObject;
/**
 *
 * @author Karsten
 */
public abstract class UnitPlayerEvent extends EventObject{
    
    private Player player;
    
    public UnitPlayerEvent(Unit u, Player p)
    {
        super(u);
        player=p;
    }

    /**
     * @return the unit
     */
    public Unit getUnit() {
        return (Unit)getSource();
    }
    
    /**
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }
}
