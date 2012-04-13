package de.jpenguin.unit.abilities;

import com.jme3.math.Vector3f;

import de.jpenguin.unit.SimpleUnit;
import de.jpenguin.unit.Unit;
import de.jpenguin.unit.Ability;
import de.jpenguin.unit.Order;
import de.jpenguin.missile.MissileHitDamage;
import de.jpenguin.missile.MissileUnit;
import de.jpenguin.engine.Sound;
import de.jpenguin.game.Game;
/**
 *
 * @author Karsten
 */
public class Attack extends Ability{
    
    private float timer=0;
    private String timerAction="";
    private boolean paused=false;
    
    public Attack(Unit u, Order o,Game game)
    {
        super(u,o,game);
        if(order.getTargetUnit() == u)
        {
            destroy();
        }
    }
    
    public void update(float tpf)
    {
        
        if(paused==false)
        {
        if(order.getTargetUnit() == null || order.getTargetUnit().getLife() == 0)
        {
                destroy();
         }else{

              if(unit.getDistance(order.getTargetUnit().getX(),order.getTargetUnit().getY()) <= unit.getUnitType().getAttackDistance())
              {
                   unit.getModel().setAnimation("attack",(float)unit.getUnitType().getAttackSpeed());
                   unit.getModel().resetAnimation();
                   unit.rotateToPoint(order.getTargetUnit().getX(), order.getTargetUnit().getY());

                   paused=true;
                 //  unit.pause(true);
                   
                   timer = (float)unit.getUnitType().getAttackSpeed()/2;
                   timerAction = "attackHit";
              }else{
                   unit.getModel().setAnimation("walk");
                   unit.rotateToPoint(order.getTargetUnit().getX(), order.getTargetUnit().getY());
                   ((SimpleUnit)unit).moveInDirection(tpf);
              }
         }
        }
        
         if(timerAction.isEmpty()==false)
         {
             timer-=tpf;
             
             if(timer <= 0)
             {
                 
                 if(timerAction.equals("attackHit"))
                 {
                     timerAction=attackHit();
                 }else if(timerAction.equals("attackEnd"))
                     timerAction=endHit();
              }
          }
    }
    
    private String attackHit()
    {
                
        if(unit.getDistance(order.getTargetUnit().getX(),order.getTargetUnit().getY()) <= unit.getUnitType().getAttackDistance())
        {
            new Sound(unit.getGame(),unit.getUnitType().getAttackSound(),unit.getLocation(),4);
            
            if(unit.getUnitType().getAttackMissile().isEmpty())
            {
                order.getTargetUnit().damage(unit,unit.getUnitType().getAttackDamage());
            }else{
                Vector3f v3f = unit.getLocation().clone();
                v3f.setY(v3f.getY()+2.2f);
                new MissileUnit(unit.getGame(),v3f,unit.getUnitType().getAttackMissile(), order.getTargetUnit(), new MissileHitDamage((float)unit.getUnitType().getAttackDamage()));
            }
            
            timer = (float)unit.getUnitType().getAttackSpeed()/2;
            return "attackEnd";
        }else{
            paused=false;
         //   unit.pause(false);
        }
        return "";
    }
    
    private String endHit()
    {
      //  unit.pause(false);
        paused=false;
        return "";
    }
    
}
