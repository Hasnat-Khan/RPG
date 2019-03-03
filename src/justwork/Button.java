/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package justwork;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 * Button class used multiple times for stat menu and upgrade buttons
 * This class is a button for the stat menu, game over menu, winner menu and stat button. 
 * It uses the x and y so that it can figure out if the mouse is within an area 
 * more easily, and so that the code can be reused.
 */
public class Button {
    private int x;
    private int y;
    private int width;
    private int height;
    private String buttonText;
    private boolean locked;
    Image img;
    
    //CONSTRUCTOR
    public Button(String text, int x , int y, int width, int height){
        buttonText = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        locked = false;
    }
    public void setImage(Image i){
        img = i;
    }
    public void setLocked(boolean b){
        locked = b;
    }
    
    public boolean isLocked(){
        return locked;
    }
    public Image getImage(){
        return img;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public boolean getLocked(){
        return locked;
    }
    
    // Draws text beside the button
    public void drawString(Graphics2D g, String text, int why){
        g.setColor(Color.WHITE);
        g.drawString(text, x+width+10, why + y);
    }
    
    //draws button and text above it
    public void draw(Graphics2D g){
        g.setColor(Color.WHITE);
        g.drawString(buttonText, x, y-7);
        if(img != null){
            g.drawImage(img, x, y, null);
        }else{
            g.setColor(Color.red);
            g.fillRect(x,y,width,height);
        }
    }
}
