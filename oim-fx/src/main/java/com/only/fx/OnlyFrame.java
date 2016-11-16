/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.only.fx;

import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.Observable;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Only
 */
public class OnlyFrame extends OnlyStage {

   // private final GaussianBlur gaussianBlur = new GaussianBlur();
    private final DropShadow dropShadow = new DropShadow();
    private final BorderPane rootPane = new BorderPane();

    private final StackPane backgroundPane = new StackPane();
    private final StackPane stackPane = new StackPane();
    private final StackPane centerPane = new StackPane();
    private final Rectangle clip = new Rectangle();
    private TitlePane titlePane;
    private final OnlyScene scene = new OnlyScene(getRootPane());
    private final Move move = new Move(this, scene);

    public OnlyFrame() {
        initFrame();
    }

    private void initFrame() {
        this.setScene(scene);
        //scene.getStylesheets().add(this.getClass().getResource("/resources/css/base.css").toString());
        
       // gaussianBlur.setRadius(60);
        //backgroundPane.setEffect(gaussianBlur);
        titlePane = move.getTitlePane();
        scene.setFill(Color.TRANSPARENT);

        dropShadow.setBlurType(BlurType.GAUSSIAN);
        //dropShadow.setSpread(0.1);

        getRootPane().setBackground(Background.EMPTY);
        getRootPane().setEffect(dropShadow);

        // borderPane.setTop(windowButtons);
        getRootPane().setCenter(stackPane);
        getRootPane().setTop(getGapNode(8));
        getRootPane().setRight(getGapNode(8));
        getRootPane().setLeft(getGapNode(8));
        getRootPane().setBottom(getGapNode(8));

//        windowButtons.addOnCloseAction(new EventHandler(){
//            @Override
//            public void handle(Event event) {
//                new FadeOutUpTransition(root).play();
//               // new BounceTransition(root).play();
//            }
//        });
        // pane.setStyle("-fx-background-color:rgba(111, 145, 112, 0.8)");
        // borderPane.getChildren().add(pane);
        // borderPane.setStyle("-fx-background-color:  rgba(197, 76, 95, 1)");
        stackPane.setClip(clip);
        stackPane.widthProperty().addListener((Observable observable) -> {
            clip.setWidth(stackPane.getWidth());
        });
        stackPane.heightProperty().addListener((Observable observable) -> {
            clip.setHeight(stackPane.getHeight());
        });

        stackPane.getChildren().add(backgroundPane);
        stackPane.getChildren().add(centerPane);
        stackPane.getChildren().add(getTitlePane());

        // setRadius(3);
        //setDropShadowRadius(6);
        // setSpread(0.12);
    }

    public void setCenter(Node value) {
        centerPane.getChildren().clear();
        centerPane.getChildren().add(value);
    }

    public void setBackground(String imagePath) {
        try {
            imagePath = imagePath.replace("\\", "/");
            String pathString = new File(imagePath).toURI().toURL().toString();
            backgroundPane.setStyle("-fx-background-image:url(\"" + pathString + "\");");
        } catch (MalformedURLException ex) {
            Logger.getLogger(OnlyFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setBackgroundColor(Color color) {
        centerPane.setStyle("-fx-background-color:rgba(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ", " + color.getOpacity() + ")");
    }

    public Node getGapNode(double value) {
        AnchorPane pane = new AnchorPane();
        pane.setPrefWidth(value);
        pane.setPrefHeight(value);
        pane.setBackground(Background.EMPTY);
        return pane;
    }

    public void setRadius(double value) {
        clip.setArcHeight(value);
        clip.setArcWidth(value);
    }

    public void setDropShadowRadius(double value) {
        dropShadow.setRadius(value);
    }

    public void setSpread(double value) {
        dropShadow.setSpread(value);
    }

    public TitlePane getTitlePane() {
        return titlePane;
    }

    public BorderPane getRootPane() {
        return rootPane;
    }

    public void setTitlePaneStyle(int style) {
        titlePane.setTitlePaneStyle(style);
    }
}
