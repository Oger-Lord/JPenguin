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
import java.util.Properties;
import com.jme3.texture.Texture2D;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture;

import de.lessvoid.nifty.controls.dynamic.CustomControlCreator;

import de.jpenguin.game.Game;
import de.jpenguin.game.GameApplication;
import de.jpenguin.player.Player;
import de.jpenguin.unit.Unit;
import de.jpenguin.input.RTSCamera;
import de.jpenguin.type.UnitType;
import de.lessvoid.nifty.tools.Color;
/**
 *
 * @author Karsten
 */
public class MinimapPanel extends GUIPlugin {
    
    
    private MinimapImage miniImage;
    private Element element;
    private int size;
    
    public static void createMinimap(Nifty nifty, Screen screen)
    {
        CustomControlCreator createMultiplayerPanel = new CustomControlCreator("myMinimapPanel", "minimapPanel");
        createMultiplayerPanel.create(nifty, screen, screen.findElementByName("box-minimap"));
    }
    
     public void bind(
 	final Nifty nifty,
	final Screen screen,
	final Element element,
	final Properties parameter,
	final Attributes controlDefinitionAttributes) {
         
         super.bind(nifty,screen,element,parameter,controlDefinitionAttributes);
         this.element = nifty.getScreen("start").findElementByName("minimap");
         
          
        size =game.getGameApplication().getTerrain().getTerrainSize()-1;

        miniImage= new MinimapImage(game,size);
        Texture2D texture = new Texture2D(size, size, Format.Depth);
          // texture.setMinFilter(Texture.MinFilter.Trilinear);
         //  texture.setMagFilter(Texture.MagFilter.Bilinear);
        texture.setImage(miniImage);


         ImageRenderer mm = nifty.getScreen("start").findElementByName("minimap").getRenderer(ImageRenderer.class);
         ((DesktopAssetManager) game.getGameApplication().getAssetManager()).addToCache(new TextureKey("minimapkey"), texture);
         NiftyImage img = nifty.getRenderEngine().createImage("minimapkey", false);
         mm.setImage(img); 
      }
    
    
    @Override
    public void update(float tpf)
    {
        if(miniImage !=null)
        {
            miniImage.update();
        }
    }


    public void click(final int mouseX, final int mouseY)
    {
        int x = mouseX-element.getX()-element.getWidth()/2;
        int y = mouseY-element.getY()-element.getHeight()/2;
        
        x=x*size/element.getWidth();
        y=y*size/element.getHeight();
        
        RTSCamera cam = game.getGameApplication().getRTSCamera();
        cam.setTranslation(x, y);
    }
    
    
    @Override
	public void onFocus(final boolean getFocus) {
	}



}
