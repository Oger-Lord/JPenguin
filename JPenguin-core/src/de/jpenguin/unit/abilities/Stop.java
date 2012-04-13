/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.unit.abilities;

import de.jpenguin.unit.Unit;
import de.jpenguin.unit.Ability;
import de.jpenguin.unit.Order;

import de.jpenguin.game.Game;
/**
 *
 * @author Karsten
 */
public class Stop extends Ability{
    
    
    public Stop(Unit u, Order o,Game game)
    {
        super(u,o,game);
        u.clearOrderList();
        unit.getModel().setAnimation("stand");
    }
    
    public void update(float tpf){}
    
}
