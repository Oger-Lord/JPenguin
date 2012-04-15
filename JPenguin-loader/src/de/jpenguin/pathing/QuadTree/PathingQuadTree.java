/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.pathing.QuadTree;

/**
 *
 * @author Karsten
 */
public interface PathingQuadTree{
    
    public int getValue(int x, int y, int size);
    public void setValue(int x, int y, int size, int value, int depth);
}
