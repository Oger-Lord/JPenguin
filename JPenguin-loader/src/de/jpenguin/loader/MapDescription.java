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
public class MapDescription implements Savable {
    
    private String title="";
    private String description="";
    private String picture="minimap.png";
    private String playerSuggestion="1";
    private String creator="";
    
    public static void save(MapDescription md, String mappath)
    {
        try{
            FileOutputStream fos = new FileOutputStream(new File(mappath+"description.xml"));
            XMLExporter.getInstance().save(md, new BufferedOutputStream(fos));
        }catch(Exception e){}
    }
    
    public static MapDescription load(AssetManager assetManager, String map)
    {
        XMLImporter imp = XMLImporter.getInstance();
        imp.setAssetManager(assetManager);

        try{
            InputStream fis = assetManager.locateAsset(new AssetKey("Scenes/"+map+"/description.xml")).openStream();
            return (MapDescription) imp.load(new BufferedInputStream(fis));
        }catch(Exception e){
            System.out.println("Error loading player " + e.getMessage());
            return new MapDescription();}
    }
    
    
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        
        capsule.write(title,   "title",   "");
        capsule.write(description,   "description",   "");
        capsule.write(picture,   "picture",   "");
        capsule.write(playerSuggestion,   "playerSuggestion",   "");
        capsule.write(creator,   "creator",   "");
    }
    
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        
        title  = capsule.readString( "title",   "New Map");
        description  = capsule.readString( "description",   "");
        picture  = capsule.readString( "picture",   "minimap.png");
        playerSuggestion  = capsule.readString( "playerSuggestion",   "1");
        creator  = capsule.readString( "creator",   "");
     }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the picture
     */
    public String getPicture() {
        return picture;
    }

    /**
     * @param picture the picture to set
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }

    /**
     * @return the playerSuggestion
     */
    public String getPlayerSuggestion() {
        return playerSuggestion;
    }

    /**
     * @param playerSuggestion the playerSuggestion to set
     */
    public void setPlayerSuggestion(String playerSuggestion) {
        this.playerSuggestion = playerSuggestion;
    }

    /**
     * @return the creator
     */
    public String getCreator() {
        return creator;
    }

    /**
     * @param creator the creator to set
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }
}
