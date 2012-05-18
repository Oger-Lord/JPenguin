/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.gui;

import com.jme3.asset.DesktopAssetManager;
import com.jme3.asset.TextureKey;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;
import java.util.Properties;


import de.jpenguin.game.Game;
import de.jpenguin.game.GameApplication;
import de.jpenguin.unit.Unit;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.PanelRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.tools.Color;

/**
 *
 * @author Karsten
 */
public class PortraitPanel extends GUIPlugin {
    
    private Portrait portrait;
    private PanelRenderer portraitPanel;
    private ImageRenderer picture;
    private TextRenderer hp;
    private TextRenderer mp;
    private UnitPanel unitPanel;
    
    private GameApplication gameApp;
    
    private Nifty nifty;
    private Screen screen;
    
    @Override
      public void bind(
 	final Nifty nifty,
	final Screen screen,
	final Element element,
	final Properties parameter,
	final Attributes controlDefinitionAttributes) {
         
          super.bind(nifty,screen,element,parameter,controlDefinitionAttributes);
           
          this.nifty=nifty;
          this.screen=screen;
          
          gameApp = game.getGameApplication();
         
          picture = nifty.getScreen("start").findElementByName("portrait").getRenderer(ImageRenderer.class);
          hp = nifty.getScreen("start").findElementByName("labelhp").getRenderer(TextRenderer.class);
          mp = nifty.getScreen("start").findElementByName("labelmp").getRenderer(TextRenderer.class);
          
          portraitPanel = nifty.getScreen("start").findElementByName("panel_portrait").getRenderer(PanelRenderer.class);
          
          
          hp.setText("");
          mp.setText("");
          
            portrait = new Portrait(gameApp,256,384);
            portrait.setPosition(300, 300);
            portrait.display(false);
            portrait.setCameraDistance(2.5f);

            ((DesktopAssetManager) gameApp.getAssetManager()).addToCache(new TextureKey("pippo"), portrait.getTexture());
            NiftyImage img = nifty.getRenderEngine().createImage("pippo", false);
            if(picture==null)
            {
                System.out.println("Empty Picture!");
            }

            picture.setImage(img);
         }
    
    
    @Override
    public void selectUnit(Unit u)
    {
        if(u != null)
        {
            portraitPanel.setBackgroundColor(new Color("#111111"));
            
            if(portrait.getModelPath().equals(u.getUnitType().getModel()) == false)
            {
                portrait.setModel(u.getUnitType().getModel());
            }
            hp.setText(((int)u.getLife())+"/"+((int)u.getUnitType().getLife()));
            if(u.getUnitType().getMana() > 0)
            {
                mp.setText(((int)u.getMana())+"/"+((int)u.getUnitType().getMana()));
            }else{
                 mp.setText("");
            }
            
            if(unitPanel == null){
                unitPanel =new UnitPanel(nifty,screen,u);
            }else{
                unitPanel.setUnit(u);
            }
        }else{    
            portraitPanel.setBackgroundColor(Color.NONE); 
            hp.setText("");
            mp.setText("");
            portrait.setModel("");
            
            if(unitPanel != null)
            {
                unitPanel.remove();
                unitPanel=null;
            }     
        }   
    }
    
    public void update(float tpf)
    {
        if(portrait != null)
        {
            portrait.update(tpf);
        }
    }
}
