package com.kingaree.game;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.nio.Buffer;

public class Game extends JPanel implements Runnable{

    private MenuScreen menuScreen;
    private Screen screen;
    private DefaultMinefield defaultMinefield;

    private boolean running = false;
    private Thread thread;
    private Handler handler;

    private BufferStrategy bufferStrategy;

    public Game(){
        handler = new Handler(this);
        init();

    }

    public void init(){
        menuScreen = new MenuScreen(handler);

    }

    public synchronized void start(){
        thread = new Thread(this);
        thread.start();
        running = true;
        handler.setGame(this);
    }

    public synchronized void stop(){
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        long lastTime = System.nanoTime();
        double frameLimit = 60.0;
        double ns = 1000000000 / frameLimit;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;

            while(running){
                long now = System.nanoTime();

                delta += (now - lastTime) / ns;
                lastTime = now;

                while(delta >= 1){
                    tick();
                    if(screen != null) {
                        screen.repaint();
                    }
                    delta--;
                    frames++;
                }



                if(System.currentTimeMillis() - timer > 1000){
                    timer += 1000;
                   // System.out.println("FPS: " + frames);
                    frames = 0;
                }
            }
        stop();
    }

    public void tick(){
        if(defaultMinefield != null) {
            defaultMinefield.tick();
        }
    }

    public void createScreen(int width, int height){
        screen = new Screen(handler,width * 10, height * 10);
        defaultMinefield = new DefaultMinefield(handler);
    }


    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public DefaultMinefield getDefaultMinefield() {
        return defaultMinefield;
    }

    public void setDefaultMinefield(DefaultMinefield defaultMinefield) {
        this.defaultMinefield = defaultMinefield;
    }

    public MenuScreen getMenuScreen() {
        return menuScreen;
    }

    public void setMenuScreen(MenuScreen menuScreen) {
        this.menuScreen = menuScreen;
    }
}



