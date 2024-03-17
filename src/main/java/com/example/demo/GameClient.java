package com.example.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 9090;
    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to server.");
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
            // Внутри main метода класса Client
            String serverResponse;
            while ((serverResponse = input.readLine()) != null) {
                System.out.println(serverResponse);
                if (serverResponse.startsWith("Enter your username:")) {
                    String username = consoleInput.readLine();
                    output.println(username);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

