/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.game;

import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.xml.xpp3.Attributes;
import java.util.Properties;

public class LoadingScreen implements ScreenController {
    
    private GameApplication gameApp;
    
    private NiftyJmeDisplay niftyDisplay;
    private Nifty nifty;
    private Element progressBarElement;
    private TextRenderer textRenderer;
    
    public LoadingScreen(GameApplication gameApp)
    {
        this.gameApp=gameApp;
        niftyDisplay = new NiftyJmeDisplay(gameApp.getAssetManager(),
                gameApp.getInputManager(),
                gameApp.getAudioRenderer(),
                gameApp.getGuiViewPort());
        nifty = niftyDisplay.getNifty();
 
        nifty.fromXml("Interface/Loadingscreen/loading.xml", "loadlevel", this); //<-Screen controller

        gameApp.getGuiViewPort().addProcessor(niftyDisplay);

        Element element = nifty.getScreen("loadlevel").findElementByName("loadingtext");
        textRenderer = element.getRenderer(TextRenderer.class);
    }
    
    
    public void setProgress(final float progress, String loadingText) {
        final int MIN_WIDTH = 32;
        int pixelWidth = (int) (MIN_WIDTH + (progressBarElement.getParent().getWidth() - MIN_WIDTH) * progress);
        progressBarElement.setConstraintWidth(new SizeValue(pixelWidth + "px"));
        progressBarElement.getParent().layoutElements();
 
        textRenderer.setText(loadingText);
    }
    
    public void clear()
    {
        nifty.gotoScreen("end");
        nifty.exit();
        gameApp.getGuiViewPort().removeProcessor(niftyDisplay);
    }
    
    @Override
    public void onStartScreen() {
    }
 
    @Override
    public void onEndScreen() {
    }
     @Override
    public void bind(Nifty nifty, Screen screen) {
        progressBarElement = nifty.getScreen("loadlevel").findElementByName("progressbar");
    }
    
}
