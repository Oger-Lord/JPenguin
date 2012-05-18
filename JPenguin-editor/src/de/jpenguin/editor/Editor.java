/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.  gui builder 1.12
 */
package de.jpenguin.editor;

import de.jpenguin.editor.engine.EditorApplication;
import de.jpenguin.editor.xml.TypeXMLManager;
import javax.swing.*;

import de.jpenguin.loader.PlayerDataManager;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author Karsten
 */
public class Editor {
    
   private TypeXMLManager typeXML;
   private PlayerDataManager playerDataManager;
   private EditorApplication editorApp;
    
   private EditorGUI editorGUI; //<- komischer füller
   private TypeManagerGUI typeManager;
   private SettingsGUI settingsGUI;
   private PlayerGUI playerGUI;
   private MapDescriptionGUI mapDescriptionGUI;
    
   private static String path="C:/Users/Karsten/Documents/jMonkeyProjects/JPenguin/TowerDefense/";
   private static String map="Map1";
   
   public static void main(String[] args){
        new Editor();
    }
    
    
    public Editor()
    {
        File f = new File(path);
        if(f.exists()==false)
        {
            path = JOptionPane.showInputDialog(null, "Project Folder","C:/jMonkeyProjects/TowerDefense/");
            f = new File(path);
            if(f.exists()==false)
            {
                 JOptionPane.showMessageDialog(null, "Error", "Projekt not found!", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            
            f = new File(getMapPath());
            if(f.exists()==false)
            {
                map = JOptionPane.showInputDialog(null, "Map Name","Map1");
                f = new File(getMapPath());
                if(f.exists()==false)
                {
                    System.exit(0);
                }
            }
        }
        
        typeXML = new TypeXMLManager(path+"TypeData");
        
        editorGUI = EditorGUI.newEditorGUI(this,path,getMapPath());
    }
    
    
    public void newOne()
    {
        map = JOptionPane.showInputDialog(null, "Map Name","Map1");
        
        String[] array = {"64","128","256","512","1024"};

        String str = (String) JOptionPane.showInputDialog(null, 
            "Select a Size",
            "Size",
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            array, 
           "512");
        
        
        int size = Integer.parseInt(str);
        
        if(size > 0 && size < 100000)
        {
            EditorGUI.getInstance().setTitle(getMapPath());
            new File(getMapPath()).mkdir();
            editorApp.newMap(size);
        }
    }
    
    public void save()
    {
        typeXML.save();
        editorApp.save();

        JOptionPane.showMessageDialog(null,"Succesfully saved!");
    }
    
    public void load()
    {
       File dir = new File(Editor.getPath()+"assets/Scenes/");

        FileFilter fileFilter = new FileFilter() {
        public boolean accept(File file) {
            return file.isDirectory();
            }
        };
        File[] files = dir.listFiles(fileFilter);
        String[] array = new String[files.length];

        for(int i=0;i<files.length;i++)
        {
            array[i] = files[i].getName();
        }

        String str = (String) JOptionPane.showInputDialog(null, 
            "Select a Map",
            "Map",
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            array, 
            array[0]);

        if(str != null)
        {
            map = str;
            
            playerDataManager= PlayerDataManager.load(editorApp.getAssetManager());
            typeXML = new TypeXMLManager(path+"TypeData");

            if(typeManager!= null)
            {
                typeManager.load(typeXML);
            }

           EditorGUI.getInstance().setTitle(getMapPath());

           editorApp.load();
        }
    }
    
    public void undo()
    {
        editorApp.undo();
    }
    
    public TypeXMLManager getTypeXML()
    {
        return typeXML;
    }
    
    public EditorApplication getEditorApplication()
    {
        return editorApp;
    }
    
   public void displaySettings()
   {
       if(settingsGUI==null)
       {
           settingsGUI = new SettingsGUI(this);
       }
       settingsGUI.setVisible(true);
   }
    
    public void newTypeManager()
    {
        if(typeManager==null)
        {
            typeManager = new TypeManagerGUI(this);
            typeManager.load(typeXML);
            typeManager.setVisible(true);
        }else{
            typeManager.setVisible(true);
        }
    }
    
    public void newPlayerGUI()
    {
        if(playerGUI==null)
        {
            playerGUI = new PlayerGUI(editorApp.getAssetManager(),playerDataManager);
        }else{
            playerGUI.setVisible(true);
        }
    }
    
    public void newMapDescriptionGUI()
    {
        if(mapDescriptionGUI==null)
        {
            mapDescriptionGUI = new MapDescriptionGUI(editorApp.getAssetManager());
        }else{
            mapDescriptionGUI.setVisible(true);
        }
    }
    
    public void setEditorApplication(EditorApplication te)
    {
        editorApp = te;
        playerDataManager= PlayerDataManager.load(editorApp.getAssetManager());
    }
    
    public static String getPath()
    {
        return path;
    }
    
    public static String getMapPath()
    {
        return path + "assets/Scenes/" + map + "/";
    }
    
    public static String getMap()
    {
        return map;
    }
    
}
