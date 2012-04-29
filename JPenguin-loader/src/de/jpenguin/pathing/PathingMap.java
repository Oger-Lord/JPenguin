/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.pathing;

/**
 *
 * @author Karsten
 */
import com.jme3.export.*;
import com.jme3.export.binary.*;
import com.jme3.export.xml.*;

import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetKey;

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.File;

import java.util.ArrayList;

import java.io.IOException;


                
/**
 *
 * @author Oger-Lord
 */
public class PathingMap implements Savable {
    
    protected ArrayList<SubPathingMap> subMaps;
    
    public static final int LayerDraw = 0;
    public static final int LayerUnit = 1;
    public static final int LayerWater = 2;
    public static final int LayerEditor = 3;
    
    
    public static final int MapGround = 0;
    public static final int MapAir = 1;
    public static final int MapBuilding= 2;
    public static final int MapWater = 3;
    
    public PathingMap()
    {
        
        subMaps = new ArrayList();
    }
    
    
    public void addSubPathingMap(SubPathingMap subMap)
    {
        subMaps.add(subMap);
    }
    
    
    public void activateUnitPathfinding()
    {
        for(int i=0;i<subMaps.size();i++)
        {
            subMaps.get(i).activateUnitPathfinding();
        }
    }
    
    
    public SubPathingMap getSubMap(int map)
    {
        return subMaps.get(map);
    }
    
    
    public boolean hasSpace( float x, float y,int w, int h, int map)
    {
        return subMaps.get(map).hasSpace(x, y, w, h);
    }
    
    public void setSpace( float x, float y,int w, int h, int map, int layer, boolean blocked)
    {
        subMaps.get(map).setBlocked(x, y, w, h, blocked, layer);
    }
    
    public void setSpace( float x, float y,int r, int map, int layer, boolean blocked)
    {
        subMaps.get(map).setBlocked(x, y, r, blocked, layer);
    }
    
    /*
    
    public boolean hasSpaceDirect(int x, int y, int map)
    {
        return subMaps.get(map).hasSpaceDirect(x, y);
    }
     * 
     */
    
   
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        
        capsule.writeSavableArrayList(subMaps, "subMaps", null);
        
    }
 
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);

        subMaps = capsule.readSavableArrayList("subMaps", null);
        
    }
    
        
    
    public static void save(PathingMap pathingMap, String mappath)
    {
        try{
           // PathingMap exportMap=pathingMap.clone();
            
            FileOutputStream fos = new FileOutputStream(new File(mappath+"pathingmap.jme"));
            BinaryExporter.getInstance().save(pathingMap, new BufferedOutputStream(fos));
            
            fos = new FileOutputStream(new File(mappath+"pathingmap.xml"));
            XMLExporter.getInstance().save(pathingMap, new BufferedOutputStream(fos));
            
        }catch(Exception e){System.out.println("Error Saving PathingMap; " + e.getMessage());}
    }
    
    public static PathingMap load(AssetManager assetManager,String map)
    {
        BinaryImporter imp = BinaryImporter.getInstance();
        imp.setAssetManager(assetManager);
        
        XMLImporter ximp = XMLImporter.getInstance();
        ximp.setAssetManager(assetManager);

        try{
            InputStream fis = assetManager.locateAsset(new AssetKey("Scenes/"+map+"/pathingmap.jme")).openStream();
            return (PathingMap) imp.load(new BufferedInputStream(fis));
        }catch(Exception e){
            System.out.println("Error loading pathing map: " + e.getMessage());
            return null;}
    }
    
}
  

