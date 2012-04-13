/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.fog;

import com.jme3.scene.Node;
import com.jme3.scene.Geometry;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.math.ColorRGBA;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Karsten
 */
public class FogOfWarDoodads extends FogOfWarEffect {
    
    private ArrayList<Node>[][] doodadRects;
    private int size;
    
    public FogOfWarDoodads(Node n, int size)
    {
        this.size=size;
        doodadRects = new ArrayList[FogOfWar.getWidth()][FogOfWar.getHeight()];
        
        List subNodes = n.getChildren();
        for(int a=0;a<subNodes.size();a++)
        {
            List children = ((Node)subNodes.get(a)).getChildren();
            for(int i=0;i<children.size();i++)
            {
                Node child = (Node)children.get(i);
                addDoodad(child);
            }
        }
    }
    
    public void addDoodad(Node n)
    {
            Vector3f v3f = n.getLocalTranslation();
            int x = FogOfWar.convertX(v3f.x);
            int y = FogOfWar.convertY(v3f.z);

            if(doodadRects[x][y] == null)
             {
                  doodadRects[x][y] = new ArrayList();
             }

            doodadRects[x][y].add(n);
    }
    
    public void valueChange(int x, int y, int value, int oldvalue)
    {
        if(doodadRects[x][y] ==null){return;}
        
        for(int i=0;i<doodadRects[x][y].size();i++)
        {
            
            if(value > 0 && oldvalue <= 0)  //make bright
            {
                if(doodadRects[x][y].get(i).getName().equals("building") == false)
                {
                    setSpatialColor(doodadRects[x][y].get(i),false);
                }else{
                    doodadRects[x][y].get(i).removeFromParent();
                }
            }else if(value <= 0 && oldvalue > 0)  //make dark
            {
                 setSpatialColor(doodadRects[x][y].get(i),true);
            }
        }
    }
    
   public void setSpatialColor(Node n,boolean darker)
   {
         List list = n.getChildren();

         for(int i=0;i<list.size();i++)
         {
          //   try{
                Geometry g = (Geometry)list.get(i);

                Material m = g.getMaterial();
                
                if(m.getParam("Ambient") != null)
                {
                    System.out.println("wooot");
                    
                    ColorRGBA c =(ColorRGBA)m.getParam("Ambient").getValue();
                    
                    
                    if(darker)
                    {
                        g.setUserData("color", c);
                        m.setColor("Ambient", new ColorRGBA(0,0,0,1));
                       // c.multLocal(0.5f);
                    }else{
                        
                      //  c.multLocal(2f);
                        m.setColor("Ambient", (ColorRGBA)g.getUserData("color"));
                    }

                  //  m.setColor("Ambient", c);
                    g.setMaterial(m);
                }
          //   }catch(Exception e){}
        }
   }
}
