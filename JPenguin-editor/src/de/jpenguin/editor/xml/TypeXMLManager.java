/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.editor.xml;

import de.jpenguin.editor.xml.TypeXMLFile;
import java.util.*;
import java.io.*;


public class TypeXMLManager {
    
    private Hashtable<String,TypeXMLFile> typeList;

    public TypeXMLManager(String path) {
        
       typeList = new Hashtable<String,TypeXMLFile>();
        
       File dir = new File(path);

        FileFilter fileFilter = new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory() == false && file.getName().endsWith(".txt");
            }
        };
        File[] files = dir.listFiles(fileFilter);
        
        for(int i=0;i<files.length;i++)
        {
            String name = files[i].getName().substring(0, files[i].getName().length()-4);
            typeList.put(name,new TypeXMLFile(path,name));
        }
    }
    

    public TypeXMLFile getXMLFile(String name)
    {
        return typeList.get(name);
    }
    
    
    
    public String[] getElements()
    {
        String array[] = new String[typeList.size()];
        int i=0;
        
        Enumeration e = typeList.keys();
        while (e.hasMoreElements()) {
            String alias = (String)e.nextElement();
            array[i] = (String)alias;
            i++;
        }
        return array;
    }
    
    
    public void save()
    {
        Enumeration e = typeList.keys();
        while (e.hasMoreElements()) {
            String alias = (String)e.nextElement();
            TypeXMLFile xml = typeList.get(alias);
            xml.save();
        }
    }
    
}
