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

import java.util.*;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jme3.math.ColorRGBA;

import com.jme3.asset.*;

import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.NiftyEventSubscriber;

import com.jme3.math.ColorRGBA;
import com.jme3.system.AppSettings;

import de.jpenguin.loader.*;

import java.util.List;

import de.lessvoid.nifty.elements.render.*;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.tools.Color;

import de.lessvoid.nifty.tools.Color;


public class Lobby implements ScreenController {

    private Nifty nifty;


    public void bind(Nifty nifty, Screen screen) {
        this.nifty=nifty;
        
      //  CustomControlCreator createMultiplayerPanel = new CustomControlCreator("myPlayerPanel", "playerPanel");
     //   createMultiplayerPanel.create(nifty, screen, screen.findElementByName("slot0"));
        playerLoader(MenuApp.getInstance(), screen,"Map1");
    }
    

    public void onStartScreen() {
        System.out.println("onStartScreen");
    }

    public void onEndScreen() {
        System.out.println("onEndScreen");
    }
    
    public void host()
    {
        nifty.gotoScreen("mapselection");
        MenuApp.getInstance().getNifty().resolutionChanged();
    }
    
    public void join()
    {
      //  nifty.gotoScreen("options");
    }

    public void back(){
        nifty.gotoScreen("start");
    }
    
    
    public void playerLoader(MenuApp menuApp,Screen screen,String map)
    {
        
        AssetManager as = menuApp.getAssetManager();
        
        
        PlayerDataManager pdm =PlayerDataManager.load(as);
        PlayerDataMapManager pdmm =PlayerDataMapManager.load(as,map);
        
        ArrayList<PlayerData> list = pdm.getPlayer();
        
        int id=0;
        
        
        int countTeams=0;
        for(int i=0;i<list.size();i++)
        {
            PlayerData playerData = list.get(i);
            
            PlayerDataMap playerDataMap = pdmm.getPlayer().get(playerData.getId());
            
            if(playerDataMap != null)
            {
                if(playerDataMap.getController() != PlayerDataMap.none)
                {
                    countTeams++;
                }
            }
        }

        
        for(int i=0;i<list.size();i++)
        {
            PlayerData playerData = list.get(i);
            PlayerDataMap playerDataMap = pdmm.getPlayer().get(playerData.getId());
            
            if(playerDataMap != null)
            {
                if(playerDataMap.getController() != PlayerDataMap.none)
                {
                    id++;

                     CustomControlCreator createMultiplayerPanel = new CustomControlCreator("myPlayerPanel", "playerPanel");
                     de.lessvoid.nifty.elements.Element e= createMultiplayerPanel.create(nifty, screen, screen.findElementByName("slot"+i));

                     
                     
                     
                     
                     TextField tf =e.findNiftyControl("id", TextField.class);
                     tf.setText(id+"");
                     tf.setEnabled(false);

                     tf =e.findNiftyControl("playername", TextField.class);
                     tf.setText(playerData.getName());
                     tf.setEnabled(false);


                     DropDown dd =e.findNiftyControl("team", DropDown.class);
                     for(int b=0;b<countTeams;b++)
                     {
                         dd.addItem("Team " + (b+1));
                     }
                     //dd.setEnabled(false);

                     PanelRenderer colorPanel =e.findElementByName("color").getRenderer(PanelRenderer.class);
                     Color color = new Color(playerData.getColor().r,playerData.getColor().g,playerData.getColor().b,playerData.getColor().a);
                     colorPanel.setBackgroundColor(color);

                } 
            }
        }
        
    }

}

