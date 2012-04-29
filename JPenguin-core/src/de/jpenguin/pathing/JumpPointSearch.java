/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.pathing;

import java.util.PriorityQueue;
import java.util.ArrayList;

import com.jme3.math.Vector3f;

/**
 *
 * @author Oger-Lord
 */
public class JumpPointSearch {
    
    private SubPathingMap subPathingMap;
    private int targetX,targetY;
    private float endX,endY;
    private PriorityQueue<SearchNode> priorityQueue;
    private ArrayList<SearchNode> resetList=new ArrayList();
    private int size;
    
    private static final int N=0;
    private static final int NW=1;
    private static final int W=2;
    private static final int SW=3;
    private static final int S=4;
    private static final int SE=5;
    private static final int E=6;
    private static final int NE=7;
    
    private SearchNode found = null;
    
    
    public JumpPointSearch(PathingMap pm, int size,int map, float startX, float startY, float endX, float endY)
    {
        this.size=size;
        
        subPathingMap = pm.getSubMap(map);

        search(startX,startY,endX,endY);
    }
    
    private void search(float startX, float startY, float endX, float endY)
    {
       priorityQueue= new PriorityQueue();
        
       this.endX=endX;
       this.endY=endY;
        targetX=subPathingMap.convertX(endX);
        targetY=subPathingMap.convertY(endY);
        
        if(subPathingMap.hasSpaceDirect(subPathingMap.convertX(startX), subPathingMap.convertY(startY))==false)
        {
            return;
        }
        
        if(subPathingMap.hasSpaceDirect(targetX,targetY)==false)
        {
            return;
        }
        
        SearchNode startPoint =subPathingMap.getSearchPoint(subPathingMap.convertX(startX), subPathingMap.convertY(startY));
        startPoint.setInfos(0, 0,8,null, false);
        priorityQueue.add(startPoint);
        resetList.add(startPoint);

        while(priorityQueue.isEmpty()==false)
        {
           SearchNode jp = priorityQueue.poll();
            
           if(jp.getX()==targetX && jp.getY() == targetY)
           {
                 found=jp;
                 return;
           }
           
           jp.setClosed(true);
            
            int[] directions = getNeighbours(jp.getX(), jp.getY(), jp.getDirection());
            
            for(int i=0;i<directions.length;i++)
            {
                if(directions[i] != -1)
                {
                   // System.out.println(jp.getX()+" "+jp.getY()+" "+directions[i]);
                    
                    SearchNode newJP = jump(jp, jp.getX(), jp.getY(),directions[i]);

                    if(newJP != null && newJP.isClosed()==false)
                    {
                        if(newJP.isUsed()==false)
                        {
                            newJP.setInfos(targetX, targetY, directionOfMove(jp , newJP), jp, false);
                            priorityQueue.add(newJP);
                            resetList.add(newJP);
                        }else if(newJP.isUsed() && newJP.getLength() > jp.getLength()+ lengthBetween(jp,newJP))
                        {
                            newJP.setInfos(targetX, targetY, directionOfMove(jp , newJP), jp, false);
                            
                            priorityQueue.remove(newJP);
                            priorityQueue.add(newJP);
                        }
                        
                    }
                }
            }
            
        }
    }
    
    public int directionOfMove(SearchNode from , SearchNode to)
    {
        if(from.getX() == to.getX())
        {
            if(from.getY() == to.getY())
                return 8;
            else if(from.getY() < to.getY())
                return S;
            else
                return N;
        }else if(from.getX() > to.getX())
        {
            if(from.getY() == to.getY())
                return W;
            else if(from.getY() < to.getY())
                return SW;
            else
                return NW;
        }else
        {
            if(from.getY() == to.getY())
                return E;
            else if(from.getY() < to.getY())
                return SE;
            else
                return NE;
        }
    }
    
    
    
    
    public float lengthBetween(SearchNode a , SearchNode b)
    {
        if(a.getX()-b.getX()==0 || a.getY()-b.getY()==0)
        {
            return Math.abs(a.getX()-b.getX()+a.getY()-b.getY());
        }
       return (float)Math.sqrt(Math.pow(a.getX()-b.getX(),2)+Math.pow(a.getY()-b.getY(),2));
    }

    
    
    public ArrayList<Vector3f> getVectorList()
    {
        ArrayList<Vector3f> list = new ArrayList();
                
        list.add(new Vector3f(endX,0,endY));
            
        SearchNode current = found;

        while(current != null)
        {
          //  if(current.isSkip()==false)
          //  {
                list.add(new Vector3f(subPathingMap.convertIntX(current.getX()),0,subPathingMap.convertIntY(current.getY())));
          //  }
            
            current = current.getParent();
        }
        
        return list;
    }
    
    public boolean wayExists()
    {
        if(found==null)
            return false;
        
        return true;
    }

    
    private int moveInDirectionX(int x, int direction)
    {
        switch(direction%8)
        {
            case N: return x;
            case NW: return x-1;
            case W: return x-1;
            case SW: return x-1;
            case S: return x;
            case SE: return x+1;
            case E: return x+1;
            case NE: return x+1;            
        }
        return -1;
    }
    private int moveInDirectionY(int y, int direction)
    {
        switch(direction%8)
        {
            case N: return y-1;
            case NW: return y-1;
            case W: return y;
            case SW: return y+1;
            case S: return y+1;
            case SE: return y+1;
            case E: return y;
            case NE: return y-1;            
        }
        return -1;
    }
    
    public SearchNode jump(SearchNode parent, int x, int y, int direction)
    {
        int newx = moveInDirectionX(x,direction);
        int newy = moveInDirectionY(y,direction);
        
        if(subPathingMap.hasSpaceDirect(newx, newy, size)==false)
        {
            return null;
        }
        
        //make sure that we are allowed to make this diagonal step
        if(directionIsDiagonal(direction))
        {
            if(isEnterable(x, y, (direction+7)%8)==false && isEnterable(x, y, (direction+1)%8)==false)
                return null;
        }
        
        if((newx==targetX && newy == targetY) || hasForcedNeighbours(newx, newy, direction))
        {
            return subPathingMap.getSearchPoint(newx, newy);
        }
        
        if(directionIsDiagonal(direction))
        {
            SearchNode next = jump(parent,newx,newy,(direction+7)%8);
            if(next != null)
            {
                return subPathingMap.getSearchPoint(newx, newy);
            }
            
            next = jump(parent,newx,newy,(direction+1)%8);
            if(next != null)
            {
                return subPathingMap.getSearchPoint(newx, newy);
            }
        }
        
        return jump(parent,newx, newy, direction);
    }
    
    
    
    private boolean hasForcedNeighbours(int x, int y, int direction)
    {
        if(direction == 8)
            return false;
        
        int farray[] = getForcedNeighbours(x, y, direction);
        if(farray[0] != -1 || farray[1] != -1)
        {
            return true;
        }
        
        return false;
    }
    
    private int[] getNeighbours(int x, int y, int direction)
    {
        if(direction == 8)
        {
            int[] array = {0,1,2,3,4,5,6,7};
            return array;
        }
        
        
        int[] array = {-1,-1,-1,-1,-1};
        
        //Natural Neighbours
        
        array[0] = direction;
        
        if(directionIsDiagonal(direction))
        {
            array[1] = (direction + 1) % 8;
            array[2] = (direction + 7) % 8;
        }
        
        //Forced Neighbours
        int farray[] = getForcedNeighbours(x, y, direction);
        
        if(farray[0] != array[1] && farray[0] != array[2])
        {
            array[3] = farray[0];
        }
        
        if(farray[1] != array[1] && farray[1] != array[2])
        {
            array[4] = farray[1];
        }
        
        return array;
    }
    
    
    public boolean isEnterable(int x, int y, int direction)
    {
        int newX=moveInDirectionX(x, direction);
        int newY=moveInDirectionY(y, direction);
        
        return subPathingMap.hasSpaceDirect(newX, newY,size);
    }
    
        public int[] getForcedNeighbours(int x, int y, int direction)
    {
        if(direction == 8)
        {
            return null;
        }
        
       int[] array = {-1,-1};
       
       switch(direction)
       {
           case N: {
               
                   if(!subPathingMap.hasSpaceDirect(x+1, y,size) && subPathingMap.hasSpaceDirect(x+1, y-1,size))
                       array[0] = NE;
                   if(!subPathingMap.hasSpaceDirect(x-1, y,size) && subPathingMap.hasSpaceDirect(x-1, y-1,size))
                       array[1] = NW;
               break;
           }
           case NW: {
               
               if(!subPathingMap.hasSpaceDirect(x, y+1,size) && subPathingMap.hasSpaceDirect(x-1, y+1,size))
                   array[0] = SW;
               if(!subPathingMap.hasSpaceDirect(x+1, y,size) && subPathingMap.hasSpaceDirect(x+1, y-1,size))
                   array[1] = NE;
               break;
           }     
           case W: {
               
                   if(!subPathingMap.hasSpaceDirect(x, y-1,size) && subPathingMap.hasSpaceDirect(x-1, y-1,size))
                       array[0] = NW;
                   if(!subPathingMap.hasSpaceDirect(x, y+1,size) && subPathingMap.hasSpaceDirect(x-1, y+1,size))
                       array[1] = SW;
               break;
           } 
           case SW: {
               if(!subPathingMap.hasSpaceDirect(x, y-1,size) && subPathingMap.hasSpaceDirect(x-1, y-1,size))
                   array[0] = NW;
               if(!subPathingMap.hasSpaceDirect(x+1, y,size) && subPathingMap.hasSpaceDirect(x+1, y+1,size))
                   array[1] = SE;
               break;
           }
           case S: {
               
                   if(!subPathingMap.hasSpaceDirect(x+1, y,size) && subPathingMap.hasSpaceDirect(x+1, y+1,size))
                       array[0] = SE;
                   if(!subPathingMap.hasSpaceDirect(x-1, y,size) && subPathingMap.hasSpaceDirect(x-1, y+1,size))
                       array[1] = SW;
               break;
           }
           case SE: {
               if(!subPathingMap.hasSpaceDirect(x, y-1,size) && subPathingMap.hasSpaceDirect(x+1, y-1,size))
                   array[0] = NE;
               if(!subPathingMap.hasSpaceDirect(x-1, y,size) && subPathingMap.hasSpaceDirect(x-1, y+1,size))
                   array[1] = SW;
               break;
           }
           case E: {
               
                   if(!subPathingMap.hasSpaceDirect(x, y-1,size) && subPathingMap.hasSpaceDirect(x+1, y-1,size))
                       array[0] = NE;
                   if(!subPathingMap.hasSpaceDirect(x, y+1,size) && subPathingMap.hasSpaceDirect(x+1, y+1,size))
                       array[1] = SE;
               break;
           } 
           case NE: {
               if(!subPathingMap.hasSpaceDirect(x, y+1,size) && subPathingMap.hasSpaceDirect(x+1, y+1,size))
                   array[0] = SE;
               if(!subPathingMap.hasSpaceDirect(x-1, y,size) && subPathingMap.hasSpaceDirect(x-1, y-1,size))
                   array[1] = NW;
               break;
           }
       }
        return array;
    }
    
 
    
    private boolean directionIsDiagonal(int direction)
    {
        return (direction%2) == 1;
    }
    
    public void finishSearch()
    {
        while(resetList.isEmpty()==false)
        {
            resetList.get(0).reset();
            resetList.remove(0);
        }
    }
    
    
    //Some old and not needed code
    
       /*
    public int[] getForcedNeighbours(int x, int y, int direction)
    {
        if(direction == 8)
            return null;
        
         int[] array = {-1,-1};
         
         if(directionIsDiagonal(direction))
         {
             if(!isEnterable(x, y, 6) && isEnterable(x, y, 5))
                 array[0] = (direction +6) % 8;
             if(!isEnterable(x, y, 2) && isEnterable(x, y, 3))
                 array[1] = (direction +2) % 8;
         }else{
             if(!isEnterable(x, y, 7) && isEnterable(x, y, 6))
                 array[0] = (direction +7) % 8;
             if(!isEnterable(x, y, 1) && isEnterable(x, y, 2))
                 array[1] = (direction +1) % 8;
         }
         
         return array;
    }
     * 
     */
/*
    public int[] getForcedNeighbours(int x, int y, int direction)
    {
        if(direction == 8)
        {
            return null;
        }
        
       int[] array = {-1,-1};
       
       switch(direction)
       {
           case N: {
               
               if(subPathingMap.hasSpaceDirect(x, y-1,size))
               {

                   if(!subPathingMap.hasSpaceDirect(x+1, y,size) && subPathingMap.hasSpaceDirect(x+1, y-1,size))
                       array[0] = NE;
                   if(!subPathingMap.hasSpaceDirect(x-1, y,size) && subPathingMap.hasSpaceDirect(x-1, y-1,size))
                       array[1] = NW;
               }
               break;
           }
           case NW: {
               
               if(!subPathingMap.hasSpaceDirect(x, y+1,size) && subPathingMap.hasSpaceDirect(x-1, y+1,size) && subPathingMap.hasSpaceDirect(x-1, y,size))
                   array[0] = SW;
               if(!subPathingMap.hasSpaceDirect(x+1, y,size) && subPathingMap.hasSpaceDirect(x+1, y-1,size) && subPathingMap.hasSpaceDirect(x, y-1,size))
                   array[1] = NE;
               break;
           }     
           case W: {
               
               if(subPathingMap.hasSpaceDirect(x-1, y,size))
               {
                   if(!subPathingMap.hasSpaceDirect(x, y-1,size) && subPathingMap.hasSpaceDirect(x-1, y-1,size))
                       array[0] = NW;
                   if(!subPathingMap.hasSpaceDirect(x, y+1,size) && subPathingMap.hasSpaceDirect(x-1, y+1,size))
                       array[1] = SW;
               }
               break;
           } 
           case SW: {
               if(!subPathingMap.hasSpaceDirect(x, y-1,size) && subPathingMap.hasSpaceDirect(x-1, y-1,size) && subPathingMap.hasSpaceDirect(x-1, y,size))
                   array[0] = NW;
               if(!subPathingMap.hasSpaceDirect(x+1, y,size) && subPathingMap.hasSpaceDirect(x+1, y+1,size) && subPathingMap.hasSpaceDirect(x, y+1,size)  )
                   array[1] = SE;
               break;
           }
           case S: {
               
               if(subPathingMap.hasSpaceDirect(x, y+1,size))
               {
                   if(!subPathingMap.hasSpaceDirect(x+1, y,size) && subPathingMap.hasSpaceDirect(x+1, y+1,size))
                       array[0] = SE;
                   if(!subPathingMap.hasSpaceDirect(x-1, y,size) && subPathingMap.hasSpaceDirect(x-1, y+1,size))
                       array[1] = SW;
               }
               break;
           }
           case SE: {
               if(!subPathingMap.hasSpaceDirect(x, y-1,size) && subPathingMap.hasSpaceDirect(x+1, y-1,size) && subPathingMap.hasSpaceDirect(x+1, y,size))
                   array[0] = NE;
               if(!subPathingMap.hasSpaceDirect(x-1, y,size) && subPathingMap.hasSpaceDirect(x-1, y+1,size) && subPathingMap.hasSpaceDirect(x, y+1,size))
                   array[1] = SW;
               break;
           }
           case E: {
               
               if(subPathingMap.hasSpaceDirect(x-1, y,size))
               {
                   if(!subPathingMap.hasSpaceDirect(x, y-1,size) && subPathingMap.hasSpaceDirect(x+1, y-1,size))
                       array[0] = NE;
                   if(!subPathingMap.hasSpaceDirect(x, y+1,size) && subPathingMap.hasSpaceDirect(x+1, y+1,size))
                       array[1] = SE;
               }
               break;
           } 
           case NE: {
               if(!subPathingMap.hasSpaceDirect(x, y+1,size) && subPathingMap.hasSpaceDirect(x+1, y+1,size) && subPathingMap.hasSpaceDirect(x+1, y,size))
                   array[0] = SE;
               if(!subPathingMap.hasSpaceDirect(x-1, y,size) && subPathingMap.hasSpaceDirect(x-1, y-1,size) && subPathingMap.hasSpaceDirect(x, y-1,size))
                   array[1] = NW;
               break;
           }
       }
        return array;
    }
*/
    
}
