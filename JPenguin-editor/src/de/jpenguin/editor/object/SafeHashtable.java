/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.editor.object;

import java.util.Hashtable;
import java.util.Map;
import com.jme3.export.*;
import java.io.IOException;
/**
 *
 * @author Karsten
 */
public class SafeHashtable implements Savable {
    
    private Hashtable hashtable;
    
    public SafeHashtable(){}
    
    public SafeHashtable(Hashtable hashtable)
    {
        this.hashtable=hashtable;
    }
    
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.writeStringSavableMap(getHashtable(), "doodadTypes", new Hashtable());
    }
 
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        
        hashtable = new Hashtable();
        Map map = capsule.readStringSavableMap( "doodadTypes", null);
        hashtable.putAll(map);
    }

    /**
     * @return the hashtable
     */
    public Hashtable getHashtable() {
        return hashtable;
    }

    /**
     * @param hashtable the hashtable to set
     */
    public void setHashtable(Hashtable hashtable) {
        this.hashtable = hashtable;
    }
    
    
}
