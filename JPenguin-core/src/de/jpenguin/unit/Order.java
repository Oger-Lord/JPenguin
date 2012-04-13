/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.unit;

/**
 *
 * @author Karsten
 */
public class Order {
    private float targetX;
    private float targetY;
    
    private Unit targetUnit;
    
    private String orderString;
    
    private int type;
    
    public static final int TARGET_UNIT=1;
    public static final int TARGET_POINT=2;
    public static final int NOTARGET=3;
    
    public Order(String s)
    {
        orderString = s;
        type=Order.NOTARGET;
    }
    
    public Order(String s,Unit u)
    {
        orderString = s;
        targetUnit = u;
        type=Order.TARGET_UNIT;
    }
    
    public Order(String s,float x, float y)
    {
        orderString = s;
        targetX = x;
        targetY = y;
        type=Order.TARGET_POINT;
    }

    /**
     * @return the targetX
     */
    public float getTargetX() {
        return targetX;
    }

    /**
     * @return the targetY
     */
    public float getTargetY() {
        return targetY;
    }

    /**
     * @return the targetUnit
     */
    public Unit getTargetUnit() {
        return targetUnit;
    }

    /**
     * @return the orderString
     */
    public String getOrderString() {
        return orderString;
    }

    
    public int getType()
    {
        return type;
    }
    
}
