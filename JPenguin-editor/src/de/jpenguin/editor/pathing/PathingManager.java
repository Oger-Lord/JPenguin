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

import de.jpenguin.editor.Editor;
import com.jme3.export.*;
import java.io.IOException;
import com.jme3.export.xml.*;
import com.jme3.export.binary.*;
import java.io.File;

import de.jpenguin.editor.engine.EditorApplication;
import de.jpenguin.editor.terrain.tools.*;
import com.jme3.shader.VarType;
import de.jpenguin.loader.PathingMapData;

import java.nio.ByteBuffer;
/**
 *
 * @author Karsten
 */
public class PathingManager extends AbstractAppState{
    
    private TerrainQuad terrain;
    private EditorApplication editorApp;
    private boolean hide=true;
    private Image image;
    private Image emptyImage;
    private DrawPathingImage draw;
    
    private PathingMap pathingMap;
    private PathingMap anotherMap;
    
   @Override
   public void initialize(AppStateManager stateManager, Application app) {
       super.initialize(stateManager,app);
            
       this.editorApp=(EditorApplication)app;
        
       editorApp.getInputManager().addMapping("change", new KeyTrigger(KeyInput.KEY_P));
       editorApp.getInputManager().addListener(actionListener, "change");
       
       emptyImage =new Image(Format.RGBA8,4,4,ByteBuffer.allocateDirect(4*4*4));
   }
    
       private ActionListener actionListener = new ActionListener() {

        public void onAction(String name, boolean pressed, float tpf) {
            
            if(pressed){return;}
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
    };
    
    public void newPathing() {
        
        terrain= editorApp.getTerrainManager().getTerrain();
        int size = terrain.getTotalSize();
        
        pathingMap = new PathingMap(size/2,size/2,size);
        anotherMap = new PathingMap(size/2,size/2,size);
        
       // image =new Image(Format.RGBA8,pathingMap.getWidth(),pathingMap.getHeight(),ByteBuffer.allocateDirect(pathingMap.getWidth()*pathingMap.getHeight()*4));
        
       // pathingMap.badLine();
        draw= new DrawPathingImage(pathingMap);
        image = draw.getImage();   
    }
    
    
    public void load() {
        terrain= editorApp.getTerrainManager().getTerrain();
        int size = terrain.getTotalSize();
        
        anotherMap = new PathingMap(size/2,size/2,size);
        
        pathingMap = new PathingMap(PathingMapData.load(editorApp.getAssetManager(), Editor.getMap()));
        /*
        BinaryImporter imp = BinaryImporter.getInstance();
        imp.setAssetManager(editorApp.getAssetManager());
         try{
            pathingMap = (PathingMap) imp.load( new File(Editor.getMapPath()+"PathingMap.bin"));
        }catch(Exception e){
            System.out.println("Error loading Pathing:" + e.getMessage());
            pathingMap = new PathingMap(size/2,size/2,size);
         //   pathingMap.badLine();
        }
         * 
         */
        
        if(pathingMap == null)
        {
            pathingMap = new PathingMap(size/2,size/2,size);
        }
         
        draw= new DrawPathingImage(pathingMap);
        image = draw.getImage(); 
    }
    
    
    public void save() {
        
        PathingMapData.save(((PathingMapData)pathingMap).clone(), Editor.getMapPath());
        /*
       try{
             BinaryExporter.getInstance().save(pathingMap, new File(Editor.getMapPath()+"PathingMap.bin"));
        }catch(Exception e){
        }
         * 
         */
    }
    
    
    @Override
    public void update(float tpf)
    {
        if(hide==false)
        {
            draw.update(anotherMap);
        }
    }
    
    

    /**
     * @return the pathingMap
     */
    public PathingMap getPathingMap() {
        return pathingMap;
    }
    
    public PathingMap getAnotherMap() {
        return anotherMap;
    }
}
