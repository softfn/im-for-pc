/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.fx.ui;

import com.oim.fx.common.box.ImageBox;
import com.oim.swing.UIBox;
import com.only.fx.OnlyFrame;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Modality;

/**
 *
 * @author Only
 */
public class BaseFrame extends OnlyFrame {

	Alert information = new Alert(AlertType.INFORMATION);

	public BaseFrame() {
		init();
	}

	private void init() {
		this.setBackground("Resources/Images/Wallpaper/14.jpg");
		this.setRadius(5);
		Image image = ImageBox.getImagePath("Resources/Images/Logo/logo_64.png");
		this.getIcons().add(image);
		getRootPane().getStylesheets().add(this.getClass().getResource("/resources/common/css/base.css").toString());
		getTitlePane().addOnCloseAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				BaseFrame.this.hide();
			}
		});
		initPrompt();
		UIBox.add(this);
	}

	private void initPrompt() {
		information.initModality(Modality.APPLICATION_MODAL);
		information.initOwner(this);
		information.getDialogPane().setHeaderText(null);
	}

	public void showPrompt(String text) {
		information.getDialogPane().setContentText(text);
		information.showAndWait();
	}
}
