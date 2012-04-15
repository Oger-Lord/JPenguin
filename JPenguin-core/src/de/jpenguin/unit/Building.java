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

public class Building extends Unit{
    
    protected BuildingType unitType;

    public Building(Game game, String ut, String p,float x, float y, float rotation)
    {
        unitType = BuildingType.getBuildingType(ut);
        if(unitType== null)
        {
            System.out.println("Error, UnitType not found " + ut);
        }
        
        if(unitType.getPathingSize()%2==0)
        {
            if(Math.abs(x%2)!=0 || Math.abs(y%2)!=0)
            {
                x = ((int)((x+0.5f)/2f))*2;
                y = ((int)((y+0.5f)/2f))*2;
            }
        }else{
            if(Math.abs(x%2)!=1 || Math.abs(y%2)!=1)
            {
                x = ((int)((x+0.5f)/2f))*2+1f;
                y = ((int)((y+0.5f)/2f))*2+1f;
            }
        }
        
       // game.getPathingMap().setSpace(x, y, unitType.getPathingSize(), true);
        
        init(game,unitType,p,x,y,rotation);
        
        model.setBuilding(true);
    }
    
    
    @Override
    public Obstacle toObstacle() {
         //return new SimpleObstacle(location, getCollisionSize(), velocity);
        return null;
    }
    
       public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
    }
 
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
    }

    
}
