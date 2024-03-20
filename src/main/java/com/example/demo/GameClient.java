package com.example.demo;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

import java.io.*;
import java.net.Socket;

public class GameClient {
    @FXML
    AnchorPane gameBar;
    @FXML
    Circle bigTarget;
    @FXML
    Circle smallTarget;
    @FXML
    VBox playersMenu;
    @FXML
    VBox playersInfoMenu;
    public static final Gson gson = new Gson();
    ServerHandler serverHandler;
    GameState state = GameState.OFF;

    public void connectServer(Socket socket, DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
        serverHandler = new ServerHandler(this, socket, dataInputStream, dataOutputStream);
    }

    @FXML
    void startGame() {
        if (state == GameState.OFF) {
            String jsonStart = gson.toJson(Action.Type.WantToStart);
            serverHandler.sendMessage(jsonStart);
        }
    }

    @FXML
    void stopGame() {
        if (state != GameState.OFF) {
            String json = gson.toJson(Action.Type.WantToPause);
            serverHandler.sendMessage(json);
        }
    }

    @FXML
    void arrowShot() {
        if (state == GameState.ON) {
            String json = gson.toJson(Action.Type.Shoot);
            serverHandler.sendMessage(json);
        }
    }

    public void setGameInfo(final GameInfo gameInfo) {
        for (PlayerInfo p : gameInfo.playerList) {
            addPlayer(p);
        }
    }

    public void addPlayer(final PlayerInfo p) {
        Platform.runLater(() -> {
            Polygon triangle = new Polygon(-57.0, -33.0, -57.0, 22.0, -16.0, -5.0);
            triangle.setId(p.username + "Triangle");
            triangle.setFill(Color.valueOf(p.color));
            if (p.wantToStart) triangle.setStroke(Color.BLACK);
            VBox pane1 = new VBox(triangle);
            pane1.setId(p.username + "Icon");
            pane1.setAlignment(Pos.CENTER);
            pane1.setPadding(new Insets(10));
            playersMenu.getChildren().add(pane1);

            Label score = new Label(p.username + " score:");
            score.setTextFill(Color.valueOf("#4c4f69"));
            score.setId(p.username + "Score");

            Label scoreCount = new Label(String.valueOf(p.score));
            scoreCount.setTextFill(Color.valueOf("#4c4f69"));
            scoreCount.setId(p.username + "ScoreCount");

            Label shots = new Label(p.username + " shots:");
            shots.setTextFill(Color.valueOf("#4c4f69"));
            shots.setId(p.username + "Shots");

            Label shotsCount = new Label(String.valueOf(p.shots));
            shotsCount.setTextFill(Color.valueOf("#4c4f69"));
            shotsCount.setId(p.username + "ShotsCount");

            VBox pane = new VBox( score, scoreCount, shots, shotsCount);
            pane.setBorder(Border.stroke(Color.valueOf("#4c4f69")));
            pane.setAlignment(Pos.CENTER);
            pane.setId(p.username + "VBox");
            playersInfoMenu.getChildren().add(pane);
        });
    }

    private Polygon findTriangle(final String nickname) {
        return (Polygon) gameBar.getScene().lookup("#" + nickname + "Triangle");
    }

    public void setPlayerWantToStart(final String nickname) {
        Polygon playerTriangle = findTriangle(nickname);
        playerTriangle.setStroke(Color.BLACK);
    }

    public void updateGameInfo(final GameInfo gameInfo) {
        Platform.runLater(() -> {
            bigTarget.setLayoutY(gameInfo.bigTarget.y);
            smallTarget.setLayoutY(gameInfo.smallTarget.y);
            for (PlayerInfo p : gameInfo.playerList) {
                Arrow playerArrow = findArrow(p.username);
                if (p.shooting) {
                    if (playerArrow == null) {
                        playerArrow = createArrow(p);
                        setShots(p);
                    }
                    playerArrow.setLayoutX(p.arrow.x);
                } else if (playerArrow != null) {
                    removeArrow(playerArrow);
                    setScore(p);
                }
            }
        });
    }

    private Arrow createArrow(final PlayerInfo p) {
        Arrow arrow = new Arrow(0, 0.0, 45, 0.0, 7.0);
        arrow.setLayoutX(5);
        arrow.setLayoutY(p.arrow.y);
        arrow.setId(p.username + "Arrow");
        gameBar.getChildren().add(arrow);
        return arrow;
    }

    private Arrow findArrow(final String nickname) {
        return (Arrow) gameBar.getScene().lookup("#" + nickname + "Arrow");
    }

    private void removeArrow(final Arrow arrow) {
        gameBar.getChildren().remove(arrow);
    }

    private Label findScoreCountLabel(final String nickname) {
        return (Label) gameBar.getScene().lookup("#" + nickname + "ScoreCount");
    }

    private void setScore(final PlayerInfo p) {
        final Label scoreLabel = findScoreCountLabel(p.username);
        scoreLabel.setText(String.valueOf(p.score));
    }

    private Label findShotsCountLabel(final String nickname) {
        return (Label) gameBar.getScene().lookup("#" + nickname + "ShotsCount");
    }

    public void setShots(final PlayerInfo playerInfo) {
        Label shotsLabel = findShotsCountLabel(playerInfo.username);
        shotsLabel.setText(String.valueOf(playerInfo.shots));
    }

    public void updatePlayerWantToPause(final String playerColor) {
        Polygon playerTriangle = findTriangle(playerColor);
        if (playerTriangle.getStroke() == Color.BLACK) playerTriangle.setStroke(Color.RED);
        else playerTriangle.setStroke(Color.BLACK);
    }

    public void setState(final GameState state) {
        this.state = state;
    }

    public void showWinner(final PlayerInfo p) {
        Platform.runLater(() -> {
            String info = "Congratulations to " + p.username + "!\n" + p.username + " won with " + p.score + " score.";
            Alert alert = new Alert(Alert.AlertType.INFORMATION, info);
            alert.show();
        });
    }

    public void resetGameInfo(final GameInfo gameInfo) {
        Platform.runLater(() -> {
            bigTarget.setLayoutY(gameInfo.bigTarget.y);
            smallTarget.setLayoutY(gameInfo.smallTarget.y);
            for (PlayerInfo p : gameInfo.playerList) {
                setShots(p);
                setScore(p);
                gameBar.getChildren().remove(findArrow(p.username));
                findTriangle(p.username).setStroke(Color.TRANSPARENT);
            }
        });
    }

    public void removePlayer(final String nickname) {
        Platform.runLater(() -> {
            gameBar.getChildren().remove(findArrow(nickname));
            playersMenu.getChildren().remove(findTriangle(nickname));
            playersInfoMenu.getChildren().remove(findVBox(nickname));
        });
    }

    private VBox findVBox(final String nickname) {
        return (VBox) gameBar.getScene().lookup("#" + nickname + "VBox");
    }

    public void showStop() {
        Platform.runLater(() -> {
            String info = "The game was stopped";
            Alert alert = new Alert(Alert.AlertType.WARNING, info);
            alert.show();
        });
    }
}

