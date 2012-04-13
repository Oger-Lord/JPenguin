/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.menu;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.CheckBox;

import java.io.*;

import com.jme3.system.AppSettings;
/**
 *
 * @author Karsten
 */
public class Options implements ScreenController {
    
    private Nifty nifty;
    private int[][] resolutions;
    
    //Profil
    private TextField textFieldName;
    
    //Display
    private DropDown dropDownResolution;
    private DropDown dropDownBpp;
    private CheckBox checkBoxVsync;
    
    private CheckBox checkBoxFullscreen;
    private DropDown dropDownHerz;
    private DropDown dropDownAntialiasing;
    
    public Options()
    {
        resolutions = new int[10][2];
        
        //4:3
        
        resolutions[0][0] = 800;
        resolutions[0][1] = 600;
        
        resolutions[1][0] = 1024;
        resolutions[1][1] = 768;
        
        resolutions[2][0] = 1280;
        resolutions[2][1] = 768;
        
        resolutions[3][0] = 1280;
        resolutions[3][1] = 1024;
 
        // 16:9/16:10
        resolutions[4][0] = 960;
        resolutions[4][1] = 600;
        
        resolutions[5][0] = 1280;
        resolutions[5][1] = 800;
        
        resolutions[6][0] = 1366;
        resolutions[6][1] = 768;
        
        resolutions[7][0] = 1680;
        resolutions[7][1] = 1050;
        
        resolutions[8][0] = 1920;
        resolutions[8][1] = 1080;
        
        resolutions[9][0] = 1920;
        resolutions[9][1] = 1200;
    }
    
    public void bind(Nifty nifty, Screen screen) {
        this.nifty=nifty;
        
        //Profil
        textFieldName =screen.findNiftyControl("playername", TextField.class);
        
        //Display
        dropDownResolution = screen.findNiftyControl("resolution", DropDown.class);
        dropDownBpp = screen.findNiftyControl("bpp", DropDown.class);
        checkBoxVsync = screen.findNiftyControl("vsync", CheckBox.class);
        
        checkBoxFullscreen = screen.findNiftyControl("fullscreen", CheckBox.class);
        dropDownHerz = screen.findNiftyControl("herz", DropDown.class);
        dropDownAntialiasing = screen.findNiftyControl("antialiasing", DropDown.class);
        
        load();
    }
    
    public void load()
    {
        AppSettings settings = MenuApp.getInstance().getSettings();
         
        //Profil
        String name = (String)settings.get("Name");
        if(name==null)
        {
            textFieldName.setText("YourName");
        }else{
            textFieldName.setText(name);
        }
        
        //Display
        for(int i=0;i<10;i++)
        {
            dropDownResolution.addItem(resolutions[i][0] + " x "+ resolutions[i][1]);
            if(settings.getWidth() == resolutions[i][0] && settings.getHeight()==resolutions[i][1])
            {
                dropDownResolution.selectItemByIndex(i);
            }
        }
        
        
        dropDownBpp.addItem("16");
        dropDownBpp.addItem("24");
        
        if(settings.getBitsPerPixel()==16)
        {
            dropDownBpp.selectItemByIndex(0);
        }else{
            dropDownBpp.selectItemByIndex(1);
        }
        
        checkBoxVsync.setChecked(settings.isVSync());
        checkBoxFullscreen.setChecked(settings.isFullscreen());
        
        dropDownHerz.addItem("59");
        dropDownHerz.addItem("60");
        dropDownHerz.addItem("75");
        dropDownHerz.setEnabled(false);
        
        dropDownAntialiasing.addItem("Disabled");
        dropDownAntialiasing.addItem("2x");
        dropDownAntialiasing.addItem("4x");
        dropDownAntialiasing.addItem("6x");
        dropDownAntialiasing.addItem("8x");
        dropDownAntialiasing.addItem("16x");
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
        //MenuApp.getInstance().getNifty().getScreen("").
        
        MenuApp.getInstance().getNifty().resolutionChanged();
    }
    
    public void apply()
    {
        System.out.println("aha");
        
        AppSettings settings = MenuApp.getInstance().getSettings();
        
        //Profil
        settings.put("Name",textFieldName.getText());
        
        
        //Display
        for(int i=0;i<10;i++)
        {
             if(dropDownResolution.getSelection().equals(resolutions[i][0] + " x "+ resolutions[i][1]))
             {
                 settings.setResolution(resolutions[i][0], resolutions[i][1]);
             }
        }
        
        settings.setBitsPerPixel(Integer.parseInt((String)(dropDownBpp.getSelection())));
        
        settings.setVSync(checkBoxVsync.isChecked());
        
        settings.setFullscreen(checkBoxFullscreen.isChecked());
        
        settings.setFrequency(Integer.parseInt((String)(dropDownHerz.getSelection())));
        
      //  settings.setSamples(value)setFrequency((Integer)(dropDownHerz.getSelection()));
        
        try{
            FileOutputStream fos = new FileOutputStream(new File("settings.txt"));
            settings.save(new BufferedOutputStream(fos));
        }catch(Exception e){}
       
        MenuApp.getInstance().restart();
        
    }
        
}
