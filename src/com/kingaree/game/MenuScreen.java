package com.kingaree.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuScreen {

    private JFrame frame;
    private JPanel panel1;
    private JButton b1, b2, b3, b4;
    private TextField textField;
    private Sound sound;

    private Handler handler;

    public MenuScreen(Handler handler){
        this.handler = handler;

        createDisplay();
    }

    public void createDisplay(){
        frame = new JFrame();
        frame.setSize(new Dimension(400, 300));
        frame.setLocationRelativeTo(null);
        frame.setTitle("MineSweeper");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setVisible(true);


        GridLayout gridLayout = new GridLayout(2 ,2);
        panel1 = new JPanel();
        panel1.setLayout(gridLayout);


        sound = new Sound("/res/audio/Blip_Select.wav");

        b1 = new JButton("Easy: 20 x 20");
        b2 = new JButton("Medium: 40 x 40");
        b3 = new JButton("Hard: 60 x 60");
        b4 = new JButton("calm down: 80 x 80");

        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                sound.start();


                handler.getGame().createScreen(20, 20);
                frame.setVisible(false);


            }
        });

        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.getGame().createScreen(40, 40);
                frame.setVisible(false);


            }
        });

        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.getGame().createScreen(60, 60);
                frame.setVisible(false);

            }
        });

        b4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.getGame().createScreen(80, 80);
                frame.setVisible(false);


            }
        });




        panel1.add(b1);
        panel1.add(b2);
        panel1.add(b3);
        panel1.add(b4);


        frame.add(panel1);
    }
}
