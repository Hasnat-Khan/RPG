/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package justwork;

import java.awt.Image;
import java.awt.Point;

/**
 * Contains x, y, vx, and vy, and image/animation. Used by almost all of the classes for movement.
 * @author hasna
 */
public class Sprite {
    
    //animation being used
    private Animation a;
    private String name;
    //x y coordinates of the sprite
    private float x, y;
    // velocity x and y of sprite(speed and direction it moves)
    private float vx, vy;
    private String dialogue1;
    private String dialogue2;
    private String dialogue3;
    private String dialogue4;
    private boolean saved;
    private String dialogue5;
    private float health;
    // constructor
    public Sprite(Animation anim){
        a = anim;
    }
    
    //updates animation
    public void update(long timePassed){
        a.update(timePassed);
    }
    public void updateX(long timePassed){
        x += vx*timePassed;
    }
    public void updateY(long timePassed){
        y += vy*timePassed;
    }
    
    //setter
    public void setX(float ex){
        x = ex;
    }
    public void setY(float ey){
        y = ey;
    }
    public void setAnimation(Animation anim){
        a = anim;
    }
    public void setVelocityX(float ex){
        vx = ex;
    }
    public void setVelocityY(float ey){
        vy = ey;
    }
    
    //getters
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
    public float getVelocityX(){
        return vx;
    }
    public float getVelocityY(){
        return vy;
    }
    public Image getImage(){
        return a.getImage();
    }
    public int getWidth(){
        return a.getImage().getWidth(null);
    }
    public int getHeight(){
        return a.getImage().getHeight(null);
    }

    
    public void setSaved(boolean b) {
        saved = b;
    }
    public void setName(String s){
        name = s;
    }
    public void setDialogue1(String s){
        dialogue1 = s;
    }
    public void setDialogue2(String s){
        dialogue2 = s;
    }
    public void setDialogue3(String s){
        dialogue3 = s;
    }
    public void setDialogue4(String s){
        dialogue4 = s;
    }
    public void setDialogue5(String s){
        dialogue5 = s;
    }
    
    public boolean isSaved(){
        return saved;
    }
    public String getName(){
        return name;
    }
    public String dialogue1(){
        return dialogue1;
    }
    public String dialogue2(){
        return dialogue2;
    }
    public String dialogue3(){
        return dialogue3;
    }
    public String dialogue4(){
        return dialogue4;
    }
    public String dialogue5(){
        return dialogue5;
    }

    float getHealth() {
        return 0;
    }
    
    public void setHealth(float h){
        health = h;
    }
    
    
    //methods for the final boss
    
    //calculates distance from mc
    public int getDistance(){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public void calcDistance(float mcX, float mcY){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    int getAttack() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    void respawn(float x, float y, int width, int height) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void updateX(long timePassed, float f) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void updateY(long timePassed, float f) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void setScreenWidth(int width) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void setScreenHeight(int height) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    float getSpeed() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void setLevel(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    float getMaxHP() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setSpeed(float f) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
