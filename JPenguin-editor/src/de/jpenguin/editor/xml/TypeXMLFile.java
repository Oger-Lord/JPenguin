/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.editor.xml;

/**
 *
 * @author Karsten
 */

import java.util.*;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class TypeXMLFile {
    
    private Hashtable<String,String[]> dataHash;
    private String[] attributeList;
    private String path;
    private String name;

    public TypeXMLFile(String path, String name) {

        this.name=name;
        this.path=path;
        
        attributeList = readAttributes(path+"/"+name+".txt");
        dataHash = readType(path+"/"+name+".xml");
    }
    
    
    public String[] getElements()
    {
        String array[] = new String[dataHash.size()];
        int i=0;
        
        Enumeration e = dataHash.keys();
        while (e.hasMoreElements()) {
            String alias = (String)e.nextElement();
            array[i] = (String)alias;
            i++;
        }
        return array;
    }
    
    public String[][] getTable(String element)
    {
        String table[][] = new String[attributeList.length][2];
    
        String array[] = dataHash.get(element);
    
        for(int i=0;i<attributeList.length;i++)
        {
            table[i][0] = attributeList[i];
            table[i][1] = array[i];
        }
        return table;
    }
    
    public String getKeyName(int id)
    {
        return attributeList[id];
    }
    
    public String getValue(String element, String key)
    {
        String array[] = dataHash.get(element);
        for(int i=0;i<attributeList.length;i++)
        {
            if(attributeList[i].equals(key))
            {
                return array[i];
            }
        }
        return null;
    }
    
    
    public void changeTable(String element,int row,String newdata)
    {
        String array[] = dataHash.get(element);
        if(array != null)
        {
            array[row] = newdata;
            dataHash.put(element, array);
        }
    }
    
    public boolean copyElement(String element, String newName)
    {
        if(dataHash.get(newName) != null)
        {
            return false;
        }
         String[] array = dataHash.get(element);
         String[] newarray = new String[array.length];
         
         for(int i=0;i<array.length;i++)
         {
             newarray[i] = array[i];
         }
                 
         dataHash.put(newName,newarray);
         return true;
    }
    
    public boolean newElement(String element)
    {
        if(dataHash.get(element) != null)
        {
            return false;
        }
        String[] array = new String[attributeList.length];
        dataHash.put(element,array);
        return true;
    }

    public void removeElement(String element)
    {
        dataHash.remove(element);
    }
    
    public boolean renameElement(String element, String newName)
    {        
        if(dataHash.get(newName) != null)
        {
            return false;
        }
        String[] array = dataHash.get(element);
        dataHash.put(newName,array);
        dataHash.remove(element);
        return true;
    }
    
    
    private Hashtable readType(String st)
    {
      Hashtable hash = new Hashtable();
      
      try {
          File file = new File(st);
          if(file.exists()==false){return new Hashtable();}
          
          DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
          DocumentBuilder db = dbf.newDocumentBuilder();
          Document doc = db.parse(file);
          doc.getDocumentElement().normalize();
         // System.out.println("Root element " + doc.getDocumentElement().getNodeName());
          NodeList nodeLst = doc.getElementsByTagName("de.jpenguin.type."+name);
         // System.out.println("Information of all employees");
        
          for (int s = 0; s < nodeLst.getLength(); s++) {

            Node fstNode = nodeLst.item(s);
            Element fstElmnt = (Element) fstNode;

            
            String[] array = new String[attributeList.length];
            
             for(int i=0;i<attributeList.length;i++)
             {
                   array[i] = fstElmnt.getAttribute(attributeList[i]);
             }
             hash.put(fstElmnt.getAttribute("typeId"),array);
              
          } 

          } catch (Exception e) {
            e.printStackTrace();
      }
      return hash;
 }
    
private String[] readAttributes(String s)
  {
      try{
          FileInputStream fstream = new FileInputStream(s);

          DataInputStream in = new DataInputStream(fstream);
          
          BufferedReader br = new BufferedReader(new InputStreamReader(in));
          
          String strLine;
          ArrayList<String> al = new ArrayList<String>();
          while ((strLine = br.readLine()) != null)   {
            al.add(strLine);
          }
          
          in.close();
          return Arrays.copyOf(al.toArray(), al.size(), String[].class);


           }catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
      return null;
   }

    public void save()
    {
        try{
            new File(path+"\\"+name+".xml").delete();
            FileWriter fstream = new FileWriter(path+"\\"+name+".xml");
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("<?xml  version='1.0' encoding='UTF-8'?>\r\n");
            out.write("<de.jpenguin.type.basics.TypeList savable_versions='0'>\r\n");
            out.write(" <myclass attackSpeed='0.0' class='de.jpenguin.type."+name+"' life='0.0' movementSpeed='0.0' savable_versions='0, 0' size='0.0' typeId=''/>\r\n");
            out.write("<instances size='"+dataHash.size()+"'>\r\n");
            
            Enumeration e = dataHash.keys();
            while (e.hasMoreElements()) {
                
                String alias = (String)e.nextElement();
                
                System.out.println(name + " " + alias);
                
                String[] array = dataHash.get(alias);
                
                String add = "      <de.jpenguin.type."+name+" ";;
                
                for(int i=0;i<attributeList.length;i++)
                {
                    if(array[i] != null)
                    {
                        add += attributeList[i]+"='"+array[i]+"' " ;
                    }
                }
                
                add += "typeId='"+alias+"'/>\r\n";
                out.write(add);
           
            }
            out.write(" </instances>\r\n");
            out.write("</de.jpenguin.type.basics.TypeList>\r\n");
            
            out.close();
        }catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }
    
}


