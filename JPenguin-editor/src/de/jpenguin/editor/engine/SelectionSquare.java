/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.editor.engine;

import de.jpenguin.editor.engine.EditorApplication;
import com.jme3.ui.Picture;

/**
 *
 * @author Karsten
 */
public class SelectionSquare {
    
    private Picture[] pictures;
    private int startx,starty;
    private int x,y;
    private int width,height;
    private boolean firstUpdate=true;
    private EditorApplication editorApp;
    
    public SelectionSquare(int x, int y)
    {
        pictures = new Picture[4];
        this.startx = x;
        this.starty = y;//editorApp.getSettings().getHeight()-y;
    }
    
    
    private void init(EditorApplication editorApp)
    {
        this.editorApp = editorApp;
        //top
        pictures[0] = new Picture("HUD Picture");
        pictures[0].setImage( editorApp.getAssetManager(), "Textures/green_pixel.png", true);
        pictures[0].setHeight(1);
        editorApp.getGuiNode().attachChild(pictures[0]);
       
        //right
        pictures[1] = new Picture("HUD Picture");
        pictures[1].setImage( editorApp.getAssetManager(), "Textures/green_pixel.png", true);
        pictures[1].setWidth(1);
        editorApp.getGuiNode().attachChild(pictures[1]);
        
        //bottom
        pictures[2] = new Picture("HUD Picture");
        pictures[2].setImage( editorApp.getAssetManager(), "Textures/green_pixel.png", true);
        pictures[2].setHeight(1);
        editorApp.getGuiNode().attachChild(pictures[2]);
        
        //left
        pictures[3] = new Picture("HUD Picture");
        pictures[3].setImage( editorApp.getAssetManager(), "Textures/green_pixel.png", true);
        pictures[3].setWidth(1);
        editorApp.getGuiNode().attachChild(pictures[3]);

    }
    
    public void update(EditorApplication editorApp,int newx, int newy)
    {
      //  newy = editorApp.getSettings().getHeight()-newy;
        if(firstUpdate)
        {
            firstUpdate=false;
            init(editorApp);
        }
        
        if(newx < startx)
        {
            x = newx;
            width = startx-x;
        }else{
            x = startx;
            width = newx-x;
        }
        
        if(newy < starty)
        {
            y = newy;
            height= starty-y;
        }else{
            y = starty;
            height= newy-y;
        }
        
        //top
        pictures[0].setWidth(width);
        pictures[0].setPosition(x, y);
        
        //right
        pictures[1].setHeight(height);
        pictures[1].setPosition(x+width, y);
        
        //bottom
        pictures[2].setWidth(width);
        pictures[2].setPosition(x, y+height);
        
        //left
        pictures[3].setHeight(height);
        pictures[3].setPosition(x, y);
    }
    
    
    public int getX()
    {
        return startx;
    }
    
    
    public int getY()
    {
        return starty;
    }
    
    
    public void clear()
    {
        for(int i=0;i<4;i++)
        {
            editorApp.getGuiNode().detachChild(pictures[i]);
        }
    }
    
}
