/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.menu;

/**
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.controls.dynamic.CustomControlCreator;
import de.lessvoid.nifty.controls.ListBox;

import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.NiftyEventSubscriber;

import com.jme3.math.ColorRGBA;
import com.jme3.system.AppSettings;

import java.util.List;



public class Menu implements ScreenController {

    private Nifty nifty;


    public void bind(Nifty nifty, Screen screen) {
        this.nifty=nifty;
    }
    

    public void onStartScreen() {
        System.out.println("onStartScreen");
    }

    public void onEndScreen() {
        System.out.println("onEndScreen");
    }
    
    public void selectMap()
    {
        nifty.gotoScreen("mapselection");
        MenuApp.getInstance().getNifty().resolutionChanged();
    }
    
    public void multiplayer()
    {
        nifty.gotoScreen("multiplayer");
        MenuApp.getInstance().getNifty().resolutionChanged();
    }
    
    public void options()
    {
        nifty.gotoScreen("options");
    }

    public void exit(){
        System.exit(0);
    }

}

