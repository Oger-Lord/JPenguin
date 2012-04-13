/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.ai.steering;

import com.jme3.math.Vector3f;

import com.jme3.math.Line;



/**
 * Used in Obstacle avoidance. Contains basic location information.
 * 
 * @author Brent Owens
 */
public interface Obstacle {
    
    /**
     * The world velocity of the obstacle
     */
    public Vector3f getVelocity();
    
    /**
     * The world location of the obstacle
     */
    public Vector3f getLocation();
    
    /**
     * The collision radius of the obstacle
     */
    public float getRadius();
    
      public float distance(Vector3f location);
  
    public float distance(Line line);
}
