/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.gui.chat;

/**
 *
 * @author Karsten
 */
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.asset.TextureKey;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.elements.render.*;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.NiftyInputControl;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.xml.xpp3.Attributes;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.controls.dynamic.CustomControlCreator;

import java.util.Properties;
import java.util.ArrayList;

import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.dynamic.TextCreator;
import de.lessvoid.nifty.controls.dynamic.PanelCreator;

public class ChatOutputController implements Controller{
    
   private Nifty nifty;
   private Screen screen;
   private Element parent;
    
   private ArrayList<Element> panels;
    
  public boolean inputEvent(final NiftyInputEvent inputEvent) {
	return false;
    }
    
    @Override
    public void onFocus(final boolean getFocus) {
    }

      
      public void bind(
 	final Nifty nifty,
	final Screen screen,
	final Element element,
	final Properties parameter,
	final Attributes controlDefinitionAttributes) {
          
          panels = new ArrayList();
          
          this.nifty=nifty;
          this.screen=screen;
          this.parent=element;
      }
      
    
    public void addRow(String text)
    {
        PanelCreator createPanel = new PanelCreator();
        createPanel.setChildLayout("horizontal");
        panels.add(0,createPanel.create(nifty, screen, parent));
          
        TextCreator createLabel = new TextCreator("");
        createLabel.setAlign("left");
        createLabel.setTextVAlign("bottom");
        createLabel.setTextHAlign("left");
        createLabel.setStyle("my");
        createLabel.setText(text);
        Element label =createLabel.create(nifty, screen, panels.get(0));
        
        if(panels.size() > 20)
        {
            nifty.removeElement(screen, panels.get(20));
            panels.remove(20);
        }
    }
    
    
    // @Override
    public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
    }

  
    public void onStartScreen() {
   // setDifficulty("easy");
  }

}
