/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package justwork;

import java.util.Random;

/**
 * Monster class. Has x, y, velocity x, velocity y and image. 
 * Also  contains health and stats of the monster, monster spawner, and distance calculator.
 * @author hasna
 */
public class Monster extends Sprite{
    private int level;
    private float maxHP;
    private float health;
    private int attack;
    private int xp;
    private float speed;
    private Random r;
    public boolean visible;
    
    
    private static int screenWidth, screenHeight;
    private int distance;
    
    // CONSTRUCTOR
    public Monster(Animation anim){
        super(anim);
        r = new Random();
    }
    
    //maxLev is the highest level the character killed
    public void respawn(int maxLev, float mapX, float mapY, int mapWidth, int mapHeight){
        level = r.nextInt(3)+ maxLev + 1;
        // left bound 0, map.getWidth
        // left bound map
        
        this.setX(r.nextInt(mapWidth) + mapX);
        this.setY(r.nextInt(mapHeight) + mapY);
        
        //if a monster spawns on the screen, it respawns the monster again, it does this until the monster spawns off the screen
        if(this.getX() > 0 && this.getX() < screenWidth && this.getY() > 0 && this.getY() < screenHeight){
            respawn(maxLev, mapX, mapY, mapWidth, mapHeight);
        }
        this.setVelocityX(0);
        this.setVelocityY(0);
        resetHealth();
        resetXP();
        resetAttack();
        resetSpeed();
    }
    public void respawn(float mapX, float mapY, int mapWidth, int mapHeight){
        // left bound 0, map.getWidth
        // left bound map
        this.setX(r.nextInt(mapWidth-500) + mapX+250);
        this.setY(r.nextInt(mapHeight-500) + mapY+250);
        
        if(this.getX() > 0 && this.getX() < screenWidth && this.getY() > 0 && this.getY() < screenHeight){
            respawn(mapX, mapY, mapWidth, mapHeight);
        }
        this.setVelocityX(0);
        this.setVelocityY(0);
    }
    
    public void setLevel(int l){
        level = l;
        resetHealth();
        resetXP();
        resetAttack();
        resetSpeed();
    }
    public void updateX(long timePassed, float vx){
        this.setX(this.getX() + vx*timePassed);
    }
    public void updateY(long timePassed, float vy){
        this.setY(this.getY() + vy*timePassed);
    }
    
    
    //sets the static variable width and height so every monster can access it
    public void setScreenWidth(int sw){
        screenWidth = sw;
    }
    public void setScreenHeight(int sh){
        screenHeight = sh;
    }
    
    public void setHealth(int i){
        health = i;
    }
    
    // used for respawn to reset all the stats
    private void resetHealth(){
        health = (int) (100 * Math.pow(1.25, level));
        maxHP = health;
    }
    private void resetAttack(){
        attack = (int) (2 * Math.pow(1.25, level));
    }
    private void resetXP(){
        xp = (int) (((r.nextInt(20)+1) + 10)*Math.pow(1.25, level));
    }
    private void resetSpeed(){
        speed = (float)(0.1* Math.pow(1.03, level));
    }
    public void setSpeed(float n){
        speed = n;
    }
    
    //getters
    public int getXP(){
        return xp;
    }
    public float getHealth(){
        return health;
    }
    public float getMaxHP(){
        return maxHP;
    }
    public int getAttack(){
        return attack;
    }
    public int getLevel(){
        return level;
    }
    public float getSpeed(){
        return speed;
    }
    public int getDistance(){
        return distance;
    }
    
    //takes dmg
    public void takeDmg(int dmg){
        health -= dmg;
    }
    public void setHealth(float h){
        health = h;
    }
    //calculates distance from mc
    public void calcDistance(float mcX, float mcY){
        distance =(int) (Math.sqrt(Math.pow(Math.abs(mcX-this.getX()),2) + Math.pow(Math.abs(mcY-this.getY()), 2)));
    }
}
