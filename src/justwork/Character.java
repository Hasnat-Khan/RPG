/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package justwork;

/**
 * Main character that the player controls
 * contains all the properties of the character like health, attack, mana and also
 * getter methods for each
 * 
 * Extends sprite. Has all the characters information, like level, hp, xp, and a lot more. Has methods to take damage, heal, and use sp.
 */
public class Character extends Sprite{
    
    public int level;
    private float maxHealth, maxMana, health, mana;
    float speed;
    private int attack, mAttack;
    private float maxExp, exp, extraExp;
    private int statPoints;
    
    public int healthUps, speedUps, attackUps, manaUps; 
    public int mRegenUps, hRegenUps, attack2Ups, specialUps;
    private int keys;
    
    public int hInc, mInc, aInc;
    public int mRegen, hRegen;
    public int percent;
    private long manaTime, mT;
    private long coolTime, cT, cRange;
    private long hpTime, hT;

    //CONSTRUCTOR
    public Character(Animation anim) {
        super(anim);
        maxHealth = 100;
        health = 100;
        maxMana = 100;
        mana = 100;
        attack = 10;
        mAttack = 10;
        speed = 0.1f;
        statPoints = 0;
        maxExp = 100;
        coolTime = 1000;
        hpTime = 1000;
        manaTime = 2000;
        mT = 0;
        cRange = 50;
        hInc = 100;
        mInc = 50;
        aInc = 10;
        mRegen = 1;
        hRegen = 1;
        keys = 0;
        percent = 1;
    }
    
    // updates the time healing/mana was last used
    public void upTime(long timePassed){
        cT += timePassed;
        hT += timePassed;
        mT += timePassed;
    }
    
    //sets key amount
    public void setKeys(){
        keys++;
    }
    public void useKey(){
        keys--;
    }
    
    // sets the time last used healing or mana, usually to reset it to 0
    public void setHTime(long t){
        hT = t;
    }
    public void setCTime(long t){
        cT = t;
    }
    public void setMTime(long t){
        mT = t;
    }
    public void setCRange(long t){
        cRange = t;
    }
    
    
    //getter methods
    public float getMaxHP(){
        return maxHealth;
    }
    public float getMaxMP(){
        return maxMana;
    }
    public float getMana(){
        return mana;
    }
    public float getHealth(){
        return health;
    }
    public int getAttack(){
        return attack;
    }
    public int getMAttack(){
        return mAttack;
    }
    public float getSpeed(){
        return speed;
    }
    public float getEXP(){
        return exp;
    }
    public float getMaxEXP(){
        return maxExp;
    } 
    public long getHTime(){
        return hT;
    }
    public long getCTime(){
        return cT;
    }
    public long getMTime(){
        return mT;
    }
    public long getCoolTime(){
        return coolTime;
    }
    public long getHPTime(){
        return hpTime;
    }
    public long getManaTime(){
        return manaTime;
    }
    public long getCRange(){
        return cRange;
    }
    public int getSP(){
        return statPoints;
    }
    public int getKeys(){
        return keys;
    }
    
    //the mc loses/gains health
    public void takeDmg(float dmg){
        health -= dmg;
    }
    public void heal(int healing){
        health += healing;
        if(health > maxHealth){
            health = maxHealth;
        }
    }
    public void manaRegen(int manaPlus){
        mana += manaPlus;
        if(mana > maxMana){
            mana = maxMana;
        }
    }
    
    //gains exp, uses recursion so that if you go over the amount of exp you need to level up, it
    // will carry on to the next level
    public void gainXP(float expGain){
        if(expGain >= maxExp-exp){
            extraExp = expGain - (maxExp - exp);
            expGain = 0;
            levelUp();
            gainXP(extraExp);
        }
        exp += expGain;
    }
    public void useMP(int mpUsed){
        mana -= mpUsed;
    }
    public void useSP(int spUsed){
        statPoints -= spUsed;
    }
    
    public void setAttack(int a){
        attack = a;
    }
    
    //stat boosts from level up or fruits
    public void vitalityUp(){
        health += maxHealth;
        maxHealth *= 1.2;
        
    }
    public void manaUp(){
        maxMana += 10;
        mana += 10;
    }
    public void attackUp(){
        attack += aInc;
    }
    public void speedUp(){
        speed += 0.01;
    }
    public void levelUp(){
        level++;
        maxExp = Math.round(1.25*maxExp);
        exp = 0;
        statPoints += 5;
    }
}
