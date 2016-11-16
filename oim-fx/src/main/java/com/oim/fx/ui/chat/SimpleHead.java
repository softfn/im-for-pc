/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.fx.ui.chat;

import java.util.HashMap;
import java.util.Map;

import com.oim.fx.ui.list.HeadImagePanel;

import javafx.beans.Observable;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author XiaHui
 */
public class SimpleHead extends StackPane {
	private Map<Object, Object> attributeMap = new HashMap<Object, Object>();
	HBox infoBox = new HBox();
	private final ImageView imageView = new ImageView();
	private final Rectangle clip = new Rectangle();
	private final VBox imageBox = new VBox();
	private boolean mouseEntered = false;
	private boolean selected = false;
	HeadImagePanel headImagePanel = new HeadImagePanel();
	Image image;
	TilePane tilePane = new TilePane();
	Label label = new Label();

	public SimpleHead() {
		initComponent();
		iniEvent();
	}

	private void initComponent() {
		this.getChildren().add(infoBox);

		clip.setArcHeight(10);
		clip.setArcWidth(10);

		imageBox.getChildren().add(imageView);
		imageBox.setClip(clip);
		imageBox.widthProperty().addListener((Observable observable) -> {
			clip.setWidth(imageBox.getWidth());
		});
		imageBox.heightProperty().addListener((Observable observable) -> {
			clip.setHeight(imageBox.getHeight());
		});

		tilePane.setPrefColumns(1); // preferred columns
		tilePane.setAlignment(Pos.CENTER);
		tilePane.getChildren().add(headImagePanel);

		infoBox.getChildren().add(tilePane);

		VBox textBox = new VBox();
		textBox.setAlignment(Pos.CENTER_LEFT);
		textBox.getChildren().add(label);

		infoBox.getChildren().add(textBox);

	}

	private void iniEvent() {
		this.setOnMouseEntered((Event event) -> {
			mouseEntered = true;
			updateBackground();
		});
		this.setOnMouseExited((Event event) -> {
			mouseEntered = false;
			updateBackground();
		});
	}

	

	public void updateBackground() {
		if (selected) {
			this.setStyle("-fx-background-color:rgba(255, 255, 255, 0.8);-fx-background-radius: 3;");
		} else {
			if (mouseEntered) {
				this.setStyle("-fx-background-color:rgba(255, 255, 255, 0.3);-fx-background-radius: 3;");
			} else {
				this.setStyle("-fx-background-color:null;");
			}
		}
	}


	public void setText(String text) {
		label.setText(text);
	}

	public void setImage(Image image) {
		headImagePanel.setImage(image);
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		if(this.selected != selected){
			this.selected = selected;
			updateBackground();
		}
	}
	
	public void addAttribute(Object key, Object value) {
		attributeMap.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T getAttribute(Object key) {
		return (T) attributeMap.get(key);
	}
}
