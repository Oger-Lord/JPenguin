/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.player;

import de.jpenguin.fog.FogOfWar;

/**
 *
 * @author Karsten
 */
public class FogOfWarPlayer {
    
    private int fogArray[][];
    private boolean blackMask=false;
    private boolean fogOfWar=true;
    

    
    public FogOfWarPlayer()
    {
        fogArray = new int[FogOfWar.getWidth()][FogOfWar.getHeight()];
        
        enableBlackMask(true);
    }
    
    public void enableBlackMask(boolean bool)
    {
        if(fogOfWar == false)
        {
            return;
        }
        
        if(blackMask == bool)
        {
            return;
        }
        
        blackMask=bool;
        
         for(int x=0;x<FogOfWar.getWidth();x++)
         {
               for(int y=0;y<FogOfWar.getHeight();y++)
               {
                   if(bool)
                   {
                        if(fogArray[x][y] == 0)
                        {
                            fogArray[x][y]=-1;
                        }
                   }else{
                       if(fogArray[x][y] == -1)
                        {
                            fogArray[x][y]=0;
                        }
                   }
               }
         }
    }
    
    public void enableFogOfWar(boolean bool)
    {
        if(fogOfWar == bool)
        {
            return;
        }
        
        if(bool==false && blackMask)
        {
            enableBlackMask(false);
        }
        
        fogOfWar=bool;
        
         for(int x=0;x<FogOfWar.getWidth();x++)
         {
               for(int y=0;y<FogOfWar.getHeight();y++)
               {
                   if(bool)
                   {
                        fogArray[x][y]--;
                   }else{
                       
                        fogArray[x][y]++;
                   }
               }
         }
    }
    
    public boolean isVisible(float fx, float fy)
    {
        
        int x = FogOfWar.convertX(fx);
        int y = FogOfWar.convertY(fy);
        
        if(fogArray[x][y] > 0)
        {
            return true;
        }
        return false;
    }
    
    
    public void setSight(float fradius,float fx, float fy,boolean see)
    {
        if(fradius==0)
        {
            return;
        }
       // int radius = (int)(fradius/size*width);
        int radius = (int)(fradius/FogOfWar.getSize()*FogOfWar.getWidth());
        int radiussquare = radius*radius;
        int x = FogOfWar.convertX(fx);
        int y = FogOfWar.convertY(fy);
        
       // System.out.println(x + " " + y + " " + (fx+size/2));
        
        for(int mx=x-radius;mx<=x+radius;mx++)
        {
            for(int my=y-radius;my<=y+radius;my++)
            {
                if(Math.pow(x-mx,2)+Math.pow(y-my,2) <= radiussquare )
                {
                    if(mx >= 0 && my >= 0 && mx < FogOfWar.getWidth() && my < FogOfWar.getHeight())
                    {
                         if(see)
                         {
                             if(fogArray[mx][my]== -1)
                             {
                                 fogArray[mx][my]++;
                             }
                             fogArray[mx][my]++;
                         }else{
                             fogArray[mx][my]--;
                         }
                    }
                }
            }
        }
    }

    /**
     * @return the fogArray
     */
    public int[][] getFogArray() {
        return fogArray;
    }
}
