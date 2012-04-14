/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.editor.pathing2;

import java.util.Enumeration;
/**
 *
 * @author Karsten
 */
public class GamePathingMap extends PathingMap {
        
    public void convert()
    {
        Enumeration e = (Enumeration)subMaps.values();
   
        while(e.hasMoreElements())
        {
          SubPathingMap subMap = (SubPathingMap)e.nextElement();
        
          for(PathingLayer layer : PathingLayer.values())
          {
              for(int x=0;x<width;x++)
              {
                  for(int y=0;y<height;y++)
                  {
                      convertField(subMap,layer,x,y);
                  }
              }
              
          }
        }
    }
    
    public void convertField(SubPathingMap subMap, PathingLayer layer, int x, int y)
    {
        int value=subMap.getValue(x, y, layer);
        
        if(value==0){return;}
        
        int newvalue=value;
        
        for(int mx=x-1;x<mx+1;mx++)
        {
            for(int my=y-1;y<my+1;my++)
            {
                if(mx != x && my != y)
                {
                    
                    if(mx < 0 || (my < 0 && mx != width))
                    {
                        newvalue=1;
                    }else if(mx == width || my == height){
                        newvalue=2;
                    }else{
                        if(subMap.getValue(x, y, layer) == 0)
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
                            subMap.setValue(x, y, layer,1);
                            return;
                        }
                    }
                }
            }
        }
        
        subMap.setValue(x, y, layer,newvalue);
        return;
        
    }
    
}
