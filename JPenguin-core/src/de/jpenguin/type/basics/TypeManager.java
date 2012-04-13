/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.type.basics;
import de.jpenguin.type.UnitType;
import java.io.File;
import java.io.FileFilter;

import java.util.*;
/**
 *
 * @author Karsten
 */
public class TypeManager {
    
    private Hashtable<Class,TypeList> types;
    private static TypeManager typemanager;
    
    /*
    public static void main(String[] args) {
       
       TypeManager utm = new TypeManager();
       TypeList tl = new TypeList(new UnitType());
       utm.addTypeList(tl);
       UnitType ut = new UnitType();
       ut.setBrutalLord();
       tl.addType(ut);
       
       utm.saveTypes();
      // utm.loadTypes();
    }
     * 
     */
    
    
    public TypeManager()
    {
        types = new Hashtable<Class,TypeList>();
        typemanager=this;
    }
    
    
    private void addTypeList(TypeList tl)
    {
        types.put(tl.getTypeClass(), tl);
    }
    
    public TypeList getTypeList(Class c)
    {
        return types.get(c);
    }
    
    public void loadTypes()
    {
        types = new Hashtable<Class,TypeList>();
        
        File dir = new File("TypeData/");

        FileFilter fileFilter = new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory() == false && (file.getName().endsWith(".xml")) && file.getName().equals("DoodadType.xml")==false;
            }
        };
        File[] files = dir.listFiles(fileFilter);

        for(int i=0;i<files.length;i++)
        {
            TypeList tl = TypeList.load("TypeData", files[i].getName());
            if(tl != null){
                 addTypeList(tl);
            }
        }
    }
    
    public void saveTypes()
    {
       for (TypeList tl : types.values() ) {
           tl.save("TypeData",tl.getTypeClass().getName()+".xml");
       }
    }
    
    public static TypeManager getInstance()
    {
        return typemanager;
    }
}
