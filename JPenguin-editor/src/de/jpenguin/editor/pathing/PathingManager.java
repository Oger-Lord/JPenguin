/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.editor.pathing;

import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.RenderState.*;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import com.jme3.util.TangentBinormalGenerator;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture2D;
import com.jme3.app.state.AbstractAppState;
import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.math.ColorRGBA;

import com.jme3.math.Vector2f;

import de.jpenguin.editor.Editor;
import com.jme3.export.*;
import java.io.IOException;
import com.jme3.export.xml.*;
import com.jme3.export.binary.*;
import com.jme3.input.controls.MouseButtonTrigger;
import java.io.File;

import de.jpenguin.editor.engine.EditorApplication;
import de.jpenguin.editor.terrain.tools.*;
import com.jme3.shader.VarType;
import de.jpenguin.editor.EditorPalette;
import de.jpenguin.pathing.*;

import java.nio.ByteBuffer;
/**
 *
 * @author Karsten
 */
public class PathingManager extends AbstractAppState{
    
    private TerrainQuad terrain;
    private EditorApplication editorApp;
    
    private boolean wasHidden=true;
    
    private boolean hide=true;
    private Image image;
    private Image emptyImage;
    private DrawPathingImage drawImage;
    
    private PathingMap pathingMap;
    
    private Geometry wfGeom;
    
    private boolean isActive=false;
    
    private EditorPalette tools;
    
    private boolean draw;
    
    public PathingManager(EditorPalette tools)
    {
        this.tools=tools; 
        super.setEnabled(true);
 }
    
   @Override
   public void initialize(AppStateManager stateManager, Application app) {
       super.initialize(stateManager,app);
       
       this.editorApp=(EditorApplication)app;
               
        wfGeom = new Geometry("test11",new Sphere(8, 8, 0.5f));
        
        // WIREFRAME material
        Material matWire = new Material(editorApp.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        matWire.getAdditionalRenderState().setWireframe(true);
        matWire.setColor("Color", ColorRGBA.Green);
        
        wfGeom.setMaterial(matWire);
        wfGeom.setLocalScale(100, 100, 100);
        wfGeom.setLocalTranslation(4, 4, 0); 
       
       
        
       editorApp.getInputManager().addMapping("mouseClickPathing", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
       editorApp.getInputManager().addMapping("change", new KeyTrigger(KeyInput.KEY_P));
       
       editorApp.getInputManager().addListener(actionListener, "change","mouseClickPathing");
       
       emptyImage =new Image(Format.RGBA8,4,4,ByteBuffer.allocateDirect(4*4*4));
   }
    
       private ActionListener actionListener = new ActionListener() {

        public void onAction(String name, boolean pressed, float tpf) {
            
            if(name.equals("change"))
            {
                if(pressed==false)
                {
                    if(isActive==false)
                    {
                        hide();
                        wasHidden = hide;
                    }
                    
                }
            }else if(name.equals("mouseClickPathing"))
            {
                draw = pressed;
            }

        }
    };
       
       
    public void hide()
    {
        System.out.println("HIDE!");
        
                    if(hide)
                    {
                        hide=false;
                        Texture texture = new Texture2D();
                        texture.setImage(image);

                        texture.setMagFilter(Texture.MagFilter.Nearest);
                        texture.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
                        texture.setWrap(Texture.WrapMode.Clamp);

                        Material m =terrain.getMaterial();
                        m.setTexture("PathingMap", texture);
                        terrain.setMaterial(m);
                       // editorApp.getRootNode().attachChild(terrain);
                    }else{
                        Texture texture = new Texture2D();
                        texture.setImage(emptyImage);
                        Material m =terrain.getMaterial();
                        m.setTexture("PathingMap", texture);
                        terrain.setMaterial(m);
                        hide=true;
                      //  editorApp.getRootNode().detachChild(terrain);
                    }
    }
       
       
    
    public void newPathing() {
        
        terrain= editorApp.getTerrainManager().getTerrain();
        int size = terrain.getTotalSize()-1;
        
        pathingMap = new PathingMap();
        
        //0 ground
        pathingMap.addSubPathingMap(new SubPathingMap(size,size,5,2));
        
        //1 air
        pathingMap.addSubPathingMap(new SubPathingMap(size,size,5,1));
        
        //2 building
        pathingMap.addSubPathingMap(new SubPathingMap(size,size,5,1));
        
        //3 water
        pathingMap.addSubPathingMap(new SubPathingMap(size,size,5,1f));
        
       // image =new Image(Format.RGBA8,pathingMap.getWidth(),pathingMap.getHeight(),ByteBuffer.allocateDirect(pathingMap.getWidth()*pathingMap.getHeight()*4));
        
       // pathingMap.badLine();
        drawImage= new DrawPathingImage(pathingMap,size,size);
        image = drawImage.getImage();   
    }
    
    
    public void load() {
        terrain= editorApp.getTerrainManager().getTerrain();
        int size = terrain.getTotalSize()-1;
        

        pathingMap = PathingMap.load(editorApp.getAssetManager(), Editor.getMap());
        
        if(pathingMap == null)
        {
            newPathing();
        }else{
         
            drawImage= new DrawPathingImage(pathingMap,size,size);
            image = drawImage.getImage(); 
        }
    }
    
    
    public void save() {
        
        PathingMap.save(pathingMap, Editor.getMapPath());
    }
    
    
    @Override
    public void update(float tpf)
    {
        if(hide==false)
        {
            drawImage.update();
        }
        
        if(isActive)
        {
            Vector3f intersection = editorApp.getTerrainManager().getWorldIntersection();
            
            if (terrain != null && intersection != null) {
                float h = terrain.getHeight(new Vector2f(intersection.x, intersection.z));
                Vector3f tl = terrain.getWorldTranslation();
                wfGeom.setLocalScale((float)tools.getPathingBrushSize(), (float)tools.getPathingBrushSize(), (float)tools.getPathingBrushSize());
                wfGeom.setLocalTranslation(tl.add(new Vector3f(intersection.x, h, intersection.z)) );
                
                
                if(draw)
                {
                    int brushSize = (int)tools.getPathingBrushSize()/2;
                    int pm = tools.getPathingBrushMap();
                    
                    if(tools.getPathingBrushType().equals("add"))
                    {
                        pathingMap.setSpace(intersection.x, intersection.z,brushSize, pm, PathingMap.LayerDraw, true);
                    }else{
                       pathingMap.setSpace(intersection.x, intersection.z,brushSize, pm, PathingMap.LayerDraw, false);
                    }
                    
                }
            }
        }
    }
    
    
    @Override
    public void setEnabled(boolean b)
    {
        if(isActive==false && b==false)
            return;
        
        isActive = b;
        
        if(b)
        {
            editorApp.getRootNode().attachChild(wfGeom);
        }else{
            editorApp.getRootNode().detachChild(wfGeom);
        }
        
        if(b==false && wasHidden)
        {
            hide();
        }else if(b && wasHidden)
        {
            hide();
        }
    }
    

    /**
     * @return the pathingMap
     */
    public PathingMap getPathingMap() {
        return pathingMap;
    }
}
