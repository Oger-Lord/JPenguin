/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.player;

import de.jpenguin.game.Game;

import java.util.ArrayList;
/**
 *
 * @author Karsten
 */
public class PlayerCommandManager {
    
    private Game game;
    private Player p;
    private ArrayList<PlayerCommand>[] commands;
    
    private static final int size=10;
    
    public PlayerCommandManager(Game game,Player p)
    {
        this.game=game;
        this.p=p;
        commands = new ArrayList[size];
        for(int i=0;i<size;i++)
        {
            commands[i] = new ArrayList();
        }
    }
    
    public void addCommand(PlayerCommand pc, int position)
    {
        commands[position].add(pc);
    }
    
    public void update()
    {
        while(commands[0].isEmpty()==false)
        {
            commands[0].get(0).run(game);
            commands[0].remove(0);
        }
        
        if(game.isMultiplayer())
        {
            for(int i=0;i<size-1;i++)
            {
                commands[i] = commands[i+1];
            }
            commands[size-1] = new ArrayList();
        }
    }
}
