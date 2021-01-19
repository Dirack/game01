package com.rsgaming.entities;

import com.rsgaming.main.Game;
import com.rsgaming.world.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {

    public static BufferedImage LIFEPOTION_EN = Game.spritesheet.getSprite(0,16, 16,16);
    public static BufferedImage MANAPOTION_EN = Game.spritesheet.getSprite(0,48, 16,16);
    public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(80,16, 16,16);
    public static BufferedImage WEAPON_SWORD_EN = Game.spritesheet.getSprite(16,16,16,16);


    protected double x;
    protected double y;
    protected int width;
    protected int height;

    private BufferedImage sprite;

    private int maskx,masky,mwidth,mheight;

    public Entity(int x, int y, int width, int height, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;

        this.maskx = 0;
        this.masky = 0;
        this.mwidth = width;
        this.mheight = height;
    }

    public void setMask(int maskx, int masky, int mwidth, int mheight){
        this.maskx = maskx;
        this.masky = masky;
        this.mwidth = mwidth;
        this.mheight = mheight;
    }

    public int getX() {
        return (int) x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return (int) y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void render(Graphics g){
        g.drawImage(sprite,this.getX() - Camera.x,this.getY() - Camera.y,null);

    }

    public void tick(){

    }

    public static boolean isColiding(Entity e1, Entity e2){
        Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx, e1.getY()+e1.masky,e1.mwidth,e1.mheight);
        Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx, e2.getY()+e2.masky,e2.mwidth,e2.mheight);

        return e1Mask.intersects(e2Mask);
    }

}
