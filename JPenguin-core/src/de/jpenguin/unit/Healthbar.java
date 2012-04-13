/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.unit;

import de.jpenguin.game.GameApplication;
import com.jme3.scene.*;
import com.jme3.material.Material;
import com.jme3.asset.AssetManager;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.shape.Quad;
import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue.Bucket;

public class Healthbar {
    
    private Unit owner;
    private Geometry greenBar;
    private Geometry redBar;
    private float hpBarLength=2;
    private Node parentNode;
    private GameApplication app;
    
    public Healthbar(Unit u)
    {
        this.app=u.getGame().getGameApplication();
        owner = u;
        
        Quad q = new Quad(1, 0.2f);
        redBar = new Geometry("Quad", q);
        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Red);
        redBar.setMaterial(mat);
       // redBar.setLocalTranslation(u.getY()-1, 0, u.getX());

        Quad q2 = new Quad(1, 0.2f);
        greenBar = new Geometry("Quad2", q2);
        Material mat2 = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat2.setColor("Color", ColorRGBA.Green);
        greenBar.setMaterial(mat2);
      //  greenBar.setLocalTranslation(u.getY(), 0, u.getX());
        
        
        greenBar.setQueueBucket(Bucket.Translucent);
        redBar.setQueueBucket(Bucket.Translucent);

        Node bb = new Node("billboard");

        BillboardControl control=new BillboardControl();
        
        bb.addControl(control);
        bb.attachChild(greenBar);
        bb.attachChild(redBar);       

        parentNode=new Node("parent");
        parentNode.attachChild(bb);
        
        parentNode.setLocalTranslation(0,u.getModel().getHeight()*2,0);
        
        u.getModel().addModel(parentNode);
        //app.getRootNode().attachChild(parentNode);
    }
    
    public void update()
    {
        owner.getUnitType();
        double lifeProcent = owner.getLife()/owner.getUnitType().getLife();
        
        greenBar.setLocalScale((float)lifeProcent*hpBarLength, 0.8f, 0.2f);
        greenBar.setLocalTranslation(-hpBarLength/2, 0,0);
        
        redBar.setLocalScale((float)(1-lifeProcent)*hpBarLength, 0.8f, 0.2f);
        redBar.setLocalTranslation(((float)lifeProcent*hpBarLength-hpBarLength/2), 0,0);
        
        
    }
    
    public void remove()
    {
        parentNode.detachChild(greenBar);
        parentNode.detachChild(redBar);
        owner.getModel().removeModel(parentNode);
        owner=null;
        parentNode=null;
        greenBar=null;
        redBar=null;
    }
    
}
