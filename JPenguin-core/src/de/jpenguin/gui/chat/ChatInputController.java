/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.gui.chat;

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

import de.lessvoid.nifty.controls.TextField;

import de.lessvoid.nifty.input.mapping.DefaultInputMapping;

import de.jpenguin.game.Game;
import de.jpenguin.gui.GUIPlugin;
import de.jpenguin.player.PlayerCommandChat;


public class ChatInputController extends GUIPlugin{
    
  private TextField inputField;
  private Element inputLayer;
  private boolean hide=true;

    
     public void deselection()
     {
          hide=true;
          inputField.setText("");
          inputLayer.hide();
     }

    @Override
    public void bind(Nifty nifty, Screen screen, Element element, Properties parameter, Attributes controlDefinitionAttributes) {
 
        super.bind(nifty,screen,element,parameter,controlDefinitionAttributes);
        
        inputField = screen.findNiftyControl("chatinput", TextField.class);
        inputLayer = element;
        
        
        screen.addKeyboardInputHandler(new DefaultInputMapping(),new KeyInputHandler() {
			@Override
			public boolean keyEvent(NiftyInputEvent inputEvent) {
				if(inputEvent == null) return false;
                                
				switch(inputEvent) {
				case Activate:
					enter();
					return true;
				}
				return false;
			}
		});
         
         
        
        inputLayer.hide();
    }

    @Override
    public void init(Properties parameter, Attributes controlDefinitionAttributes) {
    }

    @Override
    public void onFocus(boolean getFocus) {
    }
    
    public void enter()
    {
                if(hide)
                {
                    hide=false;
                    inputLayer.show();
                    inputField.setFocus();
                }else{
                    hide=true;
                    
                    if(inputField.getText().isEmpty()==false)
                    {
                        //ChatOutputController.getInstace().addRow(inputField.getText());
                       // game.addPlayerCommandChat(new PlayerCommandChat(game.getControllerPlayer(),inputField.getText()));
                        game.getChat().addMessage(inputField.getText(), true);
                    }
                    
                    inputField.setText("");
                    inputLayer.hide();
                }
    }

    @Override
    public boolean inputEvent(NiftyInputEvent inputEvent) {
        return false;
    }
}
