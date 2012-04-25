package de.jpenguin.pathing;

import com.jme3.export.*;
import java.io.IOException;

/**
 *
 * @author Oger-Lord
 */
public class PathingGrid implements Savable {
    
    private int pathingField[][];
    private int width;
    private int height;
    
    private boolean calculateUnitSize;
    
    
    public static void main(String[] args) {
        
        int layerList[] = new int[6];
        
        layerList[0] = 1;
        layerList[1] = 2;
        layerList[2] = 4;
        layerList[3] = 8;
        layerList[4] = 16;
        layerList[5] = 32;
        
        
        PathingGrid pg = new PathingGrid(10, 10, true);
       // pg.setValue(3, 4, true);
        
        pg.setArea(6,3,2,2, true, 0,layerList);
        
        System.out.println(pg.toString());
        
        pg.setArea(6,3,2,2, true, 2,layerList);
        
        System.out.println(pg.toString());
        
        pg.setArea(6,3,2,2, false, 0,layerList);
        
       // pg.testLoad();
        System.out.println(pg.toString());
    }

    
    public PathingGrid(int width, int height, boolean calculateUnitSize)
    {
        this.width=width;
        this.height=height;
        this.calculateUnitSize=calculateUnitSize;
        
        pathingField = new int[width][height];
        
        if(calculateUnitSize)
        {
            for(int x=0;x<width;x++)
            {
                for(int y=0;y<height;y++)
                {
                    int valueX = width-x;
                    int valueY = height-y;

                    if(valueX >= 5 && valueY >= 5)
                    {
                        pathingField[x][y] = 5;
                    }else{

                        if(valueX < valueY)
                        {
                            pathingField[x][y] = valueX;
                        }else{
                            pathingField[x][y] = valueY;
                        }

                    }

                }
            }
        }else{
            for(int x=0;x<width;x++)
            {
                for(int y=0;y<height;y++)
                {
                    pathingField[x][y] = 1;
                }
            }
        }
    }
    
    public PathingGrid()
    {
        
    }
    
    public void gameLoad()
    {
        calculateUnitSize=true;
        calculateNew(0, 0, getWidth(), getHeight());
    }
    
    
    public boolean hasSpace(int x, int y)
    {
        if(x<width && x >= 0 && y < height && y >= 0)
        {
            if(pathingField[x][y] < 0)
            {
                return false;
            }
        }else{
            return false;
        }
        return true;
    }
    
    public boolean hasSpace(int startX, int startY,int pWidth, int pHeight)
    {
        for(int x=startX;x<startX+pWidth;x++)
        {
            for(int y=startY;y<startY+pHeight;y++)
            {
                if(x<width && x >= 0 && y < height && y >= 0)
                {
                
                    if(pathingField[x][y] < 0)
                    {
                        return false;
                    }
                    
                }else{
                    return false;
                }
            }
        }
        return true;
    }
    
    
    public void setValue(int x, int y, boolean block, int layer, int layerList[])
    {
       if(x<width && x >= 0 && y < height && y >= 0)
       {

            if(block)
            {
                if(pathingField[x][y] >= 0 )
                {
                    pathingField[x][y] = -layerList[layer];
                }else if (Math.abs(pathingField[x][y])%layerList[layer+1]<layerList[layer])
                {
                    pathingField[x][y]-=layerList[layer];
                }

            }else{
                if(pathingField[x][y] < 0 && Math.abs(pathingField[x][y])%layerList[layer+1]>=layerList[layer])
                {
                    pathingField[x][y]+=layerList[layer];
                }
            }

            int pheight = 5;
            int pwidth = 5;

            if(x < 5)
                pwidth = x+1;

            if(y < 5)
                pheight = y+1;

            if(calculateUnitSize)
            {
                calculateNew(x-pwidth+1, y-pheight+1, pwidth, pheight);
            }
       }
    }
    
    
    public void setArea(int startX, int startY,int pWidth, int pHeight, boolean block, int layer, int layerList[])
    {
        for(int x=startX;x<startX+pWidth;x++)
        {
            for(int y=startY;y<startY+pHeight;y++)
            {
                  if(x<width && x >= 0 && y < height && y >= 0)
                  {
                     if(block)
                    {
                       // System.out.println(pathingField[x][y] + " " + x + " " + y);
                        
                        if(pathingField[x][y] >= 0 )
                        {
                            pathingField[x][y] = -layerList[layer];
                        }else if (Math.abs(pathingField[x][y])%layerList[layer+1]<layerList[layer])
                        {
                            pathingField[x][y]-=layerList[layer];
                        }
                        
                      //  System.out.println(pathingField[x][y]);

                    }else{
                        if(pathingField[x][y] < 0 && Math.abs(pathingField[x][y])%layerList[layer+1]>=layerList[layer])
                        {
                            pathingField[x][y]+=layerList[layer];
                        }
                    }
                  }
            }
        }
        
        int changeX = 5;
        int changeY = 5;
        
        if(startX < 5)
            changeX = startX;
        
        if(startY < 5)
            changeY = startY;

        if(calculateUnitSize)
        {
            calculateNew(startX-changeX, startY-changeY, pWidth+changeX, pHeight+changeY);
        }
    }
    
    public void setAreaCircle(int middleX, int middleY,int pRadius, boolean block, int layer, int layerList[])
    {
        for(int x=middleX-pRadius;x<middleX+pRadius;x++)
        {
            for(int y=middleY-pRadius;y<middleY+pRadius;y++)
            {
                if(x<width && x >= 0 && y < height && y >= 0)
                {
                
                    if(Math.sqrt(Math.pow(middleX-x, 2)+Math.pow(middleY-y, 2)) <= pRadius)
                    {
                         if(block)
                        {
                           // System.out.println(pathingField[x][y] + " " + x + " " + y);

                            if(pathingField[x][y] >= 0 )
                            {
                                pathingField[x][y] = -layerList[layer];
                            }else if (Math.abs(pathingField[x][y])%layerList[layer+1]<layerList[layer])
                            {
                                pathingField[x][y]-=layerList[layer];
                            }

                          //  System.out.println(pathingField[x][y]);

                        }else{
                            if(pathingField[x][y] < 0 && Math.abs(pathingField[x][y])%layerList[layer+1]>=layerList[layer])
                            {
                                pathingField[x][y]+=layerList[layer];
                            }
                        }
                    }
                }
            }
        }
        
        int changeX = 5;
        int changeY = 5;
        
        if(middleX-pRadius < 5)
            changeX = middleX-pRadius;
        
        if(middleY-pRadius < 5)
            changeY = middleY-pRadius;

        if(calculateUnitSize)
        {
            calculateNew(middleX-pRadius-changeX, middleY-pRadius-changeY, pRadius*2+changeX, pRadius*2+changeY);
        }
    }
    
    
    private void calculateNew(int startX, int startY, int pWidth, int pHeight)
    {
        for(int y=startY+pHeight-1;y>=startY;y--)
        {
            for(int x=startX+pWidth-1;x>=startX;x--)
            {    
                if(pathingField[x][y] < 0){
                }
                else if(x == getWidth()-1 || y == getWidth()-1)
                {
                    pathingField[x][y] = 1;
                }else{
                    if(pathingField[x][y+1] < 0 || pathingField[x+1][y] < 0 || pathingField[x+1][y+1] < 0)
                    {
                        pathingField[x][y] = 1;
                    }else if(pathingField[x][y+1] >= 5 && pathingField[x+1][y] >= 5)
                    {
                        pathingField[x][y] = 5;
                    }else{
                        
                        if(pathingField[x][y+1] < pathingField[x+1][y])
                        {
                            if(pathingField[x][y+1] < pathingField[x+1][y+1])
                            {
                                pathingField[x][y]=pathingField[x][y+1]+1;
                            }else{
                                pathingField[x][y]=pathingField[x+1][y+1]+1;
                            }
                            
                        }else{
                            if(pathingField[x+1][y] < pathingField[x+1][y+1])
                            {
                                pathingField[x][y]=pathingField[x+1][y]+1;
                            }else{
                                pathingField[x][y]=pathingField[x+1][y+1]+1;
                            }
                        }  
                    }
                }
            }  
         }
    }
    
    
    
    @Override
    public String toString()
    {
        String s="";
        
        for(int y=0;y<getHeight();y++)
        {
            for(int x=0;x<getWidth();x++)
            {
                s += pathingField[x][y]+" ";
            }
            s+="\n";
        }
        
        return s;
    }
    
    
    public int getValue(int x, int y)
    {
        return pathingField[x][y];
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        
        capsule.write(width,"width",0);
        capsule.write(height,"height",0);
        
        capsule.write(calculateUnitSize,"calculateUnitSize",true);
        
        
        capsule.write(pathingField,"pathingField",null);
    }

    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        
        width = capsule.readInt("width",1);
        height = capsule.readInt("height",1);
        
        calculateUnitSize = capsule.readBoolean("calculateUnitSize",true);
        
        pathingField = capsule.readIntArray2D("pathingField", null);
    }

}
