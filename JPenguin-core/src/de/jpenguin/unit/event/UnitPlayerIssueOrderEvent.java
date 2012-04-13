/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.unit.event;


import de.jpenguin.unit.Order;
import de.jpenguin.unit.Unit;

import de.jpenguin.player.Player;
/**
 *
 * @author Karsten
 */
public class UnitPlayerIssueOrderEvent extends UnitPlayerEvent {
    private Order order;
    
    public UnitPlayerIssueOrderEvent(Unit u, Player p,Order order)
    {
        super(u,p);
        this.order=order;
    }

    /**
     * @return the order
     */
    public Order getOrder() {
        return order;
    }
}
