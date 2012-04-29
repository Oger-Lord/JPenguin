/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.input;

import com.jme3.collision.MotionAllowedListener;
import com.jme3.input.*;
import com.jme3.input.controls.*;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector2f;
import com.jme3.renderer.Camera;
import com.jme3.terrain.geomipmap.TerrainQuad;

/**
 *
 * @author Karsten
 */
public class RTSCamera implements ActionListener {
    
    private Camera cam;
    private float moveSpeed=60;
    private float scrollSpeed=80;
    private InputManager inputManager;
    private boolean enabled = true;
    
    private boolean upPressed=false;
    private boolean downPressed=false;
    private boolean leftPressed=false;
    private boolean rightPressed=false;
    
    private boolean mouseIsActivated=false;
    
    private TerrainQuad terrain;
    private float distance=22;
    
    public RTSCamera(Camera cam)
    {
        this.cam = cam;
        cam.setLocation(new Vector3f(0, 22f, 11));
        cam.lookAt(new Vector3f(0f, 0f, 0f), Vector3f.UNIT_Y);
    }
    
    public void registerWithInput(InputManager inputManager){
        this.inputManager = inputManager;
        
        String[] mappings = new String[]{
            "RTSCAM_Left",
            "RTSCAM_Right",
            "RTSCAM_Up",
            "RTSCAM_Down",

            "RTSCAM_ZoomIn",
            "RTSCAM_ZoomOut",
        };
        
        inputManager.addMapping("RTSCAM_Left", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("RTSCAM_Right",new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("RTSCAM_Up", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("RTSCAM_Down", new KeyTrigger(KeyInput.KEY_DOWN));
        
        inputManager.addMapping("RTSCAM_ZoomIn", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping("RTSCAM_ZoomOut", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        
        
        inputManager.addListener(this, mappings);
    }
    
    public void addTerrain(TerrainQuad terrain)
    {
        this.terrain=terrain;
    }
    
    public void setTranslation(float x, float y)
    {
        Vector3f origin    = cam.getWorldCoordinates(new Vector2f(cam.getWidth()/2,cam.getHeight()/2), 0.0f);
        Vector3f direction = cam.getWorldCoordinates(new Vector2f(cam.getWidth()/2,cam.getHeight()/2), 0.3f);
        direction.subtractLocal(origin).normalizeLocal();
        
        float value = origin.getY()/direction.getY();
        
        
        Vector2f v2 = new Vector2f();
        v2.set(x, y+(direction.getY()*value)/2);
            
        cam.setLocation(new Vector3f(x, terrain.getHeight(v2)+distance, y+(direction.getY()*value)/2));
    }
    
    public void onAnalog(String name, float value, float tpf) {}

    
    public void onAction(String name, boolean keyPressed, float tpf) {
        
        if (!enabled)
            return;
        

        if (name.equals("RTSCAM_Left")){
            leftPressed = keyPressed;
        }else if (name.equals("RTSCAM_Right")){
            rightPressed = keyPressed;
        }else if (name.equals("RTSCAM_Up")){
            upPressed = keyPressed;
        }else if (name.equals("RTSCAM_Down")){
            downPressed = keyPressed;
        }
        
        if (name.equals("RTSCAM_ZoomIn")){
            distance-=scrollSpeed*tpf;
        }else if (name.equals("RTSCAM_ZoomOut")){
            distance+=scrollSpeed*tpf;
        }
    }
    
    
    public void update(float tpf)
    {
        if (!enabled)
            return;
        
        Vector3f v = cam.getLocation();
        
        float cameraX = inputManager.getCursorPosition().getX();
        float cameraY = inputManager.getCursorPosition().getY();        

        if( mouseIsActivated == false)
        {
            if(cameraX != 0 || cameraY != 0){mouseIsActivated =true;}
        }
        
        if( (mouseIsActivated && cam.getWidth() < cameraX+15) || rightPressed )
        {
            v.setX(v.getX()+moveSpeed*tpf);
            if(v.getX() > terrain.getTerrainSize()/2)
                v.setX(terrain.getTerrainSize()/2);
        }
        
        if( (mouseIsActivated && 0 > cameraX-15) || leftPressed )
        {
            v.setX(v.getX()-moveSpeed*tpf);
            if(v.getX() < -terrain.getTerrainSize()/2)
                v.setX(-terrain.getTerrainSize()/2);
        }

        if((mouseIsActivated && cam.getHeight() < cameraY+15) || upPressed)
        {
            v.setZ(v.getZ()-moveSpeed*tpf);
            if(v.getZ() < -terrain.getTerrainSize()/2)
                v.setZ(-terrain.getTerrainSize()/2);
        }
        
        if((mouseIsActivated && 0 > cameraY-15) || downPressed)
        {
           v.setZ(v.getZ()+moveSpeed*tpf);
           if(v.getZ() > terrain.getTerrainSize()/2)
                v.setZ(terrain.getTerrainSize()/2);
        }
        
        if(terrain != null)
        {
            Vector2f v2 = new Vector2f();
            v2.set(v.getX(), v.getZ());
            v.setY(terrain.getHeight(v2)+distance);
           // System.out.println(v.getZ() +" "+v.getX() + " " +f);
        }else{
            v.setY(distance);
        }
        
        
        
        cam.setLocation(v);
    }

    
    public void enable()
    {
        enabled=true;
    }
}
