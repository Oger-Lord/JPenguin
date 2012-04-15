/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.pathing.QuadTree;

/**
 *
 * @author Karsten
 */
public class PathingQuadLeave implements PathingQuadTree {
    
    private int value;
    
    public PathingQuadLeave(int value)
    {
        this.value=value;
    }

    /**
     * @return the value
     */
    public int getValue(int x, int y, int size) {
        return value;
    }

    public void setValue(int x, int y, int size, int value, int depth) {
        
    }


    
}
