/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.gui;

/**
 *
 * @author Karsten
 */
    
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.*;
import com.jme3.scene.shape.Box;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.light.*;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.ui.Picture;
import de.jpenguin.game.GameApplication;

public class Portrait {

    private GameApplication gameApp;
    private Spatial model;
    private AnimChannel channel;
    private ViewPort offView;
    private Picture picture;
    private Texture2D texture;
    private boolean display=true;
    private String modelPath="";

    //Import: only chance to set resolution, later only upscale
    public Portrait(GameApplication gameApp, int width,int height){
        this.gameApp = gameApp;
        
        int largest=width;
        if(height > width)
        {
            largest=height;
        }
        
        Camera offCamera = new Camera(largest, largest);
        
        offView = gameApp.getRenderManager().createPreView("Offscreen View", offCamera);
        offView.setClearFlags(true, true, true);
        offView.setBackgroundColor(new ColorRGBA(0,0,0,0));

        // create offscreen framebuffer
        FrameBuffer offBuffer = new FrameBuffer(width, height, 1);

        //setup framebuffer's cam
        offCamera.setFrustumPerspective(45f, 1f, 1f, 1000f);
        offCamera.setLocation(new Vector3f(-2f, 1f, 5f));
        offCamera.lookAt(new Vector3f(1f, 2f, 0f), Vector3f.UNIT_Y);

        //setup framebuffer's texture
        texture = new Texture2D(width, height, Format.RGBA8);
        texture.setMinFilter(Texture.MinFilter.Trilinear);
        texture.setMagFilter(Texture.MagFilter.Bilinear);

        //setup framebuffer to use texture
        offBuffer.setDepthBuffer(Format.Depth);
        offBuffer.setColorTexture(texture);
        
        //set viewport to render to offscreen framebuffer
        offView.setOutputFrameBuffer(offBuffer);
        

        // attach the scene to the viewport to be rendered
       
        picture = new Picture("HUD Picture");
        picture.setPosition(0, 0);
        setSize(width, height);
        picture .setTexture(gameApp.getAssetManager(), texture, true);

        gameApp.getRootNode().attachChild(picture);
    }

    
    public void setModel(String path)
    {
        modelPath = path;
        if(model !=null)
        {
            offView.detachScene(model);
        }
        
        if(!path.isEmpty())
        {
            model = gameApp.getAssetManager().loadModel(path);
            if(model != null)
            {
                model.setName("test");
                model.setLocalScale(0.5f);

                offView.attachScene(model);

                AnimControl control = model.getControl(AnimControl.class);
                if(control != null){
                    channel = control.createChannel();
                    channel.setAnim("stand");
                }else{
                    channel=null;
                }

                AmbientLight al = new AmbientLight();
                al.setColor(ColorRGBA.White.mult(2));
                model.addLight(al);

                DirectionalLight dl1 = new DirectionalLight();
                dl1.setDirection(new Vector3f(0.98f, -0.98f, 0.94f).normalizeLocal());
                dl1.setColor(new ColorRGBA(0.965f, 0.949f, 0.772f, 1f).mult(0.7f));
                model.addLight(dl1);
                update(0);
            }
        }
    }
    
    public String getModelPath()
    {
        return modelPath;
    }
    
    public void setModelAnimation(String anim)
    {
        if(model != null && channel !=null)
        {
            channel.setAnim(anim);
        }
    }
    
    public void setPosition(int x, int y)
    {
        picture.setPosition(x, y);
    }
    
    public void setSize(int width, int height)
    {
        picture.setWidth(128);
        picture.setHeight(196);
    }
    
    public void setCamera(Vector3f v3f)
    {
        offView.getCamera().setLocation(v3f);
    }
    
    public void setCameraDistance(float distance)
    {
        Vector3f v3f = offView.getCamera().getLocation();
        v3f.normalizeLocal();
        v3f.multLocal(distance);
        offView.getCamera().setLocation(v3f);
    }
    
    
    public void display(boolean b)
    {
        if(b && display==false)
        {
            gameApp.getRootNode().attachChild(picture);
            display=b;
        }
        if(!b && display)
        {
            gameApp.getRootNode().detachChild(picture);
            display=b;
        }
    }
    
    public Texture getTexture()
    {
        return texture;
    }
    
    public void destroy()
    {
        gameApp.getRootNode().detachChild(picture);
        offView.clearScenes();
    }


    public void update(float tpf){
        
        Quaternion q = new Quaternion();
        
        if (offView.isEnabled() && model != null) {
            
            model.updateLogicalState(tpf);
            model.updateGeometricState();
        }
         
    }


}
