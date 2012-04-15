/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.pathing.QuadTree;

/**
 *
 * @author Karsten
 */
public class PathingQuad implements PathingQuadTree {
    
    private PathingQuadTree subTrees[];
    
    public PathingQuad(int value)
    {
        subTrees = new PathingQuad[4];
        subTrees[0] = new PathingQuadLeave(value);
        subTrees[1] = new PathingQuadLeave(value);
        subTrees[2] = new PathingQuadLeave(value);
        subTrees[3] = new PathingQuadLeave(value);
    }
    
    /*
    public PathingQuad()
    {
        int depth;
        
        int size;
        
        if(size%4 != 0)
        {
            size *=2;
        }
        
        depth = Math.log(size)/Math.log(4);
    }
     * 
     */

    public int getValue(int x, int y, int size) {
        if(x > 0)
        {
            if(y > 0)
            {
                return subTrees[0].getValue(x-size/2, y-size/2,size/2);
            }else{
                return subTrees[1].getValue(x-size/2, y+size/2,size/2);
            }
        }else{
            if(y > 0)
            {
                return subTrees[2].getValue(x+size/2, y-size/2,size/2); 
            }else{
                return subTrees[3].getValue(x+size/2, y+size/2,size/2);
            }   
        }
    }
    
    public void setValue(int x, int y, int size, int value, int depth) {
        int newx,newy;
        int subTreeId;
        
        if(x > 0)
        {
            if(y > 0)
            {
                
            }else{
                
            }
        }else{
            if(y > 0)
            {
                
            }else{
                
            }   
        } 
        
        if(depth==0 && subTrees[subTreeId] instanceof PathingQuadLeave)
        {
            subTrees[subTreeId] = new PathingQuad(subTrees[subTreeId].getValue(0, 0, 0));
        }
        
        subTrees[subTreeId].setValue(newx, newy, size/2, value, depth-1);
    }
 
    
}
