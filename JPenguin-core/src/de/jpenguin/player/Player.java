/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.player;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Enumeration;
import de.jpenguin.unit.Unit;
import de.jpenguin.unit.Order;
import de.jpenguin.type.AbilityType;

import de.jpenguin.unit.UnitEventManager;

import com.jme3.math.ColorRGBA;
import de.jpenguin.game.Game;
/**
 *
 * @author Karsten
 */
public class Player {
    
    private String id;
    private String name;
    private ColorRGBA color;
    
    private ArrayList<Unit> selection;
    private Hashtable<Player,PlayerRelationship> relationships;
    
    private FogOfWarPlayer fog;
    
    private Game game;
    
    private PlayerEventManager playerEventManager;
    private UnitEventManager unitEventManager;
    
    public Player(Game game,String playerId, String playerName, ColorRGBA color)
    {
        this.game=game;
        this.id = playerId;
        this.name=playerName;
        this.color=color;
        this.fog = new FogOfWarPlayer();
        
        unitEventManager = new UnitEventManager();
        playerEventManager = new PlayerEventManager();
        playerEventManager.setSubUnitEventManager(game.getPlayerEventManager());
        
        selection = new ArrayList<Unit>();
        
        relationships = new Hashtable<Player,PlayerRelationship>();
        Enumeration e =game.getPlayer().keys();
        
        while(e.hasMoreElements())
        {
          Player p = game.getPlayer().get(e.nextElement());
          p.setRelationship(this, new PlayerRelationship(-1,false,false));
          setRelationship(p, new PlayerRelationship(-1,false,false));
        }
        
        game.addPlayer(this);
    }
    
    
    public FogOfWarPlayer getFogOfWarPlayer()
    {
        return fog;
    }
    
    
    public boolean isVisible(float fx, float fy)
    {
        boolean b=fog.isVisible(fx, fy);
        
        if(b==true){return true;}
        
        Enumeration e =relationships.keys();
        
        while(e.hasMoreElements())
        {
              Player p = (Player)e.nextElement();
              PlayerRelationship rs = relationships.get(p);
              
              if(rs.hasSharedVision())
              {
                  if(p.getFogOfWarPlayer().isVisible(fx, fy));
                  {
                      return true;
                  }
              }
        }
        return false;
    }
    
    
    public void setRelationship(Player p, PlayerRelationship pr)
    {
        relationships.put(p, pr);
    }
    
    public PlayerRelationship getRelationship(Player p)
    {
        return relationships.get(p);
    }
    
    
    public boolean isEnemy(Player p)
    {
        if(p == this){return false;}
        
        if(relationships.get(p).getStatus() == PlayerRelationship.enemy)
        {
            return true;
        }
        return false;
    }
    
    public boolean isFriend(Player p)
    {
        if(p == this){return true;}
        
        if(relationships.get(p).getStatus() == PlayerRelationship.friend)
        {
            return true;
        }
        return false;
    }
       
    public boolean shareVision(Player p)
    {
        if(p == this){return true;}
        
        return relationships.get(p).hasSharedVision();
    } 
 
    
    public void addSelection(Unit u)
    {  
        u.newSelection();
        selection.add(u);
    }
    
    public void clearSelection()
    {
        for(int i=0;i<selection.size();i++)
        {
            selection.get(i).removeSelection();
        }
        selection.clear();
    }
    
    public ArrayList<Unit> getSelection()
    {
        return selection;
    }

    /**
     * @return the playerId
     */
    public String getId() {
        return id;
    }

    /**
     * @return the playerName
     */
    public String getName() {
        return name;
    }

    /**
     * @return the color
     */
    public ColorRGBA getColor() {
        return color;
    }

    /**
     * @return the unitEventManager
     */
    public UnitEventManager getUnitEventManager() {
        return unitEventManager;
    }
    
     /**
     * @return the playerEventManager
     */
    public PlayerEventManager getPlayerEventManager() {
        return playerEventManager;
    }
}
