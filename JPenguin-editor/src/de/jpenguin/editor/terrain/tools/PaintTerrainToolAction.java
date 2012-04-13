/*
 * Copyright (c) 2009-2011 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.jpenguin.editor.terrain.tools;

//import com.jme3.gde.core.sceneexplorer.nodes.AbstractSceneExplorerNode;
import com.jme3.material.MatParam;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.terrain.Terrain;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.ArrayList;

import de.jpenguin.editor.engine.Undo;

/**
 * Paint the texture at the specified location.
 * 
 * @author Brent Owens and Oger-Lord
 */
public class PaintTerrainToolAction extends Undo {
    
    private Vector3f worldLoc;
    private float radius;
    private float weight;
    private int selectedTextureIndex;
    
    List<Vector2f> undoLocs;
    List<ColorRGBA[]> undoColors;
    
    
    public PaintTerrainToolAction(Vector3f markerLocation, float radius, float weight, int selectedTextureIndex) {
        this.worldLoc = markerLocation.clone();
        this.radius = radius;
        this.weight = weight;
        this.selectedTextureIndex = selectedTextureIndex;
       // name = "Paint terrain";
    }
    
   // @Override
    protected Object doApplyTool() {
       Terrain terrain = null;// Terrain terrain = getTerrain(rootNode.getLookup().lookup(Node.class));
        if (terrain == null)
            return null;
        paintTexture(terrain, worldLoc, radius, weight, selectedTextureIndex);
        return terrain;
    }
    
   // @Override
    
    public void doUndoTool(Object undoObject) {
        if (undoObject == null)
            return;
        if (undoLocs == null || undoColors == null)
            return;
        resetColor((Terrain)undoObject, undoLocs, undoColors,selectedTextureIndex);
    }
    
    public void paintTexture(Terrain terrain, Vector3f markerLocation, float toolRadius, float toolWeight, int selectedTextureIndex) {
        if (selectedTextureIndex < 0 || markerLocation == null)
            return;
        
        int alphaIdx = selectedTextureIndex/4; // 4 = rgba = 4 textures

        Vector2f UV = getPointPercentagePosition(terrain, markerLocation);

        // get the radius of the brush in pixel-percent
        float brushSize = toolRadius/(terrain.getTerrainSize()*((Node)terrain).getLocalScale().x);
        float weightSize = Math.abs(brushSize*toolWeight);//(toolRadius*toolWeight)/(terrain.getTerrainSize()*((Node)terrain).getLocalScale().x);
        int texIndex = selectedTextureIndex - ((selectedTextureIndex/4)*4); // selectedTextureIndex/4 is an int floor, do not simplify the equation
        boolean erase = toolWeight<0;
        if (erase)
            toolWeight *= -1;

        doPaintAction(texIndex, terrain, alphaIdx,UV, true, brushSize, weightSize);
        
    }
    
    public Vector2f getPointPercentagePosition(Terrain terrain, Vector3f worldLoc) {
        Vector2f uv = new Vector2f(worldLoc.x,-worldLoc.z);
        float scale = ((Node)terrain).getLocalScale().x;
        
        uv.subtractLocal(((Node)terrain).getWorldTranslation().x*scale, ((Node)terrain).getWorldTranslation().z*scale); // center it on 0,0
        float scaledSize = terrain.getTerrainSize()*scale;
        uv.addLocal(scaledSize/2, scaledSize/2); // shift the bottom left corner up to 0,0
        uv.divideLocal(scaledSize); // get the location as a percentage
        
        return uv;
    }
    
    private Image[] getAlphaImages(Terrain terrain) {
        Image[] images=null;
        
        MatParam matParam = null;
        matParam = terrain.getMaterial(null).getParam("AlphaMap_2");
        if(matParam != null)
        {
            images = new Image[3];
            images[2] = ((Texture) matParam.getValue()).getImage();
        }
        
        matParam = terrain.getMaterial(null).getParam("AlphaMap_1");
        if(matParam != null)
        {
            if(images == null)
            {
                images = new Image[2];
            }
            images[1] = ((Texture) matParam.getValue()).getImage();
        }
        
        if(images == null)
        {
            images = new Image[1];
        }
        matParam = terrain.getMaterial(null).getParam("AlphaMap");
        images[0] = ((Texture) matParam.getValue()).getImage();

        return images;
    }
    
    /**
     * Goes through each pixel in the image. At each pixel it looks to see if the UV mouse coordinate is within the
     * of the brush. If it is in the brush radius, it gets the existing color from that pixel so it can add/subtract to/from it.
     * Essentially it does a radius check and adds in a fade value. It does this to the color value returned by the
     * first pixel color query.
     * Next it sets the color of that pixel. If it was within the radius, the color will change. If it was outside
     * the radius, then nothing will change, the color will be the same; but it will set it nonetheless. Not efficient.
     *
     * If the mouse is being dragged with the button down, then the dragged value should be set to true. This will reduce
     * the intensity of the brush to 10% of what it should be per spray. Otherwise it goes to 100% opacity within a few pixels.
     * This makes it work a little more realistically.
     *
     * @param image to manipulate
     * @param uv the world x,z coordinate
     * @param dragged true if the mouse button is down and it is being dragged, use to reduce brush intensity
     * @param radius in percentage so it can be translated to the image dimensions
     * @param erase true if the tool should remove the paint instead of add it
     * @param fadeFalloff the percentage of the radius when the paint begins to start fading
     */
    protected void doPaintAction(int texIndex, Terrain terrain, int alphaIdx,Vector2f uv, boolean dragged, float radius, float fadeFalloff){
        Vector2f texuv = new Vector2f();
      //  ColorRGBA color = ColorRGBA.Black;
        
        Image[] images = getAlphaImages(terrain);

        undoLocs = new ArrayList<Vector2f>();
        undoColors = new ArrayList();
      //  System.out.println(alphaIdx + " " + texIndex + " " + images.length);
        
        float width = images[0].getWidth();
        float height = images[0].getHeight();

        int minx = (int) Math.max(0, (uv.x*width - radius*width)); // convert percents to pixels to limit how much we iterate
        int maxx = (int) Math.min(width,(uv.x*width + radius*width));
        int miny = (int) Math.max(0,(uv.y*height - radius*height));
        int maxy = (int) Math.min(height,(uv.y*height + radius*height));

        float radiusSquared = radius*radius;
        float radiusFalloff = radius*fadeFalloff;
        // go through each pixel, in the radius of the tool, in the image
        for (int y = miny; y < maxy; y++){
            for (int x = minx; x < maxx; x++){
                
                texuv.set((float)x / width, (float)y / height);// gets the position in percentage so it can compare with the mouse UV coordinate

                float dist = texuv.distanceSquared(uv);
                if (dist < radiusSquared ) { // if the pixel is within the distance of the radius, set a color (distance times intensity)
                    
                    ColorRGBA[] colors = new ColorRGBA[3];
                    ColorRGBA[] oldcolors = new ColorRGBA[3];
                    for(int i=0;i<images.length;i++)
                    {
                        colors[i] = new ColorRGBA(0,0,0,0);
                        manipulatePixel(images[i], x, y, colors[i], false); // gets the color at that location (false means don't write to the buffer)
                        oldcolors[i] = colors[i].clone();
                     //   colors
                    }
                    
                   // System.out.println(radiusSquared + " " + dist + " " + radiusFalloff);
                    // calculate the fade falloff intensity
                    float intensity = 0.1f;
                    if (dist > radiusFalloff) {
                        float dr = radius - radiusFalloff; // falloff to radius length
                        float d2 = dist - radiusFalloff; // dist minus falloff
                        float d3 = d2/dr; // dist percentage of falloff length
                       // intensity = 1-d2; // fade out more the farther away it is
                         intensity = 0.0002f/d3;
                         
                         if(intensity > 0.1)
                         {
                            intensity=0.1f;
                         }

                    }
     
                    //if (dragged)
                    //	intensity = intensity*0.1f; // magical divide it by 10 to reduce its intensity when mouse is dragged

                    float change=0;
                    
                    if(alphaIdx<images.length) //alphamap exists
                    {
                        switch (texIndex) {
                            case 0:
                                colors[alphaIdx].r += intensity;
                                colors[alphaIdx].clamp();
                                change = (1-colors[alphaIdx].r)/(1-oldcolors[alphaIdx].r);
                                break;
                            case 1:
                                colors[alphaIdx].g += intensity;
                                colors[alphaIdx].clamp();
                                change = (1-colors[alphaIdx].g)/(1-oldcolors[alphaIdx].g);
                                break;
                            case 2:
                                colors[alphaIdx].b += intensity;
                                colors[alphaIdx].clamp();
                                change = (1-colors[alphaIdx].b)/(1-oldcolors[alphaIdx].b);
                                break;
                            case 3:
                                colors[alphaIdx].a += intensity;
                                colors[alphaIdx].clamp();
                                change = (1-colors[alphaIdx].a)/(1-oldcolors[alphaIdx].a);
                                break;
                        }
                       // colors[alphaIdx].clamp();
                    }
                        
                    if(Float.isNaN(change))
                    {
                        change=0;
                    }
                    float value=0;
                        
                    for(int i=0;i<images.length;i++)
                    {
                        if(alphaIdx!=i || texIndex!=0){
                            colors[i].r *= change;
                            value+=colors[i].r;}
                        if(alphaIdx!=i || texIndex!=1){
                            colors[i].g *= change;
                            value+=colors[i].g;}
                        if(alphaIdx!=i || texIndex!=2){
                            colors[i].b *= change;
                            value+=colors[i].b;}
                        if(alphaIdx!=i || texIndex!=3){
                            colors[i].a *= change;
                            value+=colors[i].a;}
                    }
                    
                    if(alphaIdx<images.length) //alphamap exists
                    {
                        switch (texIndex) {
                            case 0:
                                colors[alphaIdx].r = 1- value;
                                break;
                            case 1:
                                colors[alphaIdx].g = 1- value;
                                break;
                            case 2:
                                colors[alphaIdx].b = 1- value;
                                break;
                            case 3:
                                colors[alphaIdx].a = 1- value;
                                break;
                        }
                    }
                    
                    
                    undoLocs.add(new Vector2f(x,y));
                    undoColors.add(oldcolors);

                    for(int i=0;i<images.length;i++)
                    {
                        if(oldcolors[i].equals(colors[i]) == false)
                        {
                        //    System.out.println("Intensity" + intensity +" "+ i + " "+ oldcolors[i].toString() + " " + colors[i].toString() + " " + value);
                            
                            manipulatePixel(images[i], x, y, colors[i], true);
                        }
                    }// set the new color
                }

            }
        }
        for(int i=0;i<images.length;i++)
        {
            images[i].getData(0).rewind();
            images[i].setUpdateNeeded();
        }
    }
    
    
    private void resetColor(Terrain terrain,List<Vector2f> undoLocs, List<ColorRGBA[]> undoColors, int selectedTextureIndex) {
       // List<Float> neg = new ArrayList<Float>();
       // int alphaIdx = selectedTextureIndex/4; // 4 = rgba = 4 textures
        Image[] images = getAlphaImages(terrain);

        for (int i=0;i<undoLocs.size();i++)
        {
            Vector2f loc = undoLocs.get(i);
            for(int a=0;a<images.length;a++)
            {
               ColorRGBA color = undoColors.get(i)[a];
                manipulatePixel(images[a], (int)loc.getX(), (int)loc.getY(), color, true);
            }
            //neg.add( intensity );
        }
        
        for(int a=0;a<images.length;a++)
        {
            images[a].getData(0).rewind();
            images[a].setUpdateNeeded();
        }
       // undoColor = neg;
    }
    
    
    /**
     * We are only using RGBA8 images for alpha textures right now.
     * @param image to get/set the color on
     * @param x location
     * @param y location
     * @param color color to get/set
     * @param write to write the color or not
     */
    protected void manipulatePixel(Image image, int x, int y, ColorRGBA color, boolean write){
        ByteBuffer buf = image.getData(0);
        int width = image.getWidth();

        int position = (y * width + x) * 4;

        if ( position> buf.capacity()-1 || position<0 )
            return;
        
        if (write) {
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
        } else {
            switch (image.getFormat()){
                case RGBA8:
                    buf.position( position );
                    color.set(byte2float(buf.get()), byte2float(buf.get()), byte2float(buf.get()), byte2float(buf.get()));
                    return;
                case ABGR8:
                    buf.position( position );
                    float a = byte2float(buf.get());
                    float b = byte2float(buf.get());
                    float g = byte2float(buf.get());
                    float r = byte2float(buf.get());
                    color.set(r,g,b,a);
                    return;
                default:
                    throw new UnsupportedOperationException("Image format: "+image.getFormat());
            }
        }
        
    }

    private float byte2float(byte b){
        return ((float)(b & 0xFF)) / 255f;
    }

    private byte float2byte(float f){
        return (byte) (f * 255f);
    }
}
