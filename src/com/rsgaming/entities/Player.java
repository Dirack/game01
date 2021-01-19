package com.rsgaming.entities;

import com.rsgaming.graficos.Spritesheet;
import com.rsgaming.main.Game;
import com.rsgaming.world.Camera;
import com.rsgaming.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends Entity{

    public boolean right, up, left, down;
    public int right_dir = 0, left_dir = 1,  up_dir = 2, down_dir = 3;
    public int dir = right_dir ;

    public double speed = 1.5;

    private int frames = 0, maxFrames = 5, index = 0, maxIndex = 2;
    private boolean moved = false;
    private BufferedImage[] rightPlayer;
    private BufferedImage[] leftPlayer;
    private BufferedImage[] upPlayer;
    private BufferedImage[] downPlayer;
    private BufferedImage playerDamager;

    public int slotWeppons = 0;
    public int maleFight = 1;

    public boolean shoot = false, mouseShoot = false;
//    public boolean isDamaged = false;
//    private int damageFrames = 0;

    public double life = 100, maxLife = 100;
    public double mana = 50, maxMana = 50;
    public int mx, my;

    public Player(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);

        rightPlayer = new BufferedImage[3];
        leftPlayer = new BufferedImage[3];
        upPlayer = new BufferedImage[3];
        downPlayer = new BufferedImage[3];
//        playerDamager = Game.spritesheet.getSprite(0,32,World.TILE_SIZE,World.TILE_SIZE);




       for (int i = 0;i < 3; i++){
            rightPlayer[i] = Game.spritesheet.getSprite(32 + (i*width), 32,16,16);
      }
       for (int i = 0;i < 3; i++){
           leftPlayer[i] = Game.spritesheet.getSprite(32 + (i*width),16,16,16);
       }
        for (int i = 0;i < 3; i++){
            upPlayer[i] = Game.spritesheet.getSprite(32 + (i*width), 48,16,16);
        }
        for (int i = 0;i < 3; i++){
            downPlayer[i] = Game.spritesheet.getSprite(32 + (i*width), 0,16,16);
        }
    }


    public void tick(){

        moved = false;
        if (right && World.isFree((int) (x+speed), this.getY())) {
            moved =true;
            dir = right_dir;
            x += speed;
        }
        else if (left && World.isFree((int)(x-speed),this.getY() ))  {
            moved =true;
            dir = left_dir;
            x -= speed;
        }
        if (up && World.isFree(this.getX(), (int)(y-speed)))  {
            moved =true;
            dir = up_dir;
            y -= speed;
        }
        else if (down && World.isFree(this.getX(),(int)(y+speed))) {
            moved =true;
           dir = down_dir;
            y += speed;
        }

        if(moved){
            frames++;
            if (frames == maxFrames) {
                frames = 0;
                index++;
                if (index > maxIndex){
                    index = 0;
                }
            }
        }

        checkCollisionPotions();
        checkCollisionWeppons();
        checkCollisionManaPotion();

//        if(isDamaged){
//            this.damageFrames++;
//            if (this.damageFrames ==30){
//                this.damageFrames = 0;
//                isDamaged =false;
//            }
//        }

        if(life <=0){
            Game.gameState = "GAME_OVER";
            Game.entities.clear();
            Game.entities.clear();
            Game.entities = new ArrayList<Entity>();
            Game.enemies = new ArrayList<Enemy>();
            Game.spritesheet = new Spritesheet("/spritesheet.png");
            Game.player = new Player(0,0,16,16,Game.spritesheet.getSprite(32,0, 16,16));
            Game.entities.add(Game.player);
            Game.world = new World("/level1.png");

        }

        if (shoot ){
            //Criar magia e atirar
            shoot = false;
            if(mana >= 1) {
                int dx = 0;
                int dy = 0;
                int px = 0;
                int py = 6;
                if (dir == right_dir) {
                    px = 18;
                    dx = 1;

                } else if (dir == left_dir) {
                    dx = -1;
                    px = -8;
                } else if (dir == up_dir) {
                    dy = -1;
                } else if (dir == down_dir) {
                    dy = 1;
                }

                MagicShoot magicShoot = new MagicShoot(this.getX() + px, this.getY()+py, width, height, null, dx, dy);
                Game.magic.add(magicShoot);
                mana -= 1;
            }
        }

//        if(mouseShoot){
//
//            mouseShoot = false;
//            if(mana >= 10) {
//
//                double angle = Math.atan2( my - (this.getY()+8 - Camera.y),mx - (this.getX()+8 - Camera.x));
//                double dx = Math.cos(angle);
//                double dy = Math.sin(angle);
//                int px = 8;
//                int py = 8;
////                if (dir == right_dir) {
////                    dx = 1;
////
////                } else if (dir == left_dir) {
////                    dx = -1;
////                } else if (dir == up_dir) {
////                    dy = -1;
////                } else if (dir == down_dir) {
////                    dy = 1;
////                }
//
//                MagicShoot magicShoot = new MagicShoot(this.getX()+px, this.getY()+py, width, height, null, dx, dy);
//                Game.magic.add(magicShoot);
//                mana -= 10;
//            }
//        }

        Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH);
        Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT*16 - Game.HEIGHT);
    }

    public void checkCollisionWeppons(){
        for (int i = 0; i < Game.entities.size(); i++){
            Entity e = Game.entities.get(i);
            if (e instanceof Weapon) {
                if (Entity.isColiding(this, e)){
                    if (slotWeppons == 0) {
                        slotWeppons++;
                        maleFight += 10;
                        Game.entities.remove(i);
                    }
                    return;
                }
            }
        }
    }

    public void checkCollisionPotions(){
        for (int i = 0; i < Game.entities.size(); i++){
            Entity e = Game.entities.get(i);
            if (e instanceof Potions) {
                if (Entity.isColiding(this, e)){
                    life+=8;
                    if (life >=100)
                        life = 100;
                    Game.entities.remove(i);
                    return;
                }
            }
        }
    }

    public void checkCollisionManaPotion(){
        for (int i = 0; i < Game.entities.size(); i++){
            Entity e = Game.entities.get(i);
            if (e instanceof ManaPotion) {
                if (Entity.isColiding(this, e)){
                    mana+=50;
                    if (mana >=50)
                        mana = 50;
                    Game.entities.remove(i);
                    return;
                }
            }
        }
    }




    public void render(Graphics g){

            if (dir == right_dir){
                g.drawImage(rightPlayer[index],this.getX() - Camera.x,this.getY() - Camera.y,null);
            } else if (dir == left_dir){
                g.drawImage(leftPlayer[index],this.getX() - Camera.x,this.getY() - Camera.y,null);
            }  if (dir == down_dir){
                g.drawImage(downPlayer[index],this.getX() - Camera.x,this.getY() - Camera.y,null);
            } else if (dir == up_dir){
                g.drawImage(upPlayer[index],this.getX() - Camera.x,this.getY() - Camera.y,null);
            }



   }
}
