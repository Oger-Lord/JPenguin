/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.editor.object;


import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.*;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.AbstractAppState;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;

import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.KeyInput;

import java.io.*;
import com.jme3.export.*;
import com.jme3.export.xml.XMLExporter;
import com.jme3.export.xml.XMLImporter;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.export.binary.BinaryImporter;

import de.jpenguin.editor.Editor;
import de.jpenguin.editor.EditorPalette;
import de.jpenguin.editor.engine.EditorApplication;
import de.jpenguin.editor.xml.TypeXMLManager;
import de.jpenguin.editor.engine.SelectionSquare;


import com.jme3.renderer.queue.RenderQueue.ShadowMode;


import java.util.List;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Enumeration;

import java.util.concurrent.*;
/**
 *
 * @author Karsten
 */
public class ObjectManager extends AbstractAppState{
    
    private EditorApplication editorApp;
    private EditorPalette tools;
    private Brush brush;
    private String nameSelected="";
    
    private TypeXMLManager typeXML;
    
    private Node myRootNode;
    
    private boolean isDragging=false;
    private boolean isDraggingRotation=false;
    private boolean drawSelection;
    
    private Selection selection;
    private SelectionSquare selectionSquare;
    private boolean modusPlant=false;
    
    private int move=0;
    private Hashtable<String,DoodadType> doodadTypes;

    private long lastTimeClick=0;
    
    private boolean selectDoodadPause=true;
    
    private boolean isSTRGPressed=false;

    @Override
   public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager,app);
        
       this.editorApp=(EditorApplication)app;
       brush = new Brush(editorApp);
       
       editorApp.getInputManager().addMapping("mouseClickLeft", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
      // editorApp.getInputManager().addMapping("mouseClickRight", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
       editorApp.getInputManager().addMapping("delete", new KeyTrigger(KeyInput.KEY_DELETE));
       
       editorApp.getInputManager().addMapping("movetop", new KeyTrigger(KeyInput.KEY_NUMPAD9));
       editorApp.getInputManager().addMapping("movebottom", new KeyTrigger(KeyInput.KEY_NUMPAD3));
       
       editorApp.getInputManager().addMapping("strg", new KeyTrigger(KeyInput.KEY_LCONTROL));
       
       editorApp.getInputManager().addListener(actionListener, "mouseClickLeft","mouseClickRight","delete","movetop","movebottom","strg");
       
       myRootNode=new Node("myRootNode");
       editorApp.getRootNode().attachChild(myRootNode);
       
      doodadTypes = new Hashtable<String,DoodadType>();
      
      selection = new Selection(editorApp);
      
      setEnabled(false);
    }
    
    @Override
    public void update(float tpf)
    {
        if(isEnabled()==false){return;}

        if(brush.isEmpty()==false){

        
            Vector3f v3f = editorApp.getTerrainManager().getWorldIntersection();
            if(v3f != null)
            {
                brush.setLocation(v3f.clone());
            }
        }
        
        if(isDragging)
        {
            Vector3f v3f = editorApp.getTerrainManager().getWorldIntersection();
            
            if(v3f != null)
            {
                selection.drag(v3f);
            }
        }
        
        if(isDraggingRotation)
        {
             // Doodad d =selection.getDoodad();
              
              if(selection.isEmpty()==false)
              {
                  Vector3f v3f =editorApp.getTerrainManager().getWorldIntersection();

                  if(v3f != null)
                  {
                      selection.rotateToPoint(v3f);
                  }
              }
        }
        
        if(drawSelection)
        {
            Vector2f v2f = editorApp.getInputManager().getCursorPosition();
            selectionSquare.update(editorApp, (int)v2f.getX(),(int)v2f.getY());
            
            if(selectDoodadPause==false)
            {
                selectDoodadsInRect(v2f,new Vector2f(selectionSquare.getX(),selectionSquare.getY()));
                selectDoodadPause=true;
            }else{
                selectDoodadPause=false;
            }
            
        }
        
        if(move != 0)
        {
            if(selection.isEmpty() ==false)
            {
                selection.changeHeight(5*tpf*move);
            }
        }
    }
    
    
    private ActionListener actionListener = new ActionListener() {

        public void onAction(String name, boolean pressed, float tpf) {
            
            if(isEnabled()==false){return;}
            
            if(name.equals("mouseClickLeft"))
            {
                if(isSTRGPressed)
                {
                     isDraggingRotation=pressed;
                }else if(pressed)
                {
                    
                    if(modusPlant)
                    {     
                        if(brush.isEmpty()){return;}

                        DoodadType dt = doodadTypes.get(nameSelected);
                        
                        if(dt !=null)
                        {
                            brush.draw();
                        }

                    }else{
                        
                        
                        if(lastTimeClick+250 > System.currentTimeMillis() )
                        {
                            
                            if(selection.getSize()==1)
                            {
                                DoodadGUI gui = new DoodadGUI();
                                gui.setVisible(true);
                                gui.load(selection.getDoodad(0));
                            }
                            
                        }else{
                            
                            Doodad d = getWorldIntersection();

                            if(d != null)
                            {
                              if(selection.containsDoodad(d)==false)
                              {
                                  selection.clear();
                                  selection.addDoodad(d);
                              }

                              Vector3f v3f = editorApp.getTerrainManager().getWorldIntersection();

                              if(v3f != null)
                              {
                                  selection.startDrag(v3f);
                              }

                              isDragging=true;

                            }else{
                                drawSelection=true;
                                Vector2f v2f = editorApp.getInputManager().getCursorPosition();
                                selectionSquare = new SelectionSquare((int)v2f.getX(),(int)v2f.getY());
                            }
                        }

                    }
                    lastTimeClick=System.currentTimeMillis();
                    
                }else{
                    
                    if(isDragging)
                    {
                        selection.endDrag();
                    }
                    
                    isDragging=false;
                    drawSelection=false;
                    if(selectionSquare != null)
                    {
                        selectionSquare.clear();
                    }
                }
          /*  }else if(name.equals("mouseClickRight"))
            {
                if(selection.isEmpty()==false)
                {
                     isDraggingRotation=pressed;
                    
                }
           * 
           */
            }else if(name.equals("delete"))
            {
                 if(selection.isEmpty()==false)
                 {
                     selection.delete();
                 }
            }else if(name.equals("movetop"))
            {
                if(pressed)
                {
                    move=1;
                }else{
                    move=0;
                }
            }else if(name.equals("movebottom"))
            {
                if(pressed)
                {
                    move=-1;
                }else{
                    move=0;
                }
            }else if(name.equals("strg"))
            {
                isSTRGPressed=pressed;
            }
        }
    };
    
    
   private Doodad getWorldIntersection() {
        Vector3f origin    = editorApp.getCamera().getWorldCoordinates(editorApp.getInputManager().getCursorPosition(), 0.0f);
        Vector3f direction = editorApp.getCamera().getWorldCoordinates(editorApp.getInputManager().getCursorPosition(), 0.3f);
        direction.subtractLocal(origin).normalizeLocal();

        Ray ray = new Ray(origin, direction);
        CollisionResults results = new CollisionResults();

        int numCollisions = myRootNode.collideWith(ray, results);
        if (numCollisions > 0) {
                CollisionResult hit = results.getClosestCollision();
                return Doodad.Spatial2Doodad(hit.getGeometry());
        }

        return null;
    }
    
    
    public void updateHeight()
    {
        Enumeration e = doodadTypes.keys();
        while (e.hasMoreElements()) {
            String alias = (String)e.nextElement();
            DoodadType dt= doodadTypes.get(alias);
            dt.updateHeight(editorApp.getTerrainManager());
        }
        
    }
    
    
    public synchronized void setModusPlant(boolean b)
    {
        System.out.println("ACHSO " + b);
        
        modusPlant=b;
        if(b==false){
            if(brush.isEmpty()==false)
            {
                brush.clear();
            }
        }
    }
    
    
   public synchronized void clear()
   {
       editorApp.getRootNode().detachChild(myRootNode);
       myRootNode=new Node("myRootNode");
       editorApp.getRootNode().attachChild(myRootNode);
       
        Enumeration e = doodadTypes.keys();
        while (e.hasMoreElements()) {
            String alias = (String)e.nextElement();
            DoodadType dt= doodadTypes.get(alias);
            dt.clear();
            dt.setParentNode(myRootNode);
        }
       
   }
    
    
    @Override
    public synchronized void setEnabled(boolean activate)
    {
        super.setEnabled(activate);
        if(activate)
        {
            if(brush.isEmpty()==false)
            {
                brush.clear();
                DoodadType dt = doodadTypes.get(nameSelected);
                if(dt != null)
                {
                    brush.addDoodad(dt);
               //     modelBrush = new Doodad(dt,true);
                //    modelBrush.createSpatial(editorApp.getAssetManager(),brushNode);
                }
            }
        }else{
            brush.clear();
            if(selection.isEmpty()==false)
            {
                selection.clear();
            }
        }
    }
    

    public synchronized void setModelBrush(String name)
    {
        modusPlant=true;
        this.nameSelected=name;
        
        editorApp.enqueue(new changeModelBrush(name));
        
        selection.clear();
    }


     private class changeModelBrush implements Callable
    {
        private String name;
        
        public changeModelBrush(String name)
        {
            this.name=name;
        }
        
         public Object call()
         {
             
        brush.clear();
        
        DoodadType dt = doodadTypes.get(name);
        if(dt == null)
        {
            System.out.println("Neu erzeugt " + name);
            dt = new DoodadType(name,myRootNode,typeXML);
            doodadTypes.put(name,dt);
        }
        
        brush.addDoodad(dt);
        
        setEnabled(true);
             return null;
             
         }
    }
    
    
    public synchronized void updateDoodads(String name,String value)
    {
        DoodadType dt = doodadTypes.get(name);
        if(dt != null)
        {
            dt.update(value,editorApp.getAssetManager(),editorApp);
        }else{
            dt = new DoodadType(name,myRootNode,typeXML);
            doodadTypes.put(name, dt);
        }
    }
    
    
    public synchronized void updateDoodadsId(String name,String newname)
    {
        DoodadType dt = doodadTypes.get(name);
        if(dt == null)
        {
            dt = new DoodadType(name,myRootNode,typeXML);
        }
        dt.setName(newname);
        doodadTypes.put(newname, dt);
        doodadTypes.remove(name);
        
    }
    
    public synchronized  void removeDoodads(String name)
    {
        DoodadType dt = doodadTypes.get(name);
        if(dt != null)
        {
            dt.clear();
        }
        doodadTypes.remove(name);
    }
    
    public void loseFocus()
    {
        isSTRGPressed=false;
        isDragging=false;
        isDraggingRotation=false;
    }

    public void setTool(EditorPalette tools)
    {
        this.tools=tools;
    }
    
    public void setTypeXML(TypeXMLManager typeXML)
    {
        this.typeXML = typeXML;
    }
    
    public Node getNode()
    {
        return myRootNode;
    }
    
    
    public void selectDoodadsInRect(Vector2f start, Vector2f end)
    {
        Vector3f v3start =editorApp.getTerrainManager().getWorldIntersection(start);
        Vector3f v3end =editorApp.getTerrainManager().getWorldIntersection(end);
        
        float startX,startY;
        float endX,endY;
        
        if(v3start.x > v3end.x)
        {
            startX=v3end.x;
            endX=v3start.x;
        }else{
            endX=v3end.x;
            startX=v3start.x;
        }
        
        if(v3start.z > v3end.z)
        {
            startY=v3end.z;
            endY=v3start.z;
        }else{
            endY=v3end.z;
            startY=v3start.z;
        }
        
        selection.clear();
        
        Enumeration e = doodadTypes.keys();
        while (e.hasMoreElements()) {
            String alias = (String)e.nextElement();
            DoodadType dt= doodadTypes.get(alias);
            ArrayList<Doodad> al = dt.getDoodadsInRect(startX,startY,endX,endY);
            
            for(int i=0;i<al.size();i++)
            {
                selection.addDoodad((Doodad)al.get(i));
            }
        }
        
        
    }
    
    public void save()
    {
        //String userHome = System.getProperty("user.home");
        BinaryExporter exporter = BinaryExporter.getInstance();
        XMLExporter exporterxml = XMLExporter.getInstance();
        
        try {
            exporterxml.save(new SafeHashtable(doodadTypes), new File(Editor.getMapPath()+"Editor_doodads.xml"));
            
            Node saveNode = (Node)myRootNode.clone();
            List list = saveNode.getChildren();
            
            //Remove doodad information from doodads for save
            for(int i=0;i<list.size();i++)
            {
                List nList = ((Node)list.get(i)).getChildren();
                for(int a=0;a<nList.size();a++)
                {
                    Node n = (Node)nList.get(a);
                    n.setUserData("doodad", "");
                }
            }
            
            exporter.save(saveNode, new File(Editor.getMapPath()+"doodads.jme"));
        } catch (IOException ex) {
        //  Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Failed to save node!", ex);
        }
    }
    
    public void load()
    {
        clear();
        
        XMLImporter importer = XMLImporter.getInstance();
        importer.setAssetManager(editorApp.getAssetManager());
        try {
          SafeHashtable safe = (SafeHashtable)importer.load(new File(Editor.getMapPath()+"Editor_doodads.xml"));
          doodadTypes = safe.getHashtable();
          
          
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
        Enumeration e = doodadTypes.keys();
        while (e.hasMoreElements()) {
            String alias = (String)e.nextElement();
            DoodadType dt= doodadTypes.get(alias);
            
            System.out.println("----Load3" + alias);
            
            dt.setParentNode(myRootNode);
            dt.setTypeXMLManager(typeXML);
            dt.createSpatials(editorApp.getAssetManager());
        }
    }
}
