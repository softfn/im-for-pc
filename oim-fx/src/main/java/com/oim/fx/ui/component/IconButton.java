/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.fx.ui.component;

import javafx.scene.control.Label;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 *
 * @author XiaHui
 */
public class IconButton extends BorderPane {

    private ImageView imageView = new ImageView();
    private Image normalImage = null;
    private Image hoverImage = null;
    private Image pressedImage = null;

    StackPane textPane = new StackPane();
    private boolean mouseEntered = false;
    private boolean mousePressed = false;
    private String text;
    AnchorPane imageBorderPane = new AnchorPane();
    Label label = new Label();
    AnchorPane backgroundPane = new AnchorPane();

    public IconButton() {
        initComponent();
        iniEvent();
    }

    public IconButton(Image normalImage) {
        this.normalImage = normalImage;
        initComponent();
        iniEvent();
    }

    public IconButton(String text, Image normalImage) {
        this.normalImage = normalImage;
        setText(text);
        initComponent();
        iniEvent();
    }

    public IconButton(Image normalImage, Image hoverImage, Image pressedImage) {
        this.normalImage = normalImage;
        this.hoverImage = hoverImage;
        this.pressedImage = pressedImage;
        initComponent();
        iniEvent();
    }

    public IconButton(String text, Image normalImage, Image hoverImage, Image pressedImage) {
        this.normalImage = normalImage;
        this.hoverImage = hoverImage;
        this.pressedImage = pressedImage;
        setText(text);
        initComponent();
        iniEvent();
    }

    private void initComponent() {
        //this.setGraphic(imageView);
        //this.getChildren().add(imageView);
        this.setTop(getGapNode(1));
        this.setRight(getGapNode(1));
        this.setLeft(getGapNode(1));
        this.setBottom(getGapNode(1));
        this.setCenter(imageBorderPane);
//        this.getChildren().add(backgroundPane);

        textPane.getChildren().add(label);

        HBox box = new HBox();

        box.getChildren().add(imageView);
        box.getChildren().add(textPane);

//        imageBorderPane.setTop(getGapNode(2));
//        imageBorderPane.setRight(getGapNode(2));
//        imageBorderPane.setLeft(getGapNode(2));
//        imageBorderPane.setBottom(getGapNode(2));
//        imageBorderPane.setCenter(box);
//        imageBorderPane.getStyleClass().add("icon-button");
        imageBorderPane.getChildren().add(backgroundPane);

        backgroundPane.getChildren().add(box);
        backgroundPane.getStyleClass().add("icon-button");
        updateImage();
    }

    private void iniEvent() {
        this.setOnMouseEntered((Event event) -> {
            mouseEntered = true;
            updateImage();
        });
        this.setOnMouseExited((Event event) -> {
            mouseEntered = false;
            updateImage();
        });
        this.setOnMousePressed((Event event) -> {
            mousePressed = true;
            updateImage();
        });
        this.setOnMouseReleased((Event event) -> {
            mousePressed = false;
            updateImage();
        });
    }

    private Node getGapNode(double value) {
        AnchorPane tempPane = new AnchorPane();
        tempPane.setPrefWidth(value);
        tempPane.setPrefHeight(value);
        tempPane.setMinHeight(value);
        tempPane.setMinWidth(value);
        tempPane.setBackground(Background.EMPTY);
        return tempPane;
    }

    private void updateImage() {

        if (!this.mousePressed) {
            if (mouseEntered) {
                if (null != hoverImage) {
                    imageView.setImage(hoverImage);
                }
            } else if (null != normalImage) {
                imageView.setImage(normalImage);
            }
        } else if (null != pressedImage) {
            imageView.setImage(pressedImage);
        }
    }

    public Image getNormalImage() {
        return normalImage;
    }

    public void setNormalImage(Image normalImage) {
        this.normalImage = normalImage;
        updateImage();
    }

    public Image getHoverImage() {
        return hoverImage;
    }

    public void setHoverImage(Image hoverImage) {
        this.hoverImage = hoverImage;
        updateImage();
    }

    public Image getPressedImage() {
        return pressedImage;
    }

    public void setPressedImage(Image pressedImage) {
        this.pressedImage = pressedImage;
        updateImage();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        label.setText(text);
    }

}
