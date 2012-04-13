/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.fog;

import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture2D;
import com.jme3.app.state.AbstractAppState;
import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.math.ColorRGBA;

import java.util.Hashtable;
import java.util.Enumeration;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import java.nio.ByteBuffer;
/**
 *
 * @author Karsten
 */
public class FogOfWarTerrain extends FogOfWarEffect {
    
    private Texture texture;
    private Image image;
    private FogOfWarTerrainThread thread;
    private ExecutorService executor = Executors.newCachedThreadPool();
    private Future result;
    
    public FogOfWarTerrain(TerrainQuad terrain)
    {
        texture = new Texture2D();
        
        thread = new FogOfWarTerrainThread();
        image =new Image(Format.RGBA8,FogOfWar.getWidth(),FogOfWar.getHeight(),ByteBuffer.allocateDirect(FogOfWar.getWidth()*FogOfWar.getHeight()*4));
        texture.setImage(image);
        
        Material m =terrain.getMaterial();
        m.setTexture("FogMap", texture);
        terrain.setMaterial(m);
    }
    
    private class FogOfWarTerrainThread implements Callable
    {
        private Hashtable<Integer,FogChange> changes;
        private ByteBuffer bb;
        private float tpf;
        
        public FogOfWarTerrainThread()
        {
            changes = new Hashtable<Integer,FogChange>();
            this.bb=ByteBuffer.allocateDirect(FogOfWar.getWidth()*FogOfWar.getHeight()*4);
        }
        
        private class FogChange
        {
            private int x,y;
            private float from,to;
            

            public FogChange(int x,int y,float from, float to)
            {
                this.x=x;
                this.y=y;
                this.from=from;
                this.to=to;

                FogChange old =changes.get(y*FogOfWar.getWidth() + x);
                if(old != null)
                {
                    this.from = old.from;
                    changes.remove(y*FogOfWar.getWidth() + x);
                }
               // if(from == to)
               // {
               //     changes.remove(y*fow.getHeight() + x);
               // }else{
                    changes.put(y*FogOfWar.getWidth() + x,this);
              //  }
                
                
            }

            public void update(float tpf)
            {
                if(to > from)
                {
                    from += tpf;
                    if(from >= to )
                    {
                        from=to;
                        changes.remove(y*FogOfWar.getWidth() + x);
                    }

                }else{
                     from -= tpf;
                    if(from <= to )
                    {
                        from=to;
                        changes.remove(y*FogOfWar.getWidth() + x);
                    } 
                }

                manipulatePixel(bb, x, y, 0.1f,from);
            }
        }
        
        public synchronized void change(int x,int y,float from, float to)
        {
            new FogChange(x,y,from, to);
        }
        
        public void setTPF(float tpf)
        {
            this.tpf=tpf;
        }
        
        public synchronized Object call()
        {
            Enumeration e = changes.keys();

            while(e.hasMoreElements())
            {

              FogChange fc =changes.get(e.nextElement());

              fc.update(tpf);
            }

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

        
        protected void manipulatePixel(ByteBuffer buf, int x, int y, float bright,float alpha){
           //  = image.getData(0);
            int width = image.getWidth();

            int position = ((FogOfWar.getHeight()-y) * width + x) * 4;

            if ( position> buf.capacity()-1 || position<0 )
                return;

                switch (image.getFormat()){
                    case RGBA8:
                        buf.position( position+3 );
                       // buf.put(float2byte(bright))
                       //    .put(float2byte(bright))
                       //    .put(float2byte(bright))
                        buf.put(float2byte(alpha));
                        return;
                     case ABGR8:
                        buf.position( position );
                        buf.put(float2byte(alpha))
                           .put(float2byte(bright))
                           .put(float2byte(bright))
                           .put(float2byte(bright));
                        return;
                    default:
                        throw new UnsupportedOperationException("Image format: "+image.getFormat());
                }

        }

        private byte float2byte(float f){
            return (byte) (f * 255f);
        }
    }
    
    
    
    public void update(float tpf)
    {
        if(result == null || result.isDone())
        {
            
            if(result != null)
            {
                try{
                    ByteBuffer bb=(ByteBuffer)result.get();
                    image.setData(0,bb);
                    
                   // if(gameApp.getGUI() != null)
                  //  {
                   //     gameApp.getGUI().setMinimapFog(bb);
                  //  }
                    
                }catch(Exception e){}
                texture.getImage().setUpdateNeeded();
            }
            thread.setTPF(tpf);
            result = executor.submit( thread );
        }
    }
    
    
    public void valueChange(int x, int y, int value, int old)
    {
        thread.change(x,y,int2float(old),int2float(value));
        
     //   System.err.println( int2float(old) + "->" + int2float(value));
      //  new FogChange(x,y,int2float(old),int2float(value));
        
        /*
        if(value==0) //Fog
        {
              manipulatePixel(image,x,y,0.20f,0.6f);
        }else if(value>0) { //No Fog
              manipulatePixel(image,x,y,0,0f);
        }else{  //Black Mask
              manipulatePixel(image,x,y,0.05f,0.85f);
        } 
         * */
    }
    
    private float int2float(int i)
    {
        if(i==0)
        {
            return 0.6f;
        }
        if(i==-1)
        {
            return 0.85f;
        }
        return 0;
    }
    
    
    public void destroy()
    {
        executor.shutdown();
    }
    
}
