/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.engine;

import com.jme3.scene.*;
import com.jme3.animation.*;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.math.*;
import com.jme3.export.*;
import java.io.*;
import de.jpenguin.game.GameApplication;
/**
 *
 * @author Karsten
 */
public class MyAnimControl implements AnimEventListener {
    
    private AnimControl control;
    private AnimChannel channel;
    private AnimBox animBox;
    
    private String currentAnimation="";
    
    public MyAnimControl(Spatial sp)
    {
        control = sp.getControl(AnimControl.class);
        if(control != null){
             animBox = new AnimBox(control.getAnimationNames());
             control.addListener(this);
             channel = control.createChannel();
             setAnimation("stand", true);
        }
    }
    
    public String getAnimation()
    {
        return currentAnimation;
    }

    
    public void setAnimation(String s, boolean alwaysStart)
    {
        if(channel != null)
        {
            if(!currentAnimation.equals(s) || alwaysStart)
            {
                if(animBox.getAnimation(s) != null)
                {
                    currentAnimation=s;
                    String newAnim = animBox.getAnimation(s);
                    if(newAnim.equals(channel.getAnimationName()) == false)
                    {    
                      //  System.out.println(newAnim);
                        channel.setAnim(newAnim);
                        channel.setSpeed(1);
                    }
                }
            }
        }
    }
    
    public void setAnimation(String s, float time)
    {
        if(channel != null)
        {
            System.out.println("nice try"+s);
           if(animBox.getAnimation(s) != null)
           {
              // System.out.println("timed animation "+ s + " " +animBox.getAnimation(s));
               currentAnimation=s;
                channel.setAnim(animBox.getAnimation(s));
                channel.setSpeed(channel.getAnimMaxTime()/time);
           }
        }
    }
    
    public void resetAnimation()
    {
        if(channel != null)
        {
            if(channel.getAnimationName().isEmpty()==false)
            {
                channel.setAnim(channel.getAnimationName());
            }
        }
    }
    
    public void onAnimChange(AnimControl control, AnimChannel channel, java.lang.String animName)
    {  
    }        
    
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, java.lang.String animName)
    {
      setAnimation(currentAnimation, true);
    }
}
