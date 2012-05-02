/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.loader;

import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import com.jme3.export.Savable;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.export.binary.BinaryImporter;
import com.jme3.export.xml.XMLExporter;
import com.jme3.export.xml.XMLImporter;
import de.jpenguin.engine.MyXML;
import de.jpenguin.engine.MyXMLImporter;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 *
 * @author Karsten
 */
public class Loader {
    public static void save(String mappath, Savable savable, String filename, boolean xml)
    {
        try{
            if(xml)
            {
                FileOutputStream fos = new FileOutputStream(new File(mappath+filename+".xml"));
                XMLExporter.getInstance().save(savable, new BufferedOutputStream(fos));
            }else{
                FileOutputStream fos = new FileOutputStream(new File(mappath+filename+".jme"));
                BinaryExporter.getInstance().save(savable, new BufferedOutputStream(fos));
            }
            
        }catch(Exception e){System.out.println("Error saving " + filename + ":" + e.getMessage());}
    }
    
    public static <T> T load(AssetManager assetManager, String map, String filename, boolean xml,Class<T> requestedRendererClass)
    {
        XMLImporter imp = XMLImporter.getInstance();
        imp.setAssetManager(assetManager);
        
     //   MyXMLImporter imp = MyXMLImporter.getInstance();
      //  imp.setAssetManager(assetManager);
        BinaryImporter bimp = BinaryImporter.getInstance();
        bimp.setAssetManager(assetManager);
        

        try{
            if(xml)
            {
                InputStream fis = assetManager.locateAsset(new AssetKey("Scenes/"+map+"/"+filename+".xml")).openStream();
                return (T) imp.load(new BufferedInputStream(fis));
            }else{
                InputStream fis = assetManager.locateAsset(new AssetKey("Scenes/"+map+"/"+filename+".jme")).openStream();
                
                return (T) bimp.load(new BufferedInputStream(fis));
            }
            
        }catch(Exception e){
            System.out.println("Error loading " + filename + ":" + e.getMessage());
            return null;
        }
    }
}
