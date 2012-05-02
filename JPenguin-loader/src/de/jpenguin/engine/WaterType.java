/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.engine;

import com.jme3.app.Application;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.terrain.Terrain;
import com.jme3.texture.Image;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import de.jpenguin.engine.MyWaterFilter;
import de.jpenguin.pathing.PathingMap;
import de.jpenguin.pathing.SubPathingMap;
import java.io.IOException;
import java.nio.ByteBuffer;
/**
 *
 * @author Karsten
 */
public class WaterType implements Savable{
    
    private MyWaterFilter waterFilter;
    
    private float waterHeight=0;
    
    private String name;
    
    private int fieldsEnabled;
    private boolean enabledArray[][];
    
    private Texture enabledTexture;

    public WaterType(){
        
    }
    
    public WaterType(String name,Node rootNode,int size,float waterHeight)
    {
        this.name=name;
        
        waterFilter = new MyWaterFilter(rootNode, new Vector3f(-1f, -1f, -1f));
        waterFilter.setWaveScale(0.003f);
        waterFilter.setMaxAmplitude(2f);
        waterFilter.setFoamExistence(new Vector3f(1f, 4, 0.5f));
     //   waterFilter.setFoamTexture((Texture2D) app.getAssetManager().loadTexture("Common/MatDefs/waterFilter/Textures/foam2.jpg"));
        //waterFilter.setNormalScale(0.5f);

        //waterFilter.setRefractionConstant(0.25f);
        waterFilter.setRefractionStrength(0.2f);
        //waterFilter.setFoamHardness(0.6f);

        waterFilter.setWaterHeight(0);
        
        waterFilter.setSize(size);
        
        this.fieldsEnabled=0;
        this.waterHeight=waterHeight;
        
        enabledArray = new boolean[size/4][size/4];
        Image image =new Image(Format.RGBA8,size/4,size/4,ByteBuffer.allocateDirect(size/4*size/4*4));
        
        enabledTexture = new Texture2D(image);
        waterFilter.setEnabledMap(enabledTexture);
    }
    
    
    
    public void setWaterHeight(Terrain terrain, PathingMap pathingMap,float height,int w, int h)
    {
        if(height == waterHeight)
            return;
            
        waterHeight=height;
        
            
        for(int x=0;x<w;x++)
        {
             for(int y=0;y<h;y++)
             {
                    float fx=x-(w)/2;
                    float fy=y-(h)/2;
                    
                    if(isAtThisPosition(fx,fy,w,h))
                        updateLocation(terrain,pathingMap,fx,fy,w,h,true);
             }
        }   
    }
    
    public void updateLocation(Terrain terrain,PathingMap pathingMap,float fx, float fy, int width, int height)
    {
         if(terrain.getHeightmapHeight(new Vector2f(fx,fy)) > waterHeight)
         {
                pathingMap.setSpace(fx,fy, 1, 1, PathingMap.MapGround,PathingMap.LayerWater,false);
                pathingMap.setSpace(fx,fy, 1, 1, PathingMap.MapBuilding,PathingMap.LayerWater,false);
                pathingMap.setSpace(fx,fy, 1, 1, PathingMap.MapWater,PathingMap.LayerWater,true);
         }else{
                 pathingMap.setSpace(fx,fy, 1, 1, PathingMap.MapGround,PathingMap.LayerWater,true);
                 pathingMap.setSpace(fx,fy, 1, 1, PathingMap.MapBuilding,PathingMap.LayerWater,true);
                 pathingMap.setSpace(fx,fy, 1, 1, PathingMap.MapWater,PathingMap.LayerWater,false);
         }
    }
    
    public void updateLocation(Terrain terrain,PathingMap pathingMap,float fx, float fy, int width, int height, boolean add)
    {
         if(terrain.getHeightmapHeight(new Vector2f(fx,fy)) > waterHeight || add==false)
         {
                pathingMap.setSpace(fx,fy, 1, 1, PathingMap.MapGround,PathingMap.LayerWater,false);
                pathingMap.setSpace(fx,fy, 1, 1, PathingMap.MapBuilding,PathingMap.LayerWater,false);
                pathingMap.setSpace(fx,fy, 1, 1, PathingMap.MapWater,PathingMap.LayerWater,true);
         }else{
                 pathingMap.setSpace(fx,fy, 1, 1, PathingMap.MapGround,PathingMap.LayerWater,true);
                 pathingMap.setSpace(fx,fy, 1, 1, PathingMap.MapBuilding,PathingMap.LayerWater,true);
                 pathingMap.setSpace(fx,fy, 1, 1, PathingMap.MapWater,PathingMap.LayerWater,false);
         }
    }
    
    public boolean isAtThisPosition(float fx, float fy, int width, int height)
    {
        int x = (int)((fx+(width/2))/4+0.5);
        int y = (int)((fy+(height/2))/4+0.5);
        
        return enabledArray[x][y];
    }
    
     public boolean changeEnabledField(int x, int y, boolean b, Terrain terrain, PathingMap pathingMap, int width, int height)
    {
        if(enabledArray[x][y]==b)
            return false;
        
        System.out.println("DRAW!" + x + " " + y);
        
        Image img =enabledTexture.getImage();
       ByteBuffer bb = img.getData().get(0);
       
       int position = ((img.getHeight()-y-1) * img.getWidth() + x) * 4+3;
       bb.position( position );
        
        if(b)
        {
            bb.put((byte)255);
            fieldsEnabled++;
        }else{
            bb.put((byte)0);
            fieldsEnabled--;
        }
        
        bb.rewind();
        img.setUpdateNeeded();
        
        enabledArray[x][y] = b;
        
        for(int xx=x*4;xx<x*4+4;xx++)
        {
            for(int yy=y*4;yy<y*4+4;yy++)
            {
                updateLocation(terrain,pathingMap,xx-width/2, yy-height/2, width, height,b);
            }  
        }
        
        
        return true;
    }

    public void setReflectionNode(Node node)
    {
        if(waterFilter!=null)
        {
             waterFilter.setReflectionScene(node);
        }
    }
    
    public void setFogMap(Texture tex)
    {
        waterFilter.setFogMap(tex);
    }
    
    public MyWaterFilter getWaterFilter()
    {
        return waterFilter;
    }
    
    
    public int getFieldsEnabled()
    {
        return fieldsEnabled;
    }
    
    
    public boolean isWaterAtPos(int x, int y)
    {
        return enabledArray[x][y];
    }
    

    

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        
        capsule.write(fieldsEnabled, "fieldsEnabled",  0);

        capsule.write(waterFilter, "waterFilter",  null);
        
        capsule.write(waterHeight,"waterHeight",0);
        
        capsule.write(enabledArray,"enabledArray",null);
        
         capsule.write(name,"name","");
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        
        fieldsEnabled = capsule.readInt("fieldsEnabled",0);
        
        waterFilter  = (MyWaterFilter)capsule.readSavable( "waterFilter",   null);
        
        waterHeight =capsule.readFloat("waterHeight",0);
        
        name = capsule.readString("name","");
        
        enabledTexture = waterFilter.getEnabledMap();
        System.out.println("HALLO");
        
        enabledArray = capsule.readBooleanArray2D("enabledArray", enabledArray);
        if(enabledArray==null)
            System.out.println("fuuuuu");
        
        System.out.println("wat?!");
        
        /*
        Image img =enabledTexture.getImage();
        ByteBuffer bb = img.getData().get(0);
        
        enabledArray = new boolean[img.getWidth()][img.getHeight()];
         * 
         */
        
        /*
        System.out.println("HALLO1");
        
        if(bb==null)
             System.out.println("HALLO2");
        bb.rewind();
        
        for(int x=0;x<img.getWidth();x++)
        {
            for(int y=0;y<img.getHeight();y++)
            {
                
                int position = ((img.getHeight()-y-1) * img.getWidth() + x) * 4+3;
                bb.position( position );
                
                System.out.println(x+" "+y);
                
                if(bb.get() ==(byte)255)
                {
                    enabledArray[x][y] = true;
                }else{
                    enabledArray[x][y] = false;
                }
            }
        }
        
        bb.rewind();
         * 
         */
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
}
