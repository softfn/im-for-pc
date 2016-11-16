/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.fx.ui;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Only
 */
public class NetSettingFrame extends BaseFrame {
    
    
    VBox vBox=new VBox();
    public NetSettingFrame() {
        init();
    }
    
    private void init() {
		StackPane stackPane = new StackPane();
		stackPane.setPrefHeight(50);
		stackPane.setStyle("-fx-background-color:rgba(255, 255, 255, 0.2)");
        
        this.setBackground("Resources/Images/Wallpaper/12.jpg");
        this.setCenter(vBox);
        this.setTitle("登录");
        
        this.setWidth(320);
        this.setHeight(240);
        
        vBox.getChildren().add(stackPane);
    }
}
