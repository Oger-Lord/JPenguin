/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.editor.engine;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.math.Quaternion;
import com.jme3.renderer.Camera;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture2D;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.texture.Texture;
import com.jme3.app.Application;
import com.jme3.post.SceneProcessor;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.texture.FrameBuffer;
import com.jme3.util.BufferUtils;
import com.jme3.util.Screenshots;
import com.jme3.app.state.*;
import com.jme3.scene.Node;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;


public class MinimapGenerator extends AbstractAppState implements SceneProcessor {

    private ViewPort offView;
    private Texture2D texture;
    private boolean madeScreen=false;
    private Renderer renderer;
    private FrameBuffer offBuffer;
    private ByteBuffer outBuf;
    private BufferedImage bi;
    
    private File file;
    
    private int width;
    private int height;
    
    private Application app;

    //Import: only chance to set resolution, later only upscale
    public MinimapGenerator(Application app,TerrainQuad terrain,Node n,int width,int height, File f){
        
        this.app=app;
        this.width=width;
        this.height=height;
        this.file=f;
        
        int largest=width;
        if(height > width)
        {
            largest=height;
        }
        
        Camera offCamera = new Camera(largest, largest);
        
        offView = app.getRenderManager().createPreView("Offscreen View", offCamera);
        offView.setClearFlags(true, true, true);
        offView.setBackgroundColor(new ColorRGBA(0,0,0,0));

        // create offscreen framebuffer
        offBuffer = new FrameBuffer(width, height, 1);

        double h = Math.tan(45)*terrain.getTotalSize()/Math.sqrt(2);
        
        //setup framebuffer's cam
        offCamera.setFrustumPerspective(45f, 1f, 1f, 1000f);
        offCamera.setLocation(new Vector3f(0f, (float)h, 0f));
        offCamera.lookAt(new Vector3f(0f, 0f, 0f), Vector3f.UNIT_Y);
        
        Quaternion q =offCamera.getRotation();
        q.fromAngles((float)Math.PI/2, (float)Math.PI,0);
        offCamera.setRotation(q);
        
        //setup framebuffer's texture
        texture = new Texture2D(width, height, Format.RGBA8);
        texture.setMinFilter(Texture.MinFilter.Trilinear);
        texture.setMagFilter(Texture.MagFilter.Bilinear);

        //setup framebuffer to use texture
        offBuffer.setDepthBuffer(Format.Depth);
        offBuffer.setColorTexture(texture);
        
        //set viewport to render to offscreen framebuffer
        offView.setOutputFrameBuffer(offBuffer);
        
        offView.attachScene(terrain);
        if(n != null)
        {
            offView.attachScene(n);
        }
        
        this.renderer = app.getRenderManager().getRenderer();
        
        terrain.updateLogicalState(0);
        terrain.updateGeometricState();
        
        app.getStateManager().attach(this);
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        
        if (!super.isInitialized()){
            offView.addProcessor(this);
        }
        super.initialize(stateManager, app);
    }

    @Override
    public void initialize(RenderManager rm, ViewPort vp) {
        renderer = rm.getRenderer();
        reshape(vp, vp.getCamera().getWidth(), vp.getCamera().getHeight());
    }


    public void reshape(ViewPort vp, int w, int h) {
        
    }

    public void preFrame(float tpf) {
    }

    public void postQueue(RenderQueue rq) {
    }

    public void postFrame(FrameBuffer out) {
         if(madeScreen==false)
         {
            madeScreen = true;

            bi = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
            outBuf = BufferUtils.createByteBuffer(width*height*4);
         
            renderer.readFrameBuffer(out, outBuf);
            Screenshots.convertScreenShot(outBuf, bi);

            try {
                ImageIO.write(bi, "png", file);
            } catch (IOException ex){}
            
            app.getStateManager().detach(this);
         }
    }
}




