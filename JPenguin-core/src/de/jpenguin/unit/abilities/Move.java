/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.unit.abilities;

import de.jpenguin.unit.Unit;
import de.jpenguin.unit.Ability;
import de.jpenguin.unit.Order;
import de.jpenguin.unit.SimpleUnit;

import de.jpenguin.pathing.JumpPointSearch;

import com.jme3.ai.steering.behaviour.ObstacleAvoidNew;
import com.jme3.ai.steering.behaviour.Seek;
import com.jme3.ai.steering.Obstacle;
import com.jme3.math.Vector3f;

import de.jpenguin.game.Game;
import de.jpenguin.pathing.PathingMap;

import java.util.ArrayList;
/**
 *
 * @author Karsten
 */
public class Move extends Ability{
    
    private float rotation=-1;
    private boolean isRotating=true;
    
    private ArrayList<Vector3f> points; 
    
    public Move(Unit u, Order o,Game game)
    {
        super(u,o,game);
      //  points = new ArrayList();
     //   points.add(new Vector3f(o.getTargetX(),0,o.getTargetY()));
        
        points = new ArrayList();
       // points.add(new Vector3f(order.getTargetX(),0,order.getTargetY()));
        unit.getModel().setAnimation("walk");
        

        JumpPointSearch jps = new JumpPointSearch(game.getPathingMap(), 1,unit.getUnitType().getPathingType(),u.getX(), u.getY(), o.getTargetX(), o.getTargetY());

        if(jps.wayExists()==false)
        {
            destroy();
            return;
        }
        
        points = jps.getVectorList();
        
        points.remove(0);
       // points.add(new Vector3f(o.getTargetX(),0,o.getTargetY()));
        
        /*
        
        PathingSearch pt =new PathingSearch(game.getPathingMap(), game.getGameApplication(),1,u.getX(), u.getY(), o.getTargetX(), o.getTargetY());

        if(pt.wayExists()==false)
        {
            destroy();
            return;
        }
        
        points = pt.getOrderList();
        
        if(((SimpleUnit)unit).getUnitType().getMovementSpeed() == 0)
        {
              unit.getModel().setAnimation("stand");
              destroy();
        }else{
            unit.getModel().setAnimation("walk");
        }
         * 
         */
    }
    
    
    public void update(float tpf)
    {
        
        if(points == null || points.isEmpty() || points.get(0) == null)
        {
            destroy();
            return;
        }
        
        Vector3f target = points.get(0);
      //  target.setX((float)order.getTargetX());
     //   target.setZ((float)order.getTargetY());
        
        
        if(isRotating)
            {
                double r =Math.atan2(target.getX()-unit.getX(),target.getZ()-unit.getY())*180/Math.PI;
                
                //unit.setRotation((float)r);
                
                double change = r-unit.getRotation();
               
              //  System.out.println("c "+change + " " + r + " " + unit.getRotation());
                
                rotation = unit.getRotation();
                isRotating=rotate((float)change,(float)tpf);
                unit.setRotation(rotation);
                
                if(isRotating==false)
                {
                    ((SimpleUnit)unit).resetVelocity();
                }
                 

                return;
         }  
        
        
        if(((SimpleUnit)unit).getUnitType().getMovementSpeed()/4 >((SimpleUnit)unit).getVeloctiy().length())
        {
            destroy();
            return;
        }


        Seek seek= new Seek();
        
        Vector3f steering = seek.calculateForce(unit.getLocation(), ((SimpleUnit)unit).getVeloctiy(), 
                                    (float)((SimpleUnit)unit).getUnitType().getMovementSpeed(), target);
        
        
        ObstacleAvoidNew oa = new ObstacleAvoidNew(); 

        Vector3f avoidance =oa.calculateForce(unit.getLocation(), ((SimpleUnit)unit).getVeloctiy(), ((SimpleUnit)unit).getCollisionSize(),
                                    (float)((SimpleUnit)unit).getUnitType().getMovementSpeed(),2f, tpf,
                                    getObstacals(unit));
        
        
        ((SimpleUnit)unit).updateVelocity(steering.add(avoidance.mult(5)),tpf);
        
        
             
        if(points.size()==1)
        {
            if(unit.getDistance((float)target.getX(),(float)target.getZ()) < 1)
            {
                destroy();
            }
        }else{
            if(unit.getDistance((float)target.getX(),(float)target.getZ()) < unit.getUnitType().getSize())
            {
                points.remove(0);
            }
        }
        
    }
    
    private boolean rotate(float change, float tpf)
    {
        float rotationSpeed=200;
        
        if(Math.abs(change) > rotationSpeed*tpf)
        {
            if(change > 0)
            {
                rotation += rotationSpeed*tpf;
            }else{
                rotation -= rotationSpeed*tpf;
            }
        }else{
            rotation += change; 
            return false;
        }
        return true;
    }
    
   
    
    private ArrayList<Obstacle> getObstacals(Unit uu) {
        ArrayList obstacles = new ArrayList();
        
        ArrayList<Unit> units = unit.getGame().getUnits();
        for(int i=0;i<units.size();i++)
        {
            Unit u = units.get(i);
            if(u != uu)
            {
                Obstacle o = u.toObstacle();
                if(o != null)
                {
                    obstacles.add(o);
                }
            }
        }

    //    ArrayList pathingBlocks = game.getPathingMap().getObstacles(uu.getX(), uu.getY(), 5);
     //   obstacles.addAll(pathingBlocks);

        return obstacles;
    }
    
}
