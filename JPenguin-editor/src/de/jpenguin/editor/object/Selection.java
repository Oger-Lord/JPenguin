/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.editor.object;

import de.jpenguin.editor.engine.EditorApplication;


import de.jpenguin.editor.object.tools.*;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector2f;
import de.jpenguin.pathing.PathingMap;

import java.util.ArrayList;
/**
 *
 * @author Karsten
 */
public class Selection {
    
    private EditorApplication editorApp;
    private ArrayList<SelectionCircle> selectionCircles;
    
    private ArrayList<Vector2f> dragPositions;
    private ArrayList<Vector2f> dragStartPositions;
    private Vector2f dragPoint;
    
    private boolean moveSmooth=true;
    
    public Selection(EditorApplication editorApp)
    {
        this.editorApp=editorApp;
        selectionCircles = new ArrayList();
        dragPositions = new ArrayList();
        dragStartPositions = new ArrayList();
    }
    
    public void addDoodad(Doodad d)
    {
        getSelectionCircles().add(new SelectionCircle(editorApp, d,new ColorRGBA(0,1,0,1),true));
        
        if(d.getPathingSize()> 0)
        {
            moveSmooth=false;
        }
    }
    
    public void startDrag(Vector3f v3f)
    {
        dragPoint = new Vector2f();
        dragPoint.setX(v3f.getX());
        dragPoint.setY(v3f.getZ());
        
        dragPositions.clear();
        
        if(moveSmooth==false)
        {
            for(int i=0;i<getSelectionCircles().size();i++)
            {
                Vector2f v2f = new Vector2f();
                v2f.x = getSelectionCircles().get(i).getDoodad().getLocation().getX();
                v2f.y = getSelectionCircles().get(i).getDoodad().getLocation().getZ();
                
                dragPositions.add(v2f);
            }
       }
        
        for(int i=0;i<getSelectionCircles().size();i++)
        {
               Vector2f v2f = new Vector2f();
               v2f.x = getSelectionCircles().get(i).getDoodad().getLocation().getX();
               v2f.y = getSelectionCircles().get(i).getDoodad().getLocation().getZ();
                
               dragStartPositions.add(v2f);
         }
    }
    
    
    public void drag(Vector3f location)
    {
        float changeX = location.x-dragPoint.x;
        float changeY = location.z-dragPoint.y;
        
        if(moveSmooth)
        {
            for(int i=0;i<getSelectionCircles().size();i++)
            {
                Doodad d = getSelectionCircles().get(i).getDoodad();
                
                Vector2f v2f =new Vector2f();
                v2f.x = d.getLocation().x + changeX;
                v2f.y = d.getLocation().z + changeY;
                
                d.setLocation(v2f, editorApp.getTerrainManager());
                d.updateLocation();
                
                getSelectionCircles().get(i).update();
            }
        }else{
            for(int i=0;i<getSelectionCircles().size();i++)
            {
                Doodad d = getSelectionCircles().get(i).getDoodad();
                
                Vector2f v2fold =new Vector2f(d.getLocation().getX(),d.getLocation().getZ());
                
                Vector2f v2f =dragPositions.get(i);
                v2f.x += changeX;
                v2f.y += changeY;

                    d.removeSpace(editorApp.getPathingManager().getPathingMap(),PathingMap.LayerUnit);
                    d.setLocation(v2f, editorApp.getTerrainManager());

                    if(d.hasSpace(editorApp.getPathingManager().getPathingMap())==false)
                    {
                        d.setLocation(v2fold, editorApp.getTerrainManager());
                    }
                    d.setSpace(editorApp.getPathingManager().getPathingMap(),PathingMap.LayerUnit);
                    d.updateLocation();
                     getSelectionCircles().get(i).update();
                    
                
            }
        }
        
        dragPoint.setX(location.x);
        dragPoint.setY(location.z);
    }
    
    public void endDrag()
    {
        System.out.println("END DRAG!");
        
        ArrayList doodads = new ArrayList();
        
         for(int i=0;i<getSelectionCircles().size();i++)
         {
                Doodad d = getSelectionCircles().get(i).getDoodad();
                
                doodads.add(d);
         }
         
         editorApp.addUndo(new MoveDoodads(doodads,dragStartPositions,editorApp));
         editorApp.nextIsBreak();
         
         dragStartPositions = new ArrayList();
          
    }
    
    
    public boolean containsDoodad(Doodad d)
    {
        for(int i=0;i<getSelectionCircles().size();i++)
        {
            if(d==getSelectionCircles().get(i).getDoodad())
            {
                return true;
            }
        }
        return false;
    }
    
    
    public void rotateToPoint(Vector3f v3f)
    {
          for(int i=0;i<getSelectionCircles().size();i++)
          {
                Doodad d = getSelectionCircles().get(i).getDoodad();
                
                Vector3f location = d.getLocation();
                float rotation = (float)Math.atan2((float)(v3f.getX()-location.getX()),(float)(v3f.getZ()-location.getZ()));
                d.setRotation(rotation);
                d.updateRotation();
          }
    }
    
    
    public void changeHeight(float value)
    {
          for(int i=0;i<getSelectionCircles().size();i++)
          {
                Doodad d = getSelectionCircles().get(i).getDoodad();
                d.setHeight(d.getHeight()+value);
                d.updateSpatial();
                getSelectionCircles().get(i).update();
          }
    }
           
    public void delete()
    {/*
       while(getSelectionCircles().size()>0)
        {
            SelectionCircle sc = getSelectionCircles().get(0);
            sc.getDoodad().removeSpace(editorApp.getPathingManager().getPathingMap());
            sc.getDoodad().remove();
            sc.close();
            getSelectionCircles().remove(0);
        }
         * 
         */
        
        DeleteDoodads dd=new DeleteDoodads(this, editorApp);
        editorApp.addUndo(dd);
        editorApp.nextIsBreak();
        
        dragPositions.clear();
        moveSmooth=true;
    }
    
    public void clear()
    {
       while(getSelectionCircles().size()>0)
        {
            SelectionCircle sc = getSelectionCircles().get(0);
            sc.close();
            getSelectionCircles().remove(0);
        }
        dragPositions.clear();
        moveSmooth=true;
    }
    
    
    public int getSize()
    {
        return getSelectionCircles().size();
    }
    
    
    public Doodad getDoodad(int position)
    {
        if(getSelectionCircles().get(position) == null){return null;}
        
        return getSelectionCircles().get(position).getDoodad();
    }
    
    
    public boolean isEmpty()
    {
        if(getSelectionCircles().isEmpty())
        {
            return true;
        }
        return false;
    }

    /**
     * @return the selectionCircles
     */
    public ArrayList<SelectionCircle> getSelectionCircles() {
        return selectionCircles;
    }
    
}
