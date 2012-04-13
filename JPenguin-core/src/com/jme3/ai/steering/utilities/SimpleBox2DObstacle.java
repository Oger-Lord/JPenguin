/*
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.ai.steering.utilities;

import com.jme3.ai.steering.Obstacle;
import com.jme3.math.Vector3f;
import com.jme3.math.Line;
import com.jme3.math.LineSegment;
import com.jme3.math.Ray;
import com.jme3.ai.steering.utilities.SimpleBox2DObstacle;

/**
 *
 * @author Karsten
 */
public class SimpleBox2DObstacle implements Obstacle {
    
    private Vector3f location;
    private float size;
    private float radius;
    
   public static void main(String[] args) {
        SimpleBox2DObstacle box =new SimpleBox2DObstacle(Vector3f.ZERO, 1f);
        
        for(int i=0;i<360;i+=10)
        {
            Vector3f v3f = new Vector3f(0,0,0);
            v3f.setZ((float)(Math.cos(i*(Math.PI/180))*10));
            v3f.setX((float)(Math.sin(i*(Math.PI/180))*10));
            
            System.out.println(" "+box.distance(v3f));
        }
    }
    
    //size*2 = 
    public SimpleBox2DObstacle(Vector3f location, float sideLength)
    {
        this.location=location;
        this.size=sideLength/2;
        
        radius = (float)Math.sqrt(2)*size;
    }
    
    public float getRadius()
    {
        return radius;
    }
    
    public float distance(Line line)
    {
        float shortestDistance = line.distance(location.add(-size, 0,-size));
        
        float newDistance = line.distance(location.add(size, 0,-size));
        if(newDistance < shortestDistance)
            shortestDistance=newDistance;
        
        newDistance = line.distance(location.add(size, 0,size));
        if(newDistance < shortestDistance)
            shortestDistance=newDistance;
        
        newDistance = line.distance(location.add(-size, 0,size));
        if(newDistance < shortestDistance)
            shortestDistance=newDistance;
        
        return shortestDistance;
    }
    
    
    public float distance(Vector3f point)
    {
        Vector3f distance = point.subtract(location);
        
        if(Math.abs(distance.x) > Math.abs(distance.z)) //top or bottom
        {
            if(distance.x > 0) //top
            {
                System.out.println("top");
                return xGetShortestDistance(point,1);
            }else{ //bottom
                System.out.println("bottom");
                 return xGetShortestDistance(point,-1);
            }
        }else{  //left or right
            if(distance.z > 0)  //right
            {
                System.out.println("right");
                 return yGetShortestDistance(point,1);
            }else{  //left
                System.out.println("left");
                return yGetShortestDistance(point,-1);
            }  
        }
    }
    
    
    //a=1: top  a=-1 : bottom
    private float xGetShortestDistance(Vector3f point, int a)
    {
        float shortestDistance = point.distance(location.add(size*a, 0,-size));
        
        float newDistance = point.distance(location.add(size*a, 0,0));
        if(newDistance < shortestDistance)
            shortestDistance=newDistance;
        
        newDistance = point.distance(location.add(size*a, 0,size));
        if(newDistance < shortestDistance)
            shortestDistance=newDistance;
        
        return shortestDistance;
    }
    
    //a=-1: left  a=1 : right
    private float yGetShortestDistance(Vector3f point, int a)
    {
        float shortestDistance = point.distance(location.add(size, 0,size*a));
        
        float newDistance = point.distance(location.add(0, 0,size*a));
        if(newDistance < shortestDistance)
            shortestDistance=newDistance;
        
        newDistance = point.distance(location.add(-size, 0,size*a));
        if(newDistance < shortestDistance)
            shortestDistance=newDistance;
        
        return shortestDistance;
    }
    
    public Vector3f getVelocity()
    {
         return Vector3f.ZERO.clone();
    }

     public Vector3f getLocation()
    {
        return this.location;
     }
     

    
}
