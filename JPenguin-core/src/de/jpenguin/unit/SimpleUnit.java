package de.jpenguin.unit;

import de.jpenguin.player.Player;
import com.jme3.scene.*;
import com.jme3.asset.AssetManager;
import com.jme3.animation.*;
import com.jme3.light.*;
import com.jme3.math.*;
import com.jme3.export.*;
import com.jme3.shadow.*;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.ai.steering.Obstacle;
import com.jme3.ai.steering.utilities.SimpleObstacle;

import java.util.ArrayList;
import java.io.*;
import java.util.*;

import de.jpenguin.engine.*;
import de.jpenguin.type.*;
import de.jpenguin.game.*;
import de.jpenguin.missile.*;
import de.jpenguin.unit.event.*;

public class SimpleUnit extends Unit{
    
    private Vector3f velocity = Vector3f.UNIT_Z.clone();
    private float collision;
    protected SimpleUnitType unitType;
    

    public SimpleUnit(Game game, String ut, String p,float x, float y, float rotation)
    {
        unitType = SimpleUnitType.getSimpleUnitType(ut);
        if(unitType== null)
        {
            System.out.println("Error, UnitType not found " + ut);
        }
        init(game,unitType,p,x,y,rotation);
        
        this.collision = unitType.getCollisionSize();
    }
    
    public void setRotation(float r)
    {
        super.setRotation(r);
       velocity.z = (float)Math.cos(rotation*(Math.PI/180));
       velocity.x = (float)Math.sin(rotation*(Math.PI/180));
    }
    
    public Vector3f getVeloctiy()
    {
        return velocity;
    }
    
    public void moveInDirection(float tick)
    {
         
        if(game.getControllerPlayer().shareVision(player))
        {
            player.getFogOfWarPlayer().setSight((float)sightRadius, location.getX(), location.getZ(), false);
        }
        
         Vector3f v3f = new Vector3f(0,0,0);
        v3f.setZ((float)(Math.cos(rotation*(Math.PI/180))*unitType.getMovementSpeed()*tick));
        v3f.setX((float)(Math.sin(rotation*(Math.PI/180))*unitType.getMovementSpeed()*tick));

        
        location = location.add(v3f); 
        location.setY(game.getGameApplication().getTerrain().getHeight(new Vector2f(location.getX(),location.getZ())));
        
        if(game.getControllerPlayer().shareVision(player))
        {
            player.getFogOfWarPlayer().setSight((float)sightRadius, location.getX(), location.getZ(), true);
        }
       
        model.setTranslation(location);
    }
    
    public void resetVelocity()
    {
        Vector3f v = new Vector3f(0,0,0);
       v.z = (float)Math.cos(rotation*(Math.PI/180));
       v.x = (float)Math.sin(rotation*(Math.PI/180));
       
       velocity = v.normalize().scaleAdd((float)unitType.getMovementSpeed(), Vector3f.ZERO);
    }
    
    public void updateVelocity(Vector3f steeringInfluence, float scale) {

        Vector3f steeringForce = truncate(steeringInfluence, 2f * scale);
        Vector3f acceleration = steeringForce.divide(1f);
        Vector3f vel = truncate(velocity.add(acceleration.clone()), (float)unitType.getMovementSpeed());
        
         velocity = vel;

        
        if(game.getControllerPlayer().shareVision(player))
        {
            player.getFogOfWarPlayer().setSight((float)sightRadius, location.getX(), location.getZ(), false);
        }
         
            
            
            location.addLocal(velocity.mult(scale));
            
            
        rotateToPoint(location.getX()+velocity.getX(),location.getZ()+velocity.getZ());
            

        if(game.getControllerPlayer().shareVision(player))
        {
            player.getFogOfWarPlayer().setSight((float)sightRadius, location.getX(), location.getZ(), true);
        }
        
        location.setY(game.getGameApplication().getTerrain().getHeight(new Vector2f(location.getX(),location.getZ())));
       
        model.setTranslation(location);
        model.setRotation(rotation);
        
     }
     
     private Vector3f truncate(Vector3f source, float limit) {
            if (source.lengthSquared() <= limit*limit) {
                return source;
            } else {
                return source.normalize().scaleAdd(limit, Vector3f.ZERO);
            }
        }
     
     
    @Override
   public SimpleUnitType getUnitType()
   {
       return unitType;
   }
     
   public float getCollisionSize()
   {
       return collision;
   }
    
    @Override
    public Obstacle toObstacle() {
         return new SimpleObstacle(location, getCollisionSize(), velocity);
    }
    
       public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
    }
 
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
    }

    
}
