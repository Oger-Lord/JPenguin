/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.unit;

import de.jpenguin.game.Game;
/**
 *
 * @author Karsten
 */
public abstract class Ability {
    
    protected Unit unit;
    protected Order order;
    protected Game game;
    
    public static Ability newAbility(Unit u,Order o,Game game)
    {
        System.out.println(o.getOrderString());
        try{
             Class cl = Class.forName(o.getOrderString());
             java.lang.reflect.Constructor constructor = cl.getConstructor(new Class[] {Unit.class,Order.class,Game.class});
             Object invoker =constructor.newInstance(new Object[]{u,o,game});
             System.out.println("success");
             return (Ability)invoker;
        }catch(Exception e){System.out.println("failed to execute ability:(");}
        return null;
    }
    
    public Ability(Unit unit, Order o,Game game)
    {
        this.unit=unit;
        this.order=o;
        this.game=game;
        unit.setAbility(this);
    }
    
    public abstract void update(float tpf);
    
    
    public void destroy(){
        unit.skipOrder();
        remove();
    }
    
    public  void remove(){}
}
