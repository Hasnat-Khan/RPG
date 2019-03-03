/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package justwork;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 * Main purpose is to stop flickering of the screen. Also makes it fullscreen(almost) and restores the screen after the game is done.
 * Taken from a youtube channel TheNewBoston
 * @author hasna
 */
public class ScreenManager {
    
    private GraphicsDevice vc;
    
    //sets ups the graphic device using the graphics environment
    public ScreenManager(){
        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        vc = e.getDefaultScreenDevice();
    }
    
    //gets all the displaymodes compatible with the monitor
    public DisplayMode[] getCompatibleDisplayModes(){
        return vc.getDisplayModes();
    }
    
    //compares the displaymodes compatible with the monitor to displaymodes 
    //compatible with the game and chooses the first compatible one
    //the best displaymodes are first when initializing dm so that the first compatible one is the best one it can handle
    public DisplayMode findFirstCompatibleMode(DisplayMode modes[]){
        DisplayMode goodModes[] = vc.getDisplayModes();
        for(int x = 0; x > modes.length; x++){
            for(int y = 0; y > goodModes.length; y++){
                if(dmMatch(modes[x],goodModes[y])){
                    return modes[x];
                }
            }
        }
        return null;
    }
    
    //checks if two modes match by combaring the width, height, bitdepth and refresh rate
    public boolean dmMatch(DisplayMode m1, DisplayMode m2){
        if(m1.getWidth() != m2.getWidth() || m1.getHeight() != m2.getHeight()){
            return false;
        }
        if(m1.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI && m2.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI && m1.getBitDepth() != m2.getBitDepth()){
            return false;
        }
        if(m1.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN && m2.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN && m1.getRefreshRate() != m2.getRefreshRate()){
            return false;
        }
        
        return true;
    }
    
    //get current dm
    public DisplayMode getCurrentDisplayMode(){
        return vc.getDisplayMode();
    }
    
    //make frame full screen and does double buffering
    public void setFullScreen(DisplayMode dm){
        JFrame f= new JFrame();
        f.setUndecorated(false);
        f.setIgnoreRepaint(true);
        f.setResizable(false);
        f.setDefaultCloseOperation(EXIT_ON_CLOSE);
        vc.setFullScreenWindow(f);
        
        if(dm != null && vc.isDisplayChangeSupported()){
            try{
                vc.setDisplayMode(dm);
            }catch(Exception ex){}
        }
        f.createBufferStrategy(2);
        
    }
    public void setDisplayMode(DisplayMode dm){
        vc.setDisplayMode(dm);
    }
    // sets equal to this
    public Graphics2D getGraphics(){
        Window w = vc.getFullScreenWindow();
        if(w != null){
            BufferStrategy s = w.getBufferStrategy();
            return (Graphics2D)s.getDrawGraphics();
        }else{
            return null;
        }
    }
    
    //updates display
    public void update(){
        Window w = vc.getFullScreenWindow();
        if(w != null){
            BufferStrategy s = w.getBufferStrategy();
            if(!s.contentsLost()){
                s.show();
            }
        }
    }
    // personal method to get the animation of the sprite while keeping it in one file
    public Image getSubImage(String path, int col, int row, int size){
        try {
            BufferedImage img = ImageIO.read(getClass().getResource(path));
            BufferedImage subimage = img.getSubimage((col-1)*size,(row-1)*size , size, size);
            return subimage;
        } catch (IOException ex) {
            Logger.getLogger(ScreenManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    // get fs window
    public Window getFullScreenWindow(){
        return vc.getFullScreenWindow();
    }
    
    //returns the width
    public int getWidth(){
        Window w = vc.getFullScreenWindow();
        if(w != null){
            return w.getWidth();
        }else{
            return 0;
        }
    }
    //returns height
    public int getHeight(){
        Window w = vc.getFullScreenWindow();
        if(w != null){
            return w.getHeight();
        }else{
            return 0;
        }
    }
    
    //restores the screen to normal
    public void restoreScreen(){
        Window w = vc.getFullScreenWindow();
        if(w != null){
            w.dispose();
        }
        vc.setFullScreenWindow(null);
    }
    
    //creates image compatible w/ monitor
    public BufferedImage createCompImage(int w, int h, int t){
        Window win = vc. getFullScreenWindow();
        if(win != null){
            GraphicsConfiguration gc = win.getGraphicsConfiguration();
            return gc.createCompatibleImage(w, h, t);
        }
        return null;
    }
}
