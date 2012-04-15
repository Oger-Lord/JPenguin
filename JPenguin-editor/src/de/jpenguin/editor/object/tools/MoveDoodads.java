/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.editor.object.tools;

/**
 *
 * @author Karsten
 */

//import com.jme3.gde.core.sceneexplorer.nodes.AbstractSceneExplorerNode;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.terrain.Terrain;
import java.util.ArrayList;
import java.util.List;
import de.jpenguin.editor.engine.Undo;

import de.jpenguin.pathing.PathingMapName;

import de.jpenguin.editor.object.SelectionCircle;
import de.jpenguin.editor.object.Selection;
import de.jpenguin.editor.object.Doodad;

import de.jpenguin.editor.engine.EditorApplication;

/**
 * Level the terrain to a desired height, executed from the OpenGL thread.
 * It will pull down or raise terrain towards the desired height, still
 * using the radius of the tool and the weight. There are some slight rounding
 * errors that are corrected with float epsilon testing.
 * 
 * @author Brent Owens
 */
public class MoveDoodads extends Undo {
    
    private EditorApplication editorApp;
    private List<Doodad> doodads;
    private List<Vector2f> positions;

    public MoveDoodads(ArrayList doodads, ArrayList positions, EditorApplication editorApp) {
        
        this.doodads=doodads;
        this.positions=positions;
        this.editorApp=editorApp;
    }

   // @Override
    protected Object doApplyTool() {
        return null;
    }
    
   // @Override
    public void doUndoTool(Object undoObject) {
        for(int i=0;i<doodads.size();i++)
        {
            
            doodads.get(i).removeSpace(editorApp.getPathingManager().getPathingMap(),PathingMapName.UnitMap);
            
            doodads.get(i).setLocation(positions.get(i), editorApp.getTerrainManager());
            
            doodads.get(i).updateLocation();
            
            doodads.get(i).setSpace(editorApp.getPathingManager().getPathingMap(),PathingMapName.UnitMap);
        }
    }
 
    
}
