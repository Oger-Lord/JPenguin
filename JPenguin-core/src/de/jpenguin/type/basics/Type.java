/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.type.basics;

import com.jme3.export.*;
import java.io.IOException;
/**
 *
 * @author Karsten
 */
public class Type implements Savable {
    protected String typeId="";
    
    public String getId()
    {
        return typeId;
    }
    
    public Type()
    {
        
    }
    
    public void add()
    {
       TypeManager utm = TypeManager.getInstance();
       TypeList tl = utm.getTypeList(getClass());
       tl.addType(this);
    }
    
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(typeId,   "typeId",   "test");
    }
    
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        typeId  = capsule.readString(    "typeId",   "test");
     }
    
    public static <T extends Type> T  getType(Class<T> requestedRendererClass,String id)
    {
        System.out.println(id +" "+ requestedRendererClass.getName());
        TypeManager utm = TypeManager.getInstance();
        TypeList tl = utm.getTypeList(requestedRendererClass);
        return (T)tl.getType(id);
    }

}
