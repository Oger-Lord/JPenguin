/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.pathing;

/**
 *
 * @author Oger-Lord
 */
    public class SearchNode implements Comparable
    {
        private int x,y;
        private float length;
        private SearchNode parent;
        private int direction;
        private float priorityvalue;
        //private boolean skip=false;
        private boolean used;
        private boolean closed;
        
        public SearchNode(int x, int y)
        {
            this.x=x;
            this.y=y;
            used=false;
            closed=false;
            length=0;
        }
        
        //Set Infos for JumpPointSearch
        public void setInfos(int targetX, int targetY,int direction,SearchNode par, boolean skip)
        {
            this.direction=direction;
            if(par != null)
            {
                length = (float)Math.sqrt(Math.pow(par.getX()-getX(), 2)+Math.pow(par.getY()-getY(), 2)) + par.getLength();
                priorityvalue = getLength()+(float)Math.sqrt(Math.pow(targetX-getX(), 2)+Math.pow(targetY-getY(), 2));
            }
                
            this.parent=par;
          //  this.skip=skip;
            this.used=true;
        }
        
        //Set Infos for normal A*
        public void setInfos(int targetX, int targetY,float length,SearchNode par)
        {
            this.length=length;
            if(par != null)
            {
                priorityvalue = length+Math.abs(targetX-getX())+Math.abs(targetY-getY());
               //priorityvalue = length+(float)Math.sqrt(Math.pow(targetX-getX(), 2)+Math.pow(targetY-getY(), 2));
            }else{
                priorityvalue=0;
            }
            this.used=true;
            this.parent=par;
        }
        
        public void reset()
        {
            this.used=false;
            this.setClosed(false);
           // this.skip=false;
        }
        
        @Override
        public int compareTo(Object o)
        {   
            return (int)(priorityvalue -((SearchNode)o).priorityvalue);
        }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @return the length
     */
    public float getLength() {
        return length;
    }

    /**
     * @return the parent
     */
    public SearchNode getParent() {
        return parent;
    }

    /**
     * @return the direction
     */
    public int getDirection() {
        return direction;
    }

    /**
     * @return the skip
     */
    /*
    public boolean isSkip() {
        return skip;
    }
     * 
     */
    

    /**
     * @return the used
     */
    public boolean isUsed() {
        return used;
    }

    /**
     * @return the closed
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * @param closed the closed to set
     */
    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    /**
     * @param skip the skip to set
     */
    /*
    public void setSkip(boolean skip) {
        this.skip = skip;
    }
     * 
     */
}