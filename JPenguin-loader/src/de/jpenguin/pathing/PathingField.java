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
public class PathingField implements Savable {
    private int width;
    private int height;
    private int size;
    
    public PathingField(){
    }
    
    public PathingField(int width, int height, int size)
    {
        this.width=width;
        this.height=height;
        this.size=size;
    }
    
    public void setField(int x, int y, int value){};
    public int getField(int x, int y){return 3;};
    public int convertX(float x){return 0;};
    public int convertY(float x){return 0;};
    
   public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        
        capsule.write(getSize(),"size",0);
        capsule.write(getWidth(),"width",0);
        capsule.write(getHeight(),"height",0);
    }
 
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        
        size = capsule.readInt("size",1);
        width = capsule.readInt("width",1);
        height = capsule.readInt("height",1);
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }
}
