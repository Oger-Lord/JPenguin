/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.editor.object;

import com.jme3.export.*;
import com.jme3.scene.*;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.shape.Box;
import com.jme3.material.Material;
import com.jme3.asset.AssetManager;
import java.io.IOException;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector2f;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
//import com.jme3.renderer
import de.jpenguin.pathing.PathingLayer;
import de.jpenguin.pathing.PathingMap;
import de.jpenguin.pathing.PathingMapName;

import de.jpenguin.editor.terrain.TerrainManager;


import java.util.List;
/**
 *
 * @author Karsten
 */
public class Doodad implements Savable {
    
   protected DoodadType type;
    
   private float rotation=0;
   private float rotationB=0;
   private float rotationC=0;
   protected Vector3f location=Vector3f.ZERO;
   private float size=1;
   protected ColorRGBA colorRGBA = new ColorRGBA();
   private float height=0;
   
   protected boolean highLocked=false;
   protected boolean rotationLocked=false;
   protected boolean rotationSpecialLocked=false;
   protected boolean sizeLocked=false;
   protected boolean colorLocked=false;
   
   protected Spatial model;
   
//   private boolean brush=false;
   
   public static Doodad Spatial2Doodad(Spatial sp)
   {
       if(sp.getUserData("doodad") == null)
       {
           Spatial sp2 = sp.getParent();
           return sp2.getUserData("doodad");
       }
       
       return sp.getUserData("doodad");
   }
   
   public Doodad()
   {
       
   }
   
   public Doodad(DoodadType t)
   {
      // brush=b;
       type=t;
       t.addDoodad(this);
   }
   
   public void setType(DoodadType t)
   {
       type=t;
   }
   
   public Node getNode()
   {
       if(model != null)
       {
           return model.getParent();
       }
       return null;
   }
   
   public void createSpatial(AssetManager assetManager,Node node)
   {
       if(model!=null){
           removeSpatial();
       }

        try{
            model = assetManager.loadModel(getType().getValue("model"));
        }catch(Exception e){}
        
        if(model == null)
        {
            model = new Geometry("",new Box(1, 1, 1));
            Material mat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
            mat.setColor("Color", ColorRGBA.Blue);   // set color of material to blue
            getModel().setMaterial(mat);     
        }
        
        model.setShadowMode(ShadowMode.CastAndReceive);
        model.setName("doodad");
        model.setUserData("doodad",this);
        
        updateSpatial();
        
        if(node != null)
        {
            node.attachChild(model);
        }
        
   }
   
   public void removeSpatial()
   {
       if(getModel()==null){return;}
       
       model.removeFromParent();
       model=null;
   }
   
   
   public void updateHeight(TerrainManager tm)
   {
       if(isHighLocked()==false)
       {
             float x =location.getX();
             float z =location.getZ();
             float y = tm.getHeight(x, z);
             
             try{
               float h = Float.parseFloat(getType().getValue("height"));
               y+=h;
             }catch(Exception e){}

             location = new Vector3f(x,y,z);
             
             if(getModel()==null){return;}
             model.setLocalTranslation(location);
       }
   }
   
   public void updateSpatial()
   {
       //Update Pathing
       
        updateSize();
        updateRotation();
        updateColor();
        updateLocation();
   }
   
   
   public void updateSize()
   {
       if(getModel()==null){return;}
        
        if(isSizeLocked())
        {
            model.scale(getSize());
            model.setLocalScale(size, size, size);
        }else{
            try{
               float s = Float.parseFloat(getType().getValue("size"));
               model.setLocalScale(s, s, s);
              //  model.scale(Float.parseFloat(type.getValue("size")));
            }catch(Exception e){}
        }
        

   }
   
   
   public void updateRotation()
   {
       if(getModel()==null){return;}
       
       float quaterx=0;
        float quatery=0;
        float quaterz=0;
        
        Quaternion q = new Quaternion(getModel().getWorldRotation());
        
        if(isRotationLocked())
        {
             quatery = rotation;
        }else{
            try{
                quatery= Float.parseFloat(getType().getValue("rotation"));
            }catch(Exception e){}
        }
        
        if(isRotationSpecialLocked())
        {
             quaterx = getRotationB();
             quaterz = getRotationC();
        }else{
            try{
                quaterx= Float.parseFloat(getType().getValue("rotationB"));
                quaterz= Float.parseFloat(getType().getValue("rotationC"));
            }catch(Exception e){}
        }
         
         q.fromAngles(quaterx, quatery,quaterz );
         getModel().setLocalRotation(q); 
   }
   
   
   public void updateColor()
   {
       if(getModel()==null){return;}
       
        if(isColorLocked())
        {
            setSpatialColor(colorRGBA);
        }else{
            try{
                float r = Float.parseFloat(getType().getValue("colorRed"));
                float g = Float.parseFloat(getType().getValue("colorGreen"));
                float b = Float.parseFloat(getType().getValue("colorBlue"));
                
               // if(r != 0.5 || g != 0.5 || b != 0.5)
               // {
                    setSpatialColor(new ColorRGBA(r,g,b,0.5f));
               // }
            }catch(Exception e){}
        }
   }
   
   public void updateLocation()
   {
       if(getModel()==null){return;}
       
        if(isHighLocked())
        {
             Vector3f v3f= location.clone();
             v3f.setY(v3f.getY()+height);
             model.setLocalTranslation(v3f);
        }else{
           Vector3f v3f= location.clone();
           
           try{
                float h = Float.parseFloat(getType().getValue("height"));
                v3f.setY(v3f.getY()+h);
           }catch(Exception e){}
           
           model.setLocalTranslation(v3f);
        }
   }
   
   
   protected void setSpatialColor(ColorRGBA c)
   {
         Node n = (Node)model;
         List list = n.getChildren();

         for(int i=0;i<list.size();i++)
         {
                Geometry g = (Geometry)list.get(i);
                Material m = g.getMaterial();
                m.setColor("Ambient", c);
                g.setMaterial(m);
                
        }
   }
   
   protected void setSpatialAlpha(float value)
   {
         Node n = (Node)model;
         List list = n.getChildren();

         for(int i=0;i<list.size();i++)
         {
                Geometry g = (Geometry)list.get(i);
                Material m = g.getMaterial();
                ColorRGBA c = ((ColorRGBA)m.getParam("Diffuse").getValue()).clone();
                c.a=value;
                m.setColor("Diffuse", c);
              //  m.setTransparent(true);
                
                m.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off); 
                m.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
                
                g.setQueueBucket(Bucket.Transparent);
                
                g.setMaterial(m);
        }
   }
   
   
   public void setRotation(float r)
   {
       rotation = r;
        setRotationLocked(true);
   }
   
   public void setSize(float s)
   {
       size=s;
        setSizeLocked(true);
   }
   
   
   public void setHeight(float h)
   {
        setHighLocked(true);
       height=h;
   }
   
   public void remove()
   {
       removeSpatial();
        getType().removeDoodad(this);
   }

   public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        
        if(isHighLocked())
        {
            capsule.write(isHighLocked(),   "highLocked",   false);
            capsule.write(height,   "height",   0);
        }
        
        if(isRotationLocked())
        {
            capsule.write(isRotationLocked(),   "rotationLocked",   false);
            capsule.write(getRotation(),   "rotation",   0);
        }
        
        if(isSizeLocked())
        { 
            capsule.write(isSizeLocked(),   "sizeLocked",   false);
            capsule.write(getSize(),   "size",   1);
        }
        
        if(isColorLocked())
        {
            capsule.write(isColorLocked(),   "colorLocked",   false);
            capsule.write(colorRGBA,   "color",   ColorRGBA.White);
        }
        
        if(isRotationSpecialLocked())
        {
            capsule.write(isRotationSpecialLocked(),   "rotationSpecialLocked",   false);
            capsule.write(getRotationB(),   "rotationB",   0);
            capsule.write(getRotationC(),   "rotationC",   0);
        }
        
        capsule.write(location,   "location",   Vector3f.ZERO);
        
    }
 
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        
        setHighLocked(capsule.readBoolean( "highLocked", false));
        setRotationLocked(capsule.readBoolean( "rotationLocked", false));
        setSizeLocked(capsule.readBoolean( "sizeLocked", false));
        setColorLocked(capsule.readBoolean( "colorLocked", false));
        setRotationSpecialLocked(capsule.readBoolean( "rotationSpecialLocked", false));
        
        if(isRotationLocked())
        {
            rotation = capsule.readFloat( "rotation", 0);
        }
        
        if(isSizeLocked())
        {
            size = capsule.readFloat( "size", 1);
        }
        
        if(isColorLocked())
        {
            colorRGBA = (ColorRGBA)capsule.readSavable( "color", ColorRGBA.White);
        }
        
        if(isHighLocked())
        {
            height = capsule.readFloat( "height", 0);
        }
        
        if(isRotationSpecialLocked())
        {
            setRotationB(capsule.readFloat( "rotationB", 0));
            setRotationC(capsule.readFloat( "rotationC", 0));
        }
        
        location = (Vector3f)capsule.readSavable( "location", Vector3f.ZERO);
    }

    /**
     * @return the rotation
     */
    public float getRotation() {
        if(isRotationLocked())
        {
            return rotation;
        }else{
            try{
                return Float.parseFloat(getType().getValue("rotation"));
           }catch(Exception e){}
        }
        return 0;
    }

    /**
     * @return the location
     */
    public Vector3f getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    /*
    public void setLocation(Vector3f loc) {
       // float height = location.getY();
        if(isHighLocked())
        {
            location = new Vector3f(loc.getX(),location.getY(),loc.getZ());
        }else{
            location = loc.clone();
        }
    }
     * */
    
    public void setLocation(Vector2f loc, TerrainManager tm)
    {
        if(type.getPathingSize()%2==1)
        {
            if(Math.abs(loc.getX()%2)==1 && Math.abs(loc.getY()%2)==1)
            {
                setLocationPrivate(loc,tm);
            }else{
                float x = ((int)((loc.getX()+0.5f)/2f))*2+1f;
                float z = ((int)((loc.getY()+0.5f)/2f))*2+1f;
                setLocationPrivate(new Vector2f(x,z),tm);
           }
        }else if(type.getPathingSize() != 0){
            if(Math.abs(loc.getX()%2)==0 && Math.abs(loc.getY()%2)==0)
            {
                setLocationPrivate(loc,tm);
            }else{
            
                float x = ((int)((loc.getX()+0.5f)/2f))*2;
                float z = ((int)((loc.getY()+0.5f)/2f))*2;
                setLocationPrivate(new Vector2f(x,z),tm);
            }
        }else{
            setLocationPrivate(loc,tm);
        }
    }
    
   private void setLocationPrivate(Vector2f loc, TerrainManager tm) {
       // float height = location.getY();
        if(isHighLocked())
        {
            location = new Vector3f(loc.getX(),location.getY(),loc.getY());
        }else{
            
            location = new Vector3f(loc.getX(),tm.getHeight(loc.getX(), loc.getY()),loc.getY());
        }
    }

    

    /**
     * @return the height
     */
    public float getHeight() {
        if(isHighLocked())
        {
            return height;
        }else{
            try{
                return Float.parseFloat(getType().getValue("height"));
           }catch(Exception e){}
        }
        return 0;
    }

    /**
     * @return the size
     */
    public float getSize() {
        if(isSizeLocked())
        {
            return size;
        }else{
            try{
                return Float.parseFloat(getType().getValue("size"));
           }catch(Exception e){}
        }
        return 1;
    }

    /**
     * @return the colorRGBA
     */
    public ColorRGBA getColorRGBA() {
        if(isColorLocked())
        {
            return colorRGBA;
        }else{
            try{
                float r = Float.parseFloat(getType().getValue("colorRed"));
                float g = Float.parseFloat(getType().getValue("colorGreen"));
                float b = Float.parseFloat(getType().getValue("colorBlue"));
                return new ColorRGBA(r,g,b,1);
           }catch(Exception e){}
        }
        return null;
    }
    
    //The Clone can be a brush
    public Doodad clone(boolean brush) //boolean b
    {
        Doodad d;
        if(brush==false)
        {
            d= new Doodad(type);
        }else{
           d = new DoodadBrush(type);
        }
        
        d.location=location.clone();
        d.colorLocked=colorLocked;
        d.colorRGBA=colorRGBA.clone();
        d.height=height;
        d.highLocked=highLocked;
        d.rotation=rotation;
        d.rotationLocked=rotationLocked;
        d.size=size;
        d.sizeLocked=sizeLocked;
        return d;
    }

    /**
     * @param colorRGBA the colorRGBA to set
     */
    public void setColorRGBA(ColorRGBA colorRGBA) {
        this.colorRGBA = colorRGBA;
        setColorLocked(true);
        
       // System.out.println(colorRGBA.toString());
    }
    
   public int getPathingSize()
   {
        try{
          return Integer.parseInt(getType().getValue("pathingSize"));   
       }catch(Exception e){}
        
       return 0;
   }

    /**
     * @return the model
     */
    public Spatial getModel() {
        return model;
    }

    /**
     * @return the highLocked
     */
    public boolean isHighLocked() {
        return highLocked;
    }

    /**
     * @param highLocked the highLocked to set
     */
    public void setHighLocked(boolean highLocked) {
        this.highLocked = highLocked;
    }

    /**
     * @return the rotationLocked
     */
    public boolean isRotationLocked() {
        return rotationLocked;
    }

    /**
     * @param rotationLocked the rotationLocked to set
     */
    public void setRotationLocked(boolean rotationLocked) {
        this.rotationLocked = rotationLocked;
    }

    /**
     * @return the sizeLocked
     */
    public boolean isSizeLocked() {
        return sizeLocked;
    }

    /**
     * @param sizeLocked the sizeLocked to set
     */
    public void setSizeLocked(boolean sizeLocked) {
        this.sizeLocked = sizeLocked;
    }

    /**
     * @return the colorLocked
     */
    public boolean isColorLocked() {
        return colorLocked;
    }

    /**
     * @param colorLocked the colorLocked to set
     */
    public void setColorLocked(boolean colorLocked) {
        this.colorLocked = colorLocked;
    }

    /**
     * @return the type
     */
    public DoodadType getType() {
        return type;
    }

    /**
     * @return the brush
     */
  //  public boolean isBrush() {
  //      return brush;
   // }

    /**
     * @return the rotationB
     */
    public float getRotationB() {
        return rotationB;
    }

    /**
     * @param rotationB the rotationB to set
     */
    public void setRotationB(float rotationB) {
        this.rotationB = rotationB;
    }

    /**
     * @return the rotationC
     */
    public float getRotationC() {
        return rotationC;
    }

    /**
     * @param rotationC the rotationC to set
     */
    public void setRotationC(float rotationC) {
        this.rotationC = rotationC;
    }

    /**
     * @return the rotationSpecialLocked
     */
    public boolean isRotationSpecialLocked() {
        return rotationSpecialLocked;
    }

    /**
     * @param rotationSpecialLocked the rotationSpecialLocked to set
     */
    public void setRotationSpecialLocked(boolean rotationSpecialLocked) {
        this.rotationSpecialLocked = rotationSpecialLocked;
    }
    
    
    
    
   public void setSpace(PathingMap pathingMap, PathingLayer layer)
   {
       if(type.getPathingSize() > 0)
       {
           pathingMap.setSpace(location.getX(), location.getZ(), type.getPathingSize(), type.getPathingSize(), PathingMapName.Building, layer, true);
           pathingMap.setSpace(location.getX(), location.getZ(), type.getPathingSize(), type.getPathingSize(), PathingMapName.Ground, layer, true);
       }
   }
   
   public void removeSpace(PathingMap pathingMap, PathingLayer layer)
   {
       if(type.getPathingSize() > 0)
       {
           pathingMap.setSpace(location.getX(), location.getZ(), type.getPathingSize(), type.getPathingSize(), PathingMapName.Building, layer, false);
           pathingMap.setSpace(location.getX(), location.getZ(), type.getPathingSize(), type.getPathingSize(), PathingMapName.Ground, layer, false);
       }
   }
   
   
   public boolean hasSpace(PathingMap pathingMap)
   {
       return pathingMap.hasSpace(location.getX(), location.getZ(), type.getPathingSize(), type.getPathingSize(), PathingMapName.Ground);
   }
}
    
    
