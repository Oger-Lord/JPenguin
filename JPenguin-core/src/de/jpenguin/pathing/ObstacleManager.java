/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.pathing;

import java.util.ArrayList;
import com.jme3.ai.steering.Obstacle;

import com.jme3.math.Vector3f;
/**
 *
 * @author Karsten
 */
public class ObstacleManager {
    
    private ArrayList<ObstacleChange>obstacleChanges;
    private ArrayList<Obstacle> obstacleMap[][];
    private int width,height;
    
    private class ObstacleChange
    {
        private Obstacle obstacle;
        private int order;
        private Vector3f vector;
        
        public ObstacleChange(Obstacle obs, int i)
        {
            obstacle=obs;
            order=i;
            obstacleChanges.add(this);
        }
        
        public ObstacleChange(Obstacle obs, Vector3f v3f)
        {
            obstacle=obs;
            vector=v3f;
            obstacleChanges.add(this);
        }
    }
    
    //
    
    public ObstacleManager()
    {
        width=64;
        height=64;
        
        obstacleChanges = new ArrayList();
        
        obstacleMap = new ArrayList[width][height];
        for(int x=0;x<width;x++)
        {
            for(int y=0;y<height;y++)
            {
                obstacleMap[x][y] = new ArrayList();
            }
        }
    }
    
    public synchronized void update()
    {
        while(obstacleChanges.isEmpty()==false)
        {
           ObstacleChange oc= obstacleChanges.get(0);
           if(oc.order==0)
           {
            //   oc.obstacle.getLocation()
           }
        }
    }
    
    public synchronized void addObstacle(Obstacle o)
    {
        
    }
    
    public synchronized void removeObstacle(Obstacle o)
    {
        
    }
    
    public synchronized void moveObstacle(Obstacle o, Vector3f newlocation)
    {
        
    }
    
}
