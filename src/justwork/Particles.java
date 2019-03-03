/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package justwork;

/**
 * Extends sprite. Has x, y, vx, and vy and image. If it touches a monster it does damage to the monster.
 * @author hasna
 */
public class Particles extends Sprite{
    private float startX, startY;
    private int distance;
    private float pSpeed;
    private static float defaultCoordX, defaultCoordY;
    private int range;
    private static float mcX, mcY;

    //CONSTRUCTOR
    public Particles(Animation anim) {
        super(anim);
        this.setX(0-this.getWidth());
        this.setY(0-this.getHeight());
        startX = 0;
        startY = 0;
        pSpeed = 0.4f;
    }
    public void resetCoord(){
        this.setX(defaultCoordX);
        this.setY(defaultCoordX);
        this.setVelocityX(0);
        this.setVelocityY(0);
    }
    
    public void updateX(long timePassed, float vx){
        this.setX(this.getX() + vx*timePassed);
    }
    public void updateY(long timePassed, float vy){
        this.setY(this.getY() + vy*timePassed);
    }
    
    public void setStartX(float sx){
        startX = sx;
    }
    public void setStartY(float sy){
        startY = sy;
    }
    public void setDefaultCoord(float ex, float ey){
        defaultCoordX = ex;
        defaultCoordY = ey;
    }public void setMC(float ex, float ey){
        mcX = ex;
        mcY = ey;
    }
    
    
    public float getStartX(){
        return startX;
    }
    public float getStartY(){
        return startY;
    }
    public float getSpeed(){
        return pSpeed;
    }
    public int getDistance(){
        return distance;
    }
    public int getRange(){
        return range;
    }
    public void calcDistance(){
        distance = (int) (Math.sqrt(Math.pow(Math.abs(startX-this.getX()),2) + Math.pow(Math.abs(startY-this.getY()), 2)));
    }
    public void calcRange(){
        range = (int) (Math.sqrt(Math.pow(mcX-this.getX(),2) + Math.pow(mcY-this.getY(), 2)));
    }
}
