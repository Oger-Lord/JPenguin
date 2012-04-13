/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.fog;

import com.jme3.math.Vector3f;

import java.util.ArrayList;

import de.jpenguin.game.GameApplication;
import de.jpenguin.engine.Model;


/**
 *
 * @author Karsten
 */
public class FogOfWarModels extends FogOfWarEffect {
    
    private ArrayList<Model>[][] modelRects;
    private FogOfWarDisplay fowDisplay;
    
    public FogOfWarModels(FogOfWarDisplay fowDisplay,GameApplication gameApp)
    {
        modelRects = new ArrayList[FogOfWar.getWidth()][FogOfWar.getHeight()];
        this.fowDisplay = fowDisplay;
    }
    
    public void valueChange(int x, int y, int value, int oldvalue)
    {
        if(modelRects[x][y] != null)
        {
            for(int i=0;i<modelRects[x][y].size();i++)
            {
                Model m = modelRects[x][y].get(i);
                
                if(value > 0)
                {
                    m.hideInFog(false);
                }else{
                    m.hideInFog(true);
                }
            }
        }
    }
    
    public void moveModel(Model m, float newx, float newy)
    {
        removeModel(m);
        
        int x =FogOfWar.convertX(newx);
        int y =FogOfWar.convertX(newy);
        
        addModel(m, x, y);
    }
    
    public void addModel(Model m)
    {
        Vector3f v3f = m.getSpatial().getLocalTranslation();
        int x =FogOfWar.convertX(v3f.x);
        int y =FogOfWar.convertX(v3f.z);
        
        addModel(m, x, y);
    }
    
    private void addModel(Model m, int x, int y)
    {
        if(modelRects[x][y] == null)
        {
            modelRects[x][y] = new ArrayList();
        }
        
        modelRects[x][y].add(m);
        

        if(fowDisplay.getOldValue(x, y) > 0)
        {
            m.hideInFog(false);
        }else{
            m.hideInFog(true);
        }
        
    }
    
    
    public void removeModel(Model m)
    {
        Vector3f v3f = m.getSpatial().getLocalTranslation();
        int x =FogOfWar.convertX(v3f.x);
        int y =FogOfWar.convertX(v3f.z);
        
        modelRects[x][y].remove(m);
    }
}
