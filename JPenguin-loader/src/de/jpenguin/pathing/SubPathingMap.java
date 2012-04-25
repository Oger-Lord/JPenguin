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
    
    private PathingGrid pathingGrid;
    private int size;
    private int proportion;
    
    public SubPathingMap(int width, int height, int size,int proportion, boolean calculate)
    {
        this.size=size;
        this.proportion=proportion;
        pathingGrid = new PathingGrid(width/proportion,height/proportion, calculate);
    }
    
    public SubPathingMap()
    {
        
    }
    
    public void loadGame()
    {
        pathingGrid.gameLoad();
    }
    
    public boolean hasSpace(float x, float y, int w, int h)
    {
        return pathingGrid.hasSpace(convertX(x)-(w/(2*proportion)),convertY(y)-(h/(2*proportion)),w/getProportion(),h/getProportion());
    }
    
    public void setBlocked(float x, float y,int w, int h, boolean block, PathingLayer layer,int layerList[])
    {
        pathingGrid.setArea(convertX(x)-(w/(2*proportion)), convertY(y)-(h/(2*proportion)), w/getProportion(), h/getProportion(), block, layer.getValue(),layerList);
    }
    
    public void setBlocked(float x, float y,int r, boolean block, PathingLayer layer,int layerList[])
    {
        pathingGrid.setAreaCircle(convertX(x), convertY(y), r/getProportion(), block, layer.getValue(),layerList);
    }
    
    public boolean hasSpaceDirect(int x, int y)
    {
        return pathingGrid.hasSpace(x/getProportion(), y/getProportion());
    }
    
    public boolean hasSpaceDirect(int x, int y, int unitSize)
    {
        int value  = pathingGrid.getValue(x/getProportion(), y/getProportion());
        
        if(value <unitSize)
        {
            return false;
        }
        return true;
    }
    
    
    public int convertX(float x)
    {
      //  System.out.println(x + " " + size +" " + pathingGrid.getWidth() + " " + (int)((x+0.5+size/2f)/size*pathingGrid.getWidth()));
        
        return (int)((x+0.5+size/2f)/size*pathingGrid.getWidth());
    }
    
    public int convertY(float y)
    {
        return (int)((y+0.5+size/2f)/size*pathingGrid.getHeight());
    }
    
    

    public float convertIntX(int x)
    {
        return (float)(x+0.5)*(size/pathingGrid.getWidth())-size/2;
    }
     
    public float convertIntY(int y)
    {
        return (float)(y-0.5f)*(size/pathingGrid.getHeight())-size/2;
    }
    
    
    
    public int getWidth()
    {
        return pathingGrid.getWidth();
    }
    
    public int getHeight()
    {
        return pathingGrid.getHeight();
    }
    
    


    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        
        capsule.write(size,"size",1);
        
        capsule.write(getProportion(),"proportion",1);
        capsule.write(pathingGrid,"pathingGrid",null);
    }

    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        
        size = capsule.readInt("size",1);
        
        proportion = capsule.readInt("proportion",1);
        pathingGrid = (PathingGrid)capsule.readSavable("pathingGrid",null);
    }

    /**
     * @return the proportion
     */
    public int getProportion() {
        return proportion;
    }
    
}
