/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.pathing;

import com.jme3.export.*;
import java.io.IOException;
/**
 *
 * @author Oger-Lord
 */
public class SubPathingMap implements Savable {
    
    private PathingGrid pathingGrid;
    private float scale;
    private SearchNode[][] searchArray;
    
    private boolean searchIsActive=false;
    
    
    //width,height = size of the field
    //width*scale * height*scale = number of cells
    public SubPathingMap(int width, int height, int maxUnitScale,float scale)
    {
        this.scale=scale;
        pathingGrid = new PathingGrid((int)(width*scale),(int)(height*scale),(int)(maxUnitScale*scale+0.75), false);
    }
    
    public SubPathingMap()
    {
        
    }
    
    public void activateUnitPathfinding()
    {
        pathingGrid.gameLoad();
        
        searchArray = new SearchNode[pathingGrid.getWidth()][pathingGrid.getHeight()];
        for(int x=0;x<pathingGrid.getWidth();x++)
        {
            for(int y=0;y<pathingGrid.getHeight();y++)
            {
                 if(pathingGrid.hasSpace(x, y))
                 {
                     searchArray[x][y] = new SearchNode(x,y);
                 }
            }
        }
        searchIsActive=true;
    }
    
    public boolean hasSpace(float x, float y, int w, int h)
    {
        return pathingGrid.hasSpace(convertX(x)-(int)(w/2*scale),convertY(y)-(int)(h/2*scale),(int)(w*scale),(int)(h*scale));
    }
    
    public void setBlocked(float x, float y,int w, int h, boolean block, int layer)
    {
        int startX =convertX(x)-(int)(w/2*scale);
        int startY=convertY(y)-(int)(h/2*scale);
        
        pathingGrid.setArea(startX,startY, (int)(w*scale), (int)(h*scale), block, layer);
        
        if(searchIsActive)
        {
            refreshNodes(startX, startY,(int)(w*scale), (int)( h*scale));
        }
    }
    
    public void setBlocked(float x, float y,int r, boolean block, int layer)
    {
        pathingGrid.setAreaCircle(convertX(x), convertY(y), (int)(r*scale), block, layer);
    
        if(searchIsActive)
        {
            refreshNodes(convertX(x)-(int)(r*scale), convertY(y)-(int)(r*scale), (int)(r*scale*2),  (int)(r*scale*2));
        }
    }
    
    
    private void refreshNodes(int startX, int startY, int w, int h)
    {

            for(int x=startX;x<startX+w*scale;x++)
            {
                for(int y=startY;y<startY+h*scale;y++)
                {
                    if(x<pathingGrid.getWidth() && x >= 0 && y < pathingGrid.getHeight() && y >= 0)
                    {
                         if(pathingGrid.hasSpace(x, y) && searchArray[x][y]==null)
                         {
                            searchArray[x][y] = new SearchNode(x,y);
                         }else if(pathingGrid.hasSpace(x, y) == false)
                         {
                            searchArray[x][y] = null;
                         }
                    }
                }
            }
    }
    
    public void setSpaceDirect(int x, int y,boolean block,int layer)
    {
        pathingGrid.setValue(x, y, block, layer);
    }
    
    public boolean hasSpaceDirect(int x, int y)
    {
        return pathingGrid.hasSpace((int)(x), (int)(y));
    }
    
    public boolean hasSpaceDirect(int x, int y, int unitSize)
    {
        return pathingGrid.hasSpace((int)(x), (int)(y),(int)(unitSize*scale+0.75f));
    }
    
    
    public int convertX(float x)
    {
        return (int)((x*scale+pathingGrid.getWidth()/2f)+0.49);
    }
    
    public int convertY(float y)
    {
        return (int)((y*scale+pathingGrid.getHeight()/2f)+0.49);
    }

    public float convertIntX(int x)
    {
        return (float)((x+0.5)-pathingGrid.getWidth()/2)/scale;
    }
     
    public float convertIntY(int y)
    {
        return (float)((y+0.5)-pathingGrid.getHeight()/2)/scale;
    }
    
    
    
    public int getWidth()
    {
        return pathingGrid.getWidth();
    }
    
    public int getHeight()
    {
        return pathingGrid.getHeight();
    }
    
    
    public SearchNode getSearchPoint(int x, int y)
    {
        return searchArray[x][y];
    }
    
    /*
    public void resetSearchPoints()
    {
        for(int x=0;x<pathingGrid.getWidth();x++)
        {
            for(int y=0;y<pathingGrid.getHeight();y++)
            {
                 if(searchArray[x][y] != null)
                 {
                     searchArray[x][y].reset();
                 }
            }
        }
    }
     * 
     */
    
    public boolean isUnitPathfinding()
    {
        return searchIsActive;
    }


    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        
        capsule.write(scale,"scale",1);
        capsule.write(pathingGrid,"pathingGrid",null);
    }

    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        
        scale = capsule.readFloat("scale",1);
        pathingGrid = (PathingGrid)capsule.readSavable("pathingGrid",null);
    }

    /**
     * @return the scale
     */
    public float getScale() {
        return scale;
    }
    
}
