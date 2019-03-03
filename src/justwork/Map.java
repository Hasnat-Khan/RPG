/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package justwork;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * Map. Basically just a background
 * Contains the map’s image and coordinates. Also updates x and y of the map for movement.
 */
public class Map{
    
    private Image img;
    private float x, y;
    
    public Map(String path){
        img = new ImageIcon(path).getImage();
    }

    Map(Image maps) {
        img = maps;
    }
    
    //updates x and y
    public void updateX(long timePassed, float vx){
        x += vx*timePassed;
    }
    public void updateY(long timePassed, float vy){
        y = vy*timePassed;
    }
    
    //setters
    public void setImage(Image i){
        img = i;
    }
    public void setX(float ex){
        x = ex;
    }
    public void setY(float ey){
        y = ey;
    }
    
    //getters
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
    public int getWidth(){
        return img.getWidth(null);
    }
    public int getHeight(){
        return img.getHeight(null);
    }
    public Image getImage(){
        return img;
    }
    
}
