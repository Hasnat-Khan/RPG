/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package justwork;
import java.awt.Image;
import java.util.ArrayList;
/**
 * Animation class for the sprites animation when it moves 
 * 
 * This class does the animation for the sprite. It uses the timepassed and figures out which image needs to be used.
 * 
 */
public class Animation {
    
    private ArrayList<OneScene> scenes;
    private int sceneIndex;
    private long movieTime;
    private long totalTime;
    
    //CONSTRUCTOR
    public Animation(){
        scenes = new ArrayList<OneScene>();
        totalTime = 0;
        start();
    }
    
    // this method is to add a frame and the length of time it is shown for
    public synchronized void addScene(Image i, long t){
        totalTime += t;
        scenes.add(new OneScene(i,totalTime));
    }
    
    //start animation from begining
    public synchronized void start(){
        movieTime = 0;
        sceneIndex = 0;
    }
    
    //this method changes the frames so that it looks like it is moving
    public synchronized void update(long timePassed){
        if(scenes.size()>1){
            movieTime += timePassed; 
            if(movieTime >= totalTime){
                movieTime = 0;
                sceneIndex = 0;
            }
            while(movieTime > getScene(sceneIndex).endTime){
                sceneIndex++;
            }
        }    
    }
    
    //gets the image, used by the drawing class, not this one, for it to figure out which
    // frame to draw
    public synchronized Image getImage(){
        if(scenes.size() == 0){
            return null;
        }else{
            return getScene(sceneIndex).pic;
        }
    }
    
    //gets the frame, can be used to get the endtime of the frame
    private OneScene getScene(int x){
        return (OneScene)scenes.get(x);
    }
    
    //another class to add frames, it isnt images because it also has the variable for endtime
    private class OneScene{
        Image pic;
        long endTime;
        
        //makes the frame with the endtime saved
        public OneScene(Image pic, long endTime){
            this.pic = pic;
            this.endTime = endTime;
        }
    }
}
