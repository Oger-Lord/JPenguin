/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.fog;

import com.jme3.app.state.AbstractAppState;
import de.jpenguin.game.Game;
import de.jpenguin.player.Player;
import de.jpenguin.player.FogOfWarPlayer;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 *
 * @author Karsten
 */
public class FogOfWarDisplay extends AbstractAppState {

    private int oldArray[][];
   // private Hashtable<Integer,FogChange> changes;

    private Game game;
    
    private int updatecount=1;
    private Hashtable<Class,FogOfWarEffect> effects;
    
    public FogOfWarDisplay(Game game)
    {
        this.game=game;

        oldArray = new int[FogOfWar.getWidth()][FogOfWar.getHeight()];

        effects = new Hashtable<Class,FogOfWarEffect>();
        
        for(int x=0;x<FogOfWar.getWidth();x++)
        {
            for(int y=0;y<FogOfWar.getHeight();y++)
            {
                    oldArray[x][y]=1;
            }
        }
    }
    
    
    @Override
    public void update(float tpf)
    {
        Enumeration<FogOfWarEffect> e = effects.elements();
        while(e.hasMoreElements())
        {
            FogOfWarEffect fowe = e.nextElement();
            fowe.update(tpf);
        }
        
        updatecount++;
        if(updatecount==2)
        {
           updatecount=0;
                  
           e = effects.elements();
           while(e.hasMoreElements())
            {
                FogOfWarEffect fowe = e.nextElement();
                fowe.startChange();
            }
            
           int[][] fogArray =generateFogArray(game.getControllerPlayer());
           
            for(int x=0;x<FogOfWar.getWidth();x++)
            {
                for(int y=0;y<FogOfWar.getHeight();y++)
                {
                    
                    if(fogArray[x][y] != oldArray[x][y] && (fogArray[x][y]<1 || oldArray[x][y]<1))
                    {
                        
                        e = effects.elements();
                        while(e.hasMoreElements())
                        {
                            FogOfWarEffect fowe = e.nextElement();
                            fowe.valueChange(x, y, fogArray[x][y],oldArray[x][y]);
                        }

                        oldArray[x][y] = fogArray[x][y];
                    }
                    
                    //---- 
                }
            }
            
           e = effects.elements();
           while(e.hasMoreElements())
            {
                FogOfWarEffect fowe = e.nextElement();
                fowe.endChange();
            }
           
        }
        

    }
    
    private int[][] generateFogArray(Player p)
    {
        int[][] fogArray = p.getFogOfWarPlayer().getFogArray();

        Enumeration e =game.getPlayer().keys();
        
        while(e.hasMoreElements())
        {
          Player pp = game.getPlayer().get(e.nextElement());
          
          if(pp != p)
          {
              if(p.getRelationship(pp).hasSharedVision())
              {
                  
                  int[][] fogArrayAdd =pp.getFogOfWarPlayer().getFogArray();
                  
                  for(int x=0;x<FogOfWar.getWidth();x++)
                  {   
                      for(int y=0;y<FogOfWar.getHeight();y++)
                       {
                           if(fogArrayAdd[x][y] > 0)
                           {
                               fogArray[x][y] +=fogArrayAdd[x][y];
                           }
                       }
                  
                 }
             }
          }

        }
        
        return fogArray;
    }
    
    public void addEffect(FogOfWarEffect fowe)
    {
        effects.put(fowe.getClass(),fowe);
    }
    
    
    public int getOldValue(int x, int y)
    {
        return oldArray[x][y];
    }

    public FogOfWarEffect getEffect(Class c)
    {
        return effects.get(c);
    }
    
    public void destroy()
    {
        Enumeration<FogOfWarEffect> e = effects.elements();
        while(e.hasMoreElements())
        {
             FogOfWarEffect fowe = e.nextElement();
             fowe.destroy();
        }
    }
    
}
