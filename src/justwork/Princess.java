/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package justwork;

import java.util.Random;

/**
 * Extends sprite. Has x, y, and image. Contains dialogue to create a subpar story.
 * @author hasna
 */
public class Princess extends Sprite{
    
    
    private static int screenWidth, screenHeight;
    private String dialogue1, dialogue2, dialogue3 , dialogue4;
    private boolean saved;
    private Random r;
    private int distance;
    
    //CONSTRUCTOR
    public Princess(Animation anim) {
        super(anim);
        r = new Random();
        saved = false;
    }
    
    public void respawn(float mapX, float mapY, int mapWidth, int mapHeight){
        // left bound 0, map.getWidth
        // left bound map
        
        this.setX(r.nextInt(mapWidth-300) + mapX+150);
        this.setY(r.nextInt(mapHeight-300) + mapY+150);
        
        //if the princess spawns on the screen, she has to respawn
        if(this.getX() > 0 && this.getX() < screenWidth && this.getY() > 0 && this.getY() < screenHeight){
            respawn(mapX, mapY, mapWidth, mapHeight);
        }
        this.setVelocityX(0);
        this.setVelocityY(0);
    }
    
    //updates x and y coordinates of princess
    public void updateX(long timePassed, float vx){
        this.setX(this.getX() + vx*timePassed);
    }
    public void updateY(long timePassed, float vy){
        this.setY(this.getY() + vy*timePassed);
    }
    
    
    public void setScreenWidth(int width){
        screenWidth = width;
    }
    public void setScreenHeight(int height){
        screenHeight = height;
    }
    
    
    
 
    public boolean isSaved(){
        return saved;
    }
    public void setSaved(boolean b){
        saved = b;
    }
    
    public float getHealth(){
        return 1;
    }
}
