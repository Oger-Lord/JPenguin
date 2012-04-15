/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.editor.settings;

import java.awt.Color;

import com.jme3.export.Savable;
import com.jme3.export.*;
import java.io.IOException;
import com.jme3.export.xml.*;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.util.SkyFactory;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;
import com.jme3.scene.Spatial;
import com.jme3.scene.Node;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;

import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.KeyInput;

import java.io.*;
import com.jme3.export.*;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.export.binary.BinaryImporter;
import com.jme3.shadow.PssmShadowRenderer;
import com.jme3.shadow.PssmShadowRenderer.CompareMode;
import com.jme3.shadow.PssmShadowRenderer.FilterMode;

import com.jme3.water.WaterFilter;
import com.jme3.post.FilterPostProcessor;
import com.jme3.texture.Texture2D;

import com.jme3.shadow.BasicShadowRenderer;


import java.util.concurrent.*;

import de.jpenguin.editor.engine.EditorApplication;
import de.jpenguin.editor.Editor;

/**
 *
 * @author Karsten
 */
public class SettingManager extends AbstractAppState {
    
    private AmbientLight al;
    private DirectionalLight dl;
    private AmbientLight standard_al;
    private DirectionalLight standard_dl;
    private EditorApplication editorApp;
    private FilterPostProcessor fpp;
    private FogFilter fog;
    private Spatial sky;
    private PssmShadowRenderer pssmRenderer;
    private BasicShadowRenderer basicRenderer;
    private boolean active=false;
    
    private boolean fogIsActive=true;
    private boolean shadowIsActive=true;
    private boolean ambientLightIsActive=true;
    
    private FilterPostProcessor postWater;
    
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager,app);
        
       this.editorApp=(EditorApplication)app;
        
       // myRootNode = new Node();
        
        al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(2));
        
        dl = new DirectionalLight();
        dl.setDirection((new Vector3f(-1f, -1f, -1f)).normalize());
        dl.setColor(new ColorRGBA(0.965f, 0.949f, 0.772f, 1f).mult(0.7f));
        
        sky = SkyFactory.createSky(editorApp.getAssetManager(), "Textures/Sky/BrightSky.dds", false);

        fpp=new FilterPostProcessor(editorApp.getAssetManager());
        fog=new FogFilter();
        fog.setFogColor(new ColorRGBA(0.9f, 0.9f, 0.9f, 1));
        fog.setFogDistance(155);
        fog.setFogDensity(1.0f);
        fpp.addFilter(fog);
        

        standard_dl = new DirectionalLight();
        standard_dl.setName("light");
        standard_dl.setColor(new ColorRGBA(1.5f, 1.5f, 1.5f, 1.5f));
        standard_dl.setDirection((new Vector3f(-1f, -1f, -1f)).normalize());
        editorApp.getRootNode().addLight(standard_dl);

        standard_al = new AmbientLight();
        standard_al.setName("ambiente");
        standard_al.setColor(new ColorRGBA(1f, 1f, 0.8f, 1f));
        editorApp.getRootNode().addLight(standard_al);
        
        editorApp.getInputManager().addMapping("view", new KeyTrigger(KeyInput.KEY_V));
        editorApp.getInputManager().addListener(actionListener, "view");
        
        
      //  pssmRenderer = new PssmShadowRenderer(editorApp.getAssetManager(), 1024, 1);
        pssmRenderer = new PssmShadowRenderer(editorApp.getAssetManager(), 1024*2, 1);
        pssmRenderer.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
       // pssmRenderer.setDirection((new Vector3f(0.5f, 1f, 0.5f)).normalize());
        pssmRenderer.setLambda(0.55f);
        pssmRenderer.setShadowIntensity(0.7f);
        pssmRenderer.setCompareMode(CompareMode.Software);
     //   pssmRenderer.setCompareMode(CompareMode.Hardware);
        pssmRenderer.setFilterMode(FilterMode.PCF4    );
       // pssmRenderer.setFilterMode(FilterMode.Bilinear    );
        
        
        basicRenderer = new BasicShadowRenderer(editorApp.getAssetManager(), 512);
        basicRenderer.setDirection(new Vector3f(-.5f,-.5f,-.5f).normalizeLocal()); // light direction
    
       // water();
    }
    
    
   private ActionListener actionListener = new ActionListener() {

        public void onAction(String name, boolean pressed, float tpf) {
            if(name.equals("view"))
            {
                if(pressed)
                {
                    if(active == false)
                    {
                        activate(true);
                    }else{
                        activate(false);
                    }
                }
            }
        }
    };
    
    public void setFogColor(ColorRGBA c)
    {
        fog.setFogColor(c);
    }
    
    public ColorRGBA getFogColor()
    {
        return fog.getFogColor();
    }
    
    public void setFogDistance(float i)
    {
        fog.setFogDistance(i);
    }
    
    public void setFogDensity(float d)
    {
        fog.setFogDensity(d);
    }
    
    public float getFogDensity()
    {
        return fog.getFogDensity();
    }
    
   public float getFogDistance()
    {
        return fog.getFogDistance();
    }
   
    public float getShadowIntensity()
    {
        return pssmRenderer.getShadowIntensity();
    }
    
    public void setShadowIntensity(float value)
    {
        pssmRenderer.setShadowIntensity(value);
    }
   
    public void setLightColor(ColorRGBA c)
    {
        al.setColor(c);
    }
    
    public ColorRGBA getLightColor()
    {
        return al.getColor();
    }
    
    public void setLightVector(String s)
    {
        String array[] = s.split("|");
        if(array.length==3)
        {
            dl.setDirection(new Vector3f(Float.parseFloat(array[0]),Float.parseFloat(array[1]),Float.parseFloat(array[2])));
        }
    }   
   
    public String getLightVector()
    {
        Vector3f v3 = dl.getDirection();
        return v3.x + "|" + v3.y + "|" + v3.z;
    }
    
    public float getAmbientLightIntensity()
    {
        return dl.getColor().getAlpha();
    }
    
    public void setAmbientLightIntensity(float f)
    {
        dl.getColor().multLocal(1/dl.getColor().getAlpha());
        dl.getColor().multLocal(f);
    }

    public void activate(boolean b)
    {
        active=b;
        
        editorApp.enqueue(new activateCallable(b));
    }
    
    private class activateCallable implements Callable
    {
        private boolean active;
        
        public activateCallable(boolean b)
        {
            this.active=b;
        }
        
        public Object call()
        {
            if(active)
            {
                editorApp.getViewPort().removeProcessor(postWater);
                
                editorApp.getRootNode().removeLight(standard_al);
                editorApp.getRootNode().removeLight(standard_dl);

                if(isShadowIsActive())
                {
                   editorApp.getViewPort().addProcessor(pssmRenderer);
                   // editorApp.getViewPort().addProcessor(basicRenderer);
                }
                if(isFogIsActive())
                {
                    editorApp.getViewPort().addProcessor(fpp);
                }

                if(isAmbientLightIsActive())
                {
                    editorApp.getRootNode().addLight(al);
                }
                editorApp.getRootNode().addLight(dl);
                editorApp.getRootNode().attachChild(sky);
                
                editorApp.getViewPort().addProcessor(postWater);
            }else{
                editorApp.getRootNode().addLight(standard_al);
                editorApp.getRootNode().addLight(standard_dl);

                editorApp.getViewPort().removeProcessor(fpp);
                editorApp.getViewPort().removeProcessor(pssmRenderer);
                editorApp.getViewPort().removeProcessor(basicRenderer);
                editorApp.getRootNode().removeLight(al);
                editorApp.getRootNode().removeLight(dl);
                editorApp.getRootNode().detachChild(sky);
            }
                    
            return null;
        }
    }
    
    
    public boolean isActive()
    {
        return active;
    }
    
    public void load()
    {
       
          //  FilterPostProcessor fpp;
            XMLImporter imp = XMLImporter.getInstance();
            imp.setAssetManager(editorApp.getAssetManager());
            
     try{
            dl = (DirectionalLight) imp.load( new File(Editor.getMapPath()+"directlightsave.xml"));
      }catch(Exception e){}
     
     try{
            al = (AmbientLight) imp.load(new File(Editor.getMapPath()+"ambientlightsave.xml"));  
       }catch(Exception e){ambientLightIsActive=false;}
            
      try{
            fog = (FogFilter) imp.load(new File(Editor.getMapPath()+"fogsave.xml"));  
            fpp.cleanup();
            fpp.addFilter(fog);
        }catch(Exception e){fogIsActive=false;}
     
      try{
     //       pssmRenderer = (PssmShadowRenderer) imp.load(new File(Editor.getPath()+"assets/Scenes/shadowsave.xml"));  
      }catch(Exception e){shadowIsActive=false;}  
        
    }
    
    public void save()
    {
        try{
             XMLExporter.getInstance().save(dl, new File(Editor.getMapPath()+"directlightsave.xml"));
        }catch(Exception e){
        }
          
        if(ambientLightIsActive)
        {
         try{
             XMLExporter.getInstance().save(al, new File(Editor.getMapPath()+"ambientlightsave.xml"));
         }catch(Exception e){}
         
        }else{
            new File(Editor.getMapPath()+"ambientlightsave.xml").delete();
        }
            
        if(fogIsActive)
        {
          try{
              XMLExporter.getInstance().save(fog, new File(Editor.getMapPath()+"fogsave.xml"));
         }catch(Exception e){}
        }else{
            new File(Editor.getMapPath()+"fogsave.xml").delete();
        }
        
        if(shadowIsActive)
        {   
           try{
          //   XMLExporter.getInstance().save(pssmRenderer, new File(Editor.getPath()+"assets/Scenes/shadowsave.xml"));
        }catch(Exception e){}
         }else{
            new File(Editor.getMapPath()+"shadowsave.xml").delete();
        }
          
        
    }

    /**
     * @return the fogIsActive
     */
    public boolean isFogIsActive() {
        return fogIsActive;
    }

    /**
     * @param fogIsActive the fogIsActive to set
     */
    public void setFogIsActive(boolean fogIsActive) {
        this.fogIsActive = fogIsActive;
    }

    /**
     * @return the shadowIsActive
     */
    public boolean isShadowIsActive() {
        return shadowIsActive;
    }

    /**
     * @param shadowIsActive the shadowIsActive to set
     */
    public void setShadowIsActive(boolean shadowIsActive) {
        this.shadowIsActive = shadowIsActive;
    }

    /**
     * @return the ambientLightIsActive
     */
    public boolean isAmbientLightIsActive() {
        return ambientLightIsActive;
    }

    /**
     * @param ambientLightIsActive the ambientLightIsActive to set
     */
    public void setAmbientLightIsActive(boolean ambientLightIsActive) {
        this.ambientLightIsActive = ambientLightIsActive;
    }
    
    
    
    private void water()
    {
        WaterFilter  water = new WaterFilter(editorApp.getRootNode(), new Vector3f(-1f, -1f, -1f));
        postWater = new FilterPostProcessor(editorApp.getAssetManager());
        postWater.addFilter(water);

        water.setWaveScale(0.003f);
        water.setMaxAmplitude(2f);
        water.setFoamExistence(new Vector3f(1f, 4, 0.5f));
        water.setFoamTexture((Texture2D) editorApp.getAssetManager().loadTexture("Common/MatDefs/Water/Textures/foam2.jpg"));
        //water.setNormalScale(0.5f);

        //water.setRefractionConstant(0.25f);
        water.setRefractionStrength(0.2f);
        //water.setFoamHardness(0.6f);

        water.setWaterHeight(0);
        
        editorApp.getViewPort().addProcessor(postWater);
    }
        
}
