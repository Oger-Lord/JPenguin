/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.editor.pathing2;

/**
 *
 * @author Karsten
 */
public class EditorPathingMap extends PathingMap {
    
    public boolean hasSpace(float x, float y,PathingLayer layer, int size,PathingMapName ... maps)
    {
        return hasSpaceInt(convertX(x), convertY(y),layer,size,maps);
    }
    
    public boolean hasSpaceInt(int x, int y, PathingLayer layer,int size,PathingMapName ... maps)
    {
        for(int mx=x-size/2;mx<x+size/2+size%2;mx++)
        {
            for(int my=y-size/2+1;my<y+size/2+1+size%2;my++)
            {
                if(mx>=0 && mx < width && my>=0 && my < height)
                {
                    if(getValue(mx,my,layer,maps) == 0)
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
    
    public void setSpace(PathingMapName subMap, PathingLayer layer,float x, float y, int size, boolean blocked)
    {
        if(blocked)
        {
            setSpaceInt(subMap, layer, convertX(x), convertY(y),size,0);
        }else{
            setSpaceInt(subMap, layer, convertX(x), convertY(y),size,3);
        }
        
    }
    
    public void setSpaceInt(PathingMapName subMap, PathingLayer layer,int x, int y, int size, int value)
    {
        for(int mx=x-size/2;mx<x+size/2+size%2;mx++)
        {
            for(int my=y-size/2+1;my<y+size/2+1+size%2;my++)
            {
                if(mx>=0 && mx < width && my>=0 && my < height)
                {
                    setValue(mx, my, layer,subMap, value);
                }
            }
        }
    }
}
