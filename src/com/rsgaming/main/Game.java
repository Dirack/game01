package com.rsgaming.main;

import com.rsgaming.entities.Enemy;
import com.rsgaming.entities.Entity;
import com.rsgaming.entities.MagicShoot;
import com.rsgaming.entities.Player;
import com.rsgaming.graficos.Spritesheet;
import com.rsgaming.graficos.Ui;
import com.rsgaming.world.Camera;
import com.rsgaming.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener {


    private Thread thread;
    private boolean isRunning = true;
    private BufferedImage image;

    public static final int WIDTH = 240;
    public static final int HEIGHT = 160;
    public static final int SCALE = 3;

    private int CUR_LEVEL = 1, MAX_LEVEL = 2;

    public static List<Entity> entities;
    public static List<Enemy> enemies;
    public static List<MagicShoot> magic;
    public static Spritesheet spritesheet;
    public static World world;
    public static Player player;
    public static JFrame frame;
    public static Random rand;

    public Ui ui;

    public static String gameState = "MENU";
    private boolean showMessageGameOver = true;
    private int framesGameOver = 0;
    private boolean restartGame = false;

    public Menu menu;

    public Game(){
        rand = new Random();
        addKeyListener(this);
        addMouseListener(this);
        setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
        initFrame();

        //inicializando o jogo.
        ui = new Ui();
        image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
        entities = new ArrayList<Entity>();
        enemies = new ArrayList<Enemy>();
        magic = new ArrayList<MagicShoot>();
        spritesheet = new Spritesheet("/spritesheet.png");
        player = new Player(0,0,16,16,spritesheet.getSprite(32,0, 16,16));
        entities.add(player);
        world = new World("/level1.png");

        menu = new Menu();
    }

    public void initFrame(){
        frame = new JFrame("Game #01");
        frame.add(this);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public synchronized void start(){
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    public synchronized void stop(){
        isRunning = false;
        try{
            thread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }

    public void tick(){

        if(gameState == "NORMAL") {
            this.restartGame = false;
            for (int i = 0; i < entities.size(); i++) {
                Entity e = entities.get(i);
                e.tick();
            }

            for (int i = 0; i < magic.size(); i++) {
                magic.get(i).tick();
            }

            if (enemies.size() == 0) {
                CUR_LEVEL++;
                if (CUR_LEVEL > MAX_LEVEL) {
                    CUR_LEVEL = 1;
                }
                String newWorld = "Level" + CUR_LEVEL + ".png";
                World.restartGame(newWorld);
            }
        } else if (gameState == "GAME_OVER"){
            this.framesGameOver++;
            if(this.framesGameOver == 30){
                this.framesGameOver = 0;
                if (this.showMessageGameOver)
                    this.showMessageGameOver = false;
                else
                    this.showMessageGameOver = true;
            }
            if (restartGame){
                this.restartGame = false;
                this.gameState = "NORMAL";
                CUR_LEVEL = 1;
                String newWorld = "Level" + CUR_LEVEL + ".png";
                World.restartGame(newWorld);
            }else if (gameState == "MENU"){
                menu.tick();
            }
        }
    }

    public void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null){
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = image.getGraphics();
        g.setColor(new Color(0,0,0));
        g.fillRect(0,0,WIDTH,HEIGHT);

        //RENDERIZACAO DO GAME
        //Graphics2D g2 = (Graphics2D) g;

        world.render(g);
        for( int i = 0; i < entities.size(); i++){
            Entity e = entities.get(i);
            e.render(g);
        }
        for (int i = 0; i < magic.size(); i++){
            magic.get(i).render(g);
        }

        ui.render(g);

        g.dispose();
        g = bs.getDrawGraphics();
        g.drawImage(image,0,0,WIDTH*SCALE, HEIGHT*SCALE, null);
        g.setFont(new Font("arial",Font.BOLD,17));
        g.setColor(Color.white);
        if (gameState == "GAME_OVER"){
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(new Color(0,0,0,100));
            g2.fillRect(0,0, WIDTH*SCALE, HEIGHT*SCALE);
            g.setFont(new Font("arial",Font.BOLD,40));
            g.setColor(Color.white);
            g.drawString("Game Over", 250,200 );
            g.setFont(new Font("arial",Font.BOLD,35));
            if(showMessageGameOver)
            g.drawString(">Pressione Enter para Reiniciar<", 90, 250);

        }else if (gameState == "MENU"){
            menu.render(g);
        }
        bs.show();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0 ;
        int frames = 0;
        double timer = System.currentTimeMillis();
        requestFocus();

        while (isRunning){
            long now = System.nanoTime();
            delta+= (now - lastTime) / ns;
            lastTime = now;
            if (delta >=1){
                tick();
                render();
                frames++;
                delta--;
            }

            if (System.currentTimeMillis() - timer >= 1000){
                System.out.println("FPS " + frames);
                frames = 0;
                timer+=1000;
            }
        }
        stop();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
                e.getKeyCode() == KeyEvent.VK_D){
            player.right = true;
        }else if (e.getKeyCode() == KeyEvent.VK_LEFT ||
                e.getKeyCode() == KeyEvent.VK_A){
            player.left = true;
        }if (e.getKeyCode() == KeyEvent.VK_UP ||
                e.getKeyCode() == KeyEvent.VK_W){
            player.up = true;

            if (gameState == "MENU"){
                menu.up = false;

            }
        }else if (e.getKeyCode() == KeyEvent.VK_DOWN ||
                e.getKeyCode() == KeyEvent.VK_S){
            player.down = true;
            if (gameState == "MENU"){
                menu.up = false;
            }


        }
        if (e.getKeyCode() == KeyEvent.VK_X){
            player.shoot = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER){
            this.restartGame = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
                e.getKeyCode() == KeyEvent.VK_D){
            player.right = false;
        }else if (e.getKeyCode() == KeyEvent.VK_LEFT ||
                e.getKeyCode() == KeyEvent.VK_A){
            player.left = false;
        }if (e.getKeyCode() == KeyEvent.VK_UP ||
                e.getKeyCode() == KeyEvent.VK_W){
            player.up = false;
        }else if (e.getKeyCode() == KeyEvent.VK_DOWN ||
                e.getKeyCode() == KeyEvent.VK_S){
            player.down = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        player.mouseShoot = true;
        player.mx = e.getX()/3;
        player.my = e.getY()/3;
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
