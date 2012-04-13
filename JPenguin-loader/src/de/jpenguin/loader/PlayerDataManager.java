/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.loader;

import java.util.ArrayList;

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
public class PlayerDataManager implements Savable {
    
    private ArrayList<PlayerData> player= new ArrayList();
    
    public static void save(PlayerDataManager pdm, String path)
    {
        try{
            FileOutputStream fos = new FileOutputStream(new File(path+"assets/Data/player.xml"));
            XMLExporter.getInstance().save(pdm, new BufferedOutputStream(fos));
        }catch(Exception e){System.out.println("Error Saving PlayerData " + e.getMessage());}
    }
    
    public static PlayerDataManager load(AssetManager assetManager)
    {
        XMLImporter imp = XMLImporter.getInstance();
        imp.setAssetManager(assetManager);

        try{
            InputStream fis = assetManager.locateAsset(new AssetKey("Data/player.xml")).openStream();
            return (PlayerDataManager) imp.load(new BufferedInputStream(fis));
        }catch(Exception e){
            System.out.println("Error loading player " + e.getMessage());
            return new PlayerDataManager();}
    }
    
    
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.writeSavableArrayList(player, "player", new ArrayList());
    }
    
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        player =capsule.readSavableArrayList("player", new ArrayList());
     }

    /**
     * @return the player
     */
    public ArrayList getPlayer() {
        return player;
    }

    /**
     * @param player the player to set
     */
    public void setPlayer(ArrayList player) {
        this.player = player;
    }
}
