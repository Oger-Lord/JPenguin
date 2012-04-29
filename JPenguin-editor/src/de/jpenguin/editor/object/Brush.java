/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.editor.object;

import com.jme3.scene.Node;
import com.jme3.app.Application;
import com.jme3.math.Vector3f;

import java.util.List;
import java.util.ArrayList;

import de.jpenguin.editor.object.tools.*;
import de.jpenguin.editor.engine.EditorApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import de.jpenguin.pathing.PathingMap;
/**
 *
 * @author Karsten
 */
public class Brush {
    
    private Node node;
  //  private ArrayList doodads;
    private EditorApplication editorApp;
    private boolean empty=true;
    private Vector3f location;
    
    private boolean moveSmooth=true;
    
    private ArrayList<Vector2f> dragPositions;
    
    public Brush(EditorApplication app)
    {
      //  doodads = new ArrayList();
        this.editorApp=app;
        node = new Node();
        app.getRootNode().attachChild(node);
        location = Vector3f.ZERO;
        
        dragPositions = new ArrayList();
    }
    
    public void addDoodad(DoodadType dt)
    {
        dragPositions = new ArrayList(); //<-  später austauschen
        
        DoodadBrush d = new DoodadBrush(dt);
        Vector2f v2f =new Vector2f(location.getX(),location.getZ());
        
        d.setLocation(v2f,editorApp.getTerrainManager());
        
        d.createSpatial(editorApp.getAssetManager(), node);
        
        d.setSpace(editorApp.getPathingManager().getPathingMap(), PathingMap.LayerEditor);


        
        if(d.getPathingSize() == 0)
        {
            moveSmooth=true;
        }else{
            moveSmooth=false;
        }
       // doodads.add(d);
        empty=false;
        
        
    }
    
    public void setLocation(Vector3f loc)
    {
        Vector3f v3f;
        v3f = loc;
        
        if(moveSmooth)
        {
            
            /*
        }else{
           // float x = ((int)((loc.getX()+0.5)/2))*2;
           // float z = ((int)((loc.getZ()+0.5)/2))*2;
            float x = ((int)((loc.getX()+0.5)/2))*2;
            float z = ((int)((loc.getZ()+0.5)/2))*2;
            
          //  System.out.println(x+" "+z);
 
            v3f= new Vector3f(x,loc.getY(),z);
             * 
             */
        }
        
        
       // Vector3f change =location.subtract(v3f);
        
        List list = node.getChildren();
        for(int i=0;i<list.size();i++)
        {
            DoodadBrush d =(DoodadBrush)Doodad.Spatial2Doodad((Node)list.get(i));
            
            d.removeSpace(editorApp.getPathingManager().getPathingMap(), PathingMap.LayerEditor);
            
            /*
            Vector2f v2f = new Vector2f();
            v2f.setX(d.getLocation().getX()-change.getX());
            v2f.setY(d.getLocation().getZ()-change.getZ());
             * 
             */
            
            
            d.setLocation(new Vector2f(loc.getX(),loc.getZ()), editorApp.getTerrainManager());
            d.updateLocation();
            
            if(d.hasSpace(editorApp.getPathingManager().getPathingMap()))
            {
                d.setCanPlant(true);
            }else{
                d.setCanPlant(false);
            }
            
            d.setSpace(editorApp.getPathingManager().getPathingMap(), PathingMap.LayerEditor);
            
           // editorApp.getPathingManager().getAnotherMap().setSpace(location.getX(), location.getZ(), 4,0);
          //  editorApp.getPathingManager().getAnotherMap().setSpace(v2f.getX(), v2f.getY(), 4,1);
        }
        location = v3f;
       // node.setLocalTranslation(v3f);
    }
    
    
    public void draw()
    {
        ArrayList doodads = new ArrayList();
        
        List list = node.getChildren();
        for(int i=0;i<list.size();i++)
        {
            DoodadBrush sample =(DoodadBrush)Doodad.Spatial2Doodad((Node)list.get(i)); 
            
            if(sample.canPlant())
            {
                Doodad d = sample.clone(false);
                d.createSpatial(editorApp.getAssetManager(), d.getType().getNode());
                d.getType().addDoodad(d);
                
                doodads.add(d);
                
                sample.setSpace(editorApp.getPathingManager().getPathingMap(), PathingMap.LayerUnit);
            }
        }
        
        editorApp.addUndo(new AddDoodads(doodads));
        editorApp.nextIsBreak();
    }
    
    public void clear()
    {
        List list = node.getChildren();
        for(int i=0;i<list.size();i++)
        {
            DoodadBrush d =(DoodadBrush)Doodad.Spatial2Doodad((Node)list.get(i)); 
            d.removeSpace(editorApp.getPathingManager().getPathingMap(), PathingMap.LayerEditor);
            d.remove();
        }
        empty=true;
    }

    /**
     * @return the empty
     */
    public boolean isEmpty() {
        return empty;
    }
}
