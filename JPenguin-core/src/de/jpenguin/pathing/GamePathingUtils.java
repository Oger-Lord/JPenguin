/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.pathing;

import de.jpenguin.pathing.SubPathingMap;
import de.jpenguin.pathing.PathingMap;
import de.jpenguin.pathing.PathingMapName;
import de.jpenguin.pathing.PathingLayer;
import de.jpenguin.pathing.PathingField;
import java.util.Enumeration;
/**
 *
 * @author Karsten
 */
public class GamePathingUtils {
        
    public static void convert(PathingMap pathingMap)
    {

        for(PathingMapName map : PathingMapName.values())
        {
        
          for(PathingLayer layer : PathingLayer.values())
          {
              PathingField pfield = pathingMap.getPathingField(layer,map);
              
              for(int x=0;x<pfield.getWidth();x++)
              {
                  for(int y=0;y<pfield.getHeight();y++)
                  {
                      convertField(pfield,x,y);
                  }
              }
              
          }
        }
    }
    
    public static void convertField(PathingField pathingField, int x, int y)
    {
        int value=pathingField.getField(x, y);
        
        if(value==0){return;}
        
        int newvalue=value;
        
        for(int mx=x-1;x<mx+1;mx++)
        {
            for(int my=y-1;y<my+1;my++)
            {
                if(mx != x && my != y)
                {
                    
                    if(mx < 0 || (my < 0 && mx != pathingField.getWidth()))
                    {
                        newvalue=1;
                    }else if(mx == pathingField.getWidth()|| my == pathingField.getHeight()){
                        newvalue=2;
                    }else{
                        if(pathingField.getField(x, y) == 0)
                        {
                            if(mx < x || (my<y && mx != x+1)){
                                newvalue=1;
                            } else{
                                newvalue=2;
                            }
                        }
                    }
                    
                    if(newvalue < value)
                    {
                        value=newvalue;
                        if(newvalue==1)
                        {
                            pathingField.setField(x, y, 1);
                            return;
                        }
                    }
                }
            }
        }
        
        pathingField.setField(x, y, newvalue);
        return;
    }
    
}
