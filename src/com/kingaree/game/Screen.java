package com.kingaree.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

public class Screen extends JPanel{

    private JFrame frame;
    private String title;
    private Handler handler;
    private int width, height;
    private LayoutManager layoutManager;
    private String wins;

    private boolean gameOverWindowOpen = false;

    private MouseListener mouseListener;
    private BorderLayout borderLayout = new BorderLayout();


    public Screen(Handler handler,int width, int height){
        this.handler = handler;
        this.width = width;
        this.height = height;

        createDisplay();
    }

    public void createDisplay(){
        frame = new JFrame();
        frame.setTitle(title);
        frame.setSize(new Dimension(width, height));
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setPreferredSize(new Dimension(width, height));
        this.setMaximumSize(new Dimension(width, height));
        this.setMinimumSize(new Dimension(width, height));

        mouseListener = new MouseListener();

        frame.addMouseListener(mouseListener);
        frame.addMouseMotionListener(mouseListener);

        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);
        this.setLayout(new GridBagLayout());

        frame.add(this);
        frame.pack();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        handler.getGame().getDefaultMinefield().render(g2d);
    }

    public void setFrameBombCount(int bombCount){
        frame.setTitle("Bombs left: " + bombCount);
    }

    public void gameOverWindow(int score){

       ScoreReader sr = new ScoreReader();

        gameOverWindowOpen = true;
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(200, 100));
        JLabel label = new JLabel("Game Over", SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(40, 15));

        JLabel winTotal = new JLabel("Total Wins: " + sr.getWinCount(score));
        winTotal.setHorizontalAlignment(JLabel.CENTER);

        JButton button_01 = new JButton("new");
        button_01.setPreferredSize(new Dimension(100, 10));

        button_01.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.getGame().setMenuScreen(new MenuScreen(handler));
                gameOverWindowOpen = false;
                frame.dispose();

            }
        });

        JButton button_02 = new JButton("Replay");

        button_02.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                handler.getGame().setScreen(new Screen(handler, width, height));
                handler.getGame().setDefaultMinefield(new DefaultMinefield(handler));
            }
        });
        button_02.setPreferredSize(new Dimension(100, 10));

        panel.add(label, BorderLayout.NORTH);
        panel.add(button_01, BorderLayout.WEST);
        panel.add(button_02, BorderLayout.EAST);
        panel.add(winTotal, BorderLayout.SOUTH);

        this.add(panel);
        this.revalidate();
    }



    public String getWins() {
        return wins;
    }

    public void setWins(String wins) {
        this.wins = wins;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public MouseListener getMouseListener() {
        return mouseListener;
    }

    public void setMouseListener(MouseListener mouseListener) {
        this.mouseListener = mouseListener;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public boolean isGameOverWindowOpen() {
        return gameOverWindowOpen;
    }

    public void setGameOverWindowOpen(boolean gameOverWindowOpen) {
        this.gameOverWindowOpen = gameOverWindowOpen;
    }
}
