/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.gui;

/**
 *
 * @author Karsten
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

import com.jme3.texture.Texture2D;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture;

import java.util.Properties;
import java.util.ArrayList;

import de.jpenguin.game.Game;
import de.jpenguin.game.GameApplication;
import de.jpenguin.player.Player;
import de.jpenguin.unit.Unit;
import de.jpenguin.type.AbilityType;
import de.jpenguin.type.UnitType;

import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectOnHoverAttributes;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.effects.Effect;

import de.lessvoid.nifty.builder.HoverEffectBuilder;

import java.util.LinkedList;
import de.lessvoid.nifty.effects.EffectProperties;
import de.lessvoid.nifty.tools.TimeProvider;
/**
 *
 * @author Karsten
 */
public class AbilityPanel implements Controller {
    
    private Element panel;
    private Nifty nifty;
    private Screen screen;
    private Game game;
    private AbilityImage images[];
    private AbilityType abilities[];
    
    private HintController hintController;
    
    public static AbilityPanel getAbilityPanel(Game game,Nifty nifty, Screen screen)
    {
         CustomControlCreator createMultiplayerPanel = new CustomControlCreator("myAbilityPanel", "abilityPanel");
         createMultiplayerPanel.create(nifty, screen, screen.findElementByName("box-ability"));
        
          
         Element e =nifty.getScreen("start").findElementByName("myAbilityPanel");
         AbilityPanel ap = (AbilityPanel)e.getAttachedInputControl().getController();
         ap.game=game;
         
         ap.images = new AbilityImage[12];
         ap.abilities = new AbilityType[12];
         for(int i=0;i<12;i++)
         {
            ap.images[i] = new AbilityImage(game);
            Texture2D texture = new Texture2D(512, 512, Format.Depth);
            texture.setImage(ap.images[i]);
             
          //  ap.elements[i] = nifty.getScreen("start").findElementByName("ability"+i);
            ImageRenderer mm = nifty.getScreen("start").findElementByName("ability"+i).getRenderer(ImageRenderer.class);
            ((DesktopAssetManager) game.getGameApplication().getAssetManager()).addToCache(new TextureKey("minimapkey"+i), texture);
            NiftyImage img = nifty.getRenderEngine().createImage("minimapkey"+i, false);
            mm.setImage(img); 
         }
         
           ap.hintController=screen.findControl("hintPanel", HintController.class);
           
         return ap;
    }
    

    public boolean inputEvent(final NiftyInputEvent inputEvent) {
	return false;
}

    public void click(String s)
    {
        int i = Integer.parseInt(s);
        
        game.getPlayerControler().abilityClick(abilities[i]);
        System.out.println("Affen werfen mit Kot " + abilities[i].getName());
    }
    
    public void loadUnit(Unit u)
    {
       ArrayList<AbilityType> list = u.getAbilities();

       for(int i=0;i<12;i++)
       {
            abilities[i] = null;
            hintController.removeKey("ability"+i);
        }
        
        for(int i=0;i<list.size();i++)
        {
            AbilityType at = list.get(i);
            images[at.getPosition()].setImage(at.getIcon());
            abilities[at.getPosition()] = at;
            
            hintController.putKey("ability"+at.getPosition(),at.getName() + "\n" + at.getDescription());
            /*
            HintEffect hintEffect = new HintEffect();
            Properties properties = new Properties();
            properties.put( "hintText" , "This is a test!" );
            Effect effect = new Effect( nifty, false, false, false, "", "", "", true, EffectEventId.onHover);
            effect.init( elements[at.getPosition()], hintEffect, new EffectProperties(properties), new TimeProvider(), new LinkedList<Object>() );

            elements[at.getPosition()].registerEffect( EffectEventId.onHover, effect );
            effect.enableInfinite();
             * 
             */

             
        }
    }

    
    public void clear()
    {
        for(int i=0;i<12;i++)
        {
            images[i].setImage("");
            abilities[i] = null;
        }
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
          
           System.out.println("123 " + nifty.getGlobalProperties().get("test"));
      }
      
      // @Override
    public void init(final Properties parameter, final Attributes controlDefinitionAttributes) {
       
    }

  
    public void onStartScreen() {
   // setDifficulty("easy");
  }
    


}
