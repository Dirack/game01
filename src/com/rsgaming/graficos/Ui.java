package com.rsgaming.graficos;

import com.rsgaming.entities.Player;
import com.rsgaming.main.Game;

import java.awt.*;

public class Ui {

    public void render(Graphics g){
        g.setColor(Color.red);
        g.fillRect(8,2,50,7);
        g.setColor(Color.green);
        g.fillRect(8,2,(int)((Game.player.life/Game.player.maxLife)*50),7);
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD,7));
        g.drawString((int)Game.player.life+" / "+(int)Game.player.maxLife,10,8 );

        g.setColor(Color.white);
        g.fillRect(8,10,50,7);
        g.setColor(Color.blue);
        g.fillRect(8,10,(int)((Game.player.mana/Game.player.maxMana)*50),7);
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD,7));
        g.drawString((int)Game.player.mana+" / "+(int)Game.player.maxMana,10,16 );


    }
}
