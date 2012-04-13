/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.unit.event;

import de.jpenguin.unit.Order;

import de.jpenguin.unit.Unit;
/**
 *
 * @author Karsten
 */
public class UnitExecuteOrderEvent extends UnitEvent {
    
    private Order order;
    
    public UnitExecuteOrderEvent(Unit u, Order o)
    {
        super(u);
        this.order = o;
    }

    /**
     * @return the order
     */
    public Order getOrder() {
        return order;
    }



}
