package de.jpenguin.editor.engine;

import com.jme3.collision.MotionAllowedListener;
import com.jme3.input.controls.*;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.input.*;

/**
 * A first person view camera controller.
 * After creation, you must register the camera controller with the
 * dispatcher using #registerWithDispatcher().
 *
 * Controls:
 *  - Move the mouse to rotate the camera
 *  - Mouse wheel for zooming in or out
 *  - WASD keys for moving forward/backward and strafing
 *  - QZ keys raise or lower the camera
 */
public class EditorCam implements AnalogListener, ActionListener {

    protected Camera cam;
    protected Vector3f initialUpVec;
    protected float rotationSpeed = 1.5f;
    protected float moveSpeed = 10f;
    protected MotionAllowedListener motionAllowed = null;
    protected boolean enabled = true;
    protected boolean dragToRotate = true;
    protected boolean canRotate = false;
    protected InputManager inputManager;
    
    protected boolean isSTRGPressed=false;
    
    /**
     * Creates a new FlyByCamera to control the given Camera object.
     * @param cam
     */
    public EditorCam(Camera cam){
        this.cam = cam;
        cam.setLocation(new Vector3f(0,50,0));
        initialUpVec = cam.getUp().clone();
    }

    /**
     * Sets the up vector that should be used for the camera.
     * @param upVec
     */
    public void setUpVector(Vector3f upVec) {
       initialUpVec.set(upVec);
    }

    public void setMotionAllowedListener(MotionAllowedListener listener){
        this.motionAllowed = listener;
    }

    /**
     * Sets the move speed. The speed is given in world units per second.
     * @param moveSpeed
     */
    public void setMoveSpeed(float moveSpeed){
        this.moveSpeed = moveSpeed;
    }

    /**
     * Sets the rotation speed.
     * @param rotationSpeed
     */
    public void setRotationSpeed(float rotationSpeed){
        this.rotationSpeed = rotationSpeed;
    }

    /**
     * @param enable If false, the camera will ignore input.
     */
    public void setEnabled(boolean enable){
        if (enabled && !enable){
            if (!dragToRotate || (dragToRotate && canRotate)){
                inputManager.setCursorVisible(true);
            }
        }
        enabled = enable;
    }

    /**
     * @return If enabled
     * @see FlyByCamera#setEnabled(boolean)
     */
    public boolean isEnabled(){
        return enabled;
    }

    /**
     * @return If drag to rotate feature is enabled.
     *
     * @see FlyByCamera#setDragToRotate(boolean) 
     */
    public boolean isDragToRotate() {
        return dragToRotate;
    }

    /**
     * Set if drag to rotate mode is enabled.
     * 
     * When true, the user must hold the mouse button
     * and drag over the screen to rotate the camera, and the cursor is
     * visible until dragged. Otherwise, the cursor is invisible at all times
     * and holding the mouse button is not needed to rotate the camera.
     * This feature is disabled by default.
     * 
     * @param dragToRotate True if drag to rotate mode is enabled.
     */
    public void setDragToRotate(boolean dragToRotate) {
        this.dragToRotate = dragToRotate;
        inputManager.setCursorVisible(dragToRotate);
    }

    /**
     * Registers the FlyByCamera to receive input events from the provided
     * Dispatcher.
     * @param inputManager
     */
    public void registerWithInput(InputManager inputManager){
        this.inputManager = inputManager;
        
        String[] mappings = new String[]{
            "MYCAM_Left",
            "MYCAM_Right",
            "MYCAM_Up",
            "MYCAM_Down",

            "MYCAM_StrafeLeft",
            "MYCAM_StrafeRight",
            "MYCAM_Forward",
            "MYCAM_Backward",

            "MYCAM_ZoomIn",
            "MYCAM_ZoomOut",
            "MYCAM_RotateDrag",

            "MYCAM_Rise",
            "MYCAM_Lower",
            
            "MYCAM_STRG",
            "MYCAM_Space"
        };

        // both mouse and button - rotation of cam
        inputManager.addMapping("MYCAM_Left", new MouseAxisTrigger(MouseInput.AXIS_X, true),
                                               new KeyTrigger(KeyInput.KEY_LEFT));

        inputManager.addMapping("MYCAM_Right", new MouseAxisTrigger(MouseInput.AXIS_X, false),
                                                new KeyTrigger(KeyInput.KEY_RIGHT));

        inputManager.addMapping("MYCAM_Up", new MouseAxisTrigger(MouseInput.AXIS_Y, false),
                                             new KeyTrigger(KeyInput.KEY_UP));

        inputManager.addMapping("MYCAM_Down", new MouseAxisTrigger(MouseInput.AXIS_Y, true),
                                               new KeyTrigger(KeyInput.KEY_DOWN));

        // mouse only - zoom in/out with wheel, and rotate drag
        inputManager.addMapping("MYCAM_ZoomIn", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping("MYCAM_ZoomOut", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        inputManager.addMapping("MYCAM_RotateDrag", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

        // keyboard only WASD for movement and WZ for rise/lower height
        inputManager.addMapping("MYCAM_StrafeLeft", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("MYCAM_StrafeRight", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("MYCAM_Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("MYCAM_Backward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("MYCAM_Rise", new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addMapping("MYCAM_Lower", new KeyTrigger(KeyInput.KEY_Z));
        
        inputManager.addMapping("MYCAM_Space", new KeyTrigger(KeyInput.KEY_SPACE));

        inputManager.addMapping("MYCAM_STRG", new KeyTrigger(KeyInput.KEY_LCONTROL));
        
        inputManager.addListener(this, mappings);
        inputManager.setCursorVisible(dragToRotate);

        Joystick[] joysticks = inputManager.getJoysticks();
        if (joysticks != null && joysticks.length > 0){
            Joystick joystick = joysticks[0];
            joystick.assignAxis("MYCAM_StrafeRight", "MYCAM_StrafeLeft", JoyInput.AXIS_POV_X);
            joystick.assignAxis("MYCAM_Forward", "MYCAM_Backward", JoyInput.AXIS_POV_Y);
            joystick.assignAxis("MYCAM_Right", "MYCAM_Left", joystick.getXAxisIndex());
            joystick.assignAxis("MYCAM_Down", "MYCAM_Up", joystick.getYAxisIndex());
        }
    }

    protected void rotateCamera(float value, Vector3f axis){
        if (dragToRotate){
            
            if (canRotate==false)
            {
                return;
            }


            if(isSTRGPressed)
            {
                Matrix3f mat = new Matrix3f();
                mat.fromAngleNormalAxis(rotationSpeed * value, axis);

                Vector3f up = cam.getUp();
                Vector3f left = cam.getLeft();
                Vector3f dir = cam.getDirection();

                mat.mult(up, up);
                mat.mult(left, left);
                mat.mult(dir, dir);

                Quaternion q = new Quaternion();
                q.fromAxes(left, up, dir);
                q.normalize();

                cam.setAxes(q);
            }else{
                moveCamera(-axis.getX()*value,false,true);
                moveCamera(-axis.getY()*value, true, false);
            }
        }
    }

    protected void zoomCamera(float value){
        // derive fovY value
        float h = cam.getFrustumTop();
        float w = cam.getFrustumRight();
        float aspect = w / h;

        float near = cam.getFrustumNear();

        float fovY = FastMath.atan(h / near)
                  / (FastMath.DEG_TO_RAD * .5f);
        fovY += value*2 ;//* 0.1f;

        h = FastMath.tan( fovY * FastMath.DEG_TO_RAD * .5f) * near;
        w = h * aspect;

        cam.setFrustumTop(h);
        cam.setFrustumBottom(-h);
        cam.setFrustumLeft(-w);
        cam.setFrustumRight(w);
    }

    protected void riseCamera(float value){
        Vector3f vel = new Vector3f(0, value * moveSpeed, 0);
        Vector3f pos = cam.getLocation().clone();

        if (motionAllowed != null)
            motionAllowed.checkMotionAllowed(pos, vel);
        else
            pos.addLocal(vel);

        cam.setLocation(pos);
    }

    protected void moveCamera(float value, boolean sideways, boolean up){
        Vector3f vel = new Vector3f();
        Vector3f pos = cam.getLocation().clone();

        if (sideways){
            cam.getLeft(vel);
        }else if(up){
            cam.getUp(vel);
        }else{
            cam.getDirection(vel);
          //  
            //cam.getDirection(vel);
        }
        vel.multLocal(value * moveSpeed);

        if (motionAllowed != null)
            motionAllowed.checkMotionAllowed(pos, vel);
        else
            pos.addLocal(vel);

        cam.setLocation(pos);
    }
    
    public void camReset()
    {
        cam.setLocation(cam.getLocation().setY(50f));
        cam.lookAt(new Vector3f(cam.getLocation().getX(), 0f, cam.getLocation().getZ()-25), Vector3f.UNIT_Y);
        
        //cam.setLocation(new Vector3f(0, 100f, 50));
       // cam.lookAt(new Vector3f(0f, 0f, 0f), Vector3f.UNIT_Y);
    }

    public void onAnalog(String name, float value, float tpf) {
        if (!enabled)
            return;

        if (name.equals("MYCAM_Left")){
            rotateCamera(value, initialUpVec);
        }else if (name.equals("MYCAM_Right")){
            rotateCamera(-value, initialUpVec);
        }else if (name.equals("MYCAM_Up")){
            rotateCamera(-value, cam.getLeft());
        }else if (name.equals("MYCAM_Down")){
            rotateCamera(value, cam.getLeft());
        }else if (name.equals("MYCAM_Forward")){
            moveCamera(value, false,true);
        }else if (name.equals("MYCAM_Backward")){
            moveCamera(-value, false,true);
        }else if (name.equals("MYCAM_StrafeLeft")){
                moveCamera(value, true,false);
        }else if (name.equals("MYCAM_StrafeRight")){
                moveCamera(-value, true,false);
        }else if (name.equals("MYCAM_Rise")){
            riseCamera(value);
        }else if (name.equals("MYCAM_Lower")){
            riseCamera(-value);
        }else if (name.equals("MYCAM_ZoomIn")){
            moveCamera(value/10,false,false);
        }else if (name.equals("MYCAM_ZoomOut")){
            moveCamera(-value/10,false,false);
        }
    }

    public void onAction(String name, boolean value, float tpf) {
        if (!enabled)
            return;
        if (name.equals("MYCAM_RotateDrag") && dragToRotate){
            
            canRotate = value;
            
            if(isSTRGPressed==true)
            {
                inputManager.setCursorVisible(!value);
            }
        }
        else if (name.equals("MYCAM_STRG")){
            if(value==false)
            {
                inputManager.setCursorVisible(true);
            }
            isSTRGPressed = value;
        }else if (name.equals("MYCAM_Space") && value){
            camReset();
        }
    }
    
    public void loseFocus()
    {
        isSTRGPressed=false;
    }

}
