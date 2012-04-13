/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.game;

/**
 *
 * @author Karsten
 */
public interface MyGame {
    
    public void init(Game game);
    public void update(float fpt);
    public String getMap();
}
