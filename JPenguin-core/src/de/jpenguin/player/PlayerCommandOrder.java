/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.player;

import de.jpenguin.unit.Unit;
import de.jpenguin.unit.Order;
import de.jpenguin.game.Game;

import java.util.ArrayList;
/**
 *
 * @author Karsten
 */
public class PlayerCommandOrder implements PlayerCommand {
     
    private Unit u;
    private Order o;
    
    public PlayerCommandOrder(Unit u, Order o)
    {
        this.u=u;
        this.o=o;
    }
    
    public void run(Game game)
    {
        u.clearOrderList();
        u.addOrder(o);
    }

    /**
     * @return the unit
     */
    public Unit getUnit() {
        return u;
    }

    /**
     * @return the oorder
     */
    public Order getOrder() {
        return o;
    }

    
}
