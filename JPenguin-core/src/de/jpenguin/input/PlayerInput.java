/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.input;

import de.jpenguin.game.Game;
import de.jpenguin.player.PlayerControler;
import com.jme3.math.Vector3f;
import com.jme3.input.*;
import com.jme3.input.event.*;
import com.jme3.input.controls.*;
import de.jpenguin.game.GameApplication;
import de.jpenguin.unit.Unit;
import de.jpenguin.player.Player;


/**
 *
 * @author Karsten
 */
public class PlayerInput implements RawInputListener {
    
    private InputManager inputManager;
    private MouseStatus mouseStatus;
    private Game game;
    private GameApplication gameApp;

    
    
    public PlayerInput(Game game)
    {
        this.game=game;
        this.gameApp =  game.getGameApplication();
        inputManager = gameApp.getInputManager();
        inputManager.addRawInputListener(this);
         mouseStatus = new MouseStatus(null,0,0);
    }
    
    public void beginInput()
    {
        mouseStatus = gameApp.getMouseCollision();
        PlayerControler p = game.getPlayerControler();
        
        if(mouseStatus != null)
        {
            if(mouseStatus.getModel() != null)
            {
                p.mouseHover((Unit)mouseStatus.getModel().getParent());
            }else{
                p.mouseHover(null);
            }
        }else{
            p.mouseHover(null);
        }
    }
    
    public void endInput()
    {
    }
    
     public void onMouseMotionEvent(MouseMotionEvent event) {}
     
     public void onKeyEvent(KeyInputEvent event)
     {
         // System.out.println("Key Event!");
     }
     
     
     public void onMouseButtonEvent(MouseButtonEvent event)
     {
        if(event.isReleased()){return;}
        if(mouseStatus==null){return;}
        PlayerControler p = game.getPlayerControler();
         
        if(event.getButtonIndex() == 0) //links
        {
             if(mouseStatus.getModel() == null)
             {
                 p.mouseClick(0,mouseStatus.getX(), mouseStatus.getY());
             }else{
                 p.mouseClick(0,(Unit)mouseStatus.getModel().getParent());
             }
        }else if(event.getButtonIndex() == 1) //rechts
        {    
             if(mouseStatus.getModel() == null)
             {
                 p.mouseClick(1,mouseStatus.getX(), mouseStatus.getY());
             }else{
                 p.mouseClick(1,(Unit)mouseStatus.getModel().getParent());
             }
        }else{ //mitte
             
        }
     }
     
     
     public void onJoyAxisEvent(JoyAxisEvent event)
     {
         
     }
     
     public void onJoyButtonEvent(JoyButtonEvent event)
     {
         
     }
     
     public void onTouchEvent(TouchEvent event)
     {
         
     }
}
   