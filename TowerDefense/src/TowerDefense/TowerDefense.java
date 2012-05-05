/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TowerDefense;

import de.jpenguin.player.Player;
import de.jpenguin.player.event.PlayerChatEvent;
import com.jme3.math.ColorRGBA;
import de.jpenguin.game.*;
import de.jpenguin.unit.*;
import com.jme3.system.AppSettings;

/**
 *
 * @author Karsten
 */
public class TowerDefense implements MyGame{
    
   private Game game;
    
   public static void main(String[] args) {
        new Game("TowerDefense.TowerDefense");
    }
   

    
    public void init(Game game)
    {
        this.game=game;
        
     //   game.getChat().addMessage("Hello World!");
      //  Player p = new Player(game,"p1","Player 1",ColorRGBA.Red);
      //  Player p2 = new Player(game,"p2","Player 2",ColorRGBA.Blue);

      //  new Unit(game,"sceletonarcher",p,15,15,0);
        
        new Building(game,"tower","p2",20,20,0);
        
        new SimpleUnit(game,"dolphin","p1",4,4,0);
        new SimpleUnit(game,"atst","p1",0,0,0);
        new SimpleUnit(game,"tank","p2",0,20,0);
        
        new SimpleUnit(game,"deepone","p1",-10,6,0);
        
        
        Player p1 =game.getPlayer().get("p1");
        
        
        Unit u =new SimpleUnit(game,"primematron","p1",0,-20,0);
        p1.getUnitEventManager().registerUnitEventListener(new TestUnitListener(game.getChat()));
        new SimpleUnit(game,"brutallord","p2",-10,0,0);
        
        u = new SimpleUnit(game,"allosaurus","p1",10,0,0);
         new Buff(game,"fire",u);
        
        
        game.getPlayerEventManager().registerPlayerEventListener(new TestPlayerListener(game.getChat()));
        
        /*
        for(int i=0;i<500;i++)
        {
           new Unit(game,"brutallord",p,0,i-250,0);
        }
         * 
         */
        
    }
    
    public void update(float tpf)
    {
        
    }
    
    public String getMap()
    {
        return "Map1";
    }
}
