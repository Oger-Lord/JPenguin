/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.pathing;

import com.jme3.export.*;
import java.io.IOException;
/**
 *
 * @author Karsten
 */
public class PathingGrid extends PathingField implements Savable{
    
    private int fields[][];
    
    public PathingGrid(){}
    
    public PathingGrid(int width, int height, int size)
    {
        super(width,height,size);
        
        fields = new int[width][height];
        
        for(int x=0;x<width;x++)
        {
            for(int y=0;y<height;y++)
            {
                fields[x][y] = 3;
            }
        }
    }

    @Override
    public void setField(int x, int y, int value) {
        fields[x][y] = value;
    }
    
    @Override
    public int getField(int x, int y) {
        return fields[x][y];
    }
    
    @Override
    public int convertX(float x)
    {
        return (int)((x+0.5+getSize()/2f)/getSize()*getWidth());
    }
    
    
    @Override
    public int convertY(float y)
    {
        return (int)((y+0.5+getSize()/2f)/getSize()*getHeight());
    }
    
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        
        OutputCapsule capsule = ex.getCapsule(this);
        
        capsule.write(fields, "fields", null); 
    }
 
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        
        InputCapsule capsule = im.getCapsule(this);
        
        fields= capsule.readIntArray2D("fields", null);
    }
    
}
