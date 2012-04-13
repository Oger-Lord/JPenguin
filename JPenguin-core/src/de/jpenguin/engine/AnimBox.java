/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.engine;

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Collection;
/**
 *
 * @author Karsten
 */
public class AnimBox {
    
  public class EqualsIgnoreCaseString 
    { 
      private final String string; 

      public EqualsIgnoreCaseString( String string ) 
      { 
        this.string = string.toLowerCase(); 
      } 

      @Override public int hashCode() 
      { 
        return string.hashCode(); 
      } 

      @Override public boolean equals( Object obj ) 
      { 
        if ( this == obj ) 
          return true; 
        if ( obj == null ) 
          return false; 
        if ( getClass() != obj.getClass() ) 
          return false; 
        if ( string == null ) 
          if ( ((EqualsIgnoreCaseString) obj).string != null ) 
            return false; 
        return string.equals( ((EqualsIgnoreCaseString) obj).string ); 
      } 
    }
    
    Hashtable<EqualsIgnoreCaseString,ArrayList> animVariations;
    Hashtable<EqualsIgnoreCaseString,String> animNames;
    
    public AnimBox(Collection<String> hashmap)
    {
        animVariations = new Hashtable<EqualsIgnoreCaseString,ArrayList>();
        animNames = new Hashtable<EqualsIgnoreCaseString,String>();
        
        for( String name: hashmap )
        {
            if(name.matches("[A-Za-z]*((\\d+)|(\\s+))[A-Za-z0-9]*")) //Number or Space->Sub animation
            {
                String sub[] = name.split("(\\d)|(\\s)");
                ArrayList<String> list = animVariations.get(new EqualsIgnoreCaseString(sub[0]));
                if(list == null)
                {
                    list = new ArrayList<String>();
                }
                list.add(name);
                animVariations.put(new EqualsIgnoreCaseString(sub[0]),list);
                
            }else{ //Main animation
                animNames.put(new EqualsIgnoreCaseString(name),name);
            }
        }
    }
    
    public String getAnimation(String s)
    {
        String name =  animNames.get(new EqualsIgnoreCaseString(s));
        ArrayList<String> list = animVariations.get(new EqualsIgnoreCaseString(s));
        
        if(name == null)
        {
            if(list == null)
            {
                return null;
            }
            
            return list.get((int)((list.size()-0.5)*Math.random()));
        }else{
            if(list == null)
            {
                return name;
            }
            
            if(Math.random() >= 0.6)
            {
                return list.get((int)((list.size()-0.5)*Math.random()));
            }else{
                return name;
            }
        }
    }
    
}
