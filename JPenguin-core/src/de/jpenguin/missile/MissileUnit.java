/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.missile;

import de.jpenguin.game.Game;
import de.jpenguin.engine.*;
import de.jpenguin.unit.Unit;
import de.jpenguin.type.MissileType;
import com.jme3.math.Vector3f;
/**
 *
 * @author Karsten
 */
public class MissileUnit extends Missile {
    
    public Unit target;
    public float targetX;
    public float targetY;
    
    public MissileUnit(Game game,Vector3f location,String mt, Unit target, MissileHit mh)
    {
        super(game,location,mt, mh);
        
        this.target=target;
    }
    
    public boolean update(float tpf)
    {
        if(target != null)
        {
            targetX = target.getX();
            targetY = target.getY();
        }
        if(getDistance(targetX, targetY) <= 1)
        {
                missileHit.missileHit(target);
                destroy();
                return false;
        }else{
                rotateToPoint(targetX, targetY);
                moveInDirection(tpf);
        }
         return true;
    }
}
