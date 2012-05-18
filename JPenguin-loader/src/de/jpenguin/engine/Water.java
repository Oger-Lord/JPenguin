/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.engine;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.export.binary.BinaryImporter;
import com.jme3.export.xml.XMLExporter;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.terrain.Terrain;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import de.jpenguin.engine.MyWaterFilter;
import de.jpenguin.pathing.PathingMap;
import de.jpenguin.pathing.SubPathingMap;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
/**
 *
 * @author Karsten
 */
public class Water implements Savable{
    
    private FilterPostProcessor postWater;

    private Application app;
    private Terrain terrain;
    private PathingMap pathingMap;
    
    private ArrayList<WaterType> waterTypes;
    
    private int width, height;

    
    public Water(int width, int height){
        this.width=width;
        this.height=height;
        
        waterTypes = new ArrayList();
    }
    
    public Water(){
        
    }
    
    public void init(Application app, Terrain terrain, PathingMap pathingMap, Node rootNode)
    {
        this.app=app;
        this.terrain=terrain;
        this.pathingMap = pathingMap;
        postWater = new FilterPostProcessor(app.getAssetManager());
        app.getViewPort().addProcessor(postWater);
        
        if(waterTypes != null)
        {
            for(int i=0;i<waterTypes.size();i++)
            {
                waterTypes.get(i).setReflectionNode(rootNode);
                if(waterTypes.get(i).getFieldsEnabled()!=0)
                    postWater.addFilter(waterTypes.get(i).getWaterFilter());
            }
        }
    }
    
    
    public void setFogMap(Texture tex)
    {
        for(int i=0;i<waterTypes.size();i++)
            waterTypes.get(i).setFogMap(tex);
    }
    
    public void setRootNode(Node rootNode)
    {
        if(waterTypes != null)
        {
            for(int i=0;i<waterTypes.size();i++)
            {
                waterTypes.get(i).setReflectionNode(rootNode);
            }
        }
    }
    
    
    public void enableEffect(boolean b)
    {
         if(b)
         {
                app.getViewPort().removeProcessor(postWater);
         }else{
                app.getViewPort().addProcessor(postWater);
          }
    }
    

    //terrain changes
    public void changeTerrainHeight(Vector3f loc, float radius)
    {
        
        for(int i=0;i<waterTypes.size();i++)
        {
            
            if(waterTypes.get(i).getFieldsEnabled() != 0)
            {
              for(float x=loc.getX()-radius;x<loc.getX()+radius;x++)
              {
                  for(float y=loc.getZ()-radius;y<loc.getZ()+radius;y++)
                  {
                      if(waterTypes.get(i).isAtThisPosition(x,y,width,height))
                        waterTypes.get(i).updateLocation(terrain,pathingMap,x,y,width,height);
                  }
              }   
            }
        }
    }
    
    
    public void drawWaterType(int id, float fx, float fy,float r,boolean draw)
    {
        int sx=convertX(fx);
        int sy=convertY(fy);
        int radius = (int)(r/4+0.5);
        
        for(int x=sx-radius;x<sx+radius;x++)
        {
            for(int y=sy-radius;y<sy+radius;y++)
            {
                if(Math.sqrt(Math.pow(sx-x,2)+Math.pow(sy-y,2))<=radius)
                {
                
                    if(x>=0 && x < width/4 && y>=0 && y < height/4)
                    {
                        for(int i=0;i<waterTypes.size();i++)
                        {
                            if(draw && id==i)
                            {
                                waterTypes.get(i).changeEnabledField(x, y, true, terrain, pathingMap, width, height);
                            }else{
                                waterTypes.get(i).changeEnabledField(x, y, false, terrain, pathingMap, width, height);
                            }
                        }
                    }
                }
            }
        }
    }
    
    
    public void removeWaterType(int id)
    {
        WaterType wt = waterTypes.remove(id);
        
        if(wt.getFieldsEnabled() == 0)
            return;
        
        for(int x=0;x<width/4;x++)
        {
            for(int y=0;y<height/4;y++)
            {
                if(wt.isWaterAtPos(x, y))
                {
                    wt.changeEnabledField(x, y, false,terrain, pathingMap, width, height);
                }
            }
        }
        
        postWater.removeFilter(wt.getWaterFilter());
    }
    
    
    public Vector3f collision(Ray ray)
    {
        Vector3f firstPoint=null;
        float shortestDistance = -1;
        
        for(int i=0;i<waterTypes.size();i++)
        {
            WaterType wt = waterTypes.get(i);
            float a = (wt.getWaterHeight()-ray.getOrigin().y)/ray.getDirection().y;
            
            if(a > 0)
            {
                float x = ray.getOrigin().x + a*ray.getDirection().x;
                float y = ray.getOrigin().z + a*ray.getDirection().z;
                
                if(wt.isAtThisPosition(x, y, width, height))
                {
                    Vector3f v3f= new Vector3f(x,wt.getWaterHeight(),y);
                    
                    float shortest=v3f.distance(ray.getOrigin());
                    
                    if(shortestDistance == -1 || shortestDistance > shortest)
                    {
                        firstPoint = v3f;
                        shortestDistance = shortest;
                    }
                }
            }
        }
        
        return firstPoint;
    }
    
    public float getHeight(float x, float y)
    {
        for(int i=0;i<waterTypes.size();i++)
        {
            WaterType wt = waterTypes.get(i);
            if(wt.isAtThisPosition(x, y, width, height))
                return wt.getWaterHeight();
                
        }
        return 0;
    }
    
    
    public void clear()
    {
        while(waterTypes.isEmpty()==false)
            removeWaterType(0);
    }
    
    public void addWaterType(WaterType wt)
    {
        waterTypes.add(wt);
        postWater.addFilter(wt.getWaterFilter());
    }
    
    public WaterType getWaterType(int id)
    {
        return waterTypes.get(id);
    }
    
    public int convertX(float x)
    {
        return (int)(((x+width/2)/4)+0.5);
    }
    
    public int convertY(float y)
    {
        return (int)(((y+height/2)/4)+0.5);
    }
    
    
    public FilterPostProcessor getFilterPostProcessor()
    {
        return postWater;
    }
    
    
    public String[] getWaterNames()
    {
        String[] s= new String[waterTypes.size()];
        
        for(int i=0;i<waterTypes.size();i++)
            s[i] = waterTypes.get(i).getName();
        
        return s;
    }
    

    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        
        capsule.writeSavableArrayList(waterTypes, "waterTypes",  null);
        
        capsule.write(width, "width",  1);
        capsule.write(height, "height",  1);
    }

    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        
        width = capsule.readInt("width",1);
        height = capsule.readInt("height",1);
        
        waterTypes = capsule.readSavableArrayList("waterTypes",null);
        
    }

    /**
     * @param pathingMap the pathingMap to set
     */
    public void setPathingMap(PathingMap pathingMap) {
        this.pathingMap = pathingMap;
    }
    
}
