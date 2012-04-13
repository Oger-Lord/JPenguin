/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.player;

import java.util.*;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.jpenguin.game.Game;

import de.jpenguin.loader.*;

import com.jme3.math.ColorRGBA;

import com.jme3.asset.*;
/**
 *
 * @author Karsten
 */
public class PlayerLoader {
    
    
    public PlayerLoader(Game game,String map)
    {
        System.out.println("Load Player!");
        
        
        
        PlayerDataManager pdm =PlayerDataManager.load(game.getGameApplication().getAssetManager());
        ArrayList<PlayerData> list = pdm.getPlayer();
        
        
        for(int i=0;i<list.size();i++)
        {
            new Player(game,list.get(i).getId(),list.get(i).getName(),list.get(i).getColor());
        }
        
        
        for(int i=0;i<list.size();i++)
        {
            
            if(list.get(i).isNeutral())
            {
                Player player_passive = game.getPlayer().get(list.get(i).getId());
                
                Enumeration e =game.getPlayer().keys();
                while(e.hasMoreElements())
                {
                  Player p = game.getPlayer().get(e.nextElement());
                  if(p != player_passive)
                  {
                      p.setRelationship(player_passive, new PlayerRelationship(PlayerRelationship.neutral,false,false));
                      player_passive.setRelationship(p, new PlayerRelationship(PlayerRelationship.neutral,false,false));
                  }
                }
            }
            
        }
        
        

        
        /*
        AssetManager as = game.getGameApplication().getAssetManager();
        
       InputStream fis = as.locateAsset(new AssetKey("Scenes/"+map+"/player.xml")).openStream();
                
        try {
          DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
          DocumentBuilder db = dbf.newDocumentBuilder();
          Document doc = db.parse(fis);
          doc.getDocumentElement().normalize();
         // System.out.println("Root element " + doc.getDocumentElement().getNodeName());
          NodeList nodeLst = doc.getElementsByTagName("Player");
         // System.out.println("Information of all employees");
        
          for (int s = 0; s < nodeLst.getLength(); s++) {

                Node fstNode = nodeLst.item(s);
                Element fistElement = (Element) fstNode;
          
                String id = fistElement.getAttribute("id");
                String name = fistElement.getAttribute("name");
                
                ColorRGBA c = new ColorRGBA();
                c.r = Float.parseFloat(fistElement.getAttribute("colorRed"));
                c.g = Float.parseFloat(fistElement.getAttribute("colorGreen"));
                c.b = Float.parseFloat(fistElement.getAttribute("colorBlue"));
                
                
                System.out.println("PLAYER " + id + " name " + name);
                
                new Player(game,id,name,c);
                
            
          }
          
        }catch(Exception e){System.out.println("Error Load Player!" + e.getMessage());}
         * 
         */
    }
    
}
