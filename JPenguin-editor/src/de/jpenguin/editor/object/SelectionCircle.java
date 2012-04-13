/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.editor.object;

/**
 *
 * @author Karsten
 */

    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import de.jpenguin.editor.engine.EditorApplication;
import com.jme3.scene.*;
import com.jme3.material.Material;
import com.jme3.asset.AssetManager;
import com.jme3.scene.shape.Box;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.bounding.BoundingBox;
/**
 *
 * @author Karsten
 */
public class SelectionCircle {
    
    private Spatial spatial;
    private Geometry model;
    private Doodad doodad;
    
    private EditorApplication editorApp;
    
    public SelectionCircle(EditorApplication editorApp,Doodad d,ColorRGBA color,boolean mouseOver)
    {
        this.doodad=d;
        this.spatial=d.getModel();
        this.editorApp=editorApp;
        
        Box box = new Box(1.5f,0,1.5f);
        model = new Geometry("buff", box);
        Material mat = new Material(editorApp.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        
        Texture tex_ml;
        if(mouseOver==false)
        {
            tex_ml= editorApp.getAssetManager().loadTexture("Textures/circle.png");
        }else{
            tex_ml= editorApp.getAssetManager().loadTexture("Textures/circle_small.png");
        }
        mat.setTexture("ColorMap", tex_ml);
        mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        mat.setColor("Color", color);
        model.setMaterial(mat);
       // model.setLocalScale(1,0.1f, 1);
      //  model.setQueueBucket(Bucket.Transparent);
       // model.setShadowMode(ShadowMode.Cast);
        model.setShadowMode(ShadowMode.Off);
        //gameApp.getRootNode().attachChild(model);
        
       // update();
        model.setQueueBucket(Bucket.Translucent);
        
        
        Vector3f v3f = spatial.getLocalTranslation().clone();
        v3f.setY(v3f.getY()+0.01f);
        
        model.setLocalTranslation(v3f);
       // model.setLocalScale(0.5f,0.5f,0.5f);
        
        float width = (((BoundingBox)model.getWorldBound()).getXExtent()+((BoundingBox)model.getWorldBound()).getZExtent())/2;
        model.setLocalScale(width,width,width);

        
        editorApp.getRootNode().attachChild(model);
        
    }
    
    public void update()
    {
        Vector3f v3f = spatial.getLocalTranslation().clone();
        v3f.setY(v3f.getY()+0.01f);
        
        model.setLocalTranslation(v3f);
    }
    
    /*
    public Spatial getSpatial()
    {
        return spatial;
    }
     * *
     */
    
    public void close()
    {
        editorApp.getRootNode().detachChild(model);
    }
    
    public Doodad getDoodad()
    {
        return doodad;
    }

}
