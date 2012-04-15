/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.game;
import com.jme3.math.ColorRGBA;
import de.jpenguin.player.PlayerControler;
import de.jpenguin.player.Player;
import de.jpenguin.player.PlayerRelationship;
import de.jpenguin.type.basics.*;
import de.jpenguin.unit.*;
import de.jpenguin.gui.GUI;
import de.jpenguin.missile.Missile;
import com.jme3.system.AppSettings;
import de.jpenguin.fog.FogOfWar;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import de.jpenguin.fog.FogOfWarDisplay;
import de.jpenguin.fog.FogOfWarTerrain;
import de.jpenguin.fog.FogOfWarDoodads;
import de.jpenguin.fog.FogOfWarModels;
import de.jpenguin.fog.FogOfWarMinimap;
import de.jpenguin.loader.*;
import de.jpenguin.player.PlayerCommandOrder;
import de.jpenguin.player.PlayerCommandChat;
import de.jpenguin.player.PlayerCommandManager;
import de.jpenguin.player.PlayerLoader;
import de.jpenguin.player.PlayerEventManager;
import de.jpenguin.pathing.PathingMap;

/**
 *
 * @author Karsten
 */
public class Game {
    
   private GameApplication gameApp;
   private MyGame myGame;
   
   private ArrayList<Unit> units;
   private ArrayList<Missile> missiles;
   private Hashtable<String,Player> player;
   private Hashtable<String,PlayerCommandManager> commandManagers;
   
   private PlayerControler controllerPlayer;
   private FogOfWarDisplay fogOfWar;
   
   private PathingMap pathingMap;
   
   private boolean multiplayer;
   
   private String playerId;
   
   private Chat chat;
   
   private PlayerEventManager playerEventManager; 
   
   
   public Game(String className)
   {
       try{
            this.myGame = (MyGame)Class.forName(className).newInstance();
       }catch(Exception e){
           System.out.println("Error " + e.getMessage());
           System.exit(-1);
       }
       
        units = new ArrayList<Unit>();
        missiles = new ArrayList<Missile>();
        player = new Hashtable<String,Player>();
        commandManagers = new Hashtable();
        
        playerEventManager = new PlayerEventManager();
       
        //=myGame;
        this.multiplayer=false;
        this.gameApp = new GameApplication(this,false,"",playerId,0,getMyGame().getMap());
        
        this.playerId=playerId;
        
        gameApp.start();
   }
   
   public Game(MyGame myGame,String joinAdress, int numberPlayer, String playerId)
   {
        units = new ArrayList<Unit>();
        missiles = new ArrayList<Missile>();
        player = new Hashtable<String,Player>();
        commandManagers = new Hashtable();
        
        playerEventManager = new PlayerEventManager();
       
        this.myGame=myGame;
        this.multiplayer=true;
        this.gameApp = new GameApplication(this,true,joinAdress,playerId,numberPlayer,myGame.getMap());
        
        this.playerId=playerId;
        
        gameApp.start();
   }
    
    public void init()
    {
        chat = new Chat(this);
        
        TypeManager utm = new TypeManager();
        TypeManager.getInstance().loadTypes();
        
        controllerPlayer = new PlayerControler(this);
        
        FogOfWar.setSize(512,512, gameApp.getTerrain().getTerrainSize());
        
        fogOfWar = new FogOfWarDisplay(this);
        fogOfWar.addEffect(new FogOfWarTerrain(gameApp.getTerrain()));
        fogOfWar.addEffect(new FogOfWarModels(fogOfWar,gameApp));
        fogOfWar.addEffect(new FogOfWarMinimap());
        fogOfWar.addEffect(new FogOfWarDoodads(gameApp.getDoodadNode(),gameApp.getTerrain().getTerrainSize()));
        gameApp.getStateManager().attach(fogOfWar);
        
        new PlayerLoader(this,getMyGame().getMap());

        
        
        //Single Player -> use first user slot as player
        if(playerId == null)
        {
            PlayerDataManager pdm=PlayerDataManager.load(gameApp.getAssetManager());
            PlayerDataMapManager pdmm=PlayerDataMapManager.load(gameApp.getAssetManager(),getMyGame().getMap());
            boolean found=false;
            int i=0;
            
            ArrayList<PlayerData> list = pdm.getPlayer();
            while(i<list.size() && found==false)
            { 
                if(list.get(i).isVisible())
                {
                    PlayerDataMap playerMap =pdmm.getPlayer().get(list.get(i).getId());
                    if(playerMap.getController() == PlayerDataMap.user)
                    {
                        playerId = list.get(i).getId();
                        found=true;
                    }
                }
                i++;
            }
        }
 
        setControllerPlayer(playerId);
        
      //  pathingMap = new PathingMap(gameApp,getMyGame().getMap());
        pathingMap = PathingMap.load(gameApp.getAssetManager(), getMyGame().getMap());
        
    }
    
    public void update(float tpf)
    {
        Enumeration<PlayerCommandManager> e = commandManagers.elements();
        
        while(e.hasMoreElements())
        {
            PlayerCommandManager pcm = e.nextElement();
            pcm.update();
        }
        
        //Update Units (kompliziert weil gelöscht werden kann)
        int i=0;
        Unit u = null;
        while(i<units.size())
        {
            u = units.get(i);
            if(u.update(tpf))
            {
                i++;
            }
        }
        
        i=0;
        Missile m = null;
        while(i<missiles.size())
        {
            m = missiles.get(i);
            if(m.update(tpf))
            {
                i++;
            }
        }
        
        getMyGame().update(tpf);
    }
    
    public void refreshGUI()
    {
        gameApp.getGUI().refresh();
    }
    
    public void addPlayer(Player p)
    {
        player.put(p.getId(),p);
        commandManagers.put(p.getId(),new PlayerCommandManager(this,p));
    }
    
    public void setControllerPlayer(String p)
    {
        controllerPlayer.setPlayer(player.get(p));
    }
    
    public Player getControllerPlayer()
    {
        return controllerPlayer.getPlayer();
    }
    
    public void addUnit(Unit u)
    {
        units.add(u);
    }
    
    public void removeUnit(Unit u)
    {
        units.remove(u);
    }
    
    public void addMissile(Missile m)
    {
        missiles.add(m);
    }
    
    public void removeMissile(Missile m)
    {
        missiles.remove(m);
    }
    
    public ArrayList<Unit> getUnits()
    {
        return units;
    }
    
    public GameApplication getGameApplication()
    {
        return gameApp;
    }
    
    public PlayerControler getPlayerControler()
    {
        return controllerPlayer;
    }
    
    public PathingMap getPathingMap()
    {
        return pathingMap;
    }

    /**
     * @return the player
     */
    public Hashtable<String,Player> getPlayer() {
        return player;
    }
    
    public FogOfWarDisplay getFogOfWar() {
        return fogOfWar;
    }
    
    public void addPlayerCommand(PlayerCommandOrder pc)
    {
        if(multiplayer==false)
        {
            PlayerCommandManager pcm =commandManagers.get(getControllerPlayer().getId());
            pcm.addCommand(pc,0);
        }else{
            gameApp.getClient().addPlayerCommand(pc);
        }
    }
    
     public void addPlayerCommandChat(PlayerCommandChat pc)
    {
        if(multiplayer==false)
        {
            PlayerCommandManager pcm =commandManagers.get(getControllerPlayer().getId());
            pcm.addCommand(pc,0);
        }else{
            gameApp.getClient().addPlayerCommandChat(pc);
        }
    }
    
    public boolean isMultiplayer()
    {
        return multiplayer;
    }
    
    public PlayerCommandManager getPlayerCommandManager(String playerId)
    {
        return commandManagers.get(playerId);
    }
    
    public Chat getChat()
    {
        return chat;
    }

    /**
     * @return the myGame
     */
    public MyGame getMyGame() {
        return myGame;
    }

    /**
     * @return the playerEventManager
     */
    public PlayerEventManager getPlayerEventManager() {
        return playerEventManager;
    }
            
}