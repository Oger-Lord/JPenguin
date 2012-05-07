/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.engine;

import com.jme3.material.Material;
import com.jme3.material.MatParam;
import com.jme3.scene.*;
import com.jme3.animation.*;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.math.*;
import com.jme3.export.*;
import java.io.*;
import de.jpenguin.game.GameApplication;
import java.util.List;
import java.util.Collection;
import com.jme3.animation.AnimControl;

import de.jpenguin.fog.FogOfWarModels;
import de.jpenguin.fog.FogOfWarDoodads;

import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.renderer.queue.RenderQueue.Bucket;

import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;

import com.jme3.scene.shape.Sphere;

public class Model implements Savable {
    
    private Node attachmentNode;
    private Node model;
    private MyAnimControl animControl;
    private GameApplication gameApp;
    private ModelClick modelClick;
    
    private SkeletonControl skeletonControl;
    
    private float height;
    private float width;
    
    private boolean hide=false;
    
    private boolean isBuilding=false;
    private boolean wasVisible=false;
    
    private FogOfWarModels fowm;
    
    public static Model Geometry2Model(Geometry g)
    {
        return Node2Model(g.getParent());
    }
    
    public static Model Node2Model(Node node)
    {
        return node.getUserData("model");
    }
    
    public Model(String path, GameApplication gameApp,ModelClick mc,Vector3f location, float rotation, float size)// float x, float y, float z, float scale)
    {
        this.gameApp= gameApp;
        this.modelClick = mc;
        
        attachmentNode = new Node();
        gameApp.getNonreflectNode().attachChild(attachmentNode);
        
        boolean failed = false;
        
        try{
            model = (Node)gameApp.getAssetManager().loadModel(path);
        }catch(Exception e){failed=true;}
        
        if(failed == false)
        {
            model.setName(path);
       
            animControl = new MyAnimControl(model);
        
          //  model.setQueueBucket(Bucket.Transparent);
            
            model.setShadowMode(ShadowMode.CastAndReceive);
        
            
            
            model.setUserData("model",this);
            

            gameApp.getClickableNode().attachChild((Spatial)model);
        
            skeletonControl = model.getControl(SkeletonControl.class);
          //  model.setUserData("model",this);
        
      //  model.updateModelBound();
        
        height = ((BoundingBox)model.getWorldBound()).getYExtent();
        width = (((BoundingBox)model.getWorldBound()).getXExtent()+((BoundingBox)model.getWorldBound()).getZExtent())/2;
        
        
        //important, otherwise fogofwar move model
        attachmentNode.setLocalTranslation(location);
        model.setLocalTranslation(location);
        
        setRotation(rotation);
        setScale(size);
        
        fowm = (FogOfWarModels)gameApp.getGame().getFogOfWar().getEffect(FogOfWarModels.class);
        fowm.addModel(this);
        
        /*
             Sphere c = new Sphere(10,10,1.5f);
            Geometry geom = new Geometry("Cylinder", c);
            Material mat = new Material(gameApp.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
            mat.setColor("Color", ColorRGBA.Blue);
            geom.setMaterial(mat);
        attachmentNode.attachChild(geom);
         * 
         */
         }
        
        
     //   System.out.println("Höhe:"+((BoundingBox)model.getWorldBound()).getXExtent()+"|"+((BoundingBox)model.getWorldBound()).getYExtent()+"|"+((BoundingBox)model.getWorldBound()).getZExtent());
      //  System.out.println("Center:"+((BoundingBox)model.getWorldBound()).getCenter().getX()+"|"+((BoundingBox)model.getWorldBound()).getCenter().getY()+"|"+((BoundingBox)model.getWorldBound()).getCenter().getY());
    }
    
    public void setColor(ColorRGBA color)
    {
       if(model==null)
            return;
        
        List list = model.getChildren();
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i) instanceof Geometry)
            {
                
                Geometry g = (Geometry)list.get(i);
                Material m = g.getMaterial();
                if(g.getName().endsWith("TeamColor"))
                {       
                    if(m.getTextureParam("DiffuseMap") != null)
                    {
                        Material mat = new Material(gameApp.getAssetManager(),"MatDefs/Light/TeamColor.j3md");
                        mat.setTexture("DiffuseMap", m.getTextureParam("DiffuseMap").getTextureValue());
                        mat.setColor("KeyColor", color);
                       // mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Back);
                       // mat.setTransparent(false);
                        g.setMaterial(mat);
                    }
                }/*else if(m!=null)
                {
                    if(m.getMaterialDef() != null)
                    {
                        if(m.getMaterialDef().getAssetName().equals("MatDefs/Light/LightingColor.j3md"))
                        {
                            m.setColor("KeyColor", color);
                            g.setMaterial(m);
                        }
                    }
                }
                 * 
                 */
            
            }
        }
        
    }
    
    public void addModel(Spatial sp)
    {
        attachmentNode.attachChild(sp);
    }
    
    public void addModel(Spatial sp,String bone)
    {
        if(skeletonControl != null)
         {
             if(bone.equalsIgnoreCase("overhead")){
             
                try
                {
                     skeletonControl.getAttachmentsNode("Overhead Ref").attachChild(sp);
                }catch(Exception e){}
                 try
                {
                     skeletonControl.getAttachmentsNode("OverHead Ref ").attachChild(sp);
                }catch(Exception e){}
                 
             }
        }
    }
    
    public void removeModel(Spatial sp)
    {
        
        fowm.removeModel(this);
        attachmentNode.detachChild(sp);
    }
    
    public void hideInFog(boolean b)
    {
        if(hide == b)
        {
            return;
        }
        hide=b;
        
        if(b == true)
        {
            if(attachmentNode !=null && model != null)
            {
                attachmentNode.removeFromParent();
                model.removeFromParent();
                
                if(isBuilding && wasVisible)
                {
                    
                    FogOfWarDoodads fogd = (FogOfWarDoodads)gameApp.getGame().getFogOfWar().getEffect(FogOfWarDoodads.class);
                    
                    Spatial copy=model.deepClone();
                    copy.setName("building");
                    fogd.setSpatialColor((Node)copy,true);
                    fogd.addDoodad((Node)copy);
                   // copy.getControl(AnimControl.class).
                    ((Node) gameApp.getDoodadNode().getChild(0)).attachChild(copy);
                }
            }
        }else{
            wasVisible=true;
            if(attachmentNode !=null)
            {
                gameApp.getNonreflectNode().attachChild(attachmentNode);
            }
            if(model != null)
            {
                gameApp.getClickableNode().attachChild(model);
            }
        }
    }
    
    public void remove()
    {
        if(model != null)
        {
            attachmentNode.removeFromParent();
            model.removeFromParent();
            attachmentNode.detachAllChildren();
            model.detachAllChildren();

        }
        
        fowm.removeModel(this);
        
        animControl=null;
        model=null;
        gameApp=null;
        
    }
    
    public void click(int mouse)
    {
        if(modelClick != null)
        {
            modelClick.click(mouse);
        }
    }
    
    public void setModelClick(ModelClick mc)
    {
        modelClick = mc;
    }
    
    public void setScale(float f)
    {
        height = height*f;
        width = width*f;
        if(model != null)
        {
            model.scale(f);
        }
    }
    
    public void setTranslation(float x, float y, float z)
    {
        fowm.moveModel(this,x,z);
        
        attachmentNode.setLocalTranslation(x, z, y);
        model.setLocalTranslation(x, z, y);
    }
    
    public void setTranslation(Vector3f v3f)
    {
        fowm.moveModel(this,v3f.x,v3f.z);
        
        attachmentNode.setLocalTranslation(v3f);
        model.setLocalTranslation(v3f);
    }
    
    public void setAnimation(String s)
    {
        if(animControl==null)
            return;
        animControl.setAnimation(s, false);
    }
    
    public void setAnimation(String s, float time)
    {
        if(animControl==null)
            return;
        animControl.setAnimation(s, time);
    }
    
    public void resetAnimation()
    {
        animControl.resetAnimation();
    }
    
    public float getHeight()
    {
        return height;
    }
    
    public float getWidth()
    {
        return width;
    }
    
    public Object getParent()
    {
        return modelClick;
    }
    
    public void setRotation(float rotation)
    {
        setRotationRadian((float)(rotation/180*Math.PI));
    }
    
    public Spatial getSpatial()
    {
        return model;
    }
    
    public void setBuilding(boolean value)
    {
        isBuilding=value;
    }
    
    public void setRotationRadian(float rotationRadian)
    {
        if(model != null){
            Quaternion q = new Quaternion(model.getWorldRotation());
            q.fromAngles(0, rotationRadian,0 );
            model.setLocalRotation(q);
        }
    }
    
           public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
      //  capsule.write(someIntValue,   "someIntValue",   1);
      //  capsule.write(someFloatValue, "someFloatValue", 0f);
     //   capsule.write(someJmeObject,  "someJmeObject",  new Material());
    }
 
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
     //   someIntValue   = capsule.readInt(    "someIntValue",   1);
     //   someFloatValue = capsule.readFloat(  "someFloatValue", 0f);
     //   someJmeObject  = capsule.readSavable("someJmeObject",  new Material());
    }
}
