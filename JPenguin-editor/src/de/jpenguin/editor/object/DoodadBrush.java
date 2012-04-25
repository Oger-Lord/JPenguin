/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.editor.object;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;

import de.jpenguin.editor.terrain.TerrainManager;
/**
 *
 * @author Karsten
 */
public class DoodadBrush extends Doodad {
    
   private boolean canBePlanted=true;
    
   public DoodadBrush(DoodadType t)
   {
       type=t;
       t.addBrush(this);
       
   }
   
   
    @Override
   public void createSpatial(AssetManager assetManager,Node node)
   {
       super.createSpatial(assetManager,node);
    //   setSpatialAlpha(0.8f);
   }
   
   
   public void setCanPlant(boolean can)
   {
       if(can==canBePlanted){return;}
       canBePlanted=can;
       
       if(model != null)
       {
           if(canBePlanted)
           {
                if(isColorLocked())
                {
                    setSpatialColor(colorRGBA);
                }else{
                   updateColor();
                }
           }else{
             //  System.out.println("muuuh");
               setSpatialColor(new ColorRGBA(1,0,0,0.6f));
           }
       }
   }
   
   
   public boolean canPlant()
   {
       return canBePlanted;
   }
   
    @Override
   public void updateSpatial()
   {
       super.updateSpatial();
       
       //if cannot planted still red after color change
       canBePlanted = !canBePlanted;
       setCanPlant(!canBePlanted);
   }
   
   
    @Override
   public void remove()
   {
       removeSpatial();
       getType().removeBrush(this);
   }
}
