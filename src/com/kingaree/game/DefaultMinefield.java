package com.kingaree.game;

import org.omg.CORBA.TIMEOUT;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class DefaultMinefield {

    private Game game;
    private Tile[][] tiles;
    private int x = 0, y =0, bombCount = 0, revealedCount = 0, tileCount, bombsLeft;
    private Handler handler;
    private boolean gameCompleted = false;


    public DefaultMinefield(Handler handler) {
        this.handler = handler;
        int width = handler.getGame().getScreen().getWidth();
        int height = handler.getGame().getScreen().getHeight();

        tiles = new Tile[height / 20][width / 20];
        tileCount = (height / 20) * (width / 20);

        bombCount = (((height / 20) * (width / 20)) / 100) * 18;
        bombsLeft = bombCount;

        int i= 0;
        for (int row = 0; row < tiles.length; row++) {
            x = 0;
            for (int column = 0; column < tiles[row].length; column++){

                tiles[row][column] = new Tile(handler, x, y);
                tiles[row][column].setTileID(i);
                i++;
                x += 20;
            }
            y += 20;
        }

        setBombs(bombsLeft, tiles);
        checkVicinity(tiles);
    }

    public void setBombs(int bombsL, Tile[][] tileBombs){
        Random rand = new Random();
        while(bombsL > 0){
            for(int row = 0; row < tileBombs.length; row++) {
                if(bombsL <= 0){
                    break;
                }
                for (int column = 0; column < tileBombs[row].length; column++) {
                    if(bombsL <= 0){
                        break;
                    }
                    if (rand.nextInt(100) <= 2) {
                        if(!tileBombs[row][column].isBomb()) {//if more than one interation
                            tileBombs[row][column].setBomb(true);
                            bombsL -= 1;
                        }
                        if(column == tileBombs.length && row == tileBombs[row].length){//if not all bombs planted on first interation re-do
                            column = 0;
                            row = 0;
                        }
                    }
                }
            }
        }
    }

    public void revealEmptyTiles(int tileID){

        ArrayList<Tile> emptyTiles = getSurroundingTiles(tileID);

        for(Tile i : emptyTiles){
            i.setRevealed(true);
        }

    }

    public void checkVicinity(Tile[][] tilesToCheck){
        int[][] borderTiles = new int[][]{{-1, -1}, {0, -1}, {+1, -1}, {-1, 0}, {+1, 0}, {-1, +1}, {0, + 1}, {+1, +1}};

        for (int row = 0; row < tilesToCheck.length; row++) {
            for (int column = 0; column < tilesToCheck[row].length; column++){
                if(!tilesToCheck[row][column].isBomb()) {
                    for(int[] direction : borderTiles){
                        int dx = column + direction[0];
                        int dy = row + direction[1];

                        if(dy >= 0 && dy < tilesToCheck.length){
                            if(dx >= 0 && dx < tilesToCheck[row].length){
                                if(tilesToCheck[dy][dx].isBomb()){
                                    tilesToCheck[row][column].addBombCount();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void tick(){
        checkIfGameCompleted();
        handler.getGame().getScreen().setFrameBombCount(bombCount);

        for (int row = 0; row < tiles.length; row++) {
            for (int column = 0; column < tiles[row].length; column++){
                tiles[row][column].tick();

            }
        }
    }

    public void render(Graphics2D g2d) {
        for (int row = 0; row < tiles.length; row++) {
            for (int column = 0; column < tiles[row].length; column++) {
                tiles[row][column].render(g2d);
            }
        }
    }



    public ArrayList<Tile> getSurroundingTiles(int tileID){
        int[][] surroundingTiles = new int[][]{{-1, -1}, {0, -1}, {+1, -1}, {-1, 0}, {+1, 0}, {-1, +1}, {0, + 1}, {+1, +1}};
        ArrayList<Tile> newTiles = new ArrayList<Tile>();

        for(int row = 0; row < tiles.length; row++){
            for(int col = 0; col < tiles[row].length; col++){
                if(tiles[row][col].getTileID() == tileID){
                    for(int[] direction: surroundingTiles){
                        int dx = col + direction[0];
                        int dy = row + direction[1];
                        int i = 0;
                        if (dy >= 0 && dy < tiles.length) {
                            if (dx >= 0 && dx < tiles[row].length) {
                                newTiles.add(tiles[dy][dx]);
                            }
                        }
                    }
                }
            }
        }
        return newTiles;
    }



    public void explodeTiles(){
        for (int row = 0; row < tiles.length; row++) {
            for (int column = 0; column < tiles[row].length; column++) {

                    tiles[row][column].setRevealed(true);

            }
        }
    }

    public void revealUnFlaggedVicinity(int tileID, Tile originalTile){
        int length = getSurroundingTiles(tileID).size();
        ArrayList<Tile> tilesUnFlagged = getSurroundingTiles(tileID);

        int numFlagged = 0;

        for(int o = 0; o < length; o++) {
            if (tilesUnFlagged.get(o).isFlagged()) {
                numFlagged += 1;
            }
                for (int i = 0; i < length; i++) {
                    if (!tilesUnFlagged.get(i).isRevealed() && !tilesUnFlagged.get(i).isFlagged()) {
                        if (numFlagged >= originalTile.getId()) {
                            tilesUnFlagged.get(i).setRevealed(true);

                            Sound sound = new Sound("/res/audio/Quick_Reveal.wav");
                            sound.start();

                            if (tilesUnFlagged.get(i).isBomb()) {
                             explodeTiles();
                                if (!handler.getGame().getScreen().isGameOverWindowOpen())
                                    handler.getGame().getScreen().gameOverWindow(0);
                            }
                        } else {
                        //create animation to show that more tiles need to be flagged in order for quickreveal to work
                        //maybe set color of tile to different colour while mouse is clicked + fix mouseclicks
                    }
                }
            }
        }
    }

    public void revealHoverLogic(int tileID, boolean bool){
        ArrayList<Tile> newTiles = getSurroundingTiles(tileID);

        for(Tile i : newTiles){
            i.setRevealHovered(bool);
        }
    }


    private void checkIfGameCompleted() {
         //System.out.println(revealedCount + " " + tileCount + " " + bombCount);
        if(revealedCount == (tileCount - bombCount)){
            handler.getGame().getScreen().gameOverWindow(1);
            revealedCount = 0;
        }
    }

    public int getRevealedCount() {
        return revealedCount;
    }

    public void addRevealedCount(int revealedCount) {
        this.revealedCount += revealedCount;
    }

    public void addBombCount(int i){
        bombCount += i;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public int getBombCount() {
        return bombCount;
    }

    public void setBombCount(int bombCount) {
        this.bombCount = bombCount;
    }
}


