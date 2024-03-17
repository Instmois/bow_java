package com.example.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PlayerThread extends Thread {
    private static int numPlayers = 0;
    private String playerName;
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public PlayerThread(Socket socket) {
        this.socket = socket;
        numPlayers++;
    }

    public static int getNumPlayers() {
        return numPlayers;
    }

    public void run() {
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            output.println("Enter your username:");
            playerName = input.readLine();
            System.out.println("Player " + playerName + " connected.");

            // Handle client interactions here

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            numPlayers--;
        }
    }
}
