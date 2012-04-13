/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.engine.loader;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetLoader;
import java.io.IOException;

import javax.imageio.ImageIO;
 
public class ImageLoader implements AssetLoader{
     public Object load(AssetInfo assetInfo) throws IOException {
        AssetKey key = assetInfo.getKey();
 
        System.out.println( key.getExtension() );
        System.out.println( key.getFolder() );
        System.out.println( key.getName() );
 
        return ImageIO.read(assetInfo.openStream());
    }
}