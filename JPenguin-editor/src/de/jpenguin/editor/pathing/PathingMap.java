/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.editor.pathing;

import com.jme3.export.*;
import java.io.IOException;

import de.jpenguin.loader.PathingMapData;
/**
 *
 * @author Karsten
 */
public class PathingMap extends PathingMapData {
    
    /*
    private int size;
    private int width,height;
    private int pathingMap[][];
     * 
     */

    
    public PathingMap(int width, int height, int size)
    {
        this.size=size;
        this.width=width;
        this.height=height;
        pathingMap = new int[width][height];
    }
    
    public PathingMap(PathingMapData pmd)
    {
        this.size = pmd.getSize();
        this.width=pmd.getWidth();
        this.height=pmd.getHeight();
        pathingMap = new int[width][height];
        
        for(int x=0;x<width;x++)
        {
            for(int y=0;y<height;y++)
            {
                pathingMap[x][y] = pmd.getValue(x, y);
            }
        }
    }
    
    public void badLine()
    {
        for(int i=0;i<width;i++)
        {
            if(i != 5 && i != 6)
            {
                pathingMap[i][150] = 1;
            }
        }
    }
    
    public int convertX(float x)
    {
        return (int)((x+0.5+size/2f)/size*width);
    }
    
    public int convertY(float y)
    {
        return (int)((y+0.5+size/2f)/size*height);
    }
    
    public boolean hasSpace(float x, float y, int size)
    {
        return hasSpaceInt(convertX(x), convertY(y),size);
    }
    
    public boolean hasSpaceInt(int x, int y, int size)
    {
        for(int mx=x-size/2;mx<x+size/2+size%2;mx++)
        {
            for(int my=y-size/2+1;my<y+size/2+1+size%2;my++)
            {
                if(mx>=0 && mx < width && my>=0 && my < height)
                {
                    if(pathingMap[mx][my] == 1)
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
    
    public void setSpace(float x, float y, int size, int value)
    {
        setSpaceInt(convertX(x), convertY(y),size,value);
    }
    
    public void setSpaceInt(int x, int y, int size, int value)
    {
        for(int mx=x-size/2;mx<x+size/2+size%2;mx++)
        {
            for(int my=y-size/2+1;my<y+size/2+1+size%2;my++)
            {
                if(mx>=0 && mx < width && my>=0 && my < height)
                {
                    pathingMap[mx][my] = value;
                }
            }
        }
    }

    
    /*
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
     *      * 
     */
    
}
