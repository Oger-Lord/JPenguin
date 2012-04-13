/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.fog;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
/**
 *
 * @author Karsten
 */
public class FogOfWarMinimap extends FogOfWarEffect {
    
    private BufferedImage fog;
    private Graphics2D g;
    
    public FogOfWarMinimap()
    {
        fog = new BufferedImage(512,512,BufferedImage.TYPE_4BYTE_ABGR);
        g = (Graphics2D)fog.getGraphics();
        g.setPaintMode();
        
        AlphaComposite ac =AlphaComposite.getInstance(AlphaComposite.SRC);
        g.setComposite(ac);
        
    }
    
    public void valueChange(int x, int y, int to, int from)
    {
       // float array[] = new float[4];
        
        if(to == -1)
        {
          //  array[3] = 1;
            g.setColor(new Color(0f,0f,0f,1));
        }else if(to == 0){
          //  array[0] = 0.3f;
         //   array[1] = 0.3f;
         //   array[2] = 0.3f;
         //   array[3] = 0.7f;
            g.setColor(new Color(0.1f,0.1f,0.1f,0.5f));
        }else{
            g.setColor(new Color(0f,0f,0f,0f));
        }
        
        g.fillRect(x*512/FogOfWar.getWidth(), (FogOfWar.getHeight()-y)*512/FogOfWar.getHeight(), (int)(512/FogOfWar.getWidth()+0.1), (int)(512/FogOfWar.getHeight()+0.1));
        
       // raster.setPixel(x, y, array);
    }
    
    
    public BufferedImage getFogImage()
    {
        return fog;
    }
}
