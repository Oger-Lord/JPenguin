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

import de.jpenguin.fog.FogOfWarMinimap;

import com.jme3.asset.AssetKey;
/**
 *
 * @author Karsten
 */
public class MinimapImage extends PaintableImage {
    
    private java.awt.Image background;
    private Game game;
    private boolean thisTime=true;
    private int size;
    
    public MinimapImage(Game game, int size)
    {
        super(512, 512);
        
        this.size=size;
        
        this.game=game;
        
        
        try {
            background = ImageIO.read(game.getGameApplication().getAssetManager().locateAsset(new AssetKey("Scenes/"+game.getGameApplication().getMap()+"/minimap.png")).openStream());
            background = PaintableImage.verticalflip((BufferedImage)background);
            // background = verticalflip(ImageIO.read(new File("assets/Textures/Minimap/minimap.png")));
        } catch (IOException e) {
        }
        

        update();
    }

    public void update()
    {
        if(thisTime)
        {
            thisTime=false;
            refreshImage();
        }else{
            thisTime=true;
        }
    }
    
    public void paint(Graphics2D g) {
               
        g.setBackground(new Color(0f, 0f, 0f, 0f));
        g.clearRect(0, 0, getWidth(), getHeight());
        
        g.drawImage(background,0,0,null);
        
        ArrayList<Unit> units = game.getUnits();
        for(int i=0;i<units.size();i++)
        {
            Unit u = units.get(i);
            if(u.getLife() > 0)
            {
                
                if(u.getPlayer() == game.getControllerPlayer())
                {
                   g.setColor(Color.white);
                }else{
                    g.setColor(new Color(0f, 0f, 1f, 1f));
                }
                
                if(game.getControllerPlayer().isVisible(u.getX(), u.getY()))
                {
                     g.fillRect(convertX(u.getX())-3, convertY(u.getY())-3, 6, 6);
                }
            }
        }
        
        FogOfWarMinimap fowm = (FogOfWarMinimap)game.getFogOfWar().getEffect(FogOfWarMinimap.class);
        g.drawImage(fowm.getFogImage(),0,0,null);
        
        drawCamera(g);
    }
    
    
    private int convertX(float x)
    {
        return (int)(((size/2)+(x+0.5))/size*512);
    }
    
    private int convertY(float y)
    {
        return (int)(((size/2)-(y+0.5))/size*512);
    }
    
    private void drawCamera(Graphics2D g)
    {
        Vector2f array[] = new Vector2f[5];
        array[0] = getCameraPoint(0, 0);
        array[1] = getCameraPoint(game.getGameApplication().getCamera().getWidth(), 0);
        array[2] = getCameraPoint(game.getGameApplication().getCamera().getWidth(), game.getGameApplication().getCamera().getHeight());
        array[3] = getCameraPoint(0, game.getGameApplication().getCamera().getHeight());
        array[4] = array[0];
        
        g.setStroke(new BasicStroke(3));
        g.setColor(new Color(1f, 1f, 0f, 1f));
        for(int i=0;i<4;i++)
        {
           // g.drawLine((int)array[i].getX(), (int)array[i].getY(), (int)array[i+1].getX(), (int)array[i+1].getY());
            g.drawLine(convertX(array[i].getX()), convertY(array[i].getY()), convertX(array[i+1].getX()), convertY(array[i+1].getY()));
        }
        
        //Draw Mouse Cursor
        // g.setColor(new Color(1f, 0f, 0f, 1f));
       //  Vector2f cursor = game.getGameApplication().getInputManager().getCursorPosition();
       //  cursor = getCameraPoint((int)cursor.getX(),(int)cursor.getY());
       //  g.fillRect((int)cursor.getX()-3, (int)cursor.getY()-3, 6, 6);

    }
    
    private Vector2f getCameraPoint(int x, int y)
    {
        GameApplication gameApp = game.getGameApplication();
        
        Vector3f origin    = gameApp.getCamera().getWorldCoordinates(new Vector2f(x,y), 0.0f);
        Vector3f direction = gameApp.getCamera().getWorldCoordinates(new Vector2f(x,y), 0.3f);
        direction.subtractLocal(origin).normalizeLocal();
        
        float value = -origin.getY()/direction.getY();
        
        Vector2f v2f=new Vector2f();
       // v2f.setX((size/2)+(origin.getX()+value*direction.getX()));
       // v2f.setY((size/2)-(origin.getZ()+value*direction.getZ()));
        v2f.setX((origin.getX()+value*direction.getX()));
        v2f.setY((origin.getZ()+value*direction.getZ()));
        return v2f;
    }
     

}