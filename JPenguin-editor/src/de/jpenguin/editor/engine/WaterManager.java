/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.editor.engine;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.export.Savable;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.export.binary.BinaryImporter;
import com.jme3.export.xml.XMLExporter;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.water.WaterFilter;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.terrain.Terrain;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import de.jpenguin.editor.Editor;
import de.jpenguin.editor.EditorPalette;
import de.jpenguin.engine.MyWaterFilter;
import de.jpenguin.engine.Water;
import de.jpenguin.engine.WaterType;
import de.jpenguin.loader.Loader;
import de.jpenguin.pathing.PathingMap;
import de.jpenguin.pathing.SubPathingMap;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
/**
 *
 * @author Karsten
 */
public class WaterManager extends AbstractAppState{
      
    private Water water;
    private EditorApplication editorApp;
    
    private Geometry wfGeom;
    
    private boolean draw=false;
    
    private EditorPalette tools;
    
    public WaterManager(EditorPalette tools)
    {
        this.tools=tools;
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.editorApp=(EditorApplication)app;
        
        wfGeom = new Geometry("test11",new Sphere(8, 8, 0.5f));
        
        // WIREFRAME material
        Material matWire = new Material(editorApp.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        matWire.getAdditionalRenderState().setWireframe(true);
        matWire.setColor("Color", ColorRGBA.Green);
        
        wfGeom.setMaterial(matWire);
        wfGeom.setLocalTranslation(4, 4, 0);
        
       editorApp.getInputManager().addMapping("mouseClickWater", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
       
       editorApp.getInputManager().addListener(actionListener, "mouseClickWater");
    
        setEnabled(false);
    }
    
   private ActionListener actionListener = new ActionListener() {

        public void onAction(String name, boolean pressed, float tpf) {
            
            if(name.equals("mouseClickWater"))
            {
                draw = pressed;
            }

        }
    };
    
    public void changeHeight(Vector3f loc, float radius)
    {
        water.changeTerrainHeight(loc, radius);
         
    }
    
    public void hide(boolean b)
    {
        water.enableEffect(b);
    }
    
        
    @Override
    public void setEnabled(boolean b)
    {
        super.setEnabled(b);
        
        if(b)
        {
            editorApp.getRootNode().attachChild(wfGeom);
        }else{
            editorApp.getRootNode().detachChild(wfGeom);
        }
    }
    
    
    @Override
    public void update(float tpf)
    {
           Vector3f intersection = editorApp.getTerrainManager().getWorldIntersection();
           TerrainQuad terrain = editorApp.getTerrainManager().getTerrain(); 
           
            if (terrain != null && intersection != null) {
                
                float h = terrain.getHeight(new Vector2f(intersection.x, intersection.z));
                //Vector3f tl = terrain.getWorldTranslation();

                wfGeom.setLocalTranslation(new Vector3f(intersection.x, h, intersection.z));
                
                
                 wfGeom.setLocalScale((float)tools.getWaterBrushSize(), (float)tools.getWaterBrushSize(), (float)tools.getWaterBrushSize());
                
                
                if(draw)
                {
                    int waterId = tools.getWater();
                    
                    if(waterId != -1)
                    {
                        if(tools.getWaterBrushType().equals("add"))
                        {
                            water.drawWaterType(waterId, intersection.x, intersection.z,(float)tools.getWaterBrushSize()/2, true);
                        }else{
                            water.drawWaterType(waterId, intersection.x, intersection.z,(float)tools.getWaterBrushSize()/2, false);
                        }
                    }
                }   
            }
    }
    
    
    
    public void newWater()
    {
        water.clear();
        
        int size=editorApp.getTerrainManager().getTerrain().getTerrainSize()-1;
        
        water.addWaterType(new WaterType("Test Water",editorApp.getRootNode(),size,0));
        
        tools.setWaterList(water.getWaterNames());
    }
    
    public void save()
    {
        Loader.save(Editor.getMapPath(), water, "water", true);
    }
    
    public void load()
    {
        water =Loader.load(editorApp.getAssetManager(), Editor.getMap(), "water", false, Water.class);
        if(water != null)
        {
            water.init(editorApp, editorApp.getTerrainManager().getTerrain(), editorApp.getPathingManager().getPathingMap(), editorApp.getRootNode());
            
            
           // water.getWaterFilter().setEnabledMap(editorApp.getAssetManager().loadTexture("Textures/test.png"));
        }else{
            int size=editorApp.getTerrainManager().getTerrain().getTerrainSize()-1;
            
            water = new Water(size,size);
            water.init(editorApp, editorApp.getTerrainManager().getTerrain(), editorApp.getPathingManager().getPathingMap(), editorApp.getRootNode());
            
            water.addWaterType(new WaterType("Test Water",editorApp.getRootNode(),size,0));
        }
        tools.setWaterList(water.getWaterNames());
    }
    
}
