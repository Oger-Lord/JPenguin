/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.type.basics;

import java.util.*;
import com.jme3.util.SafeArrayList;
import com.jme3.export.*;
import java.io.IOException;
import com.jme3.export.xml.*;
import java.io.*;
import java.util.Hashtable;
import java.util.logging.*;
import com.jme3.system.JmeSystem;
import com.jme3.export.Savable;


public class TypeList implements Savable {
        
    private SafeArrayList<Type> instances;
    private Hashtable<String,Type> hash;
    
    private Class myclass;
    
    public TypeList()
    {
        
    }
    
    
    public TypeList(Type t)
    {
        myclass = t.getClass();
        instances = new SafeArrayList<Type>(Type.class);
        hash = new Hashtable<String,Type>();
    }
    
    public void addType(Type t)
    {
        hash.put(t.getId(),t);
        instances.add(t);
    }
    
    public Type getType(String id)
    {
        return hash.get(id);
    }
    
    public Class getTypeClass()
    {
        return myclass;
    }
    
    public void write(JmeExporter e) throws IOException 
    {
        try{
            e.getCapsule(this).write((Type)myclass.newInstance(),   "myclass", new Type());
        }catch(Exception ee){}
        e.getCapsule(this).writeSavableArrayList(new ArrayList(instances), "instances", null);
    }
    
    public void read(JmeImporter e) throws IOException {
        destroy();
       // System.out.println("Test");
        
        instances = new SafeArrayList( Type.class,e.getCapsule(this).readSavableArrayList("instances", null) );
        hash = new Hashtable<String,Type>();
        
        if (instances!= null) {
           // System.out.println("LALALA");
            for (Type t : instances.getArray()) {
                hash.put(t.getId(),t);
           //     System.out.println("LALALA"+ t.getId());
            }
        }
        myclass  = (e.getCapsule(this).readSavable( "myclass", null)).getClass();
    }
    
    public void destroy()
    {
        instances=null;
        hash=null;
    }
    
        
    public static TypeList load(String gamePath, String dataName)
    {
        System.out.println("load:"+dataName);
        InputStream is = null;
        Savable sav = null;
        try {
            File file = new File(System.getProperty("user.dir") + File.separator + gamePath + File.separator + dataName);
            if(!file.exists()){
                return null;
            }
            is = new BufferedInputStream(new FileInputStream(file));
            //is = new GZIPInputStream(new BufferedInputStream(new FileInputStream(file)));
            XMLImporter imp = XMLImporter.getInstance();
           // if (manager != null) {
           //     imp.setAssetManager(manager);
           // }
            sav = imp.load(is);
        } catch (IOException ex) {
            Logger.getLogger(Type.class.getName()).log(Level.SEVERE, "Error loading data: {0}", ex);
            ex.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    Logger.getLogger(Type.class.getName()).log(Level.SEVERE, "Error loading data: {0}", ex);
                    ex.printStackTrace();
                }
            }
        }
        return (TypeList)sav;
    }
    
    public void save(String gamePath, String dataName)
    { 
        XMLExporter ex = XMLExporter.getInstance();
        OutputStream os = null;
        try {
            
            File daveFolder = new File(System.getProperty("user.dir") + File.separator + gamePath);
            if (!daveFolder.exists() && !daveFolder.mkdirs()) {
                Logger.getLogger(Type.class.getName()).log(Level.SEVERE, "Error creating save file!");
                throw new IllegalStateException("SaveGame dataset cannot be created");
            }
            File saveFile = new File(daveFolder.getAbsolutePath() + File.separator + dataName);
            if (!saveFile.exists()) {
                if (!saveFile.createNewFile()) {
                    Logger.getLogger(Type.class.getName()).log(Level.SEVERE, "Error creating save file!");
                    throw new IllegalStateException("SaveGame dataset cannot be created");
                }
            }
            os = new BufferedOutputStream(new FileOutputStream(saveFile));
            //os = new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(saveFile)));
            ex.save(this, os);
        } catch (IOException ex1) {
            Logger.getLogger(Type.class.getName()).log(Level.SEVERE, "Error saving data: {0}", ex1);
            ex1.printStackTrace();
            throw new IllegalStateException("SaveGame dataset cannot be saved");
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException ex1) {
                Logger.getLogger(Type.class.getName()).log(Level.SEVERE, "Error saving data: {0}", ex1);
                ex1.printStackTrace();
                throw new IllegalStateException("SaveGame dataset cannot be saved");
            }
        }
    }
    
}
