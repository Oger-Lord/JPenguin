/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.engine;

import com.jme3.audio.AudioNode;
import com.jme3.math.Vector3f;

import de.jpenguin.unit.Unit;
import de.jpenguin.game.Game;
import de.jpenguin.game.GameApplication;
/**
 *
 * @author Karsten
 */

public class Sound {
    
    private AudioNode audio;
    private GameApplication gameApp;
    
    public Sound(Game game, String path, Vector3f position, float volume)
    {
        if(path.isEmpty()==false)
        {
            gameApp = game.getGameApplication();
            try{
            audio = new AudioNode(gameApp.getAssetManager(), path, false);
            }catch(Exception e){System.out.println("Could not load sound: " + path);}
            if(audio!=null)
            {
                audio.setLooping(false);
                audio.setPositional(true);
                audio.setLocalTranslation(position);
                audio.setVolume(2f);
                gameApp.getRootNode().attachChild(audio);
                audio.setReverbEnabled(false);
                audio.setRefDistance(0.2f);
                audio.playInstance(); 
            }
        }
    }

}
