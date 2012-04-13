/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.fog;

/**
 *
 * @author Karsten
 */
public abstract class FogOfWarEffect {
    
    public void startChange()
    {

    }
    
    public void update(float tpf)
    {

    } 
    
    
    public abstract void valueChange(int x, int y, int value, int oldvalue);
    
    public void endChange()
    {
        
    }
    
    public void destroy()
    {
        
    }
}
