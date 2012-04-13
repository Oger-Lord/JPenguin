 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.gui;

/**
 *
 * @author Karsten
 */
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.xml.xpp3.Attributes;

import java.util.Properties;
import java.util.ArrayList;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import de.lessvoid.nifty.elements.render.TextRenderer;

import de.lessvoid.nifty.input.mapping.DefaultInputMapping;

import de.jpenguin.game.Game;
import de.jpenguin.player.PlayerCommandChat;

import de.lessvoid.nifty.controls.dynamic.TextCreator;
import de.lessvoid.nifty.controls.dynamic.PanelCreator;

import de.lessvoid.nifty.tools.SizeValue;

import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.effects.EffectEventId;

import de.lessvoid.nifty.effects.Effect;

import java.util.Hashtable;

public class HintController implements Controller{ 
    
    private TextRenderer text;
    private Element panel;

    private Hashtable<String,String> valueChanges;
      
    public void onStartScreen() {
  }
    
    public HintController()
    {
        valueChanges = new Hashtable();
    }

    @Override
    public void bind(Nifty nifty, Screen screen, Element element, Properties parameter, Attributes controlDefinitionAttributes) {

        text = nifty.getScreen("start").findElementByName("hintText").getRenderer(TextRenderer.class);
        panel = element;
        panel.hide();
    }

    @Override
    public void init(Properties parameter, Attributes controlDefinitionAttributes) {
    }

    @Override
    public void onFocus(boolean getFocus) {
    }
    
     public void setHintKey(String key)
    {
        String s =valueChanges.get(key);
        if(s != null)
        {
            text.setText(s);
            panel.show();
            panel.getParent().layoutElements();
        }
    }
    
    public void setHint(String s)
    {
        
        text.setText(s);
        panel.show();
        panel.getParent().layoutElements();
    }
    
    public void removeHint()
    {
      //  nifty.removeElement(screen, newPanel);
        panel.hide();
    }
    

    @Override
    public boolean inputEvent(NiftyInputEvent inputEvent) {
        return false;
    }
    
    public void putKey(String key, String value)
    {
        valueChanges.put(key,value);
    }
    
    public void removeKey(String key)
    {
        valueChanges.remove(key);
    }
}
