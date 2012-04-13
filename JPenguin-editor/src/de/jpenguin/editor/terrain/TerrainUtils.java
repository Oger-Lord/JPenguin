/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.editor.terrain;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import java.util.Hashtable;

import com.jme3.terrain.Terrain;
import com.jme3.texture.Texture;
import com.jme3.material.*;
import com.jme3.texture.Image;

import jme3tools.converters.ImageToAwt;


/**
 *
 * @author Karsten
 */
public class TerrainUtils {
    
    
    public static BufferedImage[] terrainGetTextures(Terrain terrain, int width, int height)
    {
        BufferedImage[] array = new BufferedImage[12];
        
        for(int i=0;i<12;i++)
        {
            BufferedImage bImage;
            if(i==0)
            {
                 bImage = terrainGetTexture(terrain, width, height,"DiffuseMap");
            }else{
                 bImage = terrainGetTexture(terrain, width, height,"DiffuseMap_"+i);
            }
            
            if(bImage != null)
            {
                array[i] = bImage;
            }else{
                array[i] = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB );
            }
        }
        return array;
    }
    
    public static BufferedImage terrainGetTexture(Terrain terrain, int width, int height,String map)
    {
        MatParam matParam = terrain.getMaterial().getParam(map);
        if(matParam ==null){return null;}
        Texture tex = (Texture) matParam.getValue();
        if(tex ==null){return null;}
        Image i =tex.getImage();
        if(i ==null){return null;}
        return scaleImage(ImageToAwt.convert(i, true, true, 0),width,height);
    }
    
    
    public static BufferedImage scaleImage(BufferedImage img, int width, int height) {
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        if (imgWidth*height < imgHeight*width) {
            width = imgWidth*height/imgHeight;
        } else {
            height = imgHeight*width/imgWidth;
        }
        BufferedImage newImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = newImage.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.clearRect(0, 0, width, height);
            g.drawImage(img, 0, 0, width, height, null);
        } finally {
            g.dispose();
        }
        return newImage;
    }
}
