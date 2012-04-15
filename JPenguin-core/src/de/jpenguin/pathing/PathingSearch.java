/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.pathing;

import de.jpenguin.game.GameApplication;
import de.jpenguin.engine.Model;

import java.util.PriorityQueue;
import java.util.ArrayList;

import com.jme3.math.Vector3f;


/*     */ import com.jme3.material.Material;
/*     */ import com.jme3.math.ColorRGBA;
/*     */ import com.jme3.math.Vector3f;
/*     */ import com.jme3.scene.Geometry;
/*     */ import com.jme3.scene.Node;
          import com.jme3.scene.shape.Sphere;
/**
 *
 * @author Karsten
 */
public class PathingSearch {
    
    private int size;
    private PathingMap pathingmap;
    private SearchNode nodeArray[][];
    private PriorityQueue<SearchNode> list;
    private int targetX,targetY;
    private SearchNode found;
    private float targetfX,targetfY;
    private GameApplication gameApp;
    
    private static ArrayList<Geometry> geometryList = new ArrayList();
    
    public PathingSearch(PathingMap pathingmap, GameApplication gameApp,int size,float x, float y, float tx, float ty)
    {
        this.size=size;
        this.gameApp=gameApp;
        this.targetfX=tx;
        this.targetfY=ty;
        this.pathingmap=pathingmap;
        nodeArray = new SearchNode[pathingmap.getWidth()][pathingmap.getHeight()];
        list = new PriorityQueue<SearchNode>();
        this.targetX=pathingmap.convertX(tx);
        this.targetY=pathingmap.convertY(ty);
        
        int xx = pathingmap.convertX(x);
        int yy = pathingmap.convertY(y);
        
        System.out.println(targetX + " " + targetY);
        System.out.println(xx + " " +yy);
        
        list.add(new SearchNode(null,0,xx,yy));
        

        if(check(targetX, targetY)>=size)
        {
            
            while(list.size()>0 && found==null)
            {
              //  System.out.println("loop");
                SearchNode sn = list.remove();
                sn.work();
            }
        }
        
    }
    
    
    private int check(float x, float y)
    {
        return pathingmap.getValue(x, y, PathingLayer.Ground, PathingMapName.UnitMap);
    }
    
    
    public boolean wayExists()
    {
        if(found!=null)
        {
            return true;
        }
        return false;
    }
    
    
    public ArrayList<Vector3f> getOrderList()
    {
        if(found == null){return null;}
        
        System.out.println("Weg gefunden!");
        
        while(geometryList.isEmpty()==false)
        {
            geometryList.get(0).removeFromParent();
            geometryList.remove(0);
        }
        
        
        ArrayList<Vector3f> orders = new ArrayList<Vector3f>();
        orders.add(new Vector3f(targetfX,0,targetfY));
        
        return orders;
        
        SearchNode sn = found.previous;
        while(sn.getPrevious() != null)
        {
            
            Vector3f v3f = new Vector3f(pathingmap.convertIntX(sn.x),0,pathingmap.convertIntY(sn.y));
            orders.add(0,v3f);

            //Draw Waypoints
            Sphere s = new Sphere(10,10,1f);
            Geometry geom = new Geometry("Sphere", s);
            Material mat = new Material(gameApp.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
            mat.setColor("Color", ColorRGBA.Red);
            geom.setMaterial(mat);
            geom.setLocalTranslation(v3f);
            gameApp.getRootNode().attachChild(geom);
            geometryList.add(geom);
            
          //  Model m = new Model("Models/BrutalLord/BrutalLord.j3o",gameApp,null);
          //  m.setTranslation((float)sn.x-(float)(pathingmap.getWidth()/2), (float)sn.y-(float)(pathingmap.getHeight()/2), 0.5f);
            
            
            sn=sn.getPrevious();
           // if(sn!=null)
           // {
          //      sn=sn.getPrevious();
         //   }else{
         //       orders.add(new Order("move",sn.x-(pathingmap.getWidth()/2),sn.y-pathingmap.getHeight()));
         //   }
        }
        return orders;
    }
    
    
    private class SearchNode implements Comparable
    {
        public int x,y;
        private float length;
        private SearchNode previous;
        private boolean worked=false;
        
        public SearchNode(SearchNode previous,float length,int x, int y)
        {
            this.previous=previous;
            this.x=x;
            this.y=y;
            this.length=length;
            
            if(nodeArray[x][y] == null)
            {
                nodeArray[x][y]=this;
                list.add(this);
            }else if(nodeArray[x][y].length > length){
                list.remove(nodeArray[x][y]);
                nodeArray[x][y]=this;
                list.add(this);
            }


            if(x==targetX && y==targetY)
            {
                found=this;
            }

        }
        
        public void work()
        {
            if(x>0) //Left
            {
                if((nodeArray[x-1][y]==null || nodeArray[x-1][y].hasWorked()==false) && check(x-1, y)>=size)
                {
                    new SearchNode(this,length+1,x-1,y);
                }
                
                if(y>0) //Left Top
                {
                    if((nodeArray[x-1][y-1]==null || nodeArray[x-1][y-1].hasWorked()==false) && check(x-1, y-1)>=size && check(x, y-1)>=size && check(x-1, y)>=size)
                    {
                        new SearchNode(this,length+1.4142f,x-1,y-1);
                    }
                }
                
                if(y<pathingmap.getHeight()-1) //Left Bottom
                {
                    if((nodeArray[x-1][y+1]==null || nodeArray[x-1][y+1].hasWorked()==false) && check(x-1, y+1)>=size && check(x, y+1)>=size && check(x-1, y)>=size)
                    {
                        new SearchNode(this,length+1.4142f,x-1,y+1);
                    }
                }
            }
            
            if(x<pathingmap.getWidth()-1) //Right
            {
                if((nodeArray[x+1][y]==null || nodeArray[x+1][y].hasWorked()==false) && check(x+1, y)>=size)
                {
                    new SearchNode(this,length+1,x+1,y);
                }
                
                if(y>0) //Rigth Top
                {
                    if((nodeArray[x+1][y-1]==null || nodeArray[x+1][y-1].hasWorked()==false) && check(x+1, y-1)>=size && check(x, y-1)>=size && check(x+1, y)>=size)
                    {
                        new SearchNode(this,length+1.4142f,x+1,y-1);
                    }
                }
                
                if(y<pathingmap.getHeight()-1) //Right Bottom
                {
                    if((nodeArray[x+1][y+1]==null || nodeArray[x+1][y+1].hasWorked()==false) && check(x+1, y+1)>=size && check(x, y+1)>=size && check(x+1, y)>=size)
                    {
                        new SearchNode(this,length+1.4142f,x+1,y+1);
                    }
                }
            }
            
           if(y>0) //Top
            {
                if((nodeArray[x][y-1]==null || nodeArray[x][y-1].hasWorked()==false) && check(x, y-1)>=size)
                {
                    new SearchNode(this,length+1,x,y-1);
                }
            }
            
            if(y<pathingmap.getHeight()-1) //Bottom
            {
                if((nodeArray[x][y+1]==null || nodeArray[x][y+1].hasWorked()==false) && check(x, y+1)>=size)
                {
                    new SearchNode(this,length+1,x,y+1);
                }
            }
            worked=true;
        }
        
        public SearchNode getPrevious()
        {
            return previous;
        }
        
        public boolean hasWorked()
        {
            return worked;
        }
        
        public int compareTo(Object o)
        {   
            return (int)(length -((SearchNode)o).length);
        }
    }
    
    
    
}
