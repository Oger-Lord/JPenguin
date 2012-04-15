/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.pathing;

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
    
    protected int size;
    protected int width,height;
    protected ArrayList<SubPathingMap> subMaps;

    public PathingMap(int width, int height, int size)
    {
        this.size=size;
        this.width=width;
        this.height=height;
        
        subMaps = new ArrayList();
        
        for(PathingMapName pNames : PathingMapName.values()) {
            
            SubPathingMap spm = new SubPathingMap();
            
            for(PathingLayer pLayer : PathingLayer.values()) {
                PathingGrid pg = new PathingGrid(width,height,size);
                spm.addLayer(pLayer, pg);
            }
            
            subMaps.add(spm);
                
	}
    }
    
    public PathingMap(){}
    
    public PathingField getPathingField(PathingLayer pt,PathingMapName map)
    {
        return subMaps.get(map.getValue()).getLayer(pt);
    }
    
    public ArrayList<PathingField> getPathingFields(PathingLayer pt,PathingMapName ... maps)
    {
        ArrayList<PathingField> al = new ArrayList();
        
        for(int i=0;i<maps.length;i++)
        {
            SubPathingMap submap = subMaps.get(maps[i].getValue());
            
            if(submap.getLayer(pt) != null)
            {
                al.add(submap.getLayer(pt));
            }
        }
        return al;
    }
    
    
    public int getValue( float x, float y, PathingLayer pt,PathingMapName ... maps)
    {
        int returnValue=3;
        int newValue;
        
        for(int i=0;i<maps.length;i++)
        {
            newValue = subMaps.get(maps[i].getValue()).getValue(x,y,pt);
            if(returnValue > newValue)
            {
                if(newValue==0)
                {
                    return newValue;
                }
                returnValue = newValue;
            }
        }
        
        return returnValue;
    }
    /*
    
    public void setValue( float x, float y, PathingLayer pt,PathingMapName map, int value)
    {
        subMaps.get(map.getValue()).setValue(x, y, pt, value);
    } 
     * 
     */
    
    public int convertX(float x)
    {
        return (int)((x+0.5+size/2f)/size*width);
    }
    
    public int convertY(float y)
    {
        return (int)((y+0.5+size/2f)/size*height);
    }
    
    public float convertIntX(int x)
    {
        return (float)(x+0.5)*(size/width)-size/2;
    }
     
    public float convertIntY(int y)
    {
        return (float)(y-0.5f)*(size/height)-size/2;
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
     

   
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        
        capsule.write(size,"size",0);
        capsule.write(width,"width",0);
        capsule.write(height,"height",0);
        
       // capsule.write(subMaps, "subMaps", null); 
        capsule.writeSavableArrayList(subMaps, "subMaps", null);
    }
 
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        
        size = capsule.readInt("size",1);
        width = capsule.readInt("width",1);
        height = capsule.readInt("height",1);
        
        subMaps = capsule.readSavableArrayList("subMaps", null);
        
        /*
        Savable[] sav = capsule.readSavableArray("subMaps", null);
        
        if(sav == null)
        {
            System.out.println("OMG SHIT ECT...");
        }else{
           sav = new PathingField[sav.length];
            
            for(int i=0;i<sav.length;i++)
            {
                if(sav[i] == null)
                {
                    System.out.println("NULL ALARM!!!..." +i);
                }else{
                    subMaps[i] = (SubPathingMap) sav[i];
                }

                
            }
         * 
         */
            
          //  pathingFields = (PathingField[])sav;
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
