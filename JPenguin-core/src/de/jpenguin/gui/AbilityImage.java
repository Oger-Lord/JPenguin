/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.gui;


import de.jpenguin.engine.PaintableImage;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector2f;

import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import java.awt.Color;

import java.awt.Graphics2D;
import java.awt.BasicStroke;

import de.jpenguin.unit.Unit;
import de.jpenguin.game.Game;
import de.jpenguin.game.GameApplication;

import com.jme3.asset.AssetKey;
/**
 *
 * @author Karsten
 */
public class AbilityImage extends PaintableImage {
    
    private java.awt.Image background;
    private Game game;
    private java.awt.Image icon;
    private String lastPath="";
    
    public AbilityImage(Game game)
    {
        super(75, 75);
        
        this.game=game;
        
        
        try {
            background = ImageIO.read(game.getGameApplication().getAssetManager().locateAsset(new AssetKey("Interface/IngameGui/empty.png")).openStream());
            background = PaintableImage.verticalflip((BufferedImage)background);
            // background = verticalflip(ImageIO.read(new File("assets/Textures/Minimap/minimap.png")));
        } catch (IOException e) {
        }
        

        refreshImage();
    }
    

    public void setImage(String path)
    {
        
        if(path.isEmpty()==false)
        {
            try{
                icon = ImageIO.read(game.getGameApplication().getAssetManager().locateAsset(new AssetKey(path)).openStream());
                icon = PaintableImage.verticalflip((BufferedImage)icon);
            } catch (IOException e) {
            }
        }else{
            icon = null;
        }
        
        if(path.equals(lastPath)==false)
        {
            lastPath=path;
            refreshImage();
        }
    }
    
    public void paint(Graphics2D g) {
               
        g.setBackground(new Color(0f, 0f, 0f, 0f));
        g.clearRect(0, 0, getWidth(), getHeight());
        
        g.drawImage(background,0,0,null);
        
        if(icon != null)
        {
            g.drawImage(icon,3,3,width-6,height-6,null);
        }
    }
     

}
