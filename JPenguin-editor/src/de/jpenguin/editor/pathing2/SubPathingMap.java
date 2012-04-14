/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.editor.pathing2;

import com.jme3.export.*;
import java.io.IOException;
/**
 *
 * @author Karsten
 */
public class SubPathingMap implements Savable {
    
    private Field pathingFields[][];
    

    
    private class Field implements Savable
    {
        private int[] values;
        
        public Field(int size, int startValue)
        {
            values=new int[size];
            for(int i=0;i<size;i++)
                values[i] = startValue;
        }
        
        public Field(){}        
        public void write(JmeExporter ex) throws IOException {
            OutputCapsule capsule = ex.getCapsule(this);
        
            capsule.write(values, "values", null);
        }
 
        public void read(JmeImporter im) throws IOException {
            InputCapsule capsule = im.getCapsule(this);

            values = capsule.readIntArray("values", new int[4]);
        }
    }
    
    public SubPathingMap(PathingMap pathingMap, int layer)
    {
        pathingFields = new Field[pathingMap.getWidth()][pathingMap.getHeight()];
        
        for(int x=0;x<pathingMap.getWidth();x++)
        {
            for(int y=0;y<pathingMap.getHeight();y++)
            {
                pathingFields[x][y] = new Field(layer,3);
            }
        }
    }
    
    public SubPathingMap(){}
    
    public void setValue(int x, int y, PathingLayer pt, int value)
    {
        pathingFields[x][y].values[pt.getValue()] = value;
    }
    
    public int getValue(int x, int y, PathingLayer pt)
    {
        return pathingFields[x][y].values[pt.getValue()];
    }
    
    
    
    
    
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        
      //  capsule.write(PathingMap.SubPathingMapName.hallo, "name", null);
     //   capsule.write(name, "name", null);
        capsule.write(pathingFields, "pathingFields", null);
    }
 
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        
      //  capsule.readEnum("name", PathingMap.SubPathingMapName.class, null);
       // name = capsule.readString("name", null);
        pathingFields = (Field[][])capsule.readSavableArray2D("pathingFields", null);
    }
}
