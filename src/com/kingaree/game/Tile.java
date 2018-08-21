package com.kingaree.game;

import java.awt.*;
import java.net.URL;

public class Tile {

        private float mX, mY;
        private int x, y, size = 20, id = 0, tileID;
        private boolean isBomb = false, isRevealed = false, emptyTileUpdated = false, isFlagged = false,
                                addedRevealed = false, revealHovered;

        private Handler handler;

        public Tile(Handler handler, int x, int y){
            this.handler = handler;
            this.x = x;
            this.y = y;
        }

        public void tick(){
            mX = handler.getGame().getScreen().getMouseListener().getX();
            mY = handler.getGame().getScreen().getMouseListener().getY();


            unRevealedLogic();
            flagTile();
            quickRevealHoverLogic();


            if(isBomb){
                isFlagged = true;
            }
            //check if tile has no bombs around it, if so reveal it
            if(isRevealed && !emptyTileUpdated) {//if emptytile is true then tile has value
                if(id == 0 && !isBomb){
                    handler.getGame().getDefaultMinefield().revealEmptyTiles(tileID);
                    emptyTileUpdated = true;
                    Sound sound = new Sound("/res/audio/Blip_Reveal.wav");
                    sound.start();
                }
            }

            if(isRevealed){//check if tile can be middle-clicked for quick reveal method
                quickReveal();
            }

            if(isRevealed && !addedRevealed){
                handler.getGame().getDefaultMinefield().addRevealedCount(1);
                addedRevealed = true;
            }

        }

        public void render(Graphics2D g2d){

            if(isRevealed){
                 if(isBomb){
                     renderBombImage(g2d);
                }else {
                    renderRevealedImage(g2d);
                }

                if(id != 0) {
                    g2d.setColor(Color.white);
                    g2d.drawString(String.valueOf(id), x + 7, y + 16);
                }
            }else{
                unRevealedTile(g2d);
            }


        }

        public void unRevealedLogic(){
            if (mX >= x && mX <= x + size) {
                if (mY >= y && mY <= y + size) {
                    if (handler.getGame().getScreen().getMouseListener().isMouseClicked()) {
                        handler.getGame().getScreen().getMouseListener().setMouseClicked(false);
                        if (!isBomb) {
                            isRevealed = true;
                            Sound sound = new Sound("/res/audio/Blip_Select.wav");
                            sound.start();
                        } else {
                            if(!isFlagged) {
                                handler.getGame().getDefaultMinefield().explodeTiles();
                                Sound sound = new Sound("/res/audio/Explosion.wav");
                                sound.start();
                                    if (!handler.getGame().getScreen().isGameOverWindowOpen())
                                        handler.getGame().getScreen().gameOverWindow(0);


                            }
                        }
                    }
                }
            }
        }

        public void flagTile(){
            if (mX >= x && mX <= x + size) {
                if (mY >= y && mY <= y + size) {
                    if(handler.getGame().getScreen().getMouseListener().isMouseRightButtonClicked()){
                        handler.getGame().getScreen().getMouseListener().setMouseRightButtonClicked(false);
                        isFlagged =! isFlagged; //toggle flag
                    }
                }
            }
        }

        public void quickReveal(){
            if (mX >= x && mX <= x + size) {
                if (mY >= y && mY <= y + size) {
                    if(handler.getGame().getScreen().getMouseListener().isMouseMiddlePressed()) {
                        handler.getGame().getScreen().getMouseListener().setMouseMiddleClicked(false);

                        handler.getGame().getDefaultMinefield().revealUnFlaggedVicinity(tileID, this);

                    }
                }
            }
        }

        public void unRevealedTile(Graphics2D g2d){
            g2d.setColor(new Color(192,192,192));
            g2d.fillRect(x , y, size, size);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y , size, size);

            if(isFlagged){
                g2d.setColor(Color.red);
                g2d.fillOval(x , y, size, size);
            }

            if(revealHovered && !isFlagged ){
                g2d.setColor(new Color(51, 133, 255));
                g2d.fillRect(x , y, size, size);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(x, y , size, size);
            }

        }

        public void quickRevealHoverLogic(){
            if (mX >= x && mX <= x + size) {
                if (mY >= y && mY <= y + size) {
                    if (handler.getGame().getScreen().getMouseListener().isMouseMiddlePressed() && isRevealed) {
                        revealHovered = true;
                    } else {
                        revealHovered = false;

                    }
                    handler.getGame().getDefaultMinefield().revealHoverLogic(tileID, revealHovered);
                }
            }
        }

        public void renderBombImage(Graphics2D g2d){
            g2d.setColor(Color.red);
            g2d.fillRect(x, y, size, size);
            g2d.setColor(Color.black);
            g2d.drawRect(x, y, size, size);
        }

        public void renderRevealedImage(Graphics2D g2d){
            g2d.setColor(Color.blue);
            g2d.fillRect(x, y, size, size);
            g2d.setColor(Color.black);
            g2d.drawRect(x, y, size, size);
        }

    public boolean isBomb() {
        return isBomb;
    }

    public void setBomb(boolean bomb) {
        isBomb = bomb;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addBombCount(){
            id += 1;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    public int getTileID() {
        return tileID;
    }

    public void setTileID(int tileID) {
        this.tileID = tileID;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    public boolean isRevealHovered() {
        return revealHovered;
    }

    public void setRevealHovered(boolean revealHovered) {
        this.revealHovered = revealHovered;
    }
}
