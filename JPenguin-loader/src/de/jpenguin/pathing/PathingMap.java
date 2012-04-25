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
 * @author Karsten
 */
public class PathingMap implements Savable {
    
    protected ArrayList<SubPathingMap> subMaps;

    private int layerList[];
    
    public PathingMap(int width, int height, int size)
    {
        layerList = new int[6];
        
        layerList[0] = 1;
        layerList[1] = 2;
        layerList[2] = 4;
        layerList[3] = 8;
        layerList[4] = 16;
        layerList[5] = 32;

        subMaps = new ArrayList();

        //Ground(0)
        subMaps.add(new SubPathingMap(size,size,size,1,false));
        //Building(1)
        subMaps.add(new SubPathingMap(size,size,size,2,false));
        //Air(2)
        subMaps.add(new SubPathingMap(size,size,size,2,false));
        //Water(3)
        subMaps.add(new SubPathingMap(size,size,size,1,false));
    }
    
    public PathingMap(){}
    
    
    public void loadGame()
    {
        for(int i=0;i<subMaps.size();i++)
        {
            subMaps.get(i).loadGame();
        }
    }
    
    
    public SubPathingMap getSubMap(PathingMapName map)
    {
        return subMaps.get(map.getValue());
    }
    
    
    public boolean hasSpace( float x, float y,int w, int h, PathingMapName map)
    {
        return subMaps.get(map.getValue()).hasSpace(x, y, w, h);
    }
    
    public void setSpace( float x, float y,int w, int h, PathingMapName map, PathingLayer layer, boolean blocked)
    {
        subMaps.get(map.getValue()).setBlocked(x, y, w, h, blocked, layer, layerList);
    }
    
    public void setSpace( float x, float y,int r, PathingMapName map, PathingLayer layer, boolean blocked)
    {
        subMaps.get(map.getValue()).setBlocked(x, y, r, blocked, layer, layerList);
    }
    
    /*
    
    public boolean hasSpaceDirect(int x, int y, PathingMapName map)
    {
        return subMaps.get(map.getValue()).hasSpaceDirect(x, y);
    }
     * 
     */
    
   
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        
        capsule.writeSavableArrayList(subMaps, "subMaps", null);
        capsule.write(layerList, "layerList", null);
        
    }
 
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);

        subMaps = capsule.readSavableArrayList("subMaps", null);
        layerList = capsule.readIntArray("layerList", null);
        
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
  

