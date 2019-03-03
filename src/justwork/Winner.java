/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package justwork;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author hasna
 */
public class Winner {
     Button done, restart;
    boolean visible;
    
    public Winner(){
        done = new Button("Quit", 200, 500, 200, 100);
        restart = new Button("Restart", 500, 500, 200, 100);
    }
    
    public void draw(Graphics2D g){
        g.setColor(Color.BLACK);
        g.fillRect(-10, -10, 1400,1000);
        done.draw(g);
        restart.draw(g);
        
    }

    public void setVisible(boolean b) {
        visible = b;
    }
    public boolean isVisible(){
        return visible;
    }
}
