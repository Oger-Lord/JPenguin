/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.gui;

import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.asset.TextureKey;

import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.xml.xpp3.Attributes;
import java.util.Properties;

import de.lessvoid.nifty.Nifty;

import de.jpenguin.game.Game;
import de.jpenguin.game.GameApplication;
import de.jpenguin.player.Player;
import de.jpenguin.unit.Unit;
import de.jpenguin.type.UnitType;
import java.util.ArrayList;


/**
 *
 * @author Karsten
 */
public class GUI implements ScreenController {
    
    private NiftyJmeDisplay niftyDisplay;
    private Nifty nifty;
    private GameApplication gameApp;
    private Game game;
    
    private ArrayList<GUIPlugin> plugins;
    
    private Cursor cursor;
    
    public GUI(Game game)
    {
        this.game=game;
        this.gameApp=game.getGameApplication();
        
        niftyDisplay = new NiftyJmeDisplay(gameApp.getAssetManager(),
                gameApp.getInputManager(),
                gameApp.getAudioRenderer(),
                gameApp.getGuiViewPort());
        nifty = niftyDisplay.getNifty();
        
        plugins = new ArrayList();
 
        Properties property = new Properties();
        property.put("game", game);
        property.put("gui", this);
        nifty.setGlobalProperties(property);
        
        nifty.registerScreenController(this);//,new AbilityPanel(nifty));
        //nifty.registerScreenController(new ChatInputController());
        
        nifty.registerMouseCursor("hand", "Interface/Cursor/cursor_enemy.png", 5, 4);
        
        nifty.addXml("Interface/IngameGui/ingame.xml"); //<-Screen controller
        nifty.addXml("Interface/IngameGui/unit.xml");
        nifty.addXml("Interface/IngameGui/abilitys.xml");
        nifty.addXml("Interface/IngameGui/minimap.xml");
       // nifty.addXml("Interface/IngameGui/minimap.xml");
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
       
    
    public void refresh()
    {
        Player p = game.getControllerPlayer();
        if(p.getSelection().isEmpty() == false)
        {
            Unit u = p.getSelection().get(0);
            
            if(u.getPlayer() == p)
            {
                for(int i=0;i<plugins.size();i++)
                    plugins.get(i).selectUnit(u);
            }else{
                for(int i=0;i<plugins.size();i++)
                    plugins.get(i).selectUnit(null);
            }
            
        }else{
          
            
            for(int i=0;i<plugins.size();i++)
                plugins.get(i).selectUnit(null);
        }
    }
    

    public void chatMessage(String s)
    {
          for(int i=0;i<plugins.size();i++)
               plugins.get(i).chatMessage(s);
    }

    
    public void update(float tpf)
    {
        for(int i=0;i<plugins.size();i++)
             plugins.get(i).update(tpf);
    }
    
    public Cursor getCursor()
    {
        return cursor;
    }
    
     @Override
    public void bind(Nifty nifty, Screen screen) {
          AbilityPanel.createAbilityPanel(nifty, screen);
          MinimapPanel.createMinimap(nifty, screen); 
    }
    
    
    public void registerPlugin(GUIPlugin plugin)
    {
        plugins.add(plugin);
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
