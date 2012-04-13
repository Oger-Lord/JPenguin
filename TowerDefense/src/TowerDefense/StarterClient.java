/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TowerDefense;

import de.jpenguin.game.Game;
import javax.swing.JOptionPane;

/**
 *
 * @author Karsten
 */
public class StarterClient {
    public static void main(String[] args) {
        String adress =JOptionPane.showInputDialog(null, "Server Adress","localhost");
        
        if(adress == null){return;}
        new Game(new TowerDefense(),adress,2,"p2");
    }
}
