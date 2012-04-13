/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.pathing;

import de.jpenguin.game.GameApplication;
import de.jpenguin.loader.PathingMapData;
import com.jme3.asset.AssetManager;

import java.util.ArrayList;
import com.jme3.ai.steering.utilities.SimpleBox2DObstacle;
import com.jme3.ai.steering.Obstacle;

import com.jme3.math.Vector3f;

/*     */ import com.jme3.material.Material;
/*     */ import com.jme3.math.ColorRGBA;
/*     */ import com.jme3.math.Vector3f;
/*     */ import com.jme3.scene.Geometry;
/*     */ import com.jme3.scene.Node;
          import com.jme3.scene.shape.Box;
/**
 *
 * @author Karsten
 */
public class PathingMap {
    
    private int size;
    private int width,height;
    private int pathingMap[][];
    
    private Obstacle obstacleMap[][];
    /*
    public PathingMap(int width, int height)
    {
        this.width=width;
        this.height=height;
        pathingMap = new int[width][height];
        
        for(int x=0;x<width;x++)
        {
            for(int y=0;y<height;y++)
            {
                pathingMap[x][y] = 2;
            }
        }
    }
     * 
     */
    
    public PathingMap(GameApplication gameApp,String map)
    {
        PathingMapData pmd =PathingMapData.load(gameApp.getAssetManager(), map);
        
        this.width=pmd.getWidth();
        this.height=pmd.getHeight();
        this.size = pmd.getSize();
        pathingMap = new int[width][height];
        obstacleMap = new Obstacle[width][height];
        
        for(int x=0;x<width;x++)
        {
            for(int y=0;y<height;y++)
            {
                if(pmd.getValue(x, y)==0)
                {
                    pathingMap[x][y] = 2;
                }else{
                    pathingMap[x][y] = 0;
                    obstacleMap[x][y] = new SimpleBox2DObstacle(new Vector3f(convertIntX(x),0,convertIntY(y)),1f);
                    
                     //Draw Waypoints
                    Box b = new Box(1,1,1f);
                    Geometry geom = new Geometry("Sphere", b);
                    Material mat = new Material(gameApp.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
                    mat.setColor("Color", ColorRGBA.Red);
                    geom.setMaterial(mat);
                    geom.setLocalTranslation(new Vector3f(convertIntX(x),0,convertIntY(y)));
                    gameApp.getRootNode().attachChild(geom);
                    
                }
            }
        }
    }
    
    
    public void setPoint(int x, int y)
    {
        pathingMap[x][y] = 0;
        
        for(int i=x-1;i<x+2;i++)
        {
            for(int j=y-1;j<x+2;j++)
            {
                if(i > 0 && i < width && j>0 && j<height)
                {
                    if(pathingMap[i][j] != 0)
                    {
                        pathingMap[i][j] = 1;
                    }
                }
            }
        }
    }
    
    public int getValue(int x, int y)
    {
        return pathingMap[x][y];
    }
    
    public int getWidth()
    {
        return width;
    }
    
   public int getHeight()
    {
        return height;
    }
   
     public int convertFloatX(float x)
    {
        return (int)((x+size/2)/size*width+0.5);
    }
    
    public int convertFloatY(float y)
    {
        return (int)((y+size/2)/size*height+0.5);
    }
    
    public float convertIntX(int x)
    {
        return (float)(x+0.5)*(size/width)-size/2;
    }
     
    public float convertIntY(int y)
    {
        return (float)(y-0.5f)*(size/height)-size/2;
    }
    
        public boolean hasSpace(float x, float y, int size)
    {
        return hasSpaceInt(convertFloatX(x), convertFloatY(y),size);
    }
    
    public boolean hasSpaceInt(int x, int y, int size)
    {
        for(int mx=x-size/2;mx<x+size/2+size%2;mx++)
        {
            for(int my=y-size/2+1;my<y+size/2+1+size%2;my++)
            {
                if(mx>=0 && mx < width && my>=0 && my < height)
                {
                    if(pathingMap[mx][my] == 0)
                    {
                        return false;
                    }
                }else{
                    return false;
                }
            }
        }
        return true;
    }
    
    public void setSpace(float x, float y, int size, boolean block)
    {
        setSpaceInt(convertFloatX(x), convertFloatY(y),size,block);
    }
    
    public void setSpaceInt(int x, int y, int size, boolean block)
    {
        for(int mx=x-size/2;mx<x+size/2+size%2;mx++)
        {
            for(int my=y-size/2+1;my<y+size/2+1+size%2;my++)
            {
                if(mx>=0 && mx < width && my>=0 && my < height)
                {
                    if(block)
                    {
                        pathingMap[mx][my] = 0;
                        obstacleMap[mx][my] = new SimpleBox2DObstacle(new Vector3f(convertIntX(mx),0,convertIntY(my)),1f);
                    }else{
                        if(pathingMap[mx][my] == 0)
                        {
                            obstacleMap[mx][my] = null;
                        }
                        pathingMap[mx][my] = 2;
                    }
                }
            }
        }
    }
    
    public ArrayList<Obstacle> getObstacles(float x, float y, float radius)
    {
        return getObstacles(convertFloatX(x), convertFloatY(y),(int)radius);
    }
    
    public ArrayList<Obstacle> getObstacles(int x, int y, int size)
    {
        ArrayList<Obstacle> obstacles = new ArrayList();
        
        for(int mx=x-size/2;mx<x+size/2+size%2;mx++)
        {
            for(int my=y-size/2;my<y+size/2+size%2;my++)
            {
                if(mx>=0 && mx < width && my>=0 && my < height)
                {
                    if(pathingMap[mx][my]==0)
                    {
                        obstacles.add(obstacleMap[mx][my]);
                    }
                }
            }
        }
        return obstacles;
    }
    
}
