/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.editor.pathing2;

import de.jpenguin.editor.pathing.*;
import com.jme3.texture.Image;
import com.jme3.texture.Image.Format;
import com.jme3.math.ColorRGBA;

import java.util.ArrayList;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import java.nio.ByteBuffer;
/**
 *
 * @author Karsten
 */
public class DrawPathingImage{
    
    private ColorRGBA[][] oldColors;
    
    private PathingMapName[] arrayMapsDisplay;
    
    private Image image;
    private FogOfWarTerrainThread thread;
    private ExecutorService executor = Executors.newCachedThreadPool();
    private Future result;
    private PathingMap pathingMap;
    
    public DrawPathingImage(PathingMap pathingMap)
    {
        this.pathingMap=pathingMap;
        
        arrayMapsDisplay = new PathingMapName[2];
        arrayMapsDisplay[0] = PathingMapName.UnitMap;
        arrayMapsDisplay[1] = PathingMapName.EditorMap;
        
        thread = new FogOfWarTerrainThread();
        image =new Image(Format.RGBA8,pathingMap.getWidth(),pathingMap.getHeight(),ByteBuffer.allocateDirect(pathingMap.getWidth()*pathingMap.getHeight()*4));
    
        oldColors = new ColorRGBA[pathingMap.getWidth()][pathingMap.getHeight()];
            for(int x=0;x<pathingMap.getWidth();x++)
            {
                for(int y=0;y<pathingMap.getHeight();y++)
                {
                    new ColorRGBA(0,0,0,0.4f);
                }
            }
    }
    
    
    private class FogOfWarTerrainThread implements Callable
    {
        private ArrayList<FogChange> changes;
        private ByteBuffer bb;
        
        public FogOfWarTerrainThread()
        {
            changes = new ArrayList<FogChange>();
            this.bb=ByteBuffer.allocateDirect(pathingMap.getWidth()*pathingMap.getHeight()*4);
        }
        
        private class FogChange
        {
            private int x,y;
            private ColorRGBA color;

            public FogChange(int x,int y,ColorRGBA color)
            {
                this.x=x;
                this.y=y;
                this.color=color;

                changes.add(this); 
            }

            public void update()
            {
                manipulatePixel(bb, x, y, color);
            }
        }
        
        public void change(int x,int y,ColorRGBA color)
        {
            new FogChange(x,y,color);
        }
        
        public Object call()
        {
            for(int i=0;i<changes.size();i++)
            {
              FogChange fc =changes.get(i);
              fc.update();
            }
            
            changes.clear();

            bb.rewind();
            return clone(bb);
        }
        
       public ByteBuffer clone(ByteBuffer original){
            ByteBuffer clone = ByteBuffer.allocateDirect(original.capacity());
            clone.put(original);
            original.rewind();
            clone.flip();
            original.rewind();//copy from the beginning
            return clone;
        }

        
   protected void manipulatePixel(ByteBuffer buf, int x, int y, ColorRGBA color){
        int width = image.getWidth();

        int position = ((image.getHeight()-y) * width + x) * 4;

        if ( position> buf.capacity()-1 || position<0 )
            return;
        

            switch (image.getFormat()){
                case RGBA8:
                    buf.position( position );
                    buf.put(float2byte(color.r))
                       .put(float2byte(color.g))
                       .put(float2byte(color.b))
                       .put(float2byte(color.a));
                    return;
                 case ABGR8:
                    buf.position( position );
                    buf.put(float2byte(color.a))
                       .put(float2byte(color.b))
                       .put(float2byte(color.g))
                       .put(float2byte(color.r));
                    return;
                default:
                    throw new UnsupportedOperationException("Image format: "+image.getFormat());
            }
    }

        private byte float2byte(float f){
            return (byte) (f * 255f);
        }
    }
    
    
    
    public void update()
    {
        if(result == null || result.isDone())
        {
            
            if(result != null)
            {
                try{
                    ByteBuffer bb=(ByteBuffer)result.get();
                    image.setData(0,bb);
                    
                }catch(Exception e){}
                image.setUpdateNeeded();
            }
            
            for(int x=0;x<pathingMap.getWidth();x++)
            {
                for(int y=0;y<pathingMap.getHeight();y++)
                {
                    
                    ColorRGBA color = getColor(x,y);
                    
                    if(oldColors.equals(color)==false)
                    {
                        thread.change(x,y,color);
                    }
                }
            }
            
            result = executor.submit( thread );
        }
    }
    
    private ColorRGBA getColor(int x, int y)
    {
        ColorRGBA color = new ColorRGBA(0,0,0,04f);
        
        if(pathingMap.getValue(x, y, PathingLayer.Ground,arrayMapsDisplay)==0)
        {
            color.r=1;
        }
        
        if(pathingMap.getValue(x, y, PathingLayer.Air,arrayMapsDisplay)==0)
        {
            color.g=1;
        }
        
        if(pathingMap.getValue(x, y, PathingLayer.Building,arrayMapsDisplay)==0)
        {
            color.b=1;
        }
        
        return color;
    }
    
    
    
    public Image getImage()
    {
        return image;
    }
}
