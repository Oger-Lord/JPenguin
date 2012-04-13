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
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.xml.xpp3.Attributes;
import java.util.Properties;

import de.lessvoid.nifty.controls.NiftyInputControl;
import de.lessvoid.nifty.controls.dynamic.CustomControlCreator;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.Nifty;

import com.jme3.texture.Texture2D;
import com.jme3.texture.Texture;
import com.jme3.app.SimpleApplication;
import com.jme3.texture.Image.Format;
import com.jme3.ui.Picture;


import de.jpenguin.gui.chat.ChatInputController;
import de.jpenguin.gui.chat.ChatOutputController;

import de.jpenguin.game.Game;
import de.jpenguin.game.GameApplication;
import de.jpenguin.player.Player;
import de.jpenguin.unit.Unit;
import de.jpenguin.type.UnitType;


/**
 *
 * @author Karsten
 */
public class GUI implements ScreenController {
    
    private NiftyJmeDisplay niftyDisplay;
    private Nifty nifty;
    private Screen screen;
    private GameApplication gameApp;
    private Game game;
    
    private Portrait portrait;
    private PanelRenderer portraitPanel;
    private ImageRenderer picture;
    private TextRenderer hp;
    private TextRenderer mp;
    private UnitPanel unitPanel;
    
    private Cursor cursor;
    private MinimapPanel minimap;
    private AbilityPanel abilityPanel;
    
    private ChatInputController chatInput;
    private ChatOutputController chatOutput;
    
    
    public GUI(Game game)
    {
        this.game=game;
        this.gameApp=game.getGameApplication();
        
        niftyDisplay = new NiftyJmeDisplay(gameApp.getAssetManager(),
                gameApp.getInputManager(),
                gameApp.getAudioRenderer(),
                gameApp.getGuiViewPort());
        nifty = niftyDisplay.getNifty();
 
        
        nifty.registerScreenController(this);//,new AbilityPanel(nifty));
        //nifty.registerScreenController(new ChatInputController());
        
        nifty.registerMouseCursor("hand", "Interface/Cursor/cursor_enemy.png", 5, 4);
        
        nifty.addXml("Interface/IngameGui/ingame.xml"); //<-Screen controller
        nifty.addXml("Interface/IngameGui/unit.xml");
        nifty.addXml("Interface/IngameGui/abilitys.xml");
        nifty.addXml("Interface/IngameGui/minimap.xml");
        nifty.addXml("Interface/IngameGui/minimap.xml");
      //  nifty.addXml("Interface/IngameGui/chatinput.xml");
        nifty.gotoScreen("start");
        //nifty.addControls();
        
        cursor = new Cursor(gameApp);
        game.getPlayerControler().setCursor(cursor);
    }
    
    
    public void init()
    {
        gameApp.getGuiViewPort().addProcessor(niftyDisplay);
    }
    
   @Override
    public void onStartScreen() {
    }
 
    @Override
    public void onEndScreen() {
    }
    
  //  public void setMinimapFog(ByteBuffer bb)
  //  {
        
  //  }
            
    
    public void refresh()
    {
        Player p = game.getControllerPlayer();
        if(p.getSelection().isEmpty() == false)
        {
            portraitPanel.setBackgroundColor(new Color("#111111"));
            Unit u = p.getSelection().get(0);
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
            
            if(u.getPlayer() == p)
            {
                abilityPanel.loadUnit(u);
            }else{
                abilityPanel.clear();
            }
            
        }else{
          // cursor.setImage("cursor");
           portraitPanel.setBackgroundColor(Color.NONE); 
            hp.setText("");
            mp.setText("");
            portrait.setModel("");
            
            if(unitPanel != null)
            {
                unitPanel.remove();
                unitPanel=null;
            }
            abilityPanel.clear();
        }
    }
    
    public void addPortrait()
    {
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
    
    
    public void update(float tpf)
    {
        if(portrait != null)
        {
            portrait.update(tpf);
        }
        
        if(minimap != null)
        {
            minimap.update();
        }
    }
    
    public Cursor getCursor()
    {
        return cursor;
    }
    
     @Override
    public void bind(Nifty nifty, Screen screen) {
         this.screen=screen;
       // picture = getRenderer(ImageRenderer.class, "start/layer2/panela/test");
          picture = nifty.getScreen("start").findElementByName("portrait").getRenderer(ImageRenderer.class);
          hp = nifty.getScreen("start").findElementByName("labelhp").getRenderer(TextRenderer.class);
          mp = nifty.getScreen("start").findElementByName("labelmp").getRenderer(TextRenderer.class);
          
          portraitPanel = nifty.getScreen("start").findElementByName("panel_portrait").getRenderer(PanelRenderer.class);
          
          
          hp.setText("");
          mp.setText("");
          
          addPortrait();
          
          
          abilityPanel = AbilityPanel.getAbilityPanel(game, nifty, screen);
          minimap = MinimapPanel.getMinimap(game, nifty, screen);
          
          
         chatInput = nifty.getScreen("start").findControl("chatLayer", ChatInputController.class);
         chatOutput = nifty.getScreen("start").findControl("chatPanel", ChatOutputController.class);
                 
         getChatInput().setGame(game);
    }

    /**
     * @return the chatInput
     */
    public ChatInputController getChatInput() {
        return chatInput;
    }

    /**
     * @return the chatOutput
     */
    public ChatOutputController getChatOutput() {
        return chatOutput;
    }
     
    
    public boolean isMouseOver()
    {
        if(nifty.getCurrentScreen().getMouseOverInfoString().contains("mouse over elements: ---"))
        {
            return false;
        }
        return true;
    }

         

}
