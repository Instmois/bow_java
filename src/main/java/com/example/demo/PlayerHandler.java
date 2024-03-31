package com.example.demo;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

import static com.example.demo.GameServer.gson;

public class PlayerHandler extends Thread {
    private final GameServer gameServer;
    private final Socket clientSocket;
    private final DataInputStream in;
    private final DataOutputStream out;
    private PlayerInfo playerInfo;
    public PlayerHandler(GameServer server, Socket socket) throws IOException {
        gameServer = server;
        clientSocket = socket;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        setDaemon(true);
        start();
    }
    @Override
    public void run() {
        try {
            checkingPlayers();
            handlingMessage();
        } catch (IOException | SQLException e) {
            stopConnection();
        }
    }
    private void checkingPlayers() throws IOException, SQLException {
        String nickname = in.readUTF();
        while (gameServer.containsNickname(nickname)) {
            out.writeUTF(nickname + " is already in use.");
            nickname = in.readUTF();
        }
        while (gameServer.isGameStarted()) {
            out.writeUTF("The game has already started");
            nickname = in.readUTF();
        }
        out.writeUTF("OK");
        //UserDao.addUser(nickname);
        gameServer.addPlayer(nickname, this);
    }
    private void handlingMessage() throws IOException {
        while (true) {
            String msg = in.readUTF();
            Action.Type actionType = gson.fromJson(msg, Action.Type.class);
            switch (actionType) {
                case WantToStart:
                    playerInfo.wantToStart = true;
                    gameServer.sendWantToStart(this);
                    gameServer.startGame();
                    break;
                case Shoot:
                    playerInfo.shooting = true;
                    ++playerInfo.shots;
                    break;
                case WantToPause:
                    playerInfo.wantToPause = !playerInfo.wantToPause;
                    gameServer.sendWantToPause(this);
                    gameServer.pauseGame();
                    break;
            }
        }
    }
    private void stopConnection() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            gameServer.removePlayer(this);
        }
    }
    public void sendMessage(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            stopConnection();
        }
    }
    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }
    public void setPlayerInfo(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }
}
