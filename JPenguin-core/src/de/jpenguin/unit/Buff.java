/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.unit;

import com.jme3.scene.*;
import com.jme3.material.Material;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;

import de.jpenguin.game.Game;
import de.jpenguin.game.GameApplication;
import de.jpenguin.unit.Unit;
import de.jpenguin.type.BuffType;

/**
 *
 * @author Karsten
 */
public class Buff {
    
    private Unit owner;
    private Spatial model;
    private GameApplication gameApp;
    private BuffType buffType;
    
    public Buff(Game game,String bt, Unit u)
    {
        this.buffType = BuffType.getBuffType(bt);
        
        this.owner = u;
        this.gameApp=game.getGameApplication();
        
        model = gameApp.getAssetManager().loadModel(buffType.getModel());
        
        for(int i=0;i<buffType.getAttachmentPoints().length;i++)
        {
            u.getModel().addModel(model, buffType.getAttachmentPoints()[i]);
        }
        
        u.addBuff(this); 
    }
    
    public void update()
    {
   //     model.setLocalTranslation(owner.getY(),owner.getZ()-2.15f, owner.getX());
    }
    
    public BuffType getBuffType()
    {
        return buffType;
    }
    
    public void close()
    {
        model.removeFromParent();
        model=null;
        owner=null;
    }
}
