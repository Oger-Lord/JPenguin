/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.ai.steering.behaviour;

/**
 *
 * @author Karsten
 */
 import com.jme3.ai.steering.Obstacle;
 import com.jme3.math.Plane;
 import com.jme3.math.Plane.Side;
 import com.jme3.math.Vector3f;
 import com.jme3.math.Line;
 import java.util.List;
 
 public class ObstacleAvoidNew
   implements Behaviour
 {
    
   public Vector3f calculateForce(Vector3f location, Vector3f velocity, float collisionRadius, float speed, float turnSpeed, float tpf, List<Obstacle> obstacles)
   {
        float cautionRange = speed*1.5f / turnSpeed;
        Line line = new Line(location, velocity.normalize());
        Plane plane = new Plane(velocity, 1);
        Vector3f closest = Vector3f.ZERO;
        float shortestDistance=-1;
 
        for (Obstacle obs : obstacles) {
            
            Vector3f target = obs.getLocation();
            
            Vector3f loc = target.subtract(location);
 
            // If the obstacle isn't ahead of him, just ignore it
            if (plane.whichSide(loc) != Side.Positive) {
                continue;
            }
 
            // Check if the target is inside the check range
            if (location.distance(target) <= collisionRadius+cautionRange+obs.getRadius()) {
                
                // Check if the target will collide with the source
                if (obs.distance(line) < collisionRadius) {
                    
                    float newDistance = obs.distance(location);
                    // Store the closest target
                    if ( shortestDistance==-1 ||  newDistance<shortestDistance)
                        shortestDistance =newDistance;
                        closest = target;
                }
            }
        }
 
        // If any target was found
        if (shortestDistance!=-1) {
 
            // Find in wich side the target is
            // To do that, we do a signed distance by
            // subtracing the location from the target
            // and the dot product between the line's normal
            float dot = closest.subtract(location).dot(line.getDirection().cross(Vector3f.UNIT_Y));
 
            if (dot <= 0)
                return velocity.cross(Vector3f.UNIT_Y);
            else
                return velocity.cross(Vector3f.UNIT_Y).negate();
 
        }
 
// No target found, just return a zero value
        return Vector3f.ZERO;
   }
 }
