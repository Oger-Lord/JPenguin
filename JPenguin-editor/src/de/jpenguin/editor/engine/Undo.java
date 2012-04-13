package de.jpenguin.editor.engine;

//import com.jme3.gde.core.sceneexplorer.nodes.AbstractSceneExplorerNode;
/**
 *
 * @author Karsten
 */
public abstract class Undo {
    
    protected boolean isbreak=false;
    
    public void setBreak(boolean b)
    {
        isbreak = b;
    }
    public boolean isBreak()
    {
        return isbreak;
    }
    
    public abstract void doUndoTool(Object undoObject);
  //  public abstract void doUndoTool(AbstractSceneExplorerNode rootNode, Object undoObject);
}
