/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package justwork;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;


/**
 *
 * @author hasna
 */
public class GameOver {
    
    Button done, restart;
    boolean visible;
    
    public GameOver(){
        done = new Button("Quit", 300, 500, 200, 100);
        restart = new Button("Restart", 800, 500, 200, 100);
    }
    
    public void draw(Graphics2D g){
        g.setFont(new Font("Black", Font.PLAIN, 24));
        done.draw(g);
        restart.draw(g);
        
    }
    public Button getDone(){
        return done;
    }
    public Button getRestart(){
        return restart;
    }

    public void setVisible(boolean b) {
        visible = b;
    }
    public boolean isVisible(){
        return visible;
    }
    
}
