package com.kingaree.game;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreReader {

    private int winCount;


    public Integer getWinCount(int score) {
        readScore();

        String path = System.getProperty("user.home");
            try {
                FileWriter fr = new FileWriter(path);
                BufferedWriter br = new BufferedWriter(fr);
                br.write(Integer.toString(winCount += score));
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return winCount;
        }


    public void readScore(){
        String path = System.getProperty("user.home");

        List<Integer> list = new ArrayList<Integer>();

        try{

            File file = new File(path);

            if(file.exists()) {
                FileReader fr = new FileReader(path);
                BufferedReader br = new BufferedReader(fr);

                String text = null;

                while ((text = br.readLine()) != null) {
                    list.add(Integer.parseInt(text));
                }

                br.close();

                winCount = list.get(0);
            }

            System.out.println("Score: " + list);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }





}
