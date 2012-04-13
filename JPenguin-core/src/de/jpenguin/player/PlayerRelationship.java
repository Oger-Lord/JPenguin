/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.player;

/**
 *
 * @author Karsten
 */
public class PlayerRelationship {
    
    public static int enemy=-1;
    public static int friend=1;
    public static int neutral=0;
    
    private boolean sharedControl;
    private boolean shareVision;
    private int status;
    
    public PlayerRelationship(int status, boolean sharedVision, boolean sharedControl)
    {
        if(status >= -1 && status <= 1)
        {
            this.status=status;
        }else{
            this.status=0;
        }
        
        this.sharedControl=sharedControl;
        this.shareVision = shareVision;
    }

    /**
     * @return the sharedVision
     */
    public boolean hasSharedVision() {
        return shareVision;
    }
    
    /**
     * @return the sharedControl
     */
    public boolean hasSharedControl() {
        return sharedControl;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }
}
