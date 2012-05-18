/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.gui;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;
import java.util.Properties;


import de.jpenguin.game.Game;
import de.jpenguin.unit.Unit;

/**
 *
 * @author Karsten
 */
public class GUIPlugin implements Controller {

    protected Game game;
    
    @Override
    public void bind(Nifty nifty, Screen screen, Element element, Properties parameter, Attributes controlDefinitionAttributes) {
        game = (Game)nifty.getGlobalProperties().get("game");
        
        GUI gui = (GUI)nifty.getGlobalProperties().get("gui");
        gui.registerPlugin(this);
    }
    
    /*
    //is called after all plugins were initialized
    public void start()
    {
        
    }
     * 
     */
    
    public void update(float tpf)
    {
        
    }
    
    public void selectUnit(Unit u)
    {
        
    }
    
    public void chatMessage(String s)
    {
        
    }
    

    @Override
    public void init(Properties parameter, Attributes controlDefinitionAttributes) {
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onFocus(boolean getFocus) {
    }

    @Override
    public boolean inputEvent(NiftyInputEvent inputEvent) {
        return false;
    }
    
}
