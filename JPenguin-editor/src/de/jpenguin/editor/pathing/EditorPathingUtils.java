/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.editor.pathing;

import de.jpenguin.pathing.PathingMap;
import de.jpenguin.pathing.PathingLayer;
import de.jpenguin.pathing.PathingMapName;
import de.jpenguin.pathing.PathingField;

import java.util.ArrayList;
/**
 *
 * @author Karsten
 */
public class EditorPathingUtils{
    
    
    public static boolean hasSpace(PathingMap pathingMap,float x, float y,PathingLayer layer, int size,PathingMapName ... maps)
    {
        ArrayList<PathingField> fieldList = pathingMap.getPathingFields(layer, maps);
        
        for(int i=0;i<fieldList.size();i++)
        {
            if(hasSpace(fieldList.get(i),x,y,size) == false)
            {
                return false;
            }
        }
        
        return true;
    }
    
    public static boolean hasSpace(PathingField pathingField,float x, float y, int size)
    {
        return hasSpaceInt(pathingField,pathingField.convertX(x), pathingField.convertY(y),size);
    }
    
    public static boolean hasSpaceInt(PathingField pathingField,int x, int y, int size)
    {
        for(int mx=x-size/2;mx<x+size/2+size%2;mx++)
        {
            for(int my=y-size/2+1;my<y+size/2+1+size%2;my++)
            {
                if(mx>=0 && mx < pathingField.getWidth() && my>=0 && my < pathingField.getHeight())
                {
                    if(pathingField.getField(mx,my) == 0)
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
    

    public static void setSpace(PathingMap pathingMap,PathingMapName subMap, PathingLayer layer,float x, float y, int size, boolean blocked)
    {
        PathingField pf =pathingMap.getPathingField(layer, subMap);
        if(blocked)
        {
            setSpaceInt(pf, pf.convertX(x), pf.convertY(y),size,0);
        }else{
            setSpaceInt(pf, pf.convertX(x), pf.convertY(y),size,3);
        }
        
    }
    
    public static void setSpaceInt(PathingField pathingField,int x, int y, int size, int value)
    {
        for(int mx=x-size/2;mx<x+size/2+size%2;mx++)
        {
            for(int my=y-size/2+1;my<y+size/2+1+size%2;my++)
            {
                if(mx>=0 && mx < pathingField.getWidth() && my>=0 && my < pathingField.getHeight())
                {
                    pathingField.setField(mx, my, value);
                }
            }
        }
    }
}
