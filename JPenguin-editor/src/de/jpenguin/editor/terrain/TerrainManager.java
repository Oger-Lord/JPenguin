   /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.editor.terrain;

import de.jpenguin.editor.terrain.TerrainUtils;
import de.jpenguin.editor.Editor;
import de.jpenguin.editor.EditorPalette;
import de.jpenguin.editor.engine.MinimapGenerator;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.*;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.Node;
import com.jme3.terrain.geomipmap.TerrainGrid;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.geomipmap.grid.FractalTileLoader;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.jme3.texture.*;
import com.jme3.texture.Image;
import com.jme3.texture.Texture.WrapMode;

import java.io.*;
import com.jme3.export.*;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.export.binary.BinaryImporter;
import com.jme3.scene.shape.Box;
import com.jme3.asset.plugins.FileLocator;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import com.jme3.texture.plugins.*;
import java.awt.BasicStroke;
import java.awt.RenderingHints;
import com.jme3.app.state.AbstractAppState;

import de.jpenguin.editor.engine.EditorApplication;
import de.jpenguin.editor.engine.Undo;
import de.jpenguin.editor.terrain.tools.*;
import com.jme3.shader.VarType;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;

import java.util.concurrent.Callable;
import java.awt.AlphaComposite;

import java.nio.ByteBuffer;

/**
 *
 * @author Karsten
 */

public class TerrainManager extends AbstractAppState{

    private EditorApplication editorApp;
    
    private TerrainQuad terrain;
   // Material matTerrain;
    
    private boolean mouseDown=false;
    
    private EditorPalette tools;
    
    private Geometry wfGeom;

    
    private Vector3f mousePressedStart;
    
    private boolean displayGrid=false;
   
    private void setupKeys() {
        editorApp.getInputManager().addMapping("displayGrid", new KeyTrigger(KeyInput.KEY_G));
        editorApp.getInputManager().addMapping("mouseClick", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        editorApp.getInputManager().addListener(actionListener, "mouseClick","displayGrid");
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager,app);
        
        editorApp = (EditorApplication)app;
        
        setupKeys();

        wfGeom = new Geometry("test11",new Sphere(8, 8, 0.5f));
        
        // WIREFRAME material
        Material matWire = new Material(editorApp.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        matWire.getAdditionalRenderState().setWireframe(true);
        matWire.setColor("Color", ColorRGBA.Green);
        
        wfGeom.setMaterial(matWire);
        wfGeom.setLocalScale(100, 100, 100);
        wfGeom.setLocalTranslation(4, 4, 0);
        
        setEnabled(true);
    }
    

    @Override
    public void update(float tpf){
        if(isEnabled()==false){return;}
        
        Vector3f intersection = getWorldIntersection();
       // updateHintText(intersection);
        
        if(mouseDown==true && intersection != null)
        {
            if (tools.getBrushType().equals("raise"))
            {
                    adjustHeight(intersection, (float)tools.getBrushSize()/2, tpf * ((float)tools.getBrushStrength())/2);
                    editorApp.getWaterManager().changeHeight(intersection, (float)tools.getBrushSize()/2);
            }else if (tools.getBrushType().equals("lower"))
            {
                    adjustHeight(intersection, (float)tools.getBrushSize()/2, -tpf * ((float)tools.getBrushStrength())/2);
                    editorApp.getWaterManager().changeHeight(intersection, (float)tools.getBrushSize()/2);
            }else if (tools.getBrushType().equals("draw"))
            {
                    adjustTexture(intersection, (float)tools.getBrushSize()/2, ((float)tools.getBrushStrength())/2);
            }else if (tools.getBrushType().equals("antidraw"))
            {
                    adjustTexture(intersection, (float)tools.getBrushSize()/2, -((float)tools.getBrushStrength()/2));
            }else if (tools.getBrushType().equals("smooth"))
            {
                    adjustSmooth(intersection, (float)tools.getBrushSize()/2, tpf *((float)tools.getBrushStrength()/2));
                    editorApp.getWaterManager().changeHeight(intersection, (float)tools.getBrushSize()/2);
            }else if (tools.getBrushType().equals("level"))
            {
                    adjustLevel(intersection, (float)tools.getBrushSize()/2, tpf *((float)tools.getBrushStrength()/2));
                    editorApp.getWaterManager().changeHeight(intersection, (float)tools.getBrushSize()/2);
            }
        }

        if(tools != null)
        {
            
            if (terrain != null && intersection != null) {
                float h = terrain.getHeight(new Vector2f(intersection.x, intersection.z));
                Vector3f tl = terrain.getWorldTranslation();
                wfGeom.setLocalScale((float)tools.getBrushSize(), (float)tools.getBrushSize(), (float)tools.getBrushSize());
                wfGeom.setLocalTranslation(tl.add(new Vector3f(intersection.x, h, intersection.z)) );
            }
        
        }
    }
    
    
    @Override
    public void setEnabled(boolean activate)
    {
        if(wfGeom==null){return;}
        super.setEnabled(activate);
        if(isEnabled())
        {
            editorApp.getRootNode().attachChild(wfGeom); 
        }else{
            editorApp.getRootNode().detachChild(wfGeom); 
        }
    }
    
    
    private ActionListener actionListener = new ActionListener() {

        public void onAction(String name, boolean pressed, float tpf) {
            
            if (name.equals("displayGrid") && pressed)
            {
                if(displayGrid==false)
                {
                   displayGrid=true;
                   Material m =terrain.getMaterial();
                   m.setInt("GridSize", terrain.getTerrainSize()/4);
                   m.setColor("GridColor", ColorRGBA.White);
                   terrain.setMaterial(m);
                }else{
                    displayGrid=false;
                   Material m =terrain.getMaterial();
                   m.setInt("GridSize", 0);
                   terrain.setMaterial(m);
                }
            }
            
            if(isEnabled()==false){return;}
            
            if (name.equals("mouseClick") && isEnabled()) {
 
                  if(mouseDown ==false && pressed==true)
                  {
                      
                       Vector3f intersection = getWorldIntersection();
                       mousePressedStart = intersection;
                  }
                  mouseDown=pressed;
                  
               if(pressed==false)
               {
                    editorApp.nextIsBreak();
               }
                  
            }
            

            
            

        }
    };
    
    
    
    private void adjustHeight(Vector3f loc, float radius, float height) {
       RaiseTerrainToolAction id = new RaiseTerrainToolAction(loc, radius, height);
       id.modifyHeight(terrain, radius, height); 
       editorApp.addUndo((Undo)id);
       
       editorApp.getObjectManager().updateHeight();
    }

    private void adjustTexture(Vector3f loc, float radius, float weight) {
       PaintTerrainToolAction id = new PaintTerrainToolAction(loc, radius, weight/50,tools.getTexture());
       id.paintTexture(terrain, loc, radius, weight/50, tools.getTexture());  
       editorApp.addUndo((Undo)id);
    }
    
    private void adjustSmooth(Vector3f loc, float radius, float weight) {
       SmoothTerrainToolAction id = new SmoothTerrainToolAction(loc, radius, weight);
       id.modifyHeight(terrain, radius, weight); 
       editorApp.addUndo((Undo)id);
    }
    
    private void adjustLevel(Vector3f loc, float radius, float weight) {
       if(mousePressedStart != null)
       {
           LevelTerrainToolAction id = new LevelTerrainToolAction(loc, radius, weight,mousePressedStart);
           id.modifyHeight(terrain, radius, weight);
           editorApp.addUndo((Undo)id);
       }
    }
    
    
    private void createTerrain(int size) {
        // First, we load up our textures and the heightmap texture for the terrain

         if (terrain != null) {
                Node existingTerrain = (Node)terrain;
                existingTerrain.removeFromParent();
                existingTerrain.removeControl(TerrainLodControl.class);
                existingTerrain.detachAllChildren();
                terrain = null;
            }

        // TERRAIN TEXTURE material
        Material matTerrain = new Material(editorApp.getAssetManager(), "MatDefs/Terrain/TerrainLighting.j3md");
        matTerrain.setBoolean("useTriPlanarMapping", false);
        matTerrain.setBoolean("WardIso", true);
        matTerrain.setFloat("Shininess", 0);
        
        updateAlphamap(size,size,matTerrain);
        
        // GRASS texture
        Texture grass = editorApp.getAssetManager().loadTexture("Textures/Terrain/grass.jpg");
        grass.setWrap(WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap", grass);
        matTerrain.setFloat("DiffuseMap_0_scale", 64);

        // DIRT texture
        Texture dirt = editorApp.getAssetManager().loadTexture("Textures/Terrain/dirt.jpg");
        dirt.setWrap(WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap_1", dirt);
        matTerrain.setFloat("DiffuseMap_1_scale", 64);

        // ROCK texture
        Texture rock = editorApp.getAssetManager().loadTexture("Textures/Terrain/road.jpg");
        rock.setWrap(WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap_2", rock);
        matTerrain.setFloat("DiffuseMap_2_scale", 128);

        // HEIGHTMAP image (for the terrain heightmap)

        float[] heightmap = new float[1];
        //AbstractHeightMap heightmap = new HeightMap(
       // heightmap.smooth(0.9f, 1);
        
        // CREATE THE TERRAIN
        //terrain = new TerrainQuad("terrain", 65, 513, heightmap.getHeightMap());
        terrain = new TerrainQuad("terrain", 65, size+1, heightmap);
        TerrainLodControl control = new TerrainLodControl(terrain, editorApp.getCamera());
        control.setLodCalculator( new DistanceLodCalculator(65, 2.7f) ); // patch size, and a multiplier
        terrain.addControl(control);
        terrain.setMaterial(matTerrain);
        terrain.setLocalTranslation(0, 0, 0);
        terrain.setLocalScale(1f, 1f, 1f);
        editorApp.getRootNode().attachChild(terrain);

        
         
        tools.setTextureList(TerrainUtils.terrainGetTextures(terrain, 64, 64));
        
    }
    
    public Vector3f getWorldIntersection() {
        Vector3f origin    = editorApp.getCamera().getWorldCoordinates(editorApp.getInputManager().getCursorPosition(), 0.0f);
        Vector3f direction = editorApp.getCamera().getWorldCoordinates(editorApp.getInputManager().getCursorPosition(), 0.3f);
        direction.subtractLocal(origin).normalizeLocal();

        Ray ray = new Ray(origin, direction);
        CollisionResults results = new CollisionResults();
        if(terrain != null)
        {
            int numCollisions = terrain.collideWith(ray, results);
            if (numCollisions > 0) {
                CollisionResult hit = results.getClosestCollision();
                return hit.getContactPoint();
            }
        }
        return null;
    }
    
    public Vector3f getWorldIntersection(Vector2f input) {
        Vector3f origin    = editorApp.getCamera().getWorldCoordinates(input, 0.0f);
        Vector3f direction = editorApp.getCamera().getWorldCoordinates(input, 0.3f);
        direction.subtractLocal(origin).normalizeLocal();

        Ray ray = new Ray(origin, direction);
        CollisionResults results = new CollisionResults();
        if(terrain != null)
        {
            int numCollisions = terrain.collideWith(ray, results);
            if (numCollisions > 0) {
                CollisionResult hit = results.getClosestCollision();
                return hit.getContactPoint();
            }
        }
        return null;
    }
    
    private void updateAlphamap(int width, int height,Material m)
    {
        Texture tex_ml = new Texture2D();
        tex_ml.setImage(getImage(width*2, height*2,false));
        m.setTexture("AlphaMap", tex_ml);

        
        tex_ml = new Texture2D();
        tex_ml.setImage(getImage(width*2, height*2,true));
        m.setTexture("AlphaMap_1", tex_ml);
        
        tex_ml = new Texture2D();
        tex_ml.setImage(getImage(width*2, height*2,true));
        m.setTexture("AlphaMap_2", tex_ml);
    }
    
    
    private Image getImage(int width, int height, boolean b)
    {
        if(b==true)
        {
            return new Image(Image.Format.RGBA8,width,height,ByteBuffer.allocateDirect(width*height*4));
        }
        
        ByteBuffer bb =ByteBuffer.allocateDirect(width*height*4);
        
        for(int i=0;i<width*height*4;i+=4)
        {
         //   bb.p
              bb.position( i );
              bb.put(float2byte(1));
        }
        bb.rewind();
        
        return new Image(Image.Format.RGBA8,width,height,bb);
    }
    
     private byte float2byte(float f){
            return (byte) (f * 255f);
     }

    
    public double getTextureSize(int i)
    {
        Material m = terrain.getMaterial();
        MatParam mParam= m.getParam("DiffuseMap_"+i+"_scale");
        return Double.parseDouble(mParam.getValueAsString());
    }
    
    public void setTextureSize(int i,double value)
    {
        Material m = terrain.getMaterial();
        m.setFloat("DiffuseMap_"+i+"_scale", (float)value);
    }
    
    
  

    
     public synchronized void  setTexture(int i,String path)
    {
        
        Material m = terrain.getMaterial();
        Texture tex= editorApp.getAssetManager().loadTexture(path);
        tex.setWrap(WrapMode.Repeat);
           
       try{getTextureSize(i);}
        catch(Exception e){
            setTextureSize(i,128);
        }
       
        if(i==0)
        {
            m.setTexture("DiffuseMap", tex);
        }else{
             m.setTexture("DiffuseMap_"+i, tex);
             
        } 
        tools.setTextureList(TerrainUtils.terrainGetTextures(terrain, 64, 64));
    }



     
     public void setTextureNormalMap(int i,String path)
    {
        Material m = terrain.getMaterial();
        
        
        Texture tex=null;
                
        if(path != null)     
        {
               tex = editorApp.getAssetManager().loadTexture(path);
               tex.setWrap(WrapMode.Repeat);
        }
        /*
        if(i > 3 && i < 8)
        {
            
            if(m.getTextureParam("AlphaMap_1") == null)
                    {
                    Texture2D tex_ml = new Texture2D();
                    tex_ml.setImage(getImage(512*2, 512*2,true));
                    m.setTexture("AlphaMap_1", tex_ml);
                    }
        }
         * 
         */
        
        try{
        if(i==0)
        {
            m.setTexture("NormalMap", tex);
        }else{
            m.setTexture("NormalMap_"+i, tex);
        }
        }catch(Exception e){System.out.println("Error changing normalmap" + e);}
    }
    
    public void setTool(EditorPalette tool)
    {
        tools = tool;
    }
    
    public void newTerrain(int size)
    {
        createTerrain(size);
    }
    
    public TerrainQuad getTerrain()
    {
        return terrain;
    }
    
    public float getHeight(float x, float y)
    {
        return terrain.getHeight(new Vector2f(x,y));
    }

    public void save()
    {
        try{
            long start = System.currentTimeMillis();
            FileOutputStream fos = new FileOutputStream(new File(Editor.getMapPath()+"terrainsave.jme"));

            TerrainQuad newTerrain = terrain.clone();
            
            Material m = newTerrain.getMaterial();
            m.setTexture("PathingMap", new Texture2D());
            m.setInt("GridSize", 0);
            m.setColor("GridColor", ColorRGBA.White);
            newTerrain.setMaterial(m);
            
            BinaryExporter.getInstance().save((Savable)newTerrain, new BufferedOutputStream(fos));

            fos.flush();
            float duration = (System.currentTimeMillis() - start) / 1000.0f;
            System.out.println("Save took " + duration + " seconds");
            
                    
           // new MinimapGenerator(editorApp, terrain,512,512,new File(Editor.getPath()+"assets/Textures/Minimap/minimap.png"));
        }catch(Exception e){
            System.out.println("Error saving Terrain:" + e.getMessage());
        }
    }
    
    public void load()
    {
        try{
           long start = System.currentTimeMillis();
            // remove the existing terrain and detach it from the root node.
            if (terrain != null) {
                Node existingTerrain = (Node)terrain;
                existingTerrain.removeFromParent();
                existingTerrain.removeControl(TerrainLodControl.class);
                existingTerrain.detachAllChildren();
                terrain = null;
            }

            // import the saved terrain, and attach it back to the root node
            File f = new File(Editor.getMapPath()+"terrainsave.jme");
            FileInputStream fis = new FileInputStream(f);
            BinaryImporter imp = BinaryImporter.getInstance();
            imp.setAssetManager(editorApp.getAssetManager());
            terrain = (TerrainQuad) imp.load(new BufferedInputStream(fis));
            terrain.setShadowMode(ShadowMode.CastAndReceive);
            editorApp.getRootNode().attachChild((Node)terrain);

            float duration = (System.currentTimeMillis() - start) / 1000.0f;
            System.out.println("Load took " + duration + " seconds");
            
              tools.setTextureList(TerrainUtils.terrainGetTextures(terrain, 64, 64));
        }catch(Exception e){}
       
    }
    
    


}
