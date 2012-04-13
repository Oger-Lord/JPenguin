/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TowerDefense;

import de.jpenguin.game.Game;
/**
 *
 * @author Karsten
 */
public class StarterHost {
     public static void main(String[] args) {
          new Game(new TowerDefense(),"",2,"p1");
    }
}
