/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.only.fx.test;

import com.oim.fx.ui.BaseFrame;
import java.text.NumberFormat;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.converter.FormatStringConverter;

/**
 *
 * @author XiaHui
 */
public class ComponentFrame extends BaseFrame {

    VBox rootVBox = new VBox();

    HBox buttonVBox = new HBox();
    HBox checkBoxVBox = new HBox();
    HBox scrollPaneVBox = new HBox();
    HBox textFieldVBox = new HBox();

    public ComponentFrame() {
        init();
    }

    private void init() {

        this.setCenter(rootVBox);
        this.setBackground("Resources/Images/Wallpaper/1.jpg");
        this.setTitle("登录");
        this.setWidth(380);
        this.setHeight(600);
        this.setRadius(10);

        rootVBox.getChildren().add(buttonVBox);
        rootVBox.getChildren().add(checkBoxVBox);
        rootVBox.getChildren().add(scrollPaneVBox);
        rootVBox.getChildren().add(textFieldVBox);

        StackPane buttonPane = new StackPane();
        buttonPane.setStyle("-fx-background-color:rgba(255, 255, 255, 1)");

        Button button = new Button("4444急急急啊");
        button.setPrefHeight(70);
        button.setPrefWidth(200);
        button.setLayoutX(20);
        button.setLayoutY(140);

        Button button2 = new Button("确定");

        AnchorPane button3Pane = new AnchorPane();

        Button button3 = new Button("确定");
        button3.setPrefHeight(25);
        button3.setPrefWidth(50);
        button3.setLayoutX(20);
        button3.setLayoutY(20);

        button3Pane.getChildren().add(button3);

        Button button4 = new Button("确定");
        button4.setPrefHeight(25);
        button4.setPrefWidth(80);
        button4.setLayoutX(20);
        button4.setLayoutY(50);
        button4.getStyleClass().add("button-border");

        button3Pane.getChildren().add(button4);

        buttonPane.getChildren().add(button);
        buttonVBox.getChildren().add(buttonPane);
        buttonVBox.getChildren().add(button2);
        buttonVBox.getChildren().add(button3Pane);

        CheckBox autoCheckBox = new CheckBox();
        checkBoxVBox.getChildren().add(autoCheckBox);

        StackPane sp = new StackPane();

        // autoCheckBox.setFocusTraversable(true);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setBackground(Background.EMPTY);
        scrollPane.setMinWidth(150);
        scrollPane.setPrefWidth(250);
        scrollPane.setPrefHeight(360);

        scrollPane.setContent(sp);
        //scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.widthProperty().addListener((Observable observable) -> {
            //sp.setPrefWidth(scrollPane.getWidth()-20);
        });

        scrollPaneVBox.getChildren().add(scrollPane);

        DoubleProperty price = new SimpleDoubleProperty(1200.555);
        TextField text = new TextField();
        NumberFormat currencyInstance = NumberFormat.getCurrencyInstance();
       // String symbol = currencyInstance.getCurrency().getSymbol();
        TextFormatter<Number> formatter = new TextFormatter<>(new FormatStringConverter<>(currencyInstance));
        formatter.valueProperty().bindBidirectional(price);
        text.setTextFormatter(formatter);
        text.setMaxSize(140, TextField.USE_COMPUTED_SIZE);

        textFieldVBox.getChildren().add(text);
    }
}
