/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.engine;

import com.jme3.asset.AssetManager;
import com.jme3.texture.Image;
import com.jme3.util.BufferUtils;

import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.LWJGLException;

import java.util.HashMap;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;


/**
 *
 * @author Karsten
 */
public class CursorManager {
    static private HashMap<String, Cursor> loadedCursors = new HashMap<String, Cursor>();
    private static AssetManager assetManager;
 
    /**
     * Loads and sets a hardware cursor
     *
     * @param url
     *            to imagefile
     * @param xHotspot
     *            from image left
     * @param yHotspot
     *            from image bottom
     */
    public CursorManager(AssetManager assetmanager) {
        this.assetManager = assetmanager;
    }
 
    public static synchronized void precache(String file, int xHotspot,
            int yHotspot) {
        if (assetManager == null) {
            throw new RuntimeException("CursorManager not initialized");
        }
        boolean eightBitAlpha = (Cursor.getCapabilities() & Cursor.CURSOR_ANIMATION) != 0;
        Image image = assetManager.loadTexture(file).getImage();
        boolean isRgba = image.getFormat() == Image.Format.RGBA8;
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        if(imageWidth%16 != 0 || imageHeight%16 != 0){
            throw new RuntimeException("Cursor must be multiple of 16");
        }
 
        ByteBuffer imageData = image.getData(0);
        imageData.rewind();
        IntBuffer imageDataCopy = BufferUtils.createIntBuffer(imageWidth
                * imageHeight);
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                int index = y * imageWidth + x;
                int r = imageData.get() & 0xff;
                int g = imageData.get() & 0xff;
                int b = imageData.get() & 0xff;
                int a = 0xff;
                if (isRgba) {
                    a = imageData.get() & 0xff;
                    if (!eightBitAlpha) {
                        if (a < 0x7f) {
                            a = 0x00;
                            // small hack to prevent triggering "reverse screen"
                            // on windows.
                            r = g = b = 0;
                        } else {
                            a = 0xff;
                        }
                    }
                }
                imageDataCopy.put(index, (a << 24) | (r << 16) | (g << 8)  | b);
            }
        }
        if (xHotspot < 0 || yHotspot < 0 || xHotspot >= imageWidth
                || yHotspot >= imageHeight) {
            xHotspot = 0;
            yHotspot = imageHeight - 1;
        }
        try {
            Cursor cursor = new Cursor(imageWidth, imageHeight, xHotspot, yHotspot, 1, imageDataCopy, null);
            loadedCursors.put(file, cursor);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
 
    }
 
    public static synchronized void setHardwareCursor(String file,int xHotspot, int yHotspot) {
        System.out.println("Setting cursor " + file);
        final String fileURI = file;
        Cursor cursor = loadedCursors.get(fileURI);
        if (cursor == null) {
            precache(file, xHotspot, yHotspot);
            cursor = loadedCursors.get(fileURI);
        }
        try {
            if (!cursor.equals(Mouse.getNativeCursor())) {
                Mouse.setNativeCursor(cursor);
            }
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }
}