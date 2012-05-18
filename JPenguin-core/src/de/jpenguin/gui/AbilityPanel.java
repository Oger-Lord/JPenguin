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


/**
 *
 * @author Karsten
 */
public class AbilityPanel extends GUIPlugin {
    
    private AbilityImage images[];
    private AbilityType abilities[];
    
    private HintController hintController;
    
    public static void createAbilityPanel(Nifty nifty, Screen screen)
    {
         CustomControlCreator createMultiplayerPanel = new CustomControlCreator("myAbilityPanel", "abilityPanel");
         createMultiplayerPanel.create(nifty, screen, screen.findElementByName("box-ability"));
    }
    
    public void bind(
 	final Nifty nifty,
	final Screen screen,
	final Element element,
	final Properties parameter,
	final Attributes controlDefinitionAttributes) {
          
        super.bind(nifty,screen,element,parameter,controlDefinitionAttributes);
        
         images = new AbilityImage[12];
         abilities = new AbilityType[12];
         for(int i=0;i<12;i++)
         {
            images[i] = new AbilityImage(game);
            Texture2D texture = new Texture2D(512, 512, Format.Depth);
            texture.setImage(images[i]);
             
          //  ap.elements[i] = nifty.getScreen("start").findElementByName("ability"+i);
            ImageRenderer mm = nifty.getScreen("start").findElementByName("ability"+i).getRenderer(ImageRenderer.class);
            ((DesktopAssetManager) game.getGameApplication().getAssetManager()).addToCache(new TextureKey("minimapkey"+i), texture);
            NiftyImage img = nifty.getRenderEngine().createImage("minimapkey"+i, false);
            mm.setImage(img); 
         }
         
         hintController=screen.findControl("hintPanel", HintController.class);
      }


    public void click(String s)
    {
        int i = Integer.parseInt(s);
        
        game.getPlayerControler().abilityClick(abilities[i]);
        System.out.println("Affen werfen mit Kot " + abilities[i].getName());
    }
    
    public void selectUnit(Unit u)
    {
       if(u != null)
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
            }
       }else{
           clear();
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
    


}
