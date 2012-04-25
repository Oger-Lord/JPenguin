/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.editor.object;

import com.jme3.scene.Node;
import com.jme3.export.*;
import com.jme3.asset.AssetManager;

import de.jpenguin.editor.engine.EditorApplication;
import de.jpenguin.pathing.PathingLayer;
import de.jpenguin.pathing.PathingMap;
import de.jpenguin.pathing.PathingMapName;
import de.jpenguin.editor.xml.TypeXMLFile;
import de.jpenguin.editor.xml.TypeXMLManager;

import de.jpenguin.editor.terrain.TerrainManager;

import java.io.IOException;
import java.util.ArrayList;
/**
 *
 * @author Karsten
 */
public class DoodadType implements Savable {
    
    private String name;
    private TypeXMLFile typeFile;
    private ArrayList<Doodad> doodads;
    private ArrayList<Doodad> brushs;
    private Node doodadNode;
    
    private int pathingSize;
    
    public DoodadType()
    {
        brushs =new ArrayList<Doodad>();
    }
    
    public DoodadType(String name, Node parentNode,TypeXMLManager typeXML)
    {
        doodads = new ArrayList<Doodad>();
        brushs =new ArrayList<Doodad>();
        this.name=name;
        this.doodadNode = new Node();
        parentNode.attachChild(doodadNode);
        
        typeFile = typeXML.getXMLFile("DoodadType");
        
       try{
          
          pathingSize = Integer.parseInt(getValue("pathingSize"));
       }catch(Exception e){}
    }
    
    public void setTypeXMLManager(TypeXMLManager txml)
    {
        typeFile = txml.getXMLFile("DoodadType");
        try{
          pathingSize = Integer.parseInt(getValue("pathingSize"));
       }catch(Exception e){}
    }
    
    public String getValue(String key)
    {
        return typeFile.getValue(name, key);
    }
    
    public void setParentNode(Node parentNode)
    {
        doodadNode.removeFromParent();;
        parentNode.attachChild(doodadNode);
    }
    
    public void setName(String name)
    {
        this.name=name;
    }
    
    public void createSpatials(AssetManager assetManager)
    {
        for(int i=0;i<doodads.size();i++)
        {
             Doodad d =doodads.get(i);
             d.createSpatial(assetManager,doodadNode);
        }
    }
    
    public void update(String value,AssetManager assetManager, EditorApplication editorApp)
    {
        if(value.equals("model"))
        {
            
            for(int i=0;i<doodads.size();i++)
            {
                Doodad d =doodads.get(i);
                d.removeSpatial();
                d.createSpatial(assetManager,doodadNode);
            }
            
            for(int i=0;i<brushs.size();i++)
            {
                Doodad d =brushs.get(i);
                d.removeSpatial();
                d.createSpatial(assetManager,doodadNode);
            }
            
        }else if(value.equals("rotation") ||value.equals("rotationB") ||value.equals("rotationC") || value.equals("height") || value.equals("size") || value.equals("colorRed") || value.equals("colorBlue") || value.equals("colorGreen")){
            
            
            for(int i=0;i<doodads.size();i++)
            {
                Doodad d =doodads.get(i);
                d.updateSpatial();
            }
            
            for(int i=0;i<brushs.size();i++)
            {
                Doodad d =brushs.get(i);
                d.updateSpatial();
            }
        }else if(value.equals("pathingSize"))
        {
            int oldsize=pathingSize;
            try{
                pathingSize = Integer.parseInt(getValue("pathingSize"));
            }catch(Exception e){}

            
            for(int i=0;i<doodads.size();i++)
            {
                Doodad d =doodads.get(i);
                
                if(oldsize>0)
                {
                    PathingMap pm = editorApp.getPathingManager().getPathingMap();
                    pm.setSpace(d.getLocation().getX(), d.getLocation().getZ(), oldsize, oldsize, PathingMapName.Building, PathingLayer.UnitMap, false);
                    pm.setSpace(d.getLocation().getX(), d.getLocation().getZ(), oldsize, oldsize, PathingMapName.Ground, PathingLayer.UnitMap, false);
                }
                
                if(pathingSize>0)
                {
                    d.setSpace(editorApp.getPathingManager().getPathingMap(),PathingLayer.UnitMap);
                }
                
            }
            
            for(int i=0;i<brushs.size();i++)
            {
                Doodad d =brushs.get(i);
                
                if(oldsize>0)
                {
                    PathingMap pm = editorApp.getPathingManager().getPathingMap();
                    pm.setSpace(d.getLocation().getX()-oldsize/2, d.getLocation().getZ()-oldsize/2, oldsize, oldsize, PathingMapName.Building, PathingLayer.EditorMap, false);
                    pm.setSpace(d.getLocation().getX()-oldsize/2, d.getLocation().getZ()-oldsize/2, oldsize, oldsize, PathingMapName.Ground, PathingLayer.EditorMap, false);
                }
                
                if(pathingSize>0)
                {
                    d.setSpace(editorApp.getPathingManager().getPathingMap(),PathingLayer.EditorMap);
                }
            } 
           
            
        }
    }
    
    public void updateHeight(TerrainManager tm)
    {
          for(int i=0;i<doodads.size();i++)
          {
                Doodad d =doodads.get(i);
                d.updateHeight(tm);
          } 
    }
    
    public void addDoodad(Doodad d)
    {
       // if(d.isBrush())
      //  {
            
       // }else{
            doodads.add(d);
       // }
    }
    
    public void removeDoodad(Doodad d)
    {
        if(d==null){return;}
        
        doodads.remove(d);
      //  brushs.remove(d);
    }
    
    public void addBrush(DoodadBrush b)
    {
        brushs.add(b);
    }
    
    public void removeBrush(DoodadBrush b)
    {
        if(b==null){return;}
        
        brushs.remove(b);
    }
    
    
    public int getPathingSize()
    {
        return pathingSize;
    }
    
    public Node getNode()
    {
        return doodadNode;
    }
    
    public void clear()
    {
       while(brushs.isEmpty()==false)
       {
           Doodad d = brushs.get(0);
           d.remove();
       }
        
       while(doodads.isEmpty()==false)
       {
           Doodad d = doodads.get(0);
           d.remove();
       }
    }
    
   public ArrayList<Doodad> getDoodadsInRect(float startX,float startY,float endX,float endY)
   {
       ArrayList<Doodad> al = new ArrayList();
       
       for(int i=0;i<doodads.size();i++)
       {
                Doodad d =doodads.get(i);
                if(d.getLocation().x >= startX && d.getLocation().x <= endX && d.getLocation().z >= startY && d.getLocation().z <= endY)
                {
                    al.add(d);
                }
       } 
       return al;
   }
    
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(name, "name", null);
        capsule.writeSavableArrayList(doodads, "doodads", new ArrayList());
    }
 
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        name = capsule.readString("name",null);
        doodads  = capsule.readSavableArrayList( "doodads",   new ArrayList());
        
        for(int i=0;i<doodads.size();i++)
        {
                Doodad d =doodads.get(i);
                d.setType(this);
        }
        doodadNode = new Node();
    }
}
