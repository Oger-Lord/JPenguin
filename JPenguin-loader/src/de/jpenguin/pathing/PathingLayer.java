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
        Water(0),Ground(1),Building(2),Air(3);
        
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

