/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.missile;

import de.jpenguin.unit.Unit;
/**
 *
 * @author Karsten
 */
public interface MissileHit
{
    public void missileHit(Unit u);
    public void missileHit(float x, float y, float z);
}
