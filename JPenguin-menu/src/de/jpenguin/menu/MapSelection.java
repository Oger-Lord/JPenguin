/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.menu;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.controls.dynamic.CustomControlCreator;
import de.lessvoid.nifty.controls.ListBox;

import de.lessvoid.nifty.render.NiftyImage;

import de.lessvoid.nifty.elements.render.*;

import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.NiftyEventSubscriber;

import com.jme3.asset.*;

import java.io.InputStream;
import java.io.StringWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.jme3.math.ColorRGBA;
import com.jme3.system.AppSettings;

import de.jpenguin.game.Game;
import de.jpenguin.loader.MapDescription;

import java.util.List;

/**
 *
 * @author Karsten
 */
public class MapSelection implements ScreenController {
    
    private Nifty nifty;
    private ImageRenderer picture;
    private TextRenderer name;
    private TextRenderer description;
    private TextRenderer playerpreferences;
    private TextRenderer creator;
    
    public void bind(Nifty nifty, Screen screen) {
        this.nifty=nifty;
        
         
       name = screen.findElementByName("name").getRenderer(TextRenderer.class);
       picture = screen.findElementByName("mappreview").getRenderer(ImageRenderer.class);
       description = screen.findElementByName("description").getRenderer(TextRenderer.class);
       playerpreferences = screen.findElementByName("playerpreferences").getRenderer(TextRenderer.class);
       creator = screen.findElementByName("creator").getRenderer(TextRenderer.class);
        
       
        ListBox listBox = screen.findNiftyControl("myListBox", ListBox.class);
        
       AssetManager as = MenuApp.getInstance().getAssetManager();
       InputStream fis = as.locateAsset(new AssetKey("Scenes/maps.txt")).openStream();
       try{
            String s = convertStreamToString(fis);
            
            String array[] = s.split("\\r?\\n");
            
            for(int i=0;i<array.length;i++)
            {
                listBox.addItem(array[i]);
            }
            
            if(array.length != 0)
            {
                 selectMap(array[0]);
            }
            
       }catch(Exception e){
           listBox.addItem("error");
       }
       
      
    }
    
    @NiftyEventSubscriber(id="myListBox")
    public void onListBoxSelectionChanged(final String id, final ListBoxSelectionChangedEvent<String> event) {
        List<String> selection = event.getSelection();
        
        for (String selectedItem : selection) {
            
            selectMap(selectedItem);
          
        //  System.out.println("listbox selection [" + selectedItem + "]");
        }
  }
    
    private void selectMap(String map)
    {
        MapDescription mapDes = MapDescription.load(MenuApp.getInstance().getAssetManager(), map);
         
         name.setText( mapDes.getTitle());
          //Change Image
          NiftyImage newImage = nifty.getRenderEngine().createImage("Scenes/"+map+"/"+ mapDes.getPicture(), false);
          picture.setImage(newImage);
            
          description.setText( mapDes.getDescription());
          playerpreferences.setText( mapDes.getPlayerSuggestion());
          creator.setText( mapDes.getCreator());
    }

    public void onStartScreen() {
        System.out.println("onStartScreen");
    }

    public void onEndScreen() {
        System.out.println("onEndScreen");
    }
    
    public void back()
    {
        nifty.gotoScreen("start");
    }
        
    public void startGame()
    {
       // System.out.println("dsfospdfdddddd");
        
        new Thread()
        {
            @Override
            public void run()
            {
                try{
                Thread.sleep(1000);
                }catch(Exception e){}
                //System.out.println("test");
                new Game("TowerDefense.TowerDefense");
            }
        }.start();
        
       MenuApp.getInstance().destroy();

    }
    

    public String convertStreamToString(InputStream is)
            throws IOException {
        /*
         * To convert the InputStream to String we use the
         * Reader.read(char[] buffer) method. We iterate until the
         * Reader return -1 which means there's no more data to
         * read. We use the StringWriter class to produce the string.
         */
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        } else {        
            return "";
        }
    }

}
