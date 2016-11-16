/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.fx.ui.main;

import com.oim.fx.ui.component.IconButton;
import com.oim.fx.ui.list.HeadImagePanel;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 *
 * @author XiaHui
 */
public class UserDataPanel extends HBox {

    private final HeadImagePanel headImagePanel = new HeadImagePanel();
    private final BorderPane headPane = new BorderPane();
    private final AnchorPane headRootPane = new AnchorPane();

    private final VBox dataRootPane = new VBox();
    private final HBox textPane = new HBox();
    private final HBox infoPane = new HBox();
    private final HBox businessPane = new HBox();

    private final IconButton statusButton = new IconButton();
    private final Label nicknameLabel = new Label();
    private final Label numberLabel = new Label();
    private final Label statusLabel = new Label();
    private final Label textLabel = new Label();

    public UserDataPanel() {
        initComponent();
        iniEvent();
        //iniTest();
    }

    private void initComponent() {
        this.getChildren().add(headRootPane);
        this.getChildren().add(dataRootPane);

        headRootPane.getChildren().add(headPane);

        headRootPane.setPrefHeight(70);
        headRootPane.setPrefWidth(70);

        headPane.setTop(getGapNode(3));
        headPane.setRight(getGapNode(5));
        headPane.setLeft(getGapNode(10));
        headPane.setBottom(getGapNode(3));
        headPane.setCenter(headImagePanel);

        headImagePanel.setPrefWidth(60);
        headImagePanel.setPrefHeight(60);

//        infoPane.setPrefHeight(22);
//        textPane.setPrefHeight(22);
//        businessPane.setPrefHeight(22);
        dataRootPane.getChildren().add(infoPane);
        dataRootPane.getChildren().add(textPane);
        dataRootPane.getChildren().add(businessPane);

        VBox.setVgrow(infoPane, Priority.ALWAYS);
        VBox.setVgrow(textPane, Priority.ALWAYS);
        VBox.setVgrow(businessPane, Priority.ALWAYS);

        infoPane.getChildren().add(statusButton);
        infoPane.getChildren().add(nicknameLabel);
        infoPane.getChildren().add(statusLabel);
        infoPane.getChildren().add(numberLabel);

        textPane.getChildren().add(textLabel);

        //  nicknameLabel.setFont(new Font("微软雅黑",14));
        nicknameLabel.setFont(Font.font("微软雅黑", FontWeight.BOLD, 14));
        nicknameLabel.setStyle("-fx-text-fill: black;");
    }

    private void iniEvent() {
    }

//    private void iniTest() {
//
//        Image normalImage = ImageBox.getImageClassPath("/resources/main/images/top/1.png");
//        Image hoverImage = ImageBox.getImageClassPath("/resources/main/images/top/1.png");
//        Image pressedImage = ImageBox.getImageClassPath("/resources/main/images/top/1.png");
//
//        IconButton iconButton = new IconButton(normalImage, hoverImage, pressedImage);
//        businessPane.getChildren().add(iconButton);
//
//        normalImage = ImageBox.getImageClassPath("/resources/main/images/top/2.png");
//        hoverImage = ImageBox.getImageClassPath("/resources/main/images/top/2.png");
//        pressedImage = ImageBox.getImageClassPath("/resources/main/images/top/2.png");
//
//        iconButton = new IconButton(normalImage, hoverImage, pressedImage);
//        businessPane.getChildren().add(iconButton);
//
//        normalImage = ImageBox.getImageClassPath("/resources/main/images/top/3.png");
//        hoverImage = ImageBox.getImageClassPath("/resources/main/images/top/3.png");
//        pressedImage = ImageBox.getImageClassPath("/resources/main/images/top/3.png");
//
//        iconButton = new IconButton(normalImage, hoverImage, pressedImage);
//        businessPane.getChildren().add(iconButton);
//
//        normalImage = ImageBox.getImageClassPath("/resources/main/images/top/4.png");
//        hoverImage = ImageBox.getImageClassPath("/resources/main/images/top/4.png");
//        pressedImage = ImageBox.getImageClassPath("/resources/main/images/top/4.png");
//
//        iconButton = new IconButton(normalImage, hoverImage, pressedImage);
//        businessPane.getChildren().add(iconButton);
//
//        normalImage = ImageBox.getImageClassPath("/resources/main/images/top/5.png");
//        hoverImage = ImageBox.getImageClassPath("/resources/main/images/top/5.png");
//        pressedImage = ImageBox.getImageClassPath("/resources/main/images/top/5.png");
//
//        iconButton = new IconButton(normalImage, hoverImage, pressedImage);
//        businessPane.getChildren().add(iconButton);
//
//        normalImage = ImageBox.getImageClassPath("/resources/common/images/status/imonline.png");
//
//        setStatusImage(normalImage);
//    }

    private Node getGapNode(double value) {
        AnchorPane tempPane = new AnchorPane();
        tempPane.setPrefWidth(value);
        tempPane.setPrefHeight(value);
        tempPane.setBackground(Background.EMPTY);
        return tempPane;
    }

    public void setHeadImage(Image headImage) {
        headImagePanel.setImage(headImage);
    }

    public void setStatusImage(Image statusImage) {
        statusButton.setNormalImage(statusImage);
    }

    public void setNickname(String nickname) {
        this.nicknameLabel.setText(nickname);
    }

    public void setText(String text) {
        this.textLabel.setText(text);
    }

    public void addBusinessIcon(Node node) {
        businessPane.getChildren().add(node);
    }

    public void removeBusinessIcon(Node node) {
        businessPane.getChildren().remove(node);
    }
    
    public void setStatusOnMouseClicked(EventHandler<? super MouseEvent> value){
    	statusButton.setOnMouseClicked(value);
    }
}
