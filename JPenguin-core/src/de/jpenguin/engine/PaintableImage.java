/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.engine;

/**
 *
 * @author Karsten
 */
 import java.awt.Graphics2D;
 import java.awt.image.BufferedImage;
 import java.nio.ByteBuffer;
 import com.jme3.texture.Image;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import jme3tools.converters.ImageToAwt;

 
 public abstract class PaintableImage extends Image {
     private BufferedImage backImg;
     private ByteBuffer scratch;
 

     public PaintableImage(int width, int height) {
          super();
          try {
            
              
              
          //  try {
          //        backImg = ImageIO.read(new File("assets/Textures/Minimap/minimap.png"));
          //  } catch (Exception e) {
          //  }
            backImg = new BufferedImage(width,height,BufferedImage.TYPE_4BYTE_ABGR_PRE);
                      
               setFormat(Format.RGBA8);
               setWidth(backImg.getWidth());
               setHeight(backImg.getHeight());
               scratch = ByteBuffer.allocateDirect(4 * backImg.getWidth()
                    * backImg.getHeight());
          } catch (IllegalArgumentException e) {
          }
     }

     public void refreshImage() {
         Graphics2D g = backImg.createGraphics();
         paint(g);
         g.dispose();
 
         /* get the image data */
         byte data[] = (byte[]) backImg.getRaster().getDataElements(0, 0,
             backImg.getWidth(), backImg.getHeight(), null);
         scratch.clear();
         scratch.put(data, 0, data.length);
         scratch.rewind();
         setData(scratch);
     }
     

     public abstract void paint(Graphics2D graphicsContext);
     
     public static BufferedImage verticalflip(BufferedImage img) {  
            int w = img.getWidth();  
            int h = img.getHeight();  
            BufferedImage dimg = dimg = new BufferedImage(w, h, img.getColorModel().getTransparency());  
            Graphics2D g = dimg.createGraphics();  
            g.drawImage(img, 0, 0, w, h, 0, h, w, 0, null);  
            g.dispose();  
            return dimg;  
    }  
 }