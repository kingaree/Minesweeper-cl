package com.kingaree.game;

public class Handler {

    private Game game;
    private DefaultMinefield defaultMinefield;


    public Handler(Game game){

        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
