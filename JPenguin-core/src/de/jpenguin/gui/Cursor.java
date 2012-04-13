/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.gui;

/**
 *
 * @author Karsten
 */
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;
import com.jme3.math.Vector2f;

import com.jme3.input.RawInputListener;
import com.jme3.input.event.*;
import com.jme3.math.FastMath;
import com.jme3.renderer.queue.RenderQueue.Bucket;

import de.jpenguin.game.GameApplication;
import de.jpenguin.engine.CursorManager;

public class Cursor {

    private GameApplication gameApp;
    private CursorManager cm;
    private String status="";
    private String name="cursor";
    
    public Cursor(GameApplication gameApp)
    {
        cm = new CursorManager(gameApp.getAssetManager());
        CursorManager.precache("Interface/Cursor/cursor.gif", 0,32);
        CursorManager.precache("Interface/Cursor/cursor_enemy.gif", 0,32);
        CursorManager.precache("Interface/Cursor/cursor_friend.gif", 0,32);
        CursorManager.precache("Interface/Cursor/cursor_neutral.gif", 0,32);
        CursorManager.precache("Interface/Cursor/cross.gif", 16,16);
        CursorManager.precache("Interface/Cursor/cross_enemy.gif", 16,16);
        CursorManager.precache("Interface/Cursor/cross_friend.gif", 16,16);
        CursorManager.precache("Interface/Cursor/cross_neutral.gif", 16,16);
        
        this.gameApp = gameApp;
        
        update();
 
    }
    
    public void setImage(String name)
    {
        this.name=name;
        update();
    }
    
    public void setStatus(String status)
    {
        this.status=status;
        update();
    }
    
    private void update()
    {
        if(status.equals(""))
        {
            CursorManager.setHardwareCursor("Interface/Cursor/"+name+".gif", 0,32);
        }else{
            CursorManager.setHardwareCursor("Interface/Cursor/"+name+"_"+status+".gif", 0,32);
        }
    }

       
}
