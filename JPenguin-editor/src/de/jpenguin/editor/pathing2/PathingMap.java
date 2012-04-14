/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.editor.pathing2;

import com.jme3.export.*;
import com.jme3.export.binary.*;

import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetKey;

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.File;

import java.util.HashMap;
import java.util.Map;

import java.io.IOException;



    
                
/**
 *
 * @author Karsten
 */
public class PathingMap implements Savable {
    
    protected int size;
    protected int width,height;
    protected HashMap<PathingMapName,SubPathingMap> subMaps;

    public PathingMap(int width, int height, int size)
    {
        this.size=size;
        this.width=width;
        this.height=height;
        
        subMaps = new HashMap();
        
        for(PathingMapName test : PathingMapName.values()) {
		subMaps.put(test, new SubPathingMap(this,size));
	}
    }
    
    public PathingMap(){}
    
    
    
    public int getValue( int x, int y, PathingLayer pt,PathingMapName ... maps)
    {
        int returnValue=3;
        int newValue;
        
        for(int i=0;i<maps.length;i++)
        {
            newValue = subMaps.get(maps[i]).getValue(x,y,pt);
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
    
    
    public void setValue( int x, int y, PathingLayer pt,PathingMapName map, int value)
    {
        subMaps.get(map).setValue(x, y, pt, value);
    } 
    
    public int convertX(float x)
    {
        return (int)((x+0.5+size/2f)/size*width);
    }
    
    public int convertY(float y)
    {
        return (int)((y+0.5+size/2f)/size*height);
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
        
        capsule.writeSavableMap((Map)subMaps, "subMaps", null); 
    }
 
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        
        size = capsule.readInt("size",1);
        width = capsule.readInt("width",1);
        height = capsule.readInt("height",1);
        
        subMaps= (HashMap)capsule.readSavableMap("subMaps", null);
    }
    
}
