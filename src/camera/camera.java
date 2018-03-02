/**
 * Author: Ross Wagner
 * 
 * Im interpreting the wall and the path of the car to be vertical to avoid 
 * infinite slope.
 * 
 * To Do
 * ------
 * implement greedy alg for taking pictures.
 * 
 * remove unused items
 * 
 * error to many pictures sometimes. might have to do with precision of double double
 *  
 * 
 * 
 */
package camera;

import java.util.*;
/**
 * This is the main class of the program. Its main takes input from stdin in the 
 * form of <number of objects> <distance from axis of occluder> 
 * <cordinates of objects (y,x)> y coordinate comes first because in the 
 * specicifations for this problem the occluder and car path are horizontal however
 * to avoid issues with infininite slope I decided to make them vertical.
 * 
 * 
 */
public class camera {

    /**
     * obj represents objects that we are trying to take pictures of
     * 
     */
    public static class obj{
        // location in index where this obj is stored
        int index;
        // cordinates
        double x,y;
        // place on y axis (car's path) where obj is visabl e 
        double viewStart, viewEnd;
        boolean aboveWall;
        
        public obj(int index,double x, double y, wall wall){
            // init the obj
            this.index = index;
            this.x = x;
            this.y = y;
            // 
            this.aboveWall = this.x > wall.dist;
            if(aboveWall){
                double slope1 = clacSlope(this.x, this.y, wall.dist, wall.openStart);
                double slope2 = clacSlope(this.x, this.y, wall.dist, wall.openEnd);
                // clac y intercepts
                this.viewStart = -this.x*slope1 + this.y; 
                this.viewEnd = -this.x*slope2 + this.y;
            }
            else{
                viewStart = Double.NEGATIVE_INFINITY;
                viewEnd = Double.POSITIVE_INFINITY;
            }
        }
        
        // give custom sorting method
        
        //to string function for printing
        @Override
        public String toString(){
            return "X: "+x+" Y: "+y+" Visable y = "+viewStart+" to "+viewEnd;
        }
        
    }
    
    public static class viewPoint implements Comparable<viewPoint>{
        obj obj;
        int value; // -1 for end +1 for start
        double pos; // position on the x axis
        
        public viewPoint(obj obj, int value, double pos){
            this.obj = obj;
            this.value = value;
            this.pos = pos;
        }
        
        @Override
        public int compareTo(viewPoint other){
            return Double.compare(this.pos, other.pos);
        }
        
        @Override
        public String toString(){
            return "Obj: "+obj.index+" at "+ pos+" "+value;
        }
        
    }
    
    public static class wall{
        double dist, openStart, openEnd;
        
        public wall(double dist, double openStart, double openEnd){
            this.dist = dist;
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
        double wallDist = in.nextDouble();
        double openingStart = in.nextDouble();
        double openingEnd = in.nextDouble();
        
        wall wall = new wall(wallDist,openingStart, openingEnd);
        //System.out.println("Wall: "+wall.dist);
        
        obj[] objs = new obj[numObj];
        boolean[] used = new boolean[numObj];
        
        
        // edges of view window of objects
        ArrayList vps = new ArrayList(numObj*2); 
        
        // load objs in into array
        for(int i = 0; i < numObj; i++){
            
            double x,y;
            y=in.nextDouble();
            x=in.nextDouble();
            
            obj tempObj = new obj(i,x, y, wall); 
            objs[i] = tempObj;
            used[i] = false;
            //System.out.println(tempObj);
            // get edges of viable area
            viewPoint tempVp1 = new viewPoint(tempObj, 1, tempObj.viewStart );
            viewPoint tempVp2 = new viewPoint(tempObj, -1, tempObj.viewEnd );
            vps.add(tempVp1);
            vps.add(tempVp2);
        }
        Collections.sort(vps);
        
        System.out.println(vps);
        
        int numPics = numPics(vps);
        System.out.println(numPics);
        
        
        
        
        
        
        
        
    
    
    
    }
    
    /**
     * Calculates the slope of two points given their x and y coordinates.
     * 
     * @param x1, 
     * @param y1 
     * @param x2 
     * @param y2 
     * 
     * @return slope, the slope of the line with given points
     */
    public static double clacSlope(double x1, double y1, double x2, double y2){
        return (y2-y1)/(x2-x1);
    }
    
    /**
     * Greedy alg ...
     * 
     * @param vps, array list of view points
     * @return numPics number of pictures needed to photograph all objs
     */
    public static int numPics(ArrayList<viewPoint> vps){
        if(vps.isEmpty()){
            return 0;
        }
        
        // where vp s not photographed will go
        ArrayList remaining = new ArrayList();
        
        int len = vps.size();
        int numVis = 0;
        int maxVis = 0;
        double pos = -Double.MAX_VALUE;
        // find location where most objs in view
        for(int i = 0;i < len; i++){
            numVis += vps.get(i).value;
            if(numVis > maxVis){
                pos = vps.get(i).pos;
            }        
        }
        
        // mark ojs as used
        for(int i = 0; i < len ; i++){
            // if obj out of view add it to remaining
            if (vps.get(i).obj.viewStart > pos || vps.get(i).obj.viewEnd < pos){
                remaining.add(vps.get(i));
            }           
        } 
        
        return 1 + numPics(remaining);
    }
}
