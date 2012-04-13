/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.loader;

import com.jme3.export.*;
import com.jme3.export.binary.*;

import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetKey;

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.File;

import java.io.IOException;


/**
 *
 * @author Karsten
 */
public class PathingMapData implements Savable {
    
    protected int size;
    protected int width,height;
    protected int pathingMap[][];
    

    public PathingMapData(int width, int height, int size)
    {
        this.size=size;
        this.width=width;
        this.height=height;
        pathingMap = new int[width][height];
    }
    
    public PathingMapData()
    {
        
    }
    
    public static void save(PathingMapData pathingMap, String mappath)
    {
        try{
            FileOutputStream fos = new FileOutputStream(new File(mappath+"pathingmap.jme"));
            BinaryExporter.getInstance().save(pathingMap, new BufferedOutputStream(fos));
        }catch(Exception e){System.out.println("Error Saving PathingData " + e.getMessage());}
    }
    
    public static PathingMapData load(AssetManager assetManager,String map)
    {
        BinaryImporter imp = BinaryImporter.getInstance();
        imp.setAssetManager(assetManager);

        try{
            InputStream fis = assetManager.locateAsset(new AssetKey("Scenes/"+map+"/pathingmap.jme")).openStream();
            return (PathingMapData) imp.load(new BufferedInputStream(fis));
        }catch(Exception e){
            System.out.println("Error loading player " + e.getMessage());
            return null;}
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
    
     public int getSize()
    {
        return size;
    }
        
    public  PathingMapData clone()
    {
        PathingMapData clone=new PathingMapData(width, height, size);
        clone.pathingMap =new int[width][height];
        for(int x=0;x<width;x++)
        {
            for(int y=0;y<height;y++)
            {
                clone.pathingMap[x][y] = pathingMap[x][y];
            }
        }
            
        return clone;
    }
   
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        
        capsule.write(size,"size",0);
        capsule.write(width,"width",0);
        capsule.write(height,"height",0);
        
        capsule.write(pathingMap, "pathingMap", null); 
    }
 
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        
        size = capsule.readInt("size",1);
        width = capsule.readInt("width",1);
        height = capsule.readInt("height",1);
        
        pathingMap = capsule.readIntArray2D("pathingMap", new int[width][height]);
    }
    
}
