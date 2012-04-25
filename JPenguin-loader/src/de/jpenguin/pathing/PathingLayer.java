/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.pathing;

/**
 *
 * @author Karsten
 */
public enum PathingLayer{
        DrawMap(0),UnitMap(1),WaterMap(2),EditorMap(3);
        
        private final int i;
        PathingLayer(int i)
        {
            this.i=i;
        }
        
        public int getValue()
        {
            return i;
        }
    }  

