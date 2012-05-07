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
import de.jpenguin.pathing.SubPathingMap;
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
        
        Vector2f v2f =findSpace(x,y);
        
        location = new Vector3f(v2f.x,0,v2f.y);
        
        if(unitType.getPathingType() != 3)
        {
            location.setY(game.getGameApplication().getTerrain().getHeight(v2f));
        }else{
            location.setY(game.getGameApplication().getWater().getHeight(v2f.x,v2f.y));
        }
        model.setTranslation(location);
    }
    
    
    private Vector2f findSpace(float x, float y)
    {
       //Find space
        SubPathingMap spm = game.getPathingMap().getSubMap(unitType.getPathingType());
        if (spm.isUnitPathfinding())
        {
            int ix =spm.convertX(x);
            int iy =spm.convertY(y);
            
            if(spm.hasSpaceDirect(ix, iy, (int)(collision+0.5)))
                return new Vector2f(x,y);
            
            for(int i=0;i<20*spm.getScale();i++)
            {
                //North
                if(iy-i >= 0)
                {
                    for(int a=ix-i-1;a<ix+2+i;a++)
                    {
                        if(spm.hasSpaceDirect(a, iy-i, (int)(collision+0.5)))
                            return new Vector2f(spm.convertX(a),spm.convertY(iy-i));
                    }
                }
                
                //South
                if(iy+i < spm.getHeight())
                {
                    for(int a=ix-i-1;a<ix+2+i;a++)
                    {
                        if(spm.hasSpaceDirect(a, iy+i, (int)(collision+0.5)))
                            return new Vector2f(spm.convertX(a),spm.convertY(iy+i));
                    }
                }
                
                //West
                if(ix-i >= 0)
                {
                    for(int a=iy-i-1;a<iy+2+i;a++)
                    {
                        if(spm.hasSpaceDirect(ix-i,a, (int)(collision+0.5)))
                            return new Vector2f(spm.convertX(ix-i),spm.convertY(a));
                    }
                }
                    
                //East
                if(ix-i < spm.getWidth())
                {
                    for(int a=iy-i-1;a<iy+2+i;a++)
                    {
                        if(spm.hasSpaceDirect(ix+i,a, (int)(collision+0.5)))
                            return new Vector2f(spm.convertX(ix+i),spm.convertY(a));
                    }
                }
            }
        }
        return new Vector2f(x,y);      
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
        
        if(unitType.getPathingType() != 3)
        {
            location.setY(game.getGameApplication().getTerrain().getHeight(new Vector2f(location.getX(),location.getZ())));
        }else{
            location.setY(game.getGameApplication().getWater().getHeight(location.getX(),location.getZ()));
        }
            
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
        
        
        if(unitType.getPathingType() != 3)
        {
            location.setY(game.getGameApplication().getTerrain().getHeight(new Vector2f(location.getX(),location.getZ())));
        }else{
            location.setY(game.getGameApplication().getWater().getHeight(location.getX(),location.getZ()));
        }
       
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
