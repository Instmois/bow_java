package com.example.demo;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadController {

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
    private Button stop;
    @FXML
    Line arrow;
    @FXML
    AnchorPane game_bar;
    public int player_counter;
    public int shot_counter;
    double arrowSpeed = 2.0;
    double smallTargetSpeed = 4.0;
    double bigTargetSpeed = 2.0;
    double smallTargetDirection = 1.0;
    double bigTargetDirection = 1.0;
    boolean arrowAllow;
    boolean gameStarted = false;
    private Thread thread_target;;

    @FXML
    void startGame(ActionEvent event) throws Exception {
        if(gameStarted){return;}
        else{
            gameStarted = true;
            resetStat();
            thread_target = new Thread(
                    () -> {
                        while (true)
                        {
                            Platform.runLater( ()->{
                                moveTarget();
                                moveArrow();
                            });
                            try {
                                Thread.sleep(30);
                            }catch (InterruptedException e){
                                throw new RuntimeException(e);
                            }
                        }
                    }
            );
            thread_target.start();
        }

        }
    @FXML
    void stopGame(ActionEvent event){
        thread_target.interrupt();
        gameStarted = false;
        big_target.setLayoutX(236);
        big_target.setLayoutY(155);
        small_target.setLayoutX(311);
        small_target.setLayoutY(155);
        arrow.setLayoutX(115);
    }

    @FXML
    void arrowShot(ActionEvent event) throws Exception {
        arrowAllow = true;
        shot_counter++;
        shot_count.setText("" + shot_counter);
    }
    void moveTarget(){
        double newY = small_target.getLayoutY() + smallTargetSpeed * smallTargetDirection;
        if (newY <= 0 || newY >= (game_bar.getHeight() - small_target.getRadius())) {
            smallTargetDirection *= -1;
        }
        small_target.setLayoutY(newY);

        newY = big_target.getLayoutY() + bigTargetSpeed * bigTargetDirection;
        if (newY <= 0 || newY >= (game_bar.getHeight() - big_target.getRadius())) {
            bigTargetDirection *= -1;
        }
        big_target.setLayoutY(newY);
    }

    void resetStat(){
        shot_counter = 0;
        player_counter = 0;
        shot_count.setText("" + shot_counter);
        player_count.setText("" + player_counter);
    }
    void moveArrow(){
        if(arrowAllow){
            arrow.setLayoutX(arrow.getLayoutX() + arrowSpeed);
            checkShot();
            if (arrow.getLayoutX() >= 395) {
                arrowAllow = false;
                arrow.setLayoutX(115);
            }
        }
    }
    void checkShot(){
        double bigTargetCenterX = big_target.getBoundsInParent().getMinX() + big_target.getBoundsInParent().getWidth() / 2;
        double bigTargetCenterY = big_target.getBoundsInParent().getMinY() + big_target.getBoundsInParent().getHeight() / 2;
        double smallTargetCenterX = small_target.getBoundsInParent().getMinX() + small_target.getBoundsInParent().getWidth() / 2;
        double smallTargetCenterY = small_target.getBoundsInParent().getMinY() + small_target.getBoundsInParent().getHeight() / 2;
        double arrowCenterX = arrow.getBoundsInParent().getMinX() + arrow.getBoundsInParent().getWidth() / 2;
        double arrowCenterY = arrow.getBoundsInParent().getMinY() + arrow.getBoundsInParent().getHeight() / 2;

        if (Math.pow(arrowCenterX - bigTargetCenterX, 2) + Math.pow(arrowCenterY - bigTargetCenterY, 2) <= Math.pow(big_target.getRadius(), 2)) {
            player_counter++;
            player_count.setText("" + player_counter);
            arrow.setLayoutX(115);
            arrowAllow = false;
        }
        if (Math.pow(arrowCenterX - smallTargetCenterX, 2) + Math.pow(arrowCenterY - smallTargetCenterY, 2) <= Math.pow(small_target.getRadius(), 2)) {
            System.out.println("Стрела попала в круг!");
            player_counter += 2;
            player_count.setText("" + player_counter);
            arrow.setLayoutX(115);
            arrowAllow = false;
        }
        shot_count.setText("" + shot_counter);
        player_count.setText("" + player_counter);
    }
}
