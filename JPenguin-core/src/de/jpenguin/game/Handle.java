/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.game;

import java.util.Stack;
/**
 *
 * @author Karsten
 */
public abstract class Handle {
    
    private static int maxHandle=0;
    private static Stack<Integer> freeHandleStack=new Stack<Integer>();
    
    private int handleId;
    
    public Handle()
    {
        if(freeHandleStack.isEmpty())
        {
            handleId=maxHandle;
            maxHandle++;
        }else{
            handleId = freeHandleStack.pop();
        }
    }
    
    public int getHandleId()
    {
        return handleId;
    }
    
    protected void removeHandle()
    {
        freeHandleStack.push(handleId);
        handleId=-1;
        
    }
}
