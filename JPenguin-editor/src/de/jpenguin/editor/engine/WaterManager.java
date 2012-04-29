/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.editor.engine;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.export.Savable;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.export.binary.BinaryImporter;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.water.WaterFilter;
import com.jme3.post.FilterPostProcessor;
import com.jme3.terrain.Terrain;
import com.jme3.texture.Texture2D;
import de.jpenguin.editor.Editor;
import de.jpenguin.pathing.PathingMap;
import de.jpenguin.pathing.SubPathingMap;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
/**
 *
 * @author Karsten
 */
public class WaterManager extends AbstractAppState{
    
        
    private FilterPostProcessor postWater;
    private WaterFilter water;
    private EditorApplication editorApp;
    
    private boolean waterEnabled=false;
    private float waterHeight=0;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.editorApp=(EditorApplication)app;

        postWater = new FilterPostProcessor(editorApp.getAssetManager());
    }
    
    public void hide(boolean b)
    {
        if(waterEnabled)
        {
            if(b)
            {
                editorApp.getViewPort().removeProcessor(postWater);
            }else{
                editorApp.getViewPort().addProcessor(postWater);
            }
        }
    }
    
    public void setWater(boolean enabled,float height)
    {
        if(waterEnabled == enabled && height == waterHeight)
            return;
            
        water.setWaterHeight(waterHeight);
        waterHeight=height;
        waterEnabled = enabled;
             
            if(enabled)
            {
                editorApp.getViewPort().addProcessor(postWater);
            }else{
                editorApp.getViewPort().removeProcessor(postWater);
            }
            
            
            PathingMap pathingMap = editorApp.getPathingManager().getPathingMap();
            
            Terrain terrain = editorApp.getTerrainManager().getTerrain();
            
            
            for(int x=0;x<terrain.getTerrainSize()-1;x++)
            {
                for(int y=0;y<terrain.getTerrainSize()-1;y++)
                {
                    float fx=x-(terrain.getTerrainSize()-1)/2;
                    float fy=y-(terrain.getTerrainSize()-1)/2;
                    
                    updateLocation(terrain,pathingMap,fx,fy);
                }
            }

        
    }
    
    
    
    
    
    private void updateLocation(Terrain terrain,PathingMap pathingMap,float fx, float fy)
    {
         if(waterEnabled==false || terrain.getHeightmapHeight(new Vector2f(fx,fy)) > waterHeight)
         {
                pathingMap.setSpace(fx,fy, 1, 1, PathingMap.MapGround,PathingMap.LayerWater,false);
                pathingMap.setSpace(fx,fy, 1, 1, PathingMap.MapBuilding,PathingMap.LayerWater,false);
                pathingMap.setSpace(fx,fy, 1, 1, PathingMap.MapWater,PathingMap.LayerWater,true);
         }else{
                 pathingMap.setSpace(fx,fy, 1, 1, PathingMap.MapGround,PathingMap.LayerWater,true);
                 pathingMap.setSpace(fx,fy, 1, 1, PathingMap.MapBuilding,PathingMap.LayerWater,true);
                 pathingMap.setSpace(fx,fy, 1, 1, PathingMap.MapWater,PathingMap.LayerWater,false);
         }
    }
    
    
    public void changeHeight(Vector3f loc, float radius)
    {
        
        if(waterEnabled==false)
            return;
            
        PathingMap pathingMap = editorApp.getPathingManager().getPathingMap();   
        Terrain terrain = editorApp.getTerrainManager().getTerrain();
        
        
          for(float x=loc.getX()-radius;x<loc.getX()+radius;x++)
          {
              for(float y=loc.getZ()-radius;y<loc.getZ()+radius;y++)
              {
                   updateLocation(terrain,pathingMap,x,y);
              }
          }  
         
    }
    
    
    public void newWater()
    {
        postWater.removeAllFilters();
        
        water = new WaterFilter(editorApp.getRootNode(), new Vector3f(-1f, -1f, -1f));
        water.setWaveScale(0.003f);
        water.setMaxAmplitude(2f);
        water.setFoamExistence(new Vector3f(1f, 4, 0.5f));
        water.setFoamTexture((Texture2D) editorApp.getAssetManager().loadTexture("Common/MatDefs/Water/Textures/foam2.jpg"));
        //water.setNormalScale(0.5f);

        //water.setRefractionConstant(0.25f);
        water.setRefractionStrength(0.2f);
        //water.setFoamHardness(0.6f);

        water.setWaterHeight(0);
        
        postWater.addFilter(water);
        
        waterEnabled=false;
        waterHeight=0;
        
        
        //No Water -> All Water fields need to be blocked
        PathingMap pathingMap = editorApp.getPathingManager().getPathingMap(); 
        SubPathingMap subMap = pathingMap.getSubMap(PathingMap.MapWater);
        for(int x=0;x<subMap.getWidth();x++)
        {
            for(int y=0;y<subMap.getHeight();y++)
            {
                subMap.setSpaceDirect(x, y, true, PathingMap.LayerWater);
            }
        }
    }
    
    
    
    
    public void save()
    {
        try{
            
            if(waterEnabled)
            {
                FileOutputStream fos = new FileOutputStream(new File(Editor.getMapPath()+"water.jme"));

                BinaryExporter.getInstance().save((Savable)water, new BufferedOutputStream(fos));

                fos.flush();
            }else{
                File f = new File(Editor.getMapPath()+"water.jme");
                f.delete();
            }
            
        }catch(Exception e){
            System.out.println("Error saving Water:" + e.getMessage());
        }
    }
    
    public void load()
    {
        try{
            // remove the existing terrain and detach it from the root node.
            postWater.removeAllFilters();

            File f = new File(Editor.getMapPath()+"water.jme");
            
            if(f.exists())
            {
                FileInputStream fis = new FileInputStream(f);
                BinaryImporter imp = BinaryImporter.getInstance();
                water = (WaterFilter)imp.load(fis);
            }else{
                newWater();
            }
            

        }catch(Exception e){}
       
    }

    public void click() {
        if(waterEnabled)
        {
            setWater(false,0);
        }else{
            setWater(true,0);
        }
    }
}
