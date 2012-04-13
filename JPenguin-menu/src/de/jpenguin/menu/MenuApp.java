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

import java.io.*;



public class MenuApp extends SimpleApplication {

    private Nifty nifty;
    private static MenuApp menuApp;
    
    /*
    public static void main(String[] args){
        MenuApp app = new MenuApp();
       // app.setPauseOnLostFocus(false);
        app.start();
    }
     * 
     */
    
    public MenuApp()
    {
        
       setShowSettings(false);
       setDisplayFps(false);
       setDisplayStatView(false);
        
       try{
            FileInputStream fis = new FileInputStream(new File("settings.txt"));
            AppSettings settings = new AppSettings(true);
            settings.load(new BufferedInputStream(fis));
            setSettings(settings);
        }catch(Exception e){
            AppSettings settings = new AppSettings(true);
            settings.setResolution(1024,768);
            settings.setFrameRate(50);
            settings.setFullscreen(false);
            settings.setTitle("JPenguin");

            setSettings(settings);
        }
        
        menuApp = this;
    }

    public void simpleInitApp() {
         
        
        new Scene(this);
                

        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
                                                          inputManager,
                                                          audioRenderer,
                                                          guiViewPort);
        nifty = niftyDisplay.getNifty();
       // nifty.fromXml("Interface/Menu/menu.xml", "start");
        nifty.addXml("Interface/Menu/menu.xml");
        nifty.addXml("Interface/Menu/mapselection.xml");
        nifty.addXml("Interface/Menu/options.xml");
        nifty.addXml("Interface/Menu/multiplayer.xml");
        nifty.addXml("Interface/Menu/lobby.xml");
        nifty.addXml("Interface/Menu/controller/lobbyplayer.xml");
        nifty.gotoScreen("start");
        
       // nifty.addXml("Interface/IngameGui/ingame.xml"); //<-Screen controller
       
      
        

      //  nifty.addXml(INPUT_MAPPING_EXIT)
        
        // attach the nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay);

        // disable the fly cam
        flyCam.setEnabled(false);
//        flyCam.setDragToRotate(true);
        inputManager.setCursorVisible(true);
        
        
    }
    
    public static MenuApp getInstance()
    {
        return menuApp;
    }
    
    public AppSettings getSettings()
    {
        return settings;
    }
    
    public Nifty getNifty()
    {
        return nifty;
    }

}

