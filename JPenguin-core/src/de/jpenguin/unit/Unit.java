package de.jpenguin.unit;

import de.jpenguin.player.Player;
import com.jme3.scene.*;
import com.jme3.asset.AssetManager;
import com.jme3.animation.*;
import com.jme3.light.*;
import com.jme3.math.*;
import com.jme3.export.*;
import com.jme3.shadow.*;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.ai.steering.Obstacle;
import com.jme3.ai.steering.utilities.SimpleObstacle;

import java.util.ArrayList;
import java.io.*;
import java.util.*;

import de.jpenguin.engine.*;
import de.jpenguin.type.*;
import de.jpenguin.game.*;
import de.jpenguin.missile.*;
import de.jpenguin.pathing.SubPathingMap;
import de.jpenguin.unit.event.*;

public abstract class Unit implements Savable,ModelClick{
    
    protected ArrayList<Order> orderList;
    
    protected Vector3f location;
    protected float rotation;
    
    protected Model model;
    protected UnitType unitType;
    
    protected Healthbar hpbar;
    protected double life=50;
    protected double mana=0;
    
    protected boolean stopped=false;
    
    protected ArrayList<AbilityType>abilities;
    protected ArrayList<Buff> buffs;
    protected Player player;
    
    protected double timer;
    protected String timerAction="";
    
    protected Selection selection;
    protected Selection mouseHover;
    
    protected double sightRadius;
    
    protected Ability ability;
    
    protected Game game;
    
    protected UnitEventManager unitEventManager;
    
    
    
    protected void init(Game game, UnitType unitType, String p,float x, float y, float rotation)
    {
        this.unitType = unitType;
        
        unitEventManager = new UnitEventManager();
        
        this.game=game;
        
        location = new Vector3f(x,0,y);
        location.setY(game.getGameApplication().getTerrain().getHeight(new Vector2f(x,y)));
        this.rotation = rotation;
        this.player=game.getPlayer().get(p);
        this.life = unitType.getLife();
        this.mana = unitType.getMana();
        
        unitEventManager.setSubUnitEventManager(player.getUnitEventManager());
        
        this.sightRadius = unitType.getSightRadius();

        model = new Model(unitType.getModel(), game.getGameApplication(),this,location,rotation,(float)unitType.getSize());
        model.setAnimation("stand");
        model.setColor(player.getColor());
        
        hpbar = new Healthbar(this);
        hpbar.update();
        
        orderList = new ArrayList<Order>();
        buffs = new ArrayList<Buff>();
        
        abilities = new ArrayList<AbilityType>();
        
        if(unitType.getAbilities() != null)
        {
            String[] array = unitType.getAbilities();
            for(int i=0;i<unitType.getAbilities().length;i++)
            {
                if(array[i].isEmpty()==false)
                {
                    //System.out.println( "."+array[i]+"-");
                    abilities.add(AbilityType.getAbilityType(array[i]));
                }
            }
        }
        
        if(game.getControllerPlayer().shareVision(player))
        {
            player.getFogOfWarPlayer().setSight((float)sightRadius, x, y, true);
        }
        
        game.addUnit(this);
    }
    
    public void addOrder(Order o)
    {
        if(orderList.isEmpty())
        {
            ability = Ability.newAbility(this,o,game);
        }
        orderList.add(o);
    }
    
    public void addOrderList(ArrayList<Order> orders)
    {
        if(orders == null){return;}
        
        if(orderList.isEmpty())
        {
            Ability.newAbility(this,orders.get(0),game);
        }
        
        for(int i=0;i<orders.size();i++)
        {
            orderList.add(orderList.size(),orders.get(i));
        }
    }
    
    public void clearOrderList()
    {
        if(ability != null)
        {
            ability.destroy();
            ability=null;
        }
        
        stopped=false;
        if(timerAction.equals("attackHit") || timerAction.equals("attackEnd"))
        {
            timerAction = "";
        }
        orderList.clear();
    }
    
   
    
    
    public void click(int i)
    {
    }
    
    public boolean update(float tick)
    {
        
        updateBuffs();
        
        
        if(life != 0){
  
            //HP Regeneration
            if(unitType.getHpRegeneration() != 0 && life<unitType.getLife())
            {
                life += unitType.getHpRegeneration()*tick;
                if(life > unitType.getLife()){life = unitType.getLife();}
                if(selection != null)
                {
                    if((int)life != (int)(life-unitType.getHpRegeneration()*tick))
                    {
                        game.refreshGUI();
                    }
                }
                
                if(hpbar != null)
                {
                    hpbar.update();
                }
            }
            
            if(ability != null)
            {
                ability.update(tick);
            }
            
            if(ability == null && orderList.isEmpty())
            {
                model.setAnimation("stand");
            }
        }
        
        //Timer Actions
        if(timerAction.isEmpty() == false)
        {
              timer-=tick;
              if(timer <= 0)
              {
                      if(timerAction.equals("remove"))
                      {
                          remove();
                          return false;
                      }
              }
        }
                  
        return true;
    }
    
    public double getDistance(float tx,float ty)
    {
        return Math.sqrt(Math.pow(location.getZ()-ty,2)+Math.pow(location.getX()-tx,2));
    }
    
    public void rotateToPoint(float tx, float ty)
    {
      // this.rotation = (float)(Math.atan2((float)(ty-location.getZ()),(float)(tx-location.getX()))*180/Math.PI);
       this.rotation = (float)(Math.atan2((float)(tx-location.getX()),(float)(ty-location.getZ()))*180/Math.PI);
       model.setRotation(rotation);
    }
    
    /*
    public void moveInDirection(float tick)
    {
         
        if(game.getControllerPlayer().shareVision(player))
        {
            player.getFogOfWarPlayer().setSight((float)sightRadius, location.getX(), location.getZ(), false);
        }
        
         Vector3f v3f = new Vector3f(0,0,0);
        v3f.setZ((float)(Math.cos(rotation*(Math.PI/180))*unitType.getMovementSpeed()*tick));
        v3f.setX((float)(Math.sin(rotation*(Math.PI/180))*unitType.getMovementSpeed()*tick));

        
        location = location.add(v3f); 
        location.setY(game.getGameApplication().getTerrain().getHeight(new Vector2f(location.getX(),location.getZ())));
        
        if(game.getControllerPlayer().shareVision(player))
        {
            player.getFogOfWarPlayer().setSight((float)sightRadius, location.getX(), location.getZ(), true);
        }
       
        model.setTranslation(location);
    }
     * 
     */
    
    protected void updateBuffs()
    {
        for(int i=0;i<buffs.size();i++)
        {
            buffs.get(i).update();
        }
    }
    
    
    
    public void damage(Unit attacker,float d)
    {
        attacker.getUnitEventManager().addEvent(new UnitDamagingEvent(attacker,d,this));
        unitEventManager.addEvent(new UnitDamagedEvent(this,d,attacker));
        
        damage(d);
    }
    
    public void damage(float d)
    {
        if(life == 0){return;}
        
        if(unitType.getArmor() > 0) //positiv armor
        {
            life = life-(d-d*(unitType.getArmor()*0.06)/(1+0.06*unitType.getArmor()));
        }else if(unitType.getArmor() < 0) //negativ armor
        {
            life = life-d-d*(2 - Math.pow(1-0.06,unitType.getArmor()));
        }else{
            life = life-d;
        }
        
        
        boolean wasSelected=false;
        if(selection != null)
        {
            wasSelected=true;
        }
        
        if(life <=0)
        {
            kill();
        }
        
        if(hpbar != null)
        {
            hpbar.update();
        }
        
        if(wasSelected)
        {
            game.refreshGUI();
        }
    }
    
    public void kill()
    {
        new Sound(game,unitType.getDeathSound(),location,4);
        
        life = 0;
        model.setAnimation("death",3);
        model.setModelClick(null);

        hpbar.remove();
        hpbar=null;
        
        buffs.clear();
        
        
        timer = 3;
        timerAction = "remove";
        
         removeSelection();
         removeMouseHoverSelection();
         
         if(ability != null){ability.destroy();}
    }
    
    public void remove()
    {
        game.removeUnit(this);
        model.remove();
        if(hpbar != null)
        {
            hpbar.remove();
            hpbar=null;
        }
        
        if(game.getControllerPlayer().shareVision(player))
        {
            player.getFogOfWarPlayer().setSight((float)sightRadius, location.getX(), location.getZ(), false);
        }
        buffs.clear();
        
        removeSelection();
        removeMouseHoverSelection();
        if(ability != null){ability.remove();}
    }
    
    public void addBuff(Buff b)
    {
        buffs.add(b);
    }
    
    public void removeBuffs()
    {
        while(buffs.size() != 0)
        {
            Buff b = buffs.get(0);
            b.close();
            buffs.remove(0);
        }
    }
    
    public void pause(boolean b)
    {
        stopped=b;
    }
    
    public void setAbility(Ability a)
    {
        ability=a;
    }
    
    public void skipOrder()
    {
        if(ability != null)
        {
            ability.remove();
        }
        ability=null;
        
        if(orderList.isEmpty()==false)
        {
            orderList.remove(0);
        }
        if(orderList.isEmpty()==false)
        {
            Ability.newAbility(this,orderList.get(0),game);
        }
    }
    
    public Order getOrder()
    {
        return orderList.get(0);
    }
    
    public double getLife()
    {
        return life;
    }
    
    public double getMana()
    {
        return mana;
    }
    
    public UnitType getUnitType()
    {
        return unitType;
    }
    
    public float getRotation()
    {
        return (float)(rotation);
    }
    

    public float getX()
    {
        return location.getX();
    }
        
    public float getY()
    {
        return location.getZ();
    }
    
    public Game getGame()
    {
        return game;
    }
    
    public Player getPlayer()
    {
        return player;
    }
    
    public Model getModel()
    {
        return model;
    }
    
    public Vector3f getLocation()
    {
        return location;
    }
    
    public void newMouseHover()
    {
        if(selection != null || mouseHover != null){return;}
        
        if(game.getControllerPlayer() == player)
        {
             mouseHover = new Selection(this,game.getGameApplication(),ColorRGBA.Green,true);
        }else{
             mouseHover = new Selection(this,game.getGameApplication(),ColorRGBA.Red,true);
        }
    }
    
    public void newSelection()
    {
        if(selection!=null){return;}
        
        if(mouseHover != null)
        {
            removeMouseHoverSelection();
        }
        
        if(game.getControllerPlayer() == player)
        {
             selection = new Selection(this,game.getGameApplication(),ColorRGBA.Green,false);
        }else{
             selection = new Selection(this,game.getGameApplication(),ColorRGBA.Red,false);
        }
    }

    
    public void removeSelection()
    {
        if(selection!=null)
        {
            selection.close();
            selection=null;
            game.getControllerPlayer().clearSelection();
        }
    }
    
    public void removeMouseHoverSelection()
    {
        if(mouseHover!=null)
        {
            mouseHover.close();
            mouseHover=null;
        }
    }
    
    public void setRotation(float r)
    {
        rotation=r;
        model.setRotation(rotation);
    }
    
    public ArrayList<AbilityType> getAbilities()
    {
        return abilities;
    }
    
   public ArrayList<Buff> getBuffs()
    {
        return buffs;
    }
   
    public UnitEventManager getUnitEventManager()
    {
        return unitEventManager;
    }
    
    public abstract Obstacle toObstacle();
    
       public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
      //  capsule.write(someIntValue,   "someIntValue",   1);
      //  capsule.write(someFloatValue, "someFloatValue", 0f);
     //   capsule.write(someJmeObject,  "someJmeObject",  new Material());
    }
 
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
     //   someIntValue   = capsule.readInt(    "someIntValue",   1);
     //   someFloatValue = capsule.readFloat(  "someFloatValue", 0f);
     //   someJmeObject  = capsule.readSavable("someJmeObject",  new Material());
    }

    
}
