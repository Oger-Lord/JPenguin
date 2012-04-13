/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.gui;

import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.asset.TextureKey;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.elements.render.*;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.elements.render.TextRenderer;
//import de.lessvoid.nifty.
import de.lessvoid.xml.xpp3.Attributes;
import java.util.Properties;

import de.lessvoid.nifty.controls.dynamic.CustomControlCreator;

import de.jpenguin.game.Game;
import de.jpenguin.game.GameApplication;
import de.jpenguin.player.Player;
import de.jpenguin.unit.Buff;
import de.jpenguin.unit.Unit;
import de.jpenguin.type.UnitType;
import de.jpenguin.type.BuffType;
import de.lessvoid.nifty.tools.Color;

import de.lessvoid.nifty.controls.dynamic.ImageCreator;
import de.lessvoid.nifty.controls.dynamic.PanelCreator;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlHoverAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectOnHoverAttributes;

import de.lessvoid.nifty.loaderv2.types.EffectValueType;

import java.util.ArrayList;
/**
 *
 * @author Karsten
 */
public class UnitPanel {
    
    private Element panel;
    private Nifty nifty;
    private Screen screen;
    
    private Element buffPanel;
    
    private ArrayList<Element> buffImagePanels;
    
    public UnitPanel(Nifty nifty, Screen screen, Unit u)
    {
        buffImagePanels = new ArrayList();
        
        this.nifty=nifty;
        this.screen=screen;
        CustomControlCreator createMultiplayerPanel = new CustomControlCreator("myUnitPanel", "unitPanel");
        panel =createMultiplayerPanel.create(nifty, screen, screen.findElementByName("box-parent"));

        setUnit(u);
    }
    
    
    public void setUnit(Unit u)
    {
        for(int i=0;i<buffImagePanels.size();i++)
        {
            nifty.removeElement(screen, buffImagePanels.get(i));
        }
        buffImagePanels.clear();
        
        TextRenderer name  = nifty.getScreen("start").findElementByName("labelunitname").getRenderer(TextRenderer.class);
        TextRenderer damage  = nifty.getScreen("start").findElementByName("labelunitdamage").getRenderer(TextRenderer.class);
        TextRenderer armor  = nifty.getScreen("start").findElementByName("labelunitarmor").getRenderer(TextRenderer.class);
        buffPanel = nifty.getScreen("start").findElementByName("buffPanel");
        
        name.setText(u.getUnitType().getName());
        damage.setText((int)u.getUnitType().getAttackDamage()+"");
        armor.setText((int)u.getUnitType().getArmor()+"");
        
        ArrayList<Buff> buffs =u.getBuffs();
        for(int i=0;i<buffs.size();i++)
        {
            addBuff(buffs.get(i).getBuffType());
        }
    }
    
    
    public void addBuff(BuffType bt)
    {
        PanelCreator createPanel = new PanelCreator();
        createPanel.setChildLayout("center");
        createPanel.setWidth("25px");
        createPanel.setAlign("left");
        Element newPanel =createPanel.create(nifty, screen, buffPanel);
          
        ImageCreator createImage = new ImageCreator();
        createImage.setFilename(bt.getIcon());
        createImage.setHeight("24px");
        createImage.setWidth("24px");
        createImage.setAlign("left");
        
       // createImage.
        
        createImage.setInteractOnClick("bla()");
        
        ControlEffectOnHoverAttributes cea = new ControlEffectOnHoverAttributes();
        cea.setName("myhint");
        cea.setAttribute("hintText", bt.getName() +"\n"+bt.getDescription());
        createImage.addEffectsOnHover(cea);
        
        Element image =createImage.create(nifty, screen, newPanel);
        
        buffImagePanels.add(newPanel);
    }
    
    
    public void remove()
    {
        nifty.removeElement(screen, panel);
    }
}
