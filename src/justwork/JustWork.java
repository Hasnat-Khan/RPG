/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package justwork;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Main class runs everything, draws, and also has key and mouse listener
 * Main class that holds all the calculations, dialogue, drawing, key implementations 
 * and mouse implementations. It is very unbalanced in terms of how much content there is.
 */
public class JustWork implements KeyListener, MouseListener{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        while(true){
            new JustWork().run();
        }
    }
    
    //mc stands for main character, easy to remember and short to write
    private Character mc;
    // final boss is a monster, but is still a princess so it uses polymorphism
    // so that both princess class and monster class can be used
    private Sprite princess;
    private Animation up, down, left, right;
    private Map map;
    int kills, guardKills;
    int attackNumber, specialNumber;
    String dialogue = "";
    int dialogueNumber = 0;
    int mapNumber = 1;
    int iNumber = 0;
    String instruction;
    
    private GameOver gameOver;
    private boolean win;
    private Button statButton;
    
    Menu statMenu;
    
    int defX, defY;
    
    Font text, bold;
    long hpTime, mpTime;
    private Monster[] monsters;
    private Monster[] guards;
    long attCool, attTime;
    private Particles[] attacks;
    private Particles[] special;
    
    // the display modes the game is capable of running on
    // it was annoying to switch between the 2 modes so I just disabled the other
    // display mode
    private static DisplayMode modes[] ={
        /*new DisplayMode(800, 600, 32, 0),
        new DisplayMode(800, 600, 24, 0),
        new DisplayMode(800, 600, 16, 0),*/
        new DisplayMode(640, 480, 32, 0),
        new DisplayMode(640, 480, 24, 0),
        new DisplayMode(640, 480, 16, 0),  
    };
    boolean running;
    protected ScreenManager s;
    public long timePassed;
   //initialization, runs only once
    // creates all the monsters, particles, bg 
    public void init(){
        s = new ScreenManager();
        DisplayMode dm = s.findFirstCompatibleMode(modes);
        s.setFullScreen(dm);
        gameOver = new GameOver();
        gameOver.setVisible(false);
        Window w = s.getFullScreenWindow();
        w.setFont(new Font("Arial", Font.PLAIN,24));
        w.setBackground(Color.GREEN);
        w.setForeground(Color.WHITE);
        Image maps = null;
        
        up = new Animation();
        down = new Animation();
        left = new Animation();
        right = new Animation();
        try {
            maps = ImageIO.read(getClass().getResource("/images/mapgrass.png"));
                    } catch (IOException ex) {
            Logger.getLogger(JustWork.class.getName()).log(Level.SEVERE, null, ex);
        }
        win = false;
        text = new Font("Arial", Font.PLAIN, 24);
        bold = new Font("Arial", Font.BOLD, 24);
        map = new Map(maps);
        map.setX(-map.getWidth()/2);
        map.setY(-map.getHeight()/2);



        monsters = new Monster[12];
        guards = new Monster[8];

        String path = "/images/spritesheet1.png";

        setup(path, up, 96, 4, 4);
        setup(path, down, 96, 4, 1);
        setup(path, left, 96, 4, 2);
        setup(path, right, 96, 4, 3);
//        setup(path, up, 48, 4, 4);
//        setup(path, down, 48, 4, 1);
//        setup(path, left, 48, 4, 2);
//        setup(path, right, 48, 4, 3);
        w.addKeyListener(this);
        w.addMouseListener(this);

        mc = new Character(down);
        defX = s.getWidth()/2;
        defY = s.getHeight()/2;
        //sets the mc's coordinates to the middle of the screen
        mc.setX(defX);
        mc.setY(defY);


        Animation mon = new Animation();
        Animation p = new Animation();
        Animation g = new Animation();
        Animation a = new Animation();
        Animation b = new Animation();
        try {
            mon.addScene(ImageIO.read(getClass().getResource("/images/monster1.png")),100);
            p.addScene(ImageIO.read(getClass().getResource("/images/princess1.png")),100);
            g.addScene(ImageIO.read(getClass().getResource("/images/guard1.png")),100);
            a.addScene(ImageIO.read(getClass().getResource("/images/attack.png")), 100);
            b.addScene(ImageIO.read(getClass().getResource("/images/specialAttack.png")), 100);

            //menu
            statMenu = new Menu(100,100, mc);
            statButton = new Button("",s.getWidth()-100, s.getHeight()-75, 60,60);
            statButton.setImage(ImageIO.read(getClass().getResource("/images/SP.png")));


                    } catch (IOException ex) {
            Logger.getLogger(JustWork.class.getName()).log(Level.SEVERE, null, ex);
        }
//        Image mons = new ImageIcon("src\\images\\monster1.png").getImage();
//        mon.addScene(mons, 200);


        //Princess
//        p.addScene(new ImageIcon("src\\images\\princess1.png").getImage(), 100);

        princess = new Princess(p);
        princess.setScreenWidth(s.getWidth());
        princess.setScreenHeight(s.getHeight());
        princess.respawn(map.getX(), map.getY(), map.getWidth(), map.getHeight());
        princess.setName("Kri");
        princess.setDialogue1("Thank you for saving me from those evil monsters.");
        princess.setDialogue2("My name is " + princess.getName()+", I hope you can help me with something.");
        princess.setDialogue3("Could you save my sisters from the monsters?");
        princess.setDialogue4("Perfect! I'll give you this key to help you get stronger.");
        princess.setDialogue5("Just walk to the edge of the map when you're ready save my sisters.");

        //monsters
        for(int i = 0; i < monsters.length; i++){
            monsters[i] = new Monster(mon);
            monsters[i].setScreenHeight(s.getHeight());
            monsters[i].setScreenWidth(s.getWidth());
            monsters[i].respawn(0, map.getX(), map.getY(), map.getWidth(), map.getHeight());
        }


        //guards
        for(int i = 0; i < guards.length; i++){
            guards[i] = new Monster(g);
            guards[i].setX((float) (Math.cos(Math.PI/4 * i)*150) + princess.getX());
            guards[i].setY((float) (Math.sin(Math.PI/4 * i)*150) + princess.getY());
            guards[i].setLevel(10);
        }

        kills = 0;
        guardKills = 0;

        // particles
        attacks = new Particles[10];
        for(int i = 0; i < 10; i++){
            attacks[i] = new Particles(a);
        }
        special = new Particles[10];
        for(int i = 0; i < 10; i++){
            special[i] = new Particles(b);
        }
        attacks[1].setDefaultCoord(-map.getWidth()-attacks[1].getWidth(), -map.getHeight()-attacks[1].getHeight());
        special[1].setDefaultCoord(-map.getWidth()-special[1].getWidth(), -map.getHeight()-special[1].getHeight());
        attackNumber = 0;

        instruction = "Use WASD to move/arrow keys";
    }

    //reinitialization, if the move on to the next map
    public void reInit(){
        switch(mapNumber){
            case 2:
                dialogueNumber = 0;
                //map.setImage(new ImageIcon("src\\images\\CAT.jpg").getImage());
                map.setX(-map.getWidth()/2);
                map.setY(-map.getHeight()/2);

                //sets the mc's coordinates to the middle of the screen
                mc.setX(defX);
                mc.setY(defY);
                princess.respawn(map.getX(), map.getY(), map.getWidth(), map.getHeight());
                princess.setName("Shna");
                princess.setDialogue1("Thank you for saving me, do you want a kiss?");
                princess.setDialogue2("Just kidding, I don't kiss strangers.");
                princess.setDialogue3("My name is Shna, now that we're not strangers, do you want a kiss?");
                princess.setDialogue4("I can give you a kiss on one condition: save my sisters.");
                princess.setDialogue5("I'll help you out by giving you a key.");
                princess.setSaved(false);
                Animation g = new Animation();
                Animation m = new Animation();
        {
            try {
                g.addScene(ImageIO.read(getClass().getResource("/images/guard2.png")),100);
                m.addScene(ImageIO.read(getClass().getResource("/images/monster2.png")),100);

            } catch (IOException ex) {
                Logger.getLogger(JustWork.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

                for(int i = 0; i<monsters.length; i++){
                    monsters[i].setAnimation(m);
                    monsters[i].respawn(10, map.getX(), map.getY(), map.getWidth(), map.getHeight());
                }
                for(int i = 0; i < guards.length; i++){
                    guards[i].setAnimation(g);
                    guards[i].setX((float) (Math.cos(Math.PI/4 * i)*150) + princess.getX());
                    guards[i].setY((float) (Math.sin(Math.PI/4 * i)*150) + princess.getY());
                    guards[i].setVelocityX(0);
                    guards[i].setVelocityY(0);
                    guards[i].setLevel(20);
                }
                break;
            case 3:
                dialogueNumber = 0;
                dialogue = "";
                //map.setImage(new ImageIcon("src\\images\\CAT.jpg").getImage());
                map.setX(-map.getWidth()/2);
                map.setY(-map.getHeight()/2);

                //sets the mc's coordinates to the middle of the screen
                mc.setX(defX);
                mc.setY(defY);
                princess.respawn(map.getX(), map.getY(), map.getWidth(), map.getHeight());
                princess.setName("Par");
                princess.setDialogue1("I didn't ask to be saved, but thanks, I guess.");
                princess.setDialogue2("Oh, my sisters sent you? Well I guess you're forgiven");
                princess.setDialogue3("My name is Par, by the way. Whats your name?");
                princess.setDialogue4("You just want the key? Fine, fine.");
                princess.setDialogue5("I'll give you the key but I have 2 more sisters that need saving.");
                princess.setSaved(false);
                Animation g2 = new Animation();
                Animation m2 = new Animation();
        {
            try {
                g2.addScene(ImageIO.read(getClass().getResource("/images/guard3.png")),100);
                m2.addScene(ImageIO.read(getClass().getResource("/images/monster3.png")),100);

            } catch (IOException ex) {
                Logger.getLogger(JustWork.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
                for(int i = 0; i<monsters.length; i++){
                    monsters[i].setAnimation(m2);
                    monsters[i].respawn(20, map.getX(), map.getY(), map.getWidth(), map.getHeight());
                }
                for(int i = 0; i < guards.length; i++){
                    guards[i].setAnimation(g2);
                    guards[i].setX((float) (Math.cos(Math.PI/4 * i)*150) + princess.getX());
                    guards[i].setY((float) (Math.sin(Math.PI/4 * i)*150) + princess.getY());
                    guards[i].setVelocityX(0);
                    guards[i].setVelocityY(0);
                    guards[i].setLevel(30);
                }

                break;

            case 4:
                dialogueNumber = 0;
                map.setImage(new ImageIcon("src\\images\\CAT.jpg").getImage());
                map.setX(-map.getWidth()/2);
                map.setY(-map.getHeight()/2);

                //sets the mc's coordinates to the middle of the screen
                mc.setX(defX);
                mc.setY(defY);
                princess.respawn(map.getX(), map.getY(), map.getWidth(), map.getHeight());
                princess.setName("Mar");
                princess.setDialogue1("Who the hell are you?");
                princess.setDialogue2("Oh, so my sisters sent you. But how'd you know I was their sister?");
                princess.setDialogue3("What do you mean we all look the same! RUDE!");
                princess.setDialogue4("Even though you're very rude, I have a request for you.");
                princess.setDialogue5("Please rescue my eldest sister from the dragon!");
                princess.setSaved(false);
                Animation g3 = new Animation();
                Animation m3 = new Animation();
        {
            try {
                g3.addScene(ImageIO.read(getClass().getResource("/images/guard4.png")),100);
                m3.addScene(ImageIO.read(getClass().getResource("/images/monster4.png")),100);

            } catch (IOException ex) {
                Logger.getLogger(JustWork.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
                for(int i = 0; i<monsters.length; i++){
                    monsters[i].setAnimation(m3);
                    monsters[i].respawn(30, map.getX(), map.getY(), map.getWidth(), map.getHeight());
                }
                for(int i = 0; i < guards.length; i++){
                    guards[i].setAnimation(g3);
                    guards[i].setX((float) (Math.cos(Math.PI/4 * i)*150) + princess.getX());
                    guards[i].setY((float) (Math.sin(Math.PI/4 * i)*150) + princess.getY());
                    guards[i].setVelocityX(0);
                    guards[i].setVelocityY(0);
                    guards[i].setLevel(40);
                }

                break;
            case 5:
                dialogueNumber = 0;
                map.setImage(new ImageIcon("src\\images\\CAT.jpg").getImage());
                map.setX(-map.getWidth()/2);
                map.setY(-map.getHeight()/2);

                //sets the mc's coordinates to the middle of the screen
                mc.setX(defX);
                mc.setY(defY);
                Animation p = new Animation();
                //aAnimation g4 = new Animation();
                Animation m4 = new Animation();
        {
            try {
                p.addScene(ImageIO.read(getClass().getResource("/images/dragon.png")), hpTime);
                //g4.addScene(ImageIO.read(getClass().getResource("/images/guard5.png")),100);
                m4.addScene(ImageIO.read(getClass().getResource("/images/monster7.png")),100);

            } catch (IOException ex) {
                Logger.getLogger(JustWork.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
                princess = new Monster(p);
                princess.respawn(map.getX(), map.getY(), map.getWidth(), map.getHeight());
                princess.setName("???");
                princess.setDialogue1("Curse you, you evil being.");
                princess.setDialogue2("I didn't think you would be so strong.");
                princess.setDialogue3("I can feel the last vestiges of my health slipping away.");
                princess.setDialogue4("Sike. You didn't really think it would be that easy did you?");
                princess.setDialogue5("No this, this is my true form.");
                princess.setSaved(false);
                princess.setLevel(50);
                princess.setSpeed(0.2f);
//                g4.addScene(new ImageIcon("src\\images\\monster7.png").getImage(), 100);
//                m4.addScene(new ImageIcon("src\\images\\.png").getImage(), 100);

                for(int i = 0; i<monsters.length; i++){
                    monsters[i].setAnimation(m4);
                    monsters[i].setX((float) (Math.cos(Math.PI/monsters.length/2 * i)*600) + princess.getX());
                    monsters[i].setY((float) (Math.sin(Math.PI/monsters.length/2 * i)*600) + princess.getY());
                    monsters[i].setVelocityX(0);
                    monsters[i].setVelocityY(0);
                    monsters[i].setLevel(45);
                }
                break;
            case 6:
                dialogueNumber = 0;
                Animation l = new Animation();
        {
            try {
                l.addScene(ImageIO.read(getClass().getResource("/images/dragon3.png")),100);

            } catch (IOException ex) {
                Logger.getLogger(JustWork.class.getName()).log(Level.SEVERE, null, ex);
            }
        }princess.setAnimation(l);
                princess.setName("???");
                princess.setDialogue1("Agh, curses. I'm still not strong enough.");
                princess.setDialogue2("I need more power... more... mooooore.");
                princess.setDialogue3("What? What is this? I feel power surging throuhg my veins.");
                princess.setDialogue4("Haha puny human, now you will feel my strength!");
                princess.setDialogue5("");
                princess.setSaved(false);
                princess.setLevel(55);
                break;
            case 7:
                dialogueNumber = 0;
                Animation f = new Animation();
        {
            try {
                f.addScene(ImageIO.read(getClass().getResource("/images/princess1.png")),100);
            } catch (IOException ex) {
                Logger.getLogger(JustWork.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
                princess.setAnimation(f);
                princess.setName("ILY");
                princess.setDialogue1("Just kidding, I'm back to normal");
                princess.setDialogue2("Thank you for saving me from that dreadful curse.");
                princess.setDialogue3("To me it felt like I was just protecting my sisters, but I guess I just caged them instead.");
                princess.setDialogue4("I thank you on behalf of the kingdom for saving us.");
                princess.setDialogue5("Sorry, I forgot to introduce myself. My name is Ily, the youngest princess of Evilor.");
                princess.setSaved(true);

        }

    }

    //method that is run first, does initialization once, and then loops the game
    // and finally restores the screen
    public void run(){
            running = true;
            try{
                init();
                gameLoop();
            }finally{
                s.restoreScreen();
            }
        }

    //loops the game
    public void gameLoop(){
        // gets the timestarted so that it can keep track of all the time passed
        // time passed is used by animation, character movement, cooldowns and monster movement
        long startTime = System.currentTimeMillis();
        long cumTime = startTime;

        while(running){
            timePassed = System.currentTimeMillis() - cumTime;
            cumTime += timePassed;

            update(timePassed);

            Graphics2D g = s.getGraphics();
            draw(g);
            g.dispose();
            s.update();

            // thread sleeps for 20 ms
            try{
                Thread.sleep(20);
            }catch(Exception ex){}
        }
    }

    //stop method sets running to false, so next iteratino it will not loop
    public void stop(){
        running = false;
    }

    //sets up animation for the character
    private Animation setup(String path, Animation a, int size, int frames, int row){
        for(int i = 1; i <= frames; i++){
            a.addScene(s.getSubImage(path, i, row, size),100);
        }
        return a;
    }

    // updates all the coordinates every loop
    // has almost all of the game logic
    public void update(long timePassed){
        //sets the mcs coordinates for the particles, only needed for once because it is a
        // static variable
        attacks[1].setMC(mc.getX(), mc.getY());

        //when map moves everything moves except mc
        if(mapMovableX()){
            map.updateX(timePassed,-mc.getVelocityX());
            for(int i = 0; i<monsters.length;i++){
                monsters[i].updateX(timePassed, -mc.getVelocityX());
            }
            for(int i = 0; i < guards.length; i++){
                guards[i].updateX(timePassed, -mc.getVelocityX());
            }
            for(int i = 0; i<attacks.length;i++){
                attacks[i].updateX(timePassed, -mc.getVelocityX());
            }
            princess.updateX(timePassed,-mc.getVelocityX());
        }else{
            mc.updateX(timePassed);
        }
        if(mapMovableY()){
            map.setY(map.getY()+ (timePassed * -mc.getVelocityY()));

            for(int i = 0; i<monsters.length; i++){
                monsters[i].updateY(timePassed, -mc.getVelocityY());
            }
            for(int i = 0; i < guards.length; i++){
                guards[i].updateY(timePassed, -mc.getVelocityY());
            }
            for(int i = 0; i<attacks.length;i++){
                attacks[i].updateY(timePassed, -mc.getVelocityY());
            }
            princess.updateY(timePassed,-mc.getVelocityY());
        }else{
            mc.updateY(timePassed);
        }


        // all the monster calculations
        for(int i=0;i<monsters.length;i++){
            monsters[i].calcDistance(mc.getX(), mc.getY());

            // if the monster is touching the mc then he takes damage
            if( mc.getX()+mc.getWidth() > monsters[i].getX()+10 && mc.getX() < monsters[i].getX()+monsters[i].getWidth()-10 &&
                mc.getY()+mc.getHeight()> monsters[i].getY()+10 && mc.getY() < monsters[i].getY()+monsters[i].getHeight()-10){
                mc.takeDmg(monsters[i].getAttack());
            }

            //if the attacks is touching the monster the monster takes damage
            for(int j = 0; j < attacks.length; j++){
                attacks[j].calcRange();
                if( attacks[j].getX()+mc.getWidth() > monsters[i].getX() && attacks[j].getX() < monsters[i].getX()+monsters[i].getWidth() &&
                    attacks[j].getY()+attacks[j].getHeight()> monsters[i].getY() && attacks[j].getY() < monsters[i].getY()+monsters[i].getHeight()){

                    monsters[i].takeDmg(mc.getAttack());
                    attacks[j].resetCoord();
                    if(monsters[i].getDistance() < 600)chase(i, monsters);
                }else if(attacks[j].getRange()> 500){
                    attacks[j].resetCoord();
                }
            }
            //same thing for special attacks
            for(int j = 0; j < special.length; j++){
                special[j].calcRange();
                if( special[j].getX()+mc.getWidth() > monsters[i].getX() && special[j].getX() < monsters[i].getX()+monsters[i].getWidth() &&
                    special[j].getY()+special[j].getHeight()> monsters[i].getY() && special[j].getY() < monsters[i].getY()+monsters[i].getHeight()){

                    monsters[i].takeDmg(mc.getAttack());
                    if(monsters[i].getDistance() < 600)chase(i,monsters);
                }else if(special[j].getRange()> 1000){
                    special[j].resetCoord();
                }
            }

            // if the monster dies, kills goes up and it respawns the monster
            if(monsters[i].getHealth() < 0){
                kills += 1;
                mc.gainXP(monsters[i].getXP());
                monsters[i].respawn(kills/4, map.getX(), map.getY(), map.getWidth(), map.getHeight());
                monsters[i].setVelocityX(0);
                monsters[i].setVelocityY(0);
                if(iNumber == 3){
                    instruction = "Click the bottom right for stat upgrades";
                    iNumber++;
                }
            }
            monsters[i].calcDistance(mc.getX(), mc.getY());
            // if the monsters get within the mc's range it will chase the mc
            if(monsters[i].getDistance() < 500){
                chase(i, monsters);
            }else{
                monsters[i].setVelocityX(0);
                monsters[i].setVelocityY(0);
            }

            // updates the monsters x and y coordinates
            monsters[i].updateX(timePassed);
            monsters[i].updateY(timePassed);

        }
        // all the guards calculations ( almost the exact same as the monsters calculations)
        if(mapNumber < 5){
        for(int i=0;i<guards.length;i++){
            guards[i].calcDistance(mc.getX(), mc.getY());

            // if the monster is touching the mc then he takes damage
            if( mc.getX()+mc.getWidth() > guards[i].getX()+10 && mc.getX() < guards[i].getX()+guards[i].getWidth()-10 &&
                mc.getY()+mc.getHeight()> guards[i].getY()+10 && mc.getY() < guards[i].getY()+guards[i].getHeight()-10){
                mc.takeDmg(guards[i].getAttack());
            }

            //if the attacks is touching the monster the monster takes damage
            for(int j = 0; j < attacks.length; j++){
                attacks[j].calcRange();
                if( attacks[j].getX()+mc.getWidth() > guards[i].getX() && attacks[j].getX() < guards[i].getX()+guards[i].getWidth() &&
                    attacks[j].getY()+attacks[j].getHeight()> guards[i].getY() && attacks[j].getY() < guards[i].getY()+guards[i].getHeight()){

                    guards[i].takeDmg(mc.getAttack());
                    attacks[j].resetCoord();
                    if(guards[i].getDistance() < 650)chase(i, guards);
                }else if(attacks[j].getRange()> 500){
                    attacks[j].resetCoord();
                }
            }
            //same thing for special attacks
            for(int j = 0; j < special.length; j++){
                special[j].calcRange();
                if( special[j].getX()+mc.getWidth() > guards[i].getX() && special[j].getX() < guards[i].getX()+guards[i].getWidth() &&
                    special[j].getY()+special[j].getHeight()> guards[i].getY() && special[j].getY() < guards[i].getY()+guards[i].getHeight()){

                    guards[i].takeDmg(mc.getAttack());
                    if(guards[i].getDistance() < 650)chase(i, guards);
                }else if(special[j].getRange()> 1000){
                    special[j].resetCoord();
                }
            }

            // if the guard dies, it does NOT respawn and guardkills go up
            if(guards[i].getHealth() < 0){
                kills += 1;
                guardKills += 1;
                if(mc.level > guards[i].getLevel()){
                    mc.gainXP((float) (guards[i].getXP()*(0.75 * (mc.level-guards[i].getLevel()))));
                }else{
                    mc.gainXP(guards[i].getX());
                }
                guards[i].setHealth(1);
                guards[i].setX(map.getWidth()*3);
                guards[i].setVelocityX(0);
                guards[i].setVelocityY(0);
            }
            guards[i].calcDistance(mc.getX(), mc.getY());
            // if the guards get within the mc's range it will chase the mc
            if(guards[i].getDistance() < 600){
                chase(i,guards);
            }else{
                guards[i].setVelocityX(0);
                guards[i].setVelocityY(0);
            }

            // updates the guards x and y coordinates
            guards[i].updateX(timePassed);
            guards[i].updateY(timePassed);
        }
        }
        //calculates the distance for all the attacks
        for(int j = 0; j < attacks.length; j++){
            attacks[j].calcDistance();
        }
        // increases time since last use for heal and mana
        mc.upTime(timePassed);
        //health regen
        if(mc.getHTime() > mc.getHPTime() && mc.getHealth() > 0){
            mc.heal(mc.hRegen);
            mc.setHTime(0);
        }
        if(mc.getHealth() <= 0){
            gameOver.setVisible(true);
        }
        // mana regen
        if(mc.getMTime() > mc.getManaTime()){
            mc.manaRegen(mc.mRegen);
            mc.setMTime(0);
        }

        //updates the x and y for the attacks
        for(int i = 0; i<attacks.length; i++){
            attacks[i].updateX(timePassed);
            attacks[i].updateY(timePassed);
        }
        //updates the x and y for the attacks
        for(int i = 0; i<special.length; i++){
            special[i].updateX(timePassed);
            special[i].updateY(timePassed);
        }

        //checks if you saved the princess
        if(guardKills == 8){
            princess.setSaved(true);
            guardKills = 0;
            statMenu.setVisible(false);
            mc.setKeys();
            dialogue = princess.dialogue1();
            dialogueNumber = 1;
            if(iNumber == 5){
                iNumber ++;
                instruction = "";
            }
        }
        switch(dialogueNumber){
            case 1: dialogue = princess.dialogue1(); break;
            case 2: dialogue = princess.dialogue2();break;
            case 3: dialogue = princess.dialogue3();break;
            case 4: dialogue = princess.dialogue4();break;
            case 5: dialogue = princess.dialogue5();break;
            default: dialogue = "";break;
        }
        if((mc.getX() > s.getWidth() || mc.getX() < 0 || mc.getY() > s.getHeight() || mc.getY() < 0) && princess.isSaved()){
            mapNumber++;
            reInit();
        }else if(princess.isSaved() && ((mapNumber == 5 && dialogueNumber == 6) || (mapNumber == 6 && dialogueNumber == 5))){
            mapNumber++;
            reInit();
        }
        if(princess.getHealth() != 1){
            princess.calcDistance(mc.getX(), mc.getY());

            // if the monster is touching the mc then he takes damage
            if( mc.getX()+mc.getWidth() > princess.getX()+10 && mc.getX() < princess.getX()+princess.getWidth()-10 &&
                mc.getY()+mc.getHeight()> princess.getY()+10 && mc.getY() < princess.getY()+princess.getHeight()-10){
                mc.takeDmg(princess.getAttack());
            }
            //if the attacks is touching the monster princess the monster takes damage
            for(int j = 0; j < attacks.length; j++){
                attacks[j].calcRange();
                if( attacks[j].getX()+mc.getWidth() > princess.getX() && attacks[j].getX() < princess.getX()+princess.getWidth() &&
                    attacks[j].getY()+attacks[j].getHeight()> princess.getY() && attacks[j].getY() < princess.getY()+princess.getHeight()){

                    princess.setHealth(princess.getHealth()- mc.getAttack());
                    attacks[j].resetCoord();
                    if(princess.getDistance() < 1000 && princess.getDistance() != 0 ){
                        princess.setVelocityX((float) ((int) (mc.getX()-princess.getX()) / (float)(princess.getDistance()/ princess.getSpeed())));
                        princess.setVelocityY((float) ((int) (mc.getY()-princess.getY()) / (float)(princess.getDistance()/ princess.getSpeed())));

                    }
                }else if(attacks[j].getRange()> 500){
                    attacks[j].resetCoord();
                }
            }
            //same thing for special attacks
            for(int j = 0; j < special.length; j++){
                special[j].calcRange();
                if( special[j].getX()+mc.getWidth() > princess.getX() && special[j].getX() < princess.getX()+princess.getWidth() &&
                    special[j].getY()+special[j].getHeight()> princess.getY() && special[j].getY() < princess.getY()+princess.getHeight()){

                    princess.setHealth(princess.getHealth()- mc.getAttack());
                    if(princess.getDistance() < 600){
                        if(princess.getDistance() !=0){
                            princess.setVelocityX((float) ((int) (mc.getX()-princess.getX()) / (float)(princess.getDistance()/ princess.getSpeed())));
                            princess.setVelocityY((float) ((int) (mc.getY()-princess.getY()) / (float)(princess.getDistance()/ princess.getSpeed())));
                        }
                    }
                }else if(special[j].getRange()> 500){
                    special[j].resetCoord();
                }
            }
            if(princess.getDistance() < 1000 && princess.getDistance() != 0 ){
                princess.setVelocityX((float) ((int) (mc.getX()-princess.getX()) / (float)(princess.getDistance()/ princess.getSpeed())));
                princess.setVelocityY((float) ((int) (mc.getY()-princess.getY()) / (float)(princess.getDistance()/ princess.getSpeed())));
            }else{
                princess.setVelocityX(0);
                princess.setVelocityY(0);
            }
            princess.updateX(timePassed);
            princess.updateY(timePassed);
            if(princess.getHealth() <= 0 && dialogueNumber == 0){
                princess.setSaved(true);
                guardKills = 0;
                statMenu.setVisible(false);
                mc.setKeys();
                dialogue = princess.dialogue1();
                dialogueNumber = 1;
            }

            if(princess.isSaved() && mapNumber == 7 && dialogue.isEmpty() && dialogueNumber >3){
                win = true;
                gameOver.setVisible(true);
            }
        }
    }

    public void chase(int i, Monster[] m){
        // sets the x and y speed of the monster
        // distance cannot be 0 because division by 0 cannot occur
        if(m[i].getDistance() !=0){
            m[i].setVelocityX((float) ((int) (mc.getX()-m[i].getX()) / (float)(m[i].getDistance()/ m[i].getSpeed())));
            m[i].setVelocityY((float) ((int) (mc.getY()-m[i].getY()) / (float)(m[i].getDistance()/ m[i].getSpeed())));
        }
    }
    // figures out if the map can move in the x direction
    private boolean mapMovableX(){
        if(map.getX() >= 0 && mc.getVelocityX() <= 0){
            return false;
        }else if(map.getX() <= s.getWidth()-map.getWidth()&& mc.getVelocityX() >= 0){
            return false;
        }else if(mc.getX() < s.getWidth()/2-mc.getSpeed()*50 || mc.getX() > s.getWidth()/2+mc.getSpeed()*50){
            return false;
        }else{
            return true;
        }
    }

    //figures out if the map can move in the y direction
    private boolean mapMovableY(){
        if(map.getY() >= 0 && mc.getVelocityY() <= 0){
            return false;
        }else if(map.getY() <= s.getHeight()-map.getHeight() && mc.getVelocityY() >= 0){
            return false;
        }
        else if(mc.getY() < s.getHeight()/2-mc.getSpeed()*50|| mc.getY() > s.getHeight()/2+mc.getSpeed()*50){
            return false;
        }else{
            return true;
        }
    }

     //draws everything
    /* In this order
    BG
    Sprite
    princess
    Monsters
    attacks
    Health
    Mana
    text boxes
    game over/ winner screens
    */
    public void draw(Graphics2D g){
        g.setFont(text);
        //BG
        g.drawImage(map.getImage(),Math.round(map.getX()),Math.round(map.getY()),null);
        //Sprite
        g.drawImage(mc.getImage(),Math.round(mc.getX()),Math.round(mc.getY()),null);
        g.setFont(new Font("Arial", Font.PLAIN ,12));

        // princess
        if(princess.getHealth()==1){
            g.drawImage(princess.getImage(), Math.round(princess.getX()), Math.round(princess.getY()), 96, 96, null);
        }else{
            g.drawImage(princess.getImage(),Math.round(princess.getX()), Math.round(princess.getY()) , null);
            g.setColor(Color.WHITE);
            g.drawString("Lvl. "+50, princess.getX()-35, princess.getY()-5);
            g.setColor(Color.RED);
            g.fillRect(Math.round(princess.getX()), Math.round(princess.getY()) - 15,(int)(princess.getWidth() *(princess.getHealth()/princess.getMaxHP())), 10);

        }
        //Monsters
        for(int i = 0; i < monsters.length; i++){
            if(monsters[i].getImage() != null){
                g.drawImage(monsters[i].getImage(),Math.round(monsters[i].getX()), Math.round(monsters[i].getY()), null);
                g.setColor(Color.WHITE);
                g.drawString("Lvl. "+monsters[i].getLevel(), monsters[i].getX()-35, monsters[i].getY()-5);
                g.setColor(Color.RED);
                g.fillRect(Math.round(monsters[i].getX()), Math.round(monsters[i].getY()) - 15,(int)(100 *(monsters[i].getHealth()/monsters[i].getMaxHP())), 10);
            }
        }
        //Guards
        if(mapNumber< 5){
        for(int i = 0; i < guards.length; i++){
            g.drawImage(guards[i].getImage(),Math.round(guards[i].getX()), Math.round(guards[i].getY()), null);
            g.setColor(Color.WHITE);
            g.drawString("Lvl. "+guards[i].getLevel(), guards[i].getX()-35, guards[i].getY()-5);
            g.setColor(Color.RED);
            g.fillRect(Math.round(guards[i].getX()), Math.round(guards[i].getY()) - 15,(int)(100 *(guards[i].getHealth()/guards[i].getMaxHP())), 10);
        }
        }

        //attacks
        for(int i = 0; i<attacks.length; i++){
            g.drawImage(attacks[i].getImage(), Math.round(attacks[i].getX()),Math.round(attacks[i].getY()), null);
        }
        for(int i = 0; i < special.length; i++){
            g.drawImage(special[i].getImage(),Math.round(special[i].getX()),Math.round(special[i].getY()), null);
        }

        //Health bar
        int height = 20;
        g.setColor(Color.RED);
        g.fillRect(15, s.getHeight()-2*height-20,(int) ((s.getWidth()-150)*mc.getHealth()/mc.getMaxHP()), height);

        //Mana bar
        g.setColor(Color.BLUE);
        g.fillRect(15, s.getHeight()-height-10,(int) ((s.getWidth()-150)*(mc.getMana()/mc.getMaxMP())), height);

        //exp bar
        g.setColor(Color.GREEN);
        g.fillRect(15, s.getHeight()-3*height-30, (int) ((s.getWidth()-150)*(mc.getEXP()/mc.getMaxEXP())), height/2);
        g.setColor(Color.WHITE);
        g.drawString(mc.getHealth() + "/" + mc.getMaxHP(),(s.getWidth()-150)/2, s.getHeight()-2*height-5 );
        g.drawString(mc.getMana() + "/" + mc.getMaxMP(),(s.getWidth()-150)/2, s.getHeight()-height+5 );
        g.drawString("Lvl. "+mc.level, 15, s.getHeight()-3*height-50);


        //menu
        statButton.draw(g);
        if(statMenu.isVisible()){
            statMenu.draw(g);
        }
        //instruction
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString(instruction, 20, 60);
        //princess dialogue
        if(princess.isSaved() && !dialogue.equals("")){
            if(dialogueNumber == 5 && mapNumber != 5){
                statMenu.setVisible(true);
            }
            int h = 100;
            g.setColor(Color.BLACK);
            g.setFont(bold);
            g.fillRect(210+10, s.getHeight()-h-10, s.getWidth()-100-210, h);
            g.setColor(Color.WHITE);
            g.drawString(princess.getName(), 210 + 20, s.getHeight()- h+27);
            g.setFont(text);
            g.drawString(dialogue, 210 + 20, s.getHeight() - h + 50);
            g.drawImage(princess.getImage(), 10, s.getHeight() - 210 -5, 210, 210, null);
        }
//        gameOver.setVisible(true);
//        win = true;
        if(gameOver.isVisible() && win == false){
            g.setColor(Color.BLACK);
            g.fillRect(0,0,s.getWidth(),s.getHeight());
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.setColor(Color.WHITE);
            g.drawString("GAME OVER", s.getWidth()/2-50*8/2, 300);
            gameOver.draw(g);
        }
        if(gameOver.isVisible() && win){
            g.drawImage(map.getImage(),0,0, s.getWidth(),s.getHeight(), null);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.setColor(Color.WHITE);
            g.drawString("YOU WIN!!!", s.getWidth()/2-50*6/2, 300);
            gameOver.draw(g);
        }

    }

    // does actions for keys pressed
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_ESCAPE){
            System.exit(0);
        }
        if(keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W){
            mc.setVelocityY(-mc.getSpeed());
            mc.setAnimation(up);
            if(iNumber==0){
                iNumber ++;
                instruction = "Click to shoot.";
            }
        }
        if(keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S){
            mc.setVelocityY(mc.getSpeed());
            mc.setAnimation(down);
            if(iNumber==0){
                iNumber ++;
                instruction = "Click to shoot.";
            }
        }
        if(keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A){
            mc.setVelocityX(-mc.getSpeed());
            mc.setAnimation(left);
            if(iNumber==0){
                iNumber ++;
                instruction = "Click to shoot.";
            }
        }
        if(keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D){
            mc.setVelocityX(mc.getSpeed());
            mc.setAnimation(right);
            if(iNumber==0){
                iNumber ++;
                instruction = "Click to shoot.";
            }
        }
        mc.update(timePassed);


    }

    // resets all the velocities to 0 after the key is released
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) mc.setVelocityY(0);
        if(keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) mc.setVelocityY(0);
        if(keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D)mc.setVelocityX(0);
        if(keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A)mc.setVelocityX(0);
    }


    // checks if you click any of the button and does stuff accordingly
    public void mousePressed(MouseEvent e) {
    /*
        princess.getWidth()+10, s.getHeight()-h-10, s.getWidth()-100-princess.getWidth(), h
        */
    //princess's dialogue
    if(princess.isSaved() && !dialogue.isEmpty() && e.getX() > 210+10 && e.getX() < s.getWidth()-110
            && e.getY() >  s.getHeight()-110  && e.getY() < s.getHeight() - 10){
        dialogueNumber++;
    }
    //statmenu button
    if(gameOver.isVisible() && e.getX() > gameOver.getDone().getX() && e.getX() < gameOver.getDone().getX()+gameOver.getDone().getWidth()
            && e.getY() > gameOver.getDone().getY() && e.getY() < gameOver.getDone().getHeight()+ gameOver.getDone().getY()){
        System.exit(0);
    }else if(gameOver.isVisible() && e.getX() > gameOver.getRestart().getX() && e.getX() < gameOver.getRestart().getX()+gameOver.getRestart().getWidth()
            && e.getY() > gameOver.getRestart().getY() && e.getY() < gameOver.getRestart().getHeight()+ gameOver.getRestart().getY()){
        stop();
    }else if(!statMenu.isVisible()&& e.getX() > statButton.getX() && e.getX() < statButton.getX() + statButton.getWidth()
            && e.getY()> statButton.getY() && e.getY() < statButton.getY() + statButton.getHeight()){
        statMenu.setVisible(true);
        if(iNumber == 4){
                    instruction = "Save the Princesses";
                    iNumber++;
        }
    }else if(statMenu.isVisible()&& e.getX() > statButton.getX() && e.getX() < statButton.getX() + statButton.getWidth()
            && e.getY()> statButton.getY() && e.getY() < statButton.getY() + statButton.getHeight()){
        statMenu.setVisible(false);
    }
    // health button
    else if(mc.getSP() > 0 && statMenu.isVisible() && e.getX()>statMenu.getHPButton().getX() && e.getX()< statMenu.getHPButton().getX() + statMenu.getHPButton().getWidth()
            && e.getY()>statMenu.getHPButton().getY() && e.getY()< statMenu.getHPButton().getY() + statMenu.getHPButton().getHeight()){
        mc.vitalityUp();
        mc.healthUps++;
        mc.useSP(1);
    }
    // mana button
    else if(mc.getSP() > 0 && statMenu.isVisible() && e.getX()>statMenu.getMPButton().getX() && e.getX()< statMenu.getMPButton().getX() + statMenu.getMPButton().getWidth()
            && e.getY()>statMenu.getMPButton().getY() && e.getY()< statMenu.getMPButton().getY() + statMenu.getMPButton().getHeight()){
        mc.manaUp();
        mc.manaUps++;
        mc.useSP(1);
    }
    //attack button
    else if(mc.getSP() > 0 && statMenu.isVisible() && e.getX()>statMenu.getAttackButton().getX() && e.getX()< statMenu.getAttackButton().getX() + statMenu.getAttackButton().getWidth()
            && e.getY()>statMenu.getAttackButton().getY() && e.getY()< statMenu.getAttackButton().getY() + statMenu.getAttackButton().getHeight()){
        mc.attackUp();
        mc.attackUps++;
        mc.useSP(1);
    }
    //speed button
    else if(mc.getSP() > 0 && statMenu.isVisible() && e.getX()>statMenu.getSpeedButton().getX() && e.getX()< statMenu.getSpeedButton().getX() + statMenu.getSpeedButton().getWidth()
            && e.getY()>statMenu.getSpeedButton().getY() && e.getY()< statMenu.getSpeedButton().getY() + statMenu.getSpeedButton().getHeight()){
        mc.speedUp();
        mc.speedUps++;
        mc.useSP(1);
    }
    //mana Regen button when unlocked
    else if(statMenu.getManaRegenButton().isLocked()== false &&
            mc.getSP() >= 3 && statMenu.isVisible() && e.getX()>statMenu.getManaRegenButton().getX() && e.getX()< statMenu.getManaRegenButton().getX() + statMenu.getManaRegenButton().getWidth()
            && e.getY()>statMenu.getManaRegenButton().getY() && e.getY()< statMenu.getManaRegenButton().getY() + statMenu.getManaRegenButton().getHeight()){
        mc.mRegen++;
        mc.mRegenUps++;
        mc.useSP(3);
    }
    //Health regen button when unlocked
    else if(statMenu.getHealthRegenButton().isLocked()== false &&
            mc.getSP() >= 3 && statMenu.isVisible() && e.getX()>statMenu.getHealthRegenButton().getX() && e.getX()< statMenu.getHealthRegenButton().getX() + statMenu.getHealthRegenButton().getWidth()
            && e.getY()>statMenu.getHealthRegenButton().getY() && e.getY()< statMenu.getHealthRegenButton().getY() + statMenu.getHealthRegenButton().getHeight()){

            mc.percent += 1;
            mc.hRegen = (int) ((mc.getMaxHP()/100)*mc.percent);
        mc.hRegenUps++;
        mc.useSP(3);
    }
    //Attack bonus button when unlocked
    else if(statMenu.getAttackBonusButton().isLocked()== false &&
            mc.getSP() >= 3 && statMenu.isVisible() && e.getX()>statMenu.getAttackBonusButton().getX() && e.getX()< statMenu.getAttackBonusButton().getX() + statMenu.getAttackBonusButton().getWidth()
            && e.getY()>statMenu.getAttackBonusButton().getY() && e.getY()< statMenu.getAttackBonusButton().getY() + statMenu.getAttackBonusButton().getHeight()){
        mc.setAttack( mc.getAttack() + mc.attackUps);
        mc.aInc++;
        mc.attack2Ups++;
        mc.useSP(3);
    }
    //special attack button when unlocked
    else if(statMenu.getSpecialButton().isLocked()== false &&
            mc.getSP() >= 3 && statMenu.isVisible() && e.getX()>statMenu.getSpecialButton().getX() && e.getX()< statMenu.getSpecialButton().getX() + statMenu.getSpecialButton().getWidth()
            && e.getY()>statMenu.getSpecialButton().getY() && e.getY()< statMenu.getSpecialButton().getY() + statMenu.getSpecialButton().getHeight()){
        mc.setCRange(mc.getCRange()+10);
        mc.specialUps++;
        mc.useSP(3);
    }
    //mana Regen button when locked
    else if(statMenu.getManaRegenButton().isLocked() && mc.getKeys() >0
            && statMenu.isVisible() && e.getX()>statMenu.getManaRegenButton().getX() && e.getX()< statMenu.getManaRegenButton().getX() + statMenu.getManaRegenButton().getWidth()
            && e.getY()>statMenu.getManaRegenButton().getY() && e.getY()< statMenu.getManaRegenButton().getY() + statMenu.getManaRegenButton().getHeight()){
        statMenu.getManaRegenButton().setLocked(false);
        try {
            statMenu.getManaRegenButton().setImage(ImageIO.read(getClass().getResource("/images/ManaUp.png")));
                    } catch (IOException ex) {
            Logger.getLogger(JustWork.class.getName()).log(Level.SEVERE, null, ex);
        }
        mc.useKey();
    }
    //Health regen button when locked
    else if(statMenu.getHealthRegenButton().isLocked() && mc.getKeys() >0 &&
            statMenu.isVisible() && e.getX()>statMenu.getHealthRegenButton().getX() && e.getX()< statMenu.getHealthRegenButton().getX() + statMenu.getHealthRegenButton().getWidth()
            && e.getY()>statMenu.getHealthRegenButton().getY() && e.getY()< statMenu.getHealthRegenButton().getY() + statMenu.getHealthRegenButton().getHeight()){
        statMenu.getHealthRegenButton().setLocked(false);
        try {
            statMenu.getHealthRegenButton().setImage(ImageIO.read(getClass().getResource("/images/HeartRegen.png")));
                    } catch (IOException ex) {
            Logger.getLogger(JustWork.class.getName()).log(Level.SEVERE, null, ex);
        }
        mc.useKey();
    }
    //Attack bonus button when locked
    else if(statMenu.getAttackBonusButton().isLocked()&& mc.getKeys() >0 &&
            statMenu.isVisible() && e.getX()>statMenu.getAttackBonusButton().getX() && e.getX()< statMenu.getAttackBonusButton().getX() + statMenu.getAttackBonusButton().getWidth()
            && e.getY()>statMenu.getAttackBonusButton().getY() && e.getY()< statMenu.getAttackBonusButton().getY() + statMenu.getAttackBonusButton().getHeight()){
        statMenu.getAttackBonusButton().setLocked(false);
        try {
            statMenu.getAttackBonusButton().setImage(ImageIO.read(getClass().getResource("/images/AttackUp.png")));
                    } catch (IOException ex) {
            Logger.getLogger(JustWork.class.getName()).log(Level.SEVERE, null, ex);
        }
        mc.useKey();
    }
    //special attack button when locked
    else if(statMenu.getSpecialButton().isLocked() && mc.getKeys() >0 &&
            statMenu.isVisible() && e.getX()>statMenu.getSpecialButton().getX() && e.getX()< statMenu.getSpecialButton().getX() + statMenu.getSpecialButton().getWidth()
            && e.getY()>statMenu.getSpecialButton().getY() && e.getY()< statMenu.getSpecialButton().getY() + statMenu.getSpecialButton().getHeight()){
        statMenu.getSpecialButton().setLocked(false);
        try {
            statMenu.getSpecialButton().setImage(ImageIO.read(getClass().getResource("/images/special.png")));
                    } catch (IOException ex) {
            Logger.getLogger(JustWork.class.getName()).log(Level.SEVERE, null, ex);
        }
        mc.useKey();
    }
    //shooting attacks
    else{
        //special
        if(mc.getCTime() > mc.getCoolTime() - mc.getCRange() && mc.getCTime() < mc.getCoolTime() + mc.getCRange() && mc.getMana() >= 10){
            // sets the start place of particle
            special[specialNumber].setStartX(e.getX());
            special[specialNumber].setStartY(e.getY());
            //calcs distance between original cursor point and the character
            special[specialNumber].setX(mc.getX()+mc.getWidth()/2);
            special[specialNumber].setY(mc.getY()+mc.getHeight()/2);
            special[specialNumber].calcDistance();
            //sets the velocity for the attack
            special[specialNumber].setVelocityX((special[specialNumber].getStartX()-(mc.getX() + mc.getWidth()/2)) / (special[specialNumber].getDistance()/special[specialNumber].getSpeed()));
            special[specialNumber].setVelocityY((special[specialNumber].getStartY()-(mc.getY() + mc.getHeight()/2)) / (special[specialNumber].getDistance()/special[specialNumber].getSpeed()));

            mc.useMP(10);
            mc.setCTime(0);
            specialNumber++;
            if(specialNumber == 9){
            specialNumber = 0;
            }

            if(iNumber == 2){
                iNumber ++;
                instruction = "Kill monsters and guards.";
            }
        }
        //normal attack
        else{
            // sets the start place of particle
            attacks[attackNumber].setStartX(e.getX());
            attacks[attackNumber].setStartY(e.getY());
            //calcs distance between original cursor point and the character
            attacks[attackNumber].setX(mc.getX()+mc.getWidth()/2);
            attacks[attackNumber].setY(mc.getY()+mc.getHeight()/2);
            attacks[attackNumber].calcDistance();
            //sets the velocity for the attack
            attacks[attackNumber].setVelocityX((attacks[attackNumber].getStartX()-(mc.getX() + mc.getWidth()/2)) / (attacks[attackNumber].getDistance()/attacks[attackNumber].getSpeed()));
            attacks[attackNumber].setVelocityY((attacks[attackNumber].getStartY()-(mc.getY() + mc.getHeight()/2)) / (attacks[attackNumber].getDistance()/attacks[attackNumber].getSpeed()));

            mc.setCTime(0);
            attackNumber++;
            if(attackNumber == 9){
                attackNumber =0;
            }
            if(iNumber == 1){
                iNumber ++;
                instruction = "Click after exactly 1 sec to shoot a special attack";
            }
        }
    }




    }
    public void mouseReleased(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mouseClicked(MouseEvent e){}
    public void keyTyped(KeyEvent e) {}
}
