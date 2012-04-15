/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.pathing;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import java.io.IOException;

/**
 *
 * @author Karsten
 */
    public enum PathingMapName{
        DrawMap(0),UnitMap(1),WaterMap(2),EditorMap(3);
        
        private final int i;
        PathingMapName(int i)
        {
            this.i=i;
        }
        
        public int getValue()
        {
            return i;
        }

}