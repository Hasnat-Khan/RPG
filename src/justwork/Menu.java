/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package justwork;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Menu. There is only one object of this class. Is a menu with buttons to increase
 * stats using stat points
 * 
 *  Stat menu holds 8 buttons in it. Each button has its own image
 * with four of them locked in the beginning. You can use stat points to increase
 * your health with the stat menu. Using a key can unlock 1 of the 4 locked stats
 * and will change the image of it.
 */
public class Menu {
    private boolean visible;
    private int x;
    private int y;
    private int width;
    private int height;
    
    // buttons for stats
    private Button plusHealth;
    private Button plusMana;
    private Button plusAttack;
    private Button plusSpeed;
    
    private Button plusMRegen;
    private Button plusHRegen;
    private Button plusAttack2;
    private Button plusSpecial;
    // gets character class so it can use that information
    // uses pass by reference so that it can get the most current information
    // with only one initialization
    private Character mc;
    
    
    //CONSTRUCTOR
    public Menu(int x, int y, Character mc) throws IOException{
        visible = false;
        this.x = x;
        this.y = y;
        this.mc = mc;
        
        width = 750;
        height = 400;
        int h = 90;
        int xs = 400;
        y += 10;
        
        plusHealth = new Button("Health",x+15,y+35, 60,60);
        Image img;
        img = ImageIO.read(getClass().getResource("/images/Heart.png"));
        plusHealth.setImage(img);
        
        plusMana = new Button("Mana",x+15,y+h+35, 60,60);
        //img = new ImageIcon("src\\images\\Mana.png").getImage();
        img = ImageIO.read(getClass().getResource("/images/Mana.png"));
        plusMana.setImage(img);
        
        plusAttack = new Button("Attack",x+15,y+2*h+35, 60,60);
        //img = new ImageIcon("src\\images\\Attack2.png").getImage();
        img = ImageIO.read(getClass().getResource("/images/Attack2.png"));
        plusAttack.setImage(img);
        
        plusSpeed = new Button("Speed",x+15,y+3*h+35, 60,60);
        //img = new ImageIcon("src\\images\\Speed.png").getImage();
        img = ImageIO.read(getClass().getResource("/images/Speed.png"));
        plusSpeed.setImage(img);
        
        plusMRegen = new Button("Mana Regen", x + xs, y+35, 60, 60);
        //img = new ImageIcon("src\\images\\Locked.png").getImage();
        img = ImageIO.read(getClass().getResource("/images/Locked.png"));
        plusMRegen.setImage(img);
        plusMRegen.setLocked(true);
        
        plusHRegen = new Button("Health Regen", x + xs, y+h+35, 60, 60);
        //img = new ImageIcon("src\\images\\Locked.png").getImage();
        plusHRegen.setImage(img);
        plusHRegen.setLocked(true);
        
        plusAttack2 = new Button("Attack Increase", x + xs, y+2*h+35, 60, 60);
        //img = new ImageIcon("src\\images\\Locked.png").getImage();
        plusAttack2.setImage(img);
        plusAttack2.setLocked(true);
        
        plusSpecial = new Button("Special Attack Chance", x + xs, y+3*h + 35, 60,60);
        //img = new ImageIcon("src\\images\\Locked.png").getImage();
        plusSpecial.setImage(img);
        plusSpecial.setLocked(true);
        y-=10;
    }
    
    
    //has its own draw method for a cleaner look
    public void draw(Graphics2D g){
        g.setColor(Color.BLACK);
        g.fillRoundRect(x, y, width, height, 15, 15);
        
        plusHealth.draw(g);
        plusHealth.drawString(g, "Health stat Boosts: "+ mc.healthUps, 10);
        plusHealth.drawString(g, "Costs 1 SP",30);
        plusHealth.drawString(g, "Increases health by "+ mc.hInc, 50);
        plusMana.draw(g);
        plusMana.drawString(g, "Mana stat Boosts: "+ mc.manaUps, 10);
        plusMana.drawString(g, "Costs 1 SP", 30);
        plusMana.drawString(g, "Increases Mana by "+ mc.mInc, 50);
        plusAttack.draw(g);
        plusAttack.drawString(g, "Attack stat Boosts: "+ mc.attackUps, 10);
        plusAttack.drawString(g, "Costs 1 SP", 30);
        plusAttack.drawString(g, "Increases attack by "+ mc.aInc, 50);
        plusSpeed.draw(g);
        plusSpeed.drawString(g, "Speed stat Boosts: "+ mc.speedUps, 10);
        plusSpeed.drawString(g, "Costs 1 SP", 30);
        plusSpeed.drawString(g,"Increases speed by 0.05", 50);
        
        
        plusMRegen.draw(g);
        plusMRegen.drawString(g, "Mana Regen is at: "+ mc.mRegen, 10);
        plusMRegen.drawString(g, "Costs 3 SP", 30);
        plusMRegen.drawString(g,"Increases mana regen by 1", 50);
        plusHRegen.draw(g);
        plusHRegen.drawString(g, "Health Regen is at: "+ mc.hRegen, 10);
        plusHRegen.drawString(g, "Costs 3 SP", 30);
        plusHRegen.drawString(g,"Increases health regen by 1", 50);
        plusAttack2.draw(g);
        plusAttack2.drawString(g, "Attack Boost is at: "+ (mc.aInc - 10), 10);
        plusAttack2.drawString(g, "Costs 3 SP", 30);
        plusAttack2.drawString(g,"Increases attack stat increase by 1", 50);
        plusSpecial.draw(g);
        plusSpecial.drawString(g, "Special Chance Range: "+ mc.getCRange()*2, 10);
        plusSpecial.drawString(g, "Costs 3 SP", 30);
        plusSpecial.drawString(g,"Increases chance of special attack by 20ms", 50);
        
        g.drawString("SP: "+mc.getSP(), x+width/2- 50, y+15);
        g.drawString("Keys: "+mc.getKeys(), x+width/2 , y+15);
    }
    
    
    //getters
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
    public Button getHPButton(){
        return plusHealth;
    }
    public Button getMPButton(){
        return plusMana;
    }
    public Button getSpeedButton(){
        return plusSpeed;
    }
    public Button getAttackButton(){
        return plusAttack;
    }
    public Button getManaRegenButton(){
        return plusMRegen;
    }
    public Button getHealthRegenButton(){
        return plusHRegen;
    }
    public Button getAttackBonusButton(){
        return plusAttack2;
    }
    public Button getSpecialButton(){
        return plusSpecial;
    }
    public boolean isVisible(){
        return visible;
    }
    
    //setters
    public void setVisible(boolean b){
        visible = b;
    }
    
}
