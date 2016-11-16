/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.fx.ui.list;

import javafx.beans.Observable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author XiaHui
 */
public class HeadImagePanel extends BorderPane {

//    private final DropShadow dropShadow = new DropShadow();
    private final AnchorPane rootPane = new AnchorPane();
    private final AnchorPane imagePane = new AnchorPane();
    private final ImageView imageView = new ImageView();
    private final Rectangle clip = new Rectangle();

    public HeadImagePanel() {
        initComponent();
        iniEvent();
    }

    private void initComponent() {
        this.getStyleClass().add("head-common-image-pane");
        this.setCenter(rootPane);
        // this.getChildren().add(headImageShowPane);

        rootPane.getChildren().add(imagePane);

        clip.setArcHeight(8);
        clip.setArcWidth(8);

        imagePane.getChildren().add(imageView);

        imagePane.setClip(clip);
        imagePane.widthProperty().addListener((Observable observable) -> {
            clip.setWidth(imagePane.getWidth());
        });
        imagePane.heightProperty().addListener((Observable observable) -> {
            clip.setHeight(imagePane.getHeight());
        });
    }

    private void iniEvent() {
//        this.setOnMouseEntered((Event event) -> {
//            HeadImagePanel.this.setEffect(dropShadow);
//        });
//        this.setOnMouseExited((Event event) -> {
//            HeadImagePanel.this.setEffect(null);
//        });
    }

    public void setImage(Image image) {
        imageView.setImage(image);
    }

//    private void initTest() {
//        Image image = ImageBox.getImagePath("Resources/Images/Head/User/1_100.gif", 60, 60);
//        setImage(image);
//    }
}
