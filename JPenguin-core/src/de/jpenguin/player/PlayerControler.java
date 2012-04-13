/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.player;

import de.jpenguin.game.Game;
import de.jpenguin.unit.Unit;
import de.jpenguin.unit.Order;
import de.jpenguin.type.AbilityType;
import de.jpenguin.gui.Cursor;
import de.jpenguin.pathing.*;
/**
 *
 * @author Karsten
 */
public class PlayerControler {
       
    private Unit lastUnitHover;
    private Player player;
    private Game game;
    private boolean isAming=false;
    private AbilityType lastAbility;
    private Cursor cursor;
    
    public PlayerControler(Game game)
    {
        this.game=game;
    }
    
    public void setCursor(Cursor c)
    {
        cursor=c;
    }
    
    public void setPlayer(Player p)
    {
        player =p;
    }
    
    public Player getPlayer()
    {
        return player;
    }
    
    public void abilityClick(AbilityType ability)
    {
        
        if(player.getSelection().size() != 0)
        {
            if(ability.getTarget() == 0)
            {
                PlayerCommandOrder pc=new PlayerCommandOrder(player.getSelection().get(0),new Order(ability.getOrderId()));
                game.addPlayerCommand(pc);
            }else{
                 lastAbility = ability;
                isAming=true;
                cursor.setImage("cross");
            }
        }
    }

    
    public void mouseClick(int i, Unit u)
    {
        System.out.println("bla"+isAming);
        if(isAming==true)
        { 
            if(i==0)
            {
                if(u==null){return;}
                if(lastAbility.getTarget() == 2 || lastAbility.getTarget() == 3)
                {
                    if(player.getSelection().isEmpty()==false)
                    {
                        PlayerCommandOrder pc =new PlayerCommandOrder(player.getSelection().get(0),new Order(lastAbility.getOrderId(),u));
                        game.addPlayerCommand(pc);
                    }
                    isAming=false;
                    cursor.setImage("cursor");
                }
            }else{
                isAming=false;
                cursor.setImage("cursor");
            }
            return;
        }      
                
        if(i == 0)
        {
            player.clearSelection();
            player.addSelection(u);
            game.refreshGUI();
        }else{
            if(player.getSelection().size() == 0)
            {
               return;
            }
            Unit uo = player.getSelection().get(0);
            if(uo == null){return;}
            
            if(uo.getPlayer() == player || player.getRelationship(uo.getPlayer()).hasSharedControl() )
            {
                PlayerCommandOrder pc;
                if(player.isEnemy(u.getPlayer()))
                {
                    pc=new PlayerCommandOrder(uo,new Order("de.jpenguin.unit.abilities.Attack",u));
                }else{
                    pc=new PlayerCommandOrder(uo,new Order("de.jpenguin.unit.abilities.Move",u.getX(),u.getY()));
                }
                game.addPlayerCommand(pc);
            }
        }
    }
    
    public void mouseClick(int i, float x, float y)
    {
        System.out.println("bla"+isAming);

        if(isAming==true)
        { 
            if(i==0)
            {
                if(lastAbility.getTarget() == 1 || lastAbility.getTarget() == 3)
                {
                    if(player.getSelection().isEmpty()==false)
                    {
                        PlayerCommandOrder pc=new PlayerCommandOrder(player.getSelection().get(0),new Order(lastAbility.getOrderId(),x,y));
                        game.addPlayerCommand(pc);
                    }
                    cursor.setImage("cursor");
                    isAming=false;
                }
            }else{
                cursor.setImage("cursor");
                isAming=false;
            }
            return;
        }  
                
        if(i == 0)
        {
            player.clearSelection();
            game.refreshGUI();
        }else{
            if(player.getSelection().size() == 0)
            {
               return;
            }
            Unit uo = player.getSelection().get(0);
            if(uo == null){return;}
            
            if(uo.getPlayer() == player)
            {
                uo.clearOrderList();
                
                PlayerCommandOrder pc=new PlayerCommandOrder(uo,new Order("de.jpenguin.unit.abilities.Move",x,y));
                game.addPlayerCommand(pc);
            }
        }
    }
    
    public void mouseHover(Unit u)
    {
        if(lastUnitHover != u)
        {
            if(u !=null)
            {
                u.newMouseHover();
                if(player.isFriend(u.getPlayer()))
                {
                    cursor.setStatus("friend");
                }else if(player.isEnemy(u.getPlayer())){
                    cursor.setStatus("enemy");
                }else{
                    cursor.setStatus("neutral");
                }
            }else{
                cursor.setStatus("");
            }
            
            
            if(lastUnitHover != null)
            {
                lastUnitHover.removeMouseHoverSelection();
            }
            
            lastUnitHover=u;
        }
    }
}

