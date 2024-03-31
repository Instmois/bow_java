package com.example.demo;

public class PlayerInfo {
    public String username;
    public String color;
    public int score;
    public int shots;
    public int wins;

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public int getShots() {
        return shots;
    }

    public int getWins() {
        return wins;
    }

    public ArrowInfo arrow = new ArrowInfo();
    public boolean wantToPause = false;
    public boolean wantToStart = false;
    public boolean shooting = false;

    public PlayerInfo(String name, String color) {
        this.username = name;
        this.color = color;
    }
    public PlayerInfo(String name, int wins) {
        this.username = name;
        this.wins = wins;
    }
}
