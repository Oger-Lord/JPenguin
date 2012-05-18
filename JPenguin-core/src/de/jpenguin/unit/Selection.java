/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.unit;

import com.jme3.scene.*;
import com.jme3.material.Material;
import com.jme3.asset.AssetManager;
import com.jme3.scene.shape.Box;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import de.jpenguin.game.GameApplication;

/**
 *
 * @author Karsten
 */
public class Selection {
    
    private Unit owner;
    private Geometry model;
    private GameApplication gameApp;
    
    public Selection(Unit u,GameApplication gameApp,ColorRGBA color,boolean mouseOver)
    {
        this.owner = u;
        this.gameApp=gameApp;
        
        Box box = new Box(1.5f,0,1.5f);
        model = new Geometry("buff", box);
       // Material mat = new Material(gameApp.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        Material mat = new Material(gameApp.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        
        Texture tex_ml;
        if(mouseOver==false)
        {
            tex_ml= gameApp.getAssetManager().loadTexture("Textures/circle.png");
        }else{
            tex_ml= gameApp.getAssetManager().loadTexture("Textures/circle_small.png");
        }
       // mat.setTexture("DiffuseMap", tex_ml);
      //  mat.setColor("Diffuse", color);
       mat.setTexture("ColorMap", tex_ml);
       mat.setColor("Color", color);
        
        mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        mat.getAdditionalRenderState().setAlphaFallOff(0.9f);
         mat.getAdditionalRenderState().setAlphaTest(true);
    //    mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Back);
        
        model.setMaterial(mat);
       // model.setLocalScale(1,0.1f, 1);
      //  model.setQueueBucket(Bucket.Transparent);
       // model.setShadowMode(ShadowMode.Cast);
        model.setShadowMode(ShadowMode.Off);
        //gameApp.getRootNode().attachChild(model);
        
       // update();
        model.setQueueBucket(Bucket.Transparent);
        
        
        model.setLocalTranslation(0,0.01f,0);
      //  model.setLocalScale(0.5f,0.5f,0.5f);
        
     //   model.setLocalScale(u.getModel().getWidth()/2,u.getModel().getWidth()/2,u.getModel().getWidth()/2);
        model.setLocalScale(owner.getUnitType().getSelectionSize());
        
        owner.getModel().addModel(model);
    }
    
    public void close()
    {
        owner.getModel().removeModel(model);
        model.removeFromParent();
        model=null;
        owner=null;
    }
}
