package com.example.demo;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Player {
    private String username;
    private int score;
    private int shots;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Player(Socket socket, BufferedReader in, PrintWriter out) {
        this.socket = socket;
        this.in = in;
        this.out = out;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getShots() {
        return shots;
    }

    public void setShots(int shots) {
        this.shots = shots;
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getIn() {
        return in;
    }

    public PrintWriter getOut() {
        return out;
    }
}
