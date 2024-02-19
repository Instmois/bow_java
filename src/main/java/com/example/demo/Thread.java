package com.example.demo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class Thread {

    @FXML
    private Button start_game;
    @FXML
    private Label shot_count;
    @FXML
    private Label player_count;
    @FXML
    private Circle small_target;
    @FXML
    private Circle big_target;
    @FXML
    AnchorPane arrow;
    @FXML
    AnchorPane game_bar;
    @FXML
    Polygon arrowhead;
    public int player_counter ;
    public int shot_counter ;
    private boolean isHit = false;
    double arrowSpeed = 2.0;
    double smallTargetSpeed = 4.0;
    double bigTargetSpeed = 2.0;
    double smallTargetDirection = 1.0;
    double bigTargetDirection = 1.0;

    @FXML
    void startGame(ActionEvent event) throws Exception {
        shot_count.setText("" + player_counter);
        player_count.setText("" + shot_counter);
        new java.lang.Thread(
                () -> {
                    while (true)
                    {
                        Platform.runLater(
                                () ->
                                {
                                    double newY = small_target.getLayoutY() + smallTargetSpeed * smallTargetDirection;
                                    if (newY <= 0 || newY >= (game_bar.getHeight() - small_target.getRadius() * 2)) {
                                        smallTargetDirection *= -1;
                                    }
                                    small_target.setLayoutY(newY);

                                    newY = big_target.getLayoutY() + bigTargetSpeed * bigTargetDirection;
                                    if (newY <= 0 || newY >= (game_bar.getHeight() - big_target.getRadius() * 2)) {
                                        bigTargetDirection *= -1;
                                    }
                                    big_target.setLayoutY(newY);
                                }
                        );
                        try {
                            java.lang.Thread.sleep(30);
                        }catch (InterruptedException e){
                            throw new RuntimeException(e);
                        }
                    }
                }
        ).start();
    }
    @FXML
    void stopGame(ActionEvent event) throws Exception {
        shot_count.setText("" + player_counter);
        player_count.setText("" + shot_counter);
    }
    @FXML
    void arrowShot(ActionEvent event) throws Exception {
        shot_counter++;
        shot_count.setText("" + shot_counter);

        isHit = false;
        new java.lang.Thread(
                () -> {
                    while (!isHit)
                    {
                        Platform.runLater(
                                () ->
                                {
                                    arrow.setLayoutX(arrow.getLayoutX() + arrowSpeed);
                                    double centerX = arrowhead.getLayoutX(); // X-координата центра arrowhead
                                    double centerY = arrowhead.getLayoutY(); // Y-координата центра arrowhead
                                    double bigCenterX = big_target.getLayoutX(); // X-координата центра big_target
                                    double bigCenterY = big_target.getLayoutY(); // Y-координата центра big_target
                                    double smallCenterX = small_target.getLayoutX(); // X-координата центра small_target
                                    double smallCenterY = small_target.getLayoutY(); // Y-координата центра small_target
                                    double distance_big = Math.sqrt(Math.pow(centerX - bigCenterX, 2) + Math.pow(centerY - bigCenterY, 2));
                                    System.out.println(distance_big);
                                    if (distance_big <= big_target.getRadius()+ 5) {
                                        player_counter++;
                                        player_count.setText("" + player_counter);
                                        isHit = true;
                                    }
                                    double distance_small = Math.sqrt(Math.pow(centerX - smallCenterX, 2) + Math.pow(centerY - smallCenterY, 2)); // Расстояние между центрами
                                    if (distance_small <= small_target.getRadius()+ 5) {
                                        System.out.println("Стрела попала в круг!");
                                        player_counter += 2;
                                        player_count.setText("" + player_counter);
                                        isHit = true;
                                    }
                                    if(arrow.getLayoutX() >= 195){
                                        isHit = true;
                                        arrow.setLayoutX(-84);
                                        arrow.setLayoutY(0);

                                    }
                                    shot_count.setText("" + shot_counter);
                                    player_count.setText("" + player_counter);

                                }
                        );
                        try {
                            java.lang.Thread.sleep(10);
                        }catch (InterruptedException e){
                            throw new RuntimeException(e);
                        }
                    }
                }
        ).start();

    }
}
