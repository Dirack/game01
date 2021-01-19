package com.rsgaming.main;

import java.awt.*;

public class Menu {

    public String[] options = {"Novo Jogo", "Carregar Jogo", "Sair"};

    public int currentOption = 0;
    public int maxOption = options.length -1;

    public boolean up,down;

    public void tick() {
        if (up) {
            up = false;
            currentOption--;
            System.out.println("teste up");
            if (currentOption < 0) {
                currentOption = maxOption;
                System.out.println("teste up");
            }
        }
        if (down){
            down = false;
            currentOption++;
            System.out.println("teste down");
            if (currentOption > maxOption) {
                currentOption = 0;
                System.out.println("teste down");
            }
        }
    }

    public void render(Graphics g){
        g.setColor(Color.black);
        g.fillRect(0,0,Game.WIDTH*Game.SCALE, Game.HEIGHT * Game.SCALE);
        g.setColor(Color.red);
        g.setFont(new Font("arial", Font.BOLD, 36));
        g.drawString(">RS Gaming<", (Game.WIDTH*Game.SCALE) / 2 - 120, (Game.HEIGHT*Game.SCALE) / 2 -160 );

   //opcoes menu
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.BOLD, 24));
        g.drawString("Novo Jogo", (Game.WIDTH*Game.SCALE) / 2 - 70, 200 );
        g.drawString("Carregar Jogo", (Game.WIDTH*Game.SCALE) / 2 - 90, 240 );
        g.drawString("Sair", (Game.WIDTH*Game.SCALE) / 2 - 30, 280 );

    if (options[currentOption].equals("Novo Jogo") ){
        g.drawString(">", (Game.WIDTH*Game.SCALE) / 2 - 100, 200 );
    }else if (options[currentOption].equals("Carregar Jogo")){
        g.drawString(">", (Game.WIDTH*Game.SCALE) / 2 - 120, 240 );
    }else if (options[currentOption].equals("Sair")){
        g.drawString(">", (Game.WIDTH*Game.SCALE) / 2 - 120, 280 );
    }
    }
}
