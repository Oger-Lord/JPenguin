/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TowerDefense;

import de.jpenguin.player.PlayerEventListener;
import de.jpenguin.game.Chat;
import de.jpenguin.player.event.PlayerChatEvent;
import de.jpenguin.unit.event.*;
/**
 *
 * @author Karsten
 */
public class TestPlayerListener implements PlayerEventListener {

    private Chat chat;
    
    public TestPlayerListener(Chat chat)
    {
        this.chat=chat;
    }

    public void playerChatEvent(PlayerChatEvent e) {
         chat.addMessage("Listener detect chat message!");
    }

}
