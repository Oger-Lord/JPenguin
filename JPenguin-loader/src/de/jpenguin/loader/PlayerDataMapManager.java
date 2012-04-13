/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.loader;

import java.util.ArrayList;
import java.util.HashMap;

import com.jme3.export.*;
import com.jme3.export.xml.XMLImporter;
import com.jme3.export.xml.XMLExporter;

import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetKey;

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.File;

import java.io.IOException;
/**
 *
 * @author Karsten
 */
public class PlayerDataMapManager implements Savable {
    
    private boolean fixedTeams;
    
    private HashMap<String,PlayerDataMap> player= new HashMap();
    private ArrayList teams= new ArrayList();
    
    
    public static void save(PlayerDataMapManager pdm, String mappath)
    {
        try{
            FileOutputStream fos = new FileOutputStream(new File(mappath+"player.xml"));
            XMLExporter.getInstance().save(pdm, new BufferedOutputStream(fos));
        }catch(Exception e){}
    }
    
    public static PlayerDataMapManager load(AssetManager assetManager, String map)
    {
        XMLImporter imp = XMLImporter.getInstance();
        imp.setAssetManager(assetManager);

        try{
            InputStream fis = assetManager.locateAsset(new AssetKey("Scenes/"+map+"/player.xml")).openStream();
            return (PlayerDataMapManager) imp.load(new BufferedInputStream(fis));
        }catch(Exception e){
            System.out.println("Error loading player " + e.getMessage());
            return new PlayerDataMapManager();}
    }
    
    
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(fixedTeams,   "fixedTeams",   false);
        capsule.writeStringSavableMap(getPlayer(), "player", new HashMap());
    }
    
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        fixedTeams  = capsule.readBoolean( "fixedTeams",   false);
        setPlayer((HashMap<String, PlayerDataMap>) (HashMap)capsule.readStringSavableMap("player", new HashMap()));
     }

    /**
     * @return the fixedTeams
     */
    public boolean isFixedTeams() {
        return fixedTeams;
    }

    /**
     * @param fixedTeams the fixedTeams to set
     */
    public void setFixedTeams(boolean fixedTeams) {
        this.fixedTeams = fixedTeams;
    }

    /**
     * @return the player
     */
    public HashMap<String,PlayerDataMap> getPlayer() {
        return player;
    }

    /**
     * @param player the player to set
     */
    public void setPlayer(HashMap<String,PlayerDataMap> player) {
        this.player = player;
    }


}
