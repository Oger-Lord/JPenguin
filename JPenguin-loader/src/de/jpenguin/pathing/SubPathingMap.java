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
public class SubPathingMap implements Savable {
    
    private PathingField pathingFields[];
    
    
    public SubPathingMap()
    {
        pathingFields = new PathingField[PathingLayer.values().length];
    }
    
    public void addLayer(PathingLayer layer, PathingField field)
    {
        pathingFields[layer.getValue()] = field;
    }
    
     public PathingField getLayer(PathingLayer layer)
    {
        return pathingFields[layer.getValue()];
    }
    
    /*
    public void setValue(float x, float y, PathingLayer layer, int value)
    {
        PathingField f = pathingFields[layer.getValue()];
        
        if(f != null)
        {
            f.setField(x, y, value);
        }
    }
   
      * 
      */
    
    public int getValue(float x, float y, PathingLayer layer)
    {
        PathingField f = pathingFields[layer.getValue()];
        
        if(f == null)
        {
            return 3; //empty
        }
        
        return f.getField(f.convertX(x), f.convertY(y));
    }

    
    
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        

        capsule.write(pathingFields, "pathingFields", null);
    }
 
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        
        Savable[] sav = capsule.readSavableArray("pathingFields", null);
        
        System.out.println("Hier war ich!....");
        
        if(sav == null)
        {
            System.out.println("OMG SHIT ECT...");
        }else{
            pathingFields = new PathingField[sav.length];
            
            for(int i=0;i<sav.length;i++)
            {
                if(sav[i] == null)
                {
                    System.out.println("NULL ALARM!!!...");
                }

                pathingFields[i] = (PathingField) sav[i];
            }
            
          //  pathingFields = (PathingField[])sav;
        }
        
    }
}
