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
public class UnitPlayerSelectionEvent extends UnitPlayerEvent {
    
    private boolean select;
    
    public UnitPlayerSelectionEvent(Unit u, Player p,boolean select)
    {
        super(u,p);
        this.select=select;
    }

    /**
     * @return the select
     */
    public boolean isSelect() {
        return select;
    }


}
