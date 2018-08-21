package com.kingaree.game;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.sound.sampled.*;
import java.applet.AudioClip;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static java.applet.Applet.newAudioClip;

public class Sound extends Thread{
    private AudioInputStream audioStream;
    private Clip clip;

    public Sound(String url){
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(new File(url));
            DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat());
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(stream);
            play();
        } catch( Exception e ) {
                e.printStackTrace();
            }
        }

    public void play() {
        if(clip == null) return;
        stopFile();
        clip.start();

    }

    public void stopFile() {
        if(clip.isRunning()) clip.stop();
    }

    public void close() {
        stopFile();
        clip.close();
    }
}


