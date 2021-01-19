package com.rsgaming.entities;

import com.rsgaming.main.Game;
import com.rsgaming.world.Camera;
import com.rsgaming.world.World;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class MagicShoot extends Entity {

    private double dx;
    private double dy;
    private  double spd = 4;

    private int life = 10, curLife = 0;

    private BufferedImage rightMagicWave;
    private BufferedImage leftMagicWave;
    private BufferedImage upMagicWave;
    private BufferedImage downMagicWave;

    public MagicShoot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
        super(x, y, width, height, sprite);

        this.dx = dx;
        this.dy = dy;

        rightMagicWave = Game.spritesheet.getSprite(16,64, World.TILE_SIZE,World.TILE_SIZE);
       leftMagicWave = Game.spritesheet.getSprite(0,64, World.TILE_SIZE,World.TILE_SIZE);
        upMagicWave = Game.spritesheet.getSprite(16,48, World.TILE_SIZE,World.TILE_SIZE);
        downMagicWave = Game.spritesheet.getSprite(16,32, World.TILE_SIZE,World.TILE_SIZE);

    }

    public void tick(){
        x+=dx*spd;
        y+=dy*spd;
        curLife++;
        if (curLife == life){
            Game.magic.remove(this);
            return;
        }
    }

    public void render(Graphics g){
        if (Game.player.dir == Game.player.right_dir){
            g.drawImage(rightMagicWave,this.getX() - Camera.x,this.getY() - Camera.y,null);
        } else if (Game.player.dir == Game.player.left_dir){
            g.drawImage(leftMagicWave,this.getX() - Camera.x,this.getY() - Camera.y,null);
        }  if (Game.player.dir == Game.player.down_dir){
            g.drawImage(downMagicWave,this.getX() - Camera.x,this.getY() - Camera.y,null);
        } else if (Game.player.dir == Game.player.up_dir){
            g.drawImage(upMagicWave,this.getX() - Camera.x,this.getY() - Camera.y,null);
        }
    }
}
