/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jpenguin.game;


import java.util.PriorityQueue;

public class Timer implements Comparable{
    
    private static PriorityQueue heap;
    private static long myTime;
    
    private Runnable runnable;
    //private Object value;
    private long time;
    private boolean loop;
    private long loopDuration;
    
    public static void init()
    {
        myTime=0;
        heap = new PriorityQueue();
    }
    
    public static void update(float tpf)
    {
        if(heap.isEmpty()==false)
        {
            myTime += (long)(tpf*1000);
            while(heap.isEmpty()==false && ((Timer)heap.peek()).time < myTime )
            {
                Timer t = (Timer)heap.peek();
                heap.remove();
                t.execute();
            }
        }
    }
    
    public Timer(Runnable runnable, Object value, int time, boolean loop )
    {
        this.runnable=runnable;
     //   this.value=value;
        this.time = myTime+(long)time;
        this.loop=loop;
        if(loop)
        {
            loopDuration = (long)time;
        }
        heap.add(this);
    }

    private void execute()
    {
        if(runnable != null)
        {
            runnable.run();
            if(loop)
            {
                time = time + loopDuration;
                heap.add(this);
            }else{
              //  value=null;
                runnable=null;
            }
        }
    }
    
    public void cancel()
    {
       // value=null;
        runnable=null;
        heap.remove(this);
    }

    public int compareTo(Object o)
    {
        return (int)(time -((Timer)o).time);
    }
    
}
 