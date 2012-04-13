/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.menu;


import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.MagFilter;
import com.jme3.texture.Texture.MinFilter;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.TangentBinormalGenerator;

/**
 *
 * @author Karsten
 */
public class Scene {
    
       	Texture tex_Clouds;
	Material mat_Clouds;

	float cloudsSpeed = 0.004f;
	float cloudsDirection = FastMath.HALF_PI+0.25f;
	float cloudsIncX = 0, cloudsIncY = 0;
	float cloudsAlpha = 0.8f;
        float cloudValue = 0;
	boolean cloudsSwitch = false;
    
    public Scene(MenuApp menu)
    {
                // Clouds
		Box b2 = new Box(Vector3f.ZERO, 10f, 10f, 1.2f);
                Geometry geom2 = new Geometry("Box2", b2);
	//	geom2.setLocalRotation(q.set(0.12f,0.12f,0.12f,0f));
		geom2.setQueueBucket(Bucket.Transparent);
		
		tex_Clouds = menu.getAssetManager().loadTexture("Textures/Clouds1.png");
		tex_Clouds.setMinFilter(MinFilter.NearestNoMipMaps);
		tex_Clouds.setMagFilter(MagFilter.Nearest);
		tex_Clouds.setWrap(WrapMode.Repeat);
		
		mat_Clouds = new Material(menu.getAssetManager(), "MatDefs/MovingTexture.j3md");
		mat_Clouds.setTexture("ColorMap", tex_Clouds);
                mat_Clouds.setFloat("Speed", cloudsSpeed*3);
                mat_Clouds.setFloat("Rotation", cloudsDirection);
                mat_Clouds.setFloat("Alpha", 0.5f);
		mat_Clouds.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		
                geom2.setMaterial(mat_Clouds);
		menu.getRootNode().attachChild(geom2);
                
                Geometry geom = geom2.clone();
                geom.setLocalTranslation(0, 0, -5);
                geom.setLocalScale(2, 2, 2);
                Material m = geom.getMaterial().clone();
                m.setFloat("Speed", cloudsSpeed);
                m.setFloat("Alpha", cloudsAlpha);
                geom.setMaterial(m);
                menu.getRootNode().attachChild(geom);
                
                menu.getViewPort().setBackgroundColor(new ColorRGBA(0.15f,0.1f,0.2f,0.5f));
                
    }
}
