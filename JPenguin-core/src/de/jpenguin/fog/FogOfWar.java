/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.fog;

/**
 *
 * @author Karsten
 */
public class FogOfWar {
    
    private static int width;
    private static int height;
    private static int size;
    
    //Never ever call this
    public static void setSize(int width,int height,int size)
    {
        FogOfWar.width=width;
        FogOfWar.height=height;
        FogOfWar.size=size;
    }
    
    public static int getWidth()
    {
        return width;
    }
    
    public static int getHeight()
    {
        return height;
    }
    
    public static int getSize()
    {
        return size;
    }
    
    public static int convertX(float x)
    {
        return (int)((x+size/2)/size*width+0.5);
    }
    
    public static int convertY(float y)
    {
        return (int)((y+size/2)/size*height+0.5);
    }
    
    
}
