package com.example.demo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameServer {
    private static final int PORT = 9090;
    private static final int MAX_PLAYERS = 4;
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started, waiting for players...");
            while (true) {
                if (PlayerThread.getNumPlayers() < MAX_PLAYERS) {
                    Socket socket = serverSocket.accept();
                    PlayerThread handler = new PlayerThread(socket);
                    handler.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}