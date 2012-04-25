/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.editor.engine;


import de.jpenguin.editor.object.ObjectManager;
import de.jpenguin.editor.terrain.TerrainUtils;
import de.jpenguin.editor.settings.SettingManager;
import de.jpenguin.editor.Editor;
import de.jpenguin.editor.EditorPalette;
import de.jpenguin.editor.engine.MinimapGenerator;
import de.jpenguin.editor.xml.TypeXMLManager;
import com.jme3.app.SimpleApplication;
import com.jme3.math.Quaternion;
import com.jme3.material.RenderState.*;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.app.*;
import java.io.*;
import com.jme3.asset.plugins.FileLocator;

import com.jme3.renderer.queue.RenderQueue.ShadowMode;

import de.jpenguin.editor.pathing.PathingManager;
import de.jpenguin.editor.terrain.TerrainManager;
import com.jme3.shader.VarType;


import java.util.concurrent.Callable ;

import java.util.Stack;


public class EditorApplication extends SimpleApplication {

    private EditorPalette tools;
    private TerrainManager terrainManager;
    private ObjectManager objectManager;
    private SettingManager settingManager;
    private PathingManager pathingManager;
    
    private Stack<Undo> undoStack;
    private boolean nextUndo=false;
    
    private TypeXMLManager typeXML;
    
    private EditorCam camera;

    
    @Override
    public void simpleInitApp() {
        
        
        flyCam.setEnabled(false);
        
        camera = new EditorCam(getCamera());
        camera.registerWithInput(inputManager);
        camera.setMoveSpeed(100);
        camera.setDragToRotate(true);
        
        assetManager.registerLocator(Editor.getPath()+"assets/", FileLocator.class);

        cam.setLocation(new Vector3f(0, 100f, 50));
        cam.lookAt(new Vector3f(0f, 0f, 0f), Vector3f.UNIT_Y);
        
        undoStack = new Stack<Undo>();
        
        terrainManager = new TerrainManager();
        objectManager = new ObjectManager();
        settingManager = new SettingManager();
        
        tools = new EditorPalette(this);
        
        rootNode.setShadowMode(ShadowMode.Off); 
    }
    
    
    @Override
    public void simpleUpdate(float tpf){
        Quaternion q =cam.getRotation();
        
        if(nextUndo)
        {
           nextUndo =false;
           myUndo();
        }
    }

    
    public void createTools(TypeXMLManager typeXML)
    {
        this.typeXML=typeXML;
        
        
        
       // objectManager = new ObjectManager(this,tools,typeXML,false);
       // terrainManager = new TerrainManager(this,tools,true);
       // settingManager = new SettingManager(this);
        objectManager.setTool(tools);
        objectManager.setTypeXML(typeXML);
        terrainManager.setTool(tools);
        pathingManager = new PathingManager(tools);
        
        stateManager.attach(objectManager);
        stateManager.attach(terrainManager);
        stateManager.attach(settingManager);
        stateManager.attach(pathingManager);
        
        stateManager.update(0);
            
        tools.addTypeList(typeXML);
        
        tools.setVisible(true);
        
    }
    
    public void updateType(String typename,String name, String value)
    {
         enqueue(new updateTypeCallable(typename,name,value));
    }
    
    public class updateTypeCallable implements Callable
    {
        String typename,name,value;
        
        public updateTypeCallable(String typename,String name,String value)
        {
            this.typename=typename;
            this.name=name;
            this.value=value;
        }
        
        public Object call()
        {
           if(typename.equals("DoodadType"))
            {
                if(objectManager != null)
                {
                    objectManager.updateDoodads(name, value);
                }
            }
            return null;
        }
    }
    
    public void newType(String typename,String name)
    {
        if(typename.equals("DoodadType"))
        {
            tools.addTypeList(typeXML);
        }
    }
    
    public void removeType(String typename,String name)
    {
        enqueue(new removeTypeCallable(typename,name));
    }
    
    public class removeTypeCallable implements Callable
    {
            String typename,name;
            public removeTypeCallable(String typename,String name)
            {
                this.typename=typename;
                this.name=name;
            }
            
            public Object call()
            {
                if(typename.equals("DoodadType"))
                {
                    tools.addTypeList(typeXML);
                    objectManager.removeDoodads(name);
                }
                
                return null;
            }
    }
    
    public void renameType(String typename,String name,String newname)
    {
        if(typename.equals("DoodadType"))
        {
            objectManager.updateDoodadsId(name, newname);
            tools.addTypeList(typeXML);
        }
    }
    
    
    public void newMap(int size)
    {
        enqueue(new newMapCallable(size));
        undoStack.clear();
    }
    
    private class newMapCallable implements Callable
    {
        private int size;
        
        public newMapCallable(int size)
        {
            this.size=size;
        }
        
        public Object call()
        {
            undoStack = new Stack<Undo>();
            terrainManager.newTerrain(size);
            objectManager.clear();
            pathingManager.newPathing();
            
            return null;
        }
    }
    

    public void save()
    {
        enqueue(new Callable()
          {
              public Object call()
              {
                settingManager.save();
                terrainManager.save();
                objectManager.save();
                pathingManager.save();
                new MinimapGenerator(EditorApplication.this, terrainManager.getTerrain(),objectManager.getNode(),512,512,new File(Editor.getMapPath()+"minimap.png"));
                          
                  return null;
              }
          });
    }
    
    
    public void load()
    {
          enqueue(new Callable()
          {
              public Object call()
              {
                  terrainManager.load();
                  objectManager.load();
                  settingManager.load();
                  pathingManager.load();

                  tools.setTextureList(TerrainUtils.terrainGetTextures(terrainManager.getTerrain(), 64, 64));
                  
                  undoStack.clear();
                  
                  return null;
              }
          });
    }

    
    public TerrainManager getTerrainManager()
    {
        return terrainManager;
    }
    
    public ObjectManager getObjectManager()
    {
        return objectManager;
    }
    
    public SettingManager getSettingManager()
    {
        return settingManager;
    }
    
    public PathingManager getPathingManager()
    {
        return pathingManager;
    }
    
    public void activate(String name)
    {
        if(tools == null){return;}
        
        enqueue(new activateCallable(name));
    }
    
    private class activateCallable implements Callable
        {
            private String name;
            
            public activateCallable(String name)
            {
                this.name=name;
            }
            
            public Object call()
            {
                
                if(name.equals("Terrain"))
                {
                    terrainManager.setEnabled(true);
                    objectManager.setEnabled(false);
                    pathingManager.setEnabled(false);
                }else if(name.equals("Objects")){
                    terrainManager.setEnabled(false);
                    objectManager.setEnabled(true);
                    pathingManager.setEnabled(false);
                }else{
                    terrainManager.setEnabled(false);
                    objectManager.setEnabled(false);
                    pathingManager.setEnabled(true);
                }
                
                return null;
            }
        }
    

    public void undo()
    {
        nextUndo =true;

    }
    
    private void myUndo()
    {
        if(undoStack.empty()==false)
        {
            Undo undo= (Undo)undoStack.pop();
            undo.doUndoTool(terrainManager.getTerrain());

            while(undoStack.empty()==false && undoStack.peek().isBreak()==false)
            {
                  undoStack.pop().doUndoTool(terrainManager.getTerrain());
            }
        }
    }
    
    public void addUndo(Undo undo)
    {
        undoStack.add(undo);
    }
    
    
    public void nextIsBreak()
    {
        if(undoStack.empty()==false)
        {
            undoStack.peek().setBreak(true);
        }
    }
    
    public void loseFocus()
    {
        super.loseFocus();
        objectManager.loseFocus();
        camera.loseFocus();
    }
    
    


}
