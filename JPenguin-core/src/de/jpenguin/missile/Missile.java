package de.jpenguin.missile;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.jme3.math.Vector3f;


import de.jpenguin.game.Game;
import de.jpenguin.engine.*;
import de.jpenguin.unit.Unit;
import de.jpenguin.type.MissileType;
/**
 *
 * @author Karsten
 */

public abstract class Missile {
    
    protected Game game;
    
    protected Vector3f location;
    protected float rotation;
    
    protected MissileType missileType;
    protected MissileHit missileHit;
    
    protected Model model;
    
    public Missile(Game game,Vector3f location,String mt, MissileHit mh)
    {
        this.game=game;
        this.missileType = MissileType.getMissileType(mt);
        
        this.location=location;
        
        this.missileHit = mh;
        
        model = new Model(missileType.getModel(), game.getGameApplication(),null,this.location,0,(float)missileType.getSize());
       // model.setScale((float)missileType.getSize());
      //  model.setTranslation(this.location);
        
        game.addMissile(this);
    }
    
    public abstract boolean update(float tpf);
    
    public double getDistance(float tx,float ty)
    {
        return Math.sqrt(Math.pow(location.getZ()-ty,2)+Math.pow(location.getX()-tx,2));
    }
    
    protected void rotateToPoint(float tx, float ty)
    {
       this.rotation = (float)(Math.atan2((float)(tx-location.getX()),(float)(ty-location.getZ()))*180/Math.PI);
       model.setRotation(rotation);
    }
    
    protected void moveInDirection(float tick)
    {
        float z = (float)(Math.cos(rotation*(Math.PI/180))*missileType.getSpeed()*tick);
        float x = (float)(Math.sin(rotation*(Math.PI/180))*missileType.getSpeed()*tick);
        
        location.setZ(location.getZ()+z);
        location.setX(location.getX()+x);
       // this.z=game.getGameApplication().getTerrain().getHeight(new Vector2f(y,x))+2.2f;
        model.setTranslation(location);
    }
    
    public void destroy()
    {
        new Sound(game, missileType.getDeathSound(),location,4);
        remove();
    }
    
    public void remove()
    {
        model.remove();
        missileHit=null;
        game.removeMissile(this);
    }
    
}
