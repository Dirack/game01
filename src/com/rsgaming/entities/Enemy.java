package com.rsgaming.entities;

import com.rsgaming.main.Game;
import com.rsgaming.world.Camera;
import com.rsgaming.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends Entity{

    public boolean right, up, left, down;
    public int right_dir = 0, left_dir = 1,  up_dir = 2, down_dir = 3;
    public int dir = right_dir ;

    private double speed = 0.5;
    private int maskx = 8, masky =8, maskw=10, maskh = 10;

    private int frames = 0, maxFrames = 5, index = 0, maxIndex = 2;

    private BufferedImage[] rightEnemy;
    private BufferedImage[] leftEnemy;
    private BufferedImage[] upEnemy;
    private BufferedImage[] downEnemy;

    private int life = 10;


    public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);

        rightEnemy = new BufferedImage[3];
        leftEnemy = new BufferedImage[3];
        upEnemy = new BufferedImage[3];
        downEnemy = new BufferedImage[3];

        for (int i = 0;i < 3; i++){
            rightEnemy[i] = Game.spritesheet.getSprite(80 + (i*width), 32,16,16);
        }
        for (int i = 0;i < 3; i++){
            leftEnemy[i] = Game.spritesheet.getSprite(80 + (i*width),16,16,16);
        }
        for (int i = 0;i < 3; i++){
            upEnemy[i] = Game.spritesheet.getSprite(80 + (i*width), 48,16,16);
        }
        for (int i = 0;i < 3; i++){
            downEnemy[i] = Game.spritesheet.getSprite(80 + (i*width), 0,16,16);
        }
    }

    public void tick(){
        if (isColidingWithPlayer() == false){
            if((int)x < Game.player.getX() && World.isFree((int)(x+speed), this.getY())
                    && !isColiding((int)(x+speed), this.getY())){
                dir = right_dir;
                x+=speed;
            }else if ((int)x > Game.player.getX() && World.isFree((int)(x-speed), this.getY())
                    && !isColiding((int)(x-speed), this.getY())){
                dir = left_dir;
                x-=speed;
            }if((int)y < Game.player.getY()&& World.isFree(this.getX(),(int)(y+speed))
                    && !isColiding(this.getX(),(int)(y+speed))){
                dir = down_dir;
                y+=speed;
            }else if ((int)y > Game.player.getY()&& World.isFree(this.getX(),(int)(y-speed))
                    && !isColiding(this.getX(),(int)(y-speed))){
                dir = up_dir;
                y-=speed;
            }else {
                //estamos perto do player
                if (Game.rand.nextInt(100) < 10){
                    Game.player.life-=Game.rand.nextInt(3);
//                    Game.player.isDamaged = true;
                }
            }

        }

            frames++;
            if (frames == maxFrames) {
                frames = 0;
                index++;
                if (index > maxIndex){
                    index = 0;
                }
            }

            colidingMagic();

            if (life <= 0){
                destroySelf();
                return;
            }
        }

        public void destroySelf(){
            Game.enemies.remove(this);
            Game.entities.remove(this);
        }

        public void colidingMagic(){
            for(int i = 0; i < Game.magic.size(); i++){
                Entity e = Game.magic.get(i);
                if (e instanceof MagicShoot){
                    if(Entity.isColiding(this, e)){
                        if(Game.player.maleFight == 1){
                            life--;
                            Game.magic.remove(i);
                        }else{
                            life =- Game.player.maleFight;
                            Game.magic.remove(i);
                        }

                        return;
                    }
                }
            }
        }

    public boolean isColidingWithPlayer(){
        Rectangle enemyCurrent = new Rectangle(this.getX() + maskx,this.getY() + masky, maskw, maskh);
        Rectangle player =  new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);

        return enemyCurrent.intersects(player);
    }

    public boolean isColiding (int xnext, int ynext){
        Rectangle enemyCurrent = new Rectangle(xnext + maskx,ynext + masky, maskw, maskh);
        for (int i = 0 ; i < Game.enemies.size(); i++){
            Enemy e = Game.enemies.get(i);
            if(e == this)
                continue;
                Rectangle targetEnemy = new Rectangle(e.getX() + maskx, e.getY() + masky, maskw, maskh);
                if (enemyCurrent.intersects(targetEnemy)){
                    return true;
                }

        }
            return false;
    }

    public void render(Graphics g){

        if (dir == right_dir){
            g.drawImage(rightEnemy[index],this.getX() - Camera.x,this.getY() - Camera.y,null);
        } else if (dir == left_dir){
            g.drawImage(leftEnemy[index],this.getX() - Camera.x,this.getY() - Camera.y,null);
        }  if (dir == down_dir){
            g.drawImage(downEnemy[index],this.getX() - Camera.x,this.getY() - Camera.y,null);
        } else if (dir == up_dir){
            g.drawImage(upEnemy[index],this.getX() - Camera.x,this.getY() - Camera.y,null);
        }

    }
}
