/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.input;

import de.jpenguin.engine.Model;

/**
 *
 * @author Karsten
 */
public class MouseStatus {
    private Model model;
    private float x,y;
    
    public MouseStatus(Model model, float x, float y)
    {
        this.model=model;
        this.x=x;
        this.y=y;
    }

    /**
     * @return the model
     */
    public Model getModel() {
        return model;
    }

    /**
     * @return the x
     */
    public float getX() {
        return x;
    }

    /**
     * @return the y
     */
    public float getY() {
        return y;
    }
}
