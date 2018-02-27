/*
 * 
 */
package camera;

import java.util.*;
/**
 *
 * @author Ross Wagner
 */
public class camera {

    public static class obj{
        int index;
        float x,y;
        float viewStart, viewEnd;
        boolean aboveWall;
        
        public obj(int index,float x, float y, wall wall){
            // init the obj
            this.index = index;
            this.x = x;
            this.y = y;
            this.aboveWall = this.y > wall.height;
            if(aboveWall){
                float slope1 = clacSlope(this.x, this.y, wall.openStart, wall.height);
                float slope2 = clacSlope(this.x, this.y, wall.openEnd, wall.height);
                // clac x intercepts
                this.viewStart = -this.y/slope1 + this.x; // may cause issues if slope is inf
                this.viewEnd = -this.y/slope2 + this.x;
            }
            else{
                viewStart = Float.NEGATIVE_INFINITY;
                viewEnd = Float.POSITIVE_INFINITY;
            }
        }
        
        // give custom sorting method
        
        //to string function for printing
        @Override
        public String toString(){
            return "X: "+x+" Y: "+y+" Visable x= "+viewStart+" to "+viewEnd;
        }
        
    }
    
    public static class viewPoint implements Comparable<viewPoint>{
        obj obj;
        int value; // -1 for end +1 for start
        float pos; // position on the x axis
        
        public viewPoint(obj obj, int value, float pos){
            this.obj = obj;
            this.value = value;
            this.pos = pos;
        }
        
        @Override
        public int compareTo(viewPoint other){
            if(this.pos > other.pos){
                return 1;
            }
            else if(this.pos < other.pos){
                return -1;
            } 
            else{
                // if tie -1 comes first
                return  other.value - this.value;
            }
        }
        
        @Override
        public String toString(){
            return "Obj: "+obj.index+" at "+ pos+" "+value;
        }
        
    }
    
    public static class wall{
        float height, openStart, openEnd;
        
        public wall(float height, float openStart, float openEnd){
            this.height = height;
            this.openStart = openStart;
            this.openEnd = openEnd;
        }
        
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // get input from stdin
        Scanner in = new Scanner(System.in);
        int numObj = in.nextInt();
        float wallDist = in.nextFloat();
        float openingStart = in.nextFloat();
        float openingEnd = in.nextFloat();
        
        wall wall = new wall(wallDist,openingStart, openingEnd);
        
        obj[] objs = new obj[numObj];
        boolean[] used = new boolean[numObj];
        
        
        // edges of view window of objects
        ArrayList vps = new ArrayList(numObj*2); 
        
        // load objs in into array
        for(int i = 0; i < numObj; i++){
            
            obj tempObj = new obj(i,in.nextFloat(), in.nextFloat(), wall); 
            objs[i] = tempObj;
            used[i] = false;
            
            // get edges of viable area
            viewPoint tempVp1 = new viewPoint(tempObj, 1, tempObj.viewStart );
            viewPoint tempVp2 = new viewPoint(tempObj, -1, tempObj.viewEnd );
            vps.add(tempVp1);
            vps.add(tempVp2);
        }
        Collections.sort(vps);
        
        
    
    
    
    }
    
    public static float clacSlope(float x1, float y1, float x2, float y2){
        if(x1 == x2){
            if(y2 > y1){
                return Float.POSITIVE_INFINITY;
            }
            else if(y2 < y1){
                return Float.NEGATIVE_INFINITY;
            }
            else{
                return Float.NaN;
            }
        }
        return ((float)(y2-y1))/((float)(x2-x1));
    }
    
}
