/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.fx.ui.list;

import java.util.HashMap;
import java.util.Map;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 *
 * @author XiaHui
 */
public class HeadItem extends HBox {

	private Map<Object, Object> attributeMap = new HashMap<Object, Object>();

	private final StackPane headBaseRootPane = new StackPane();
	private final BorderPane headRootPane = new BorderPane();
	private final AnchorPane headImageShowPane = new AnchorPane();
	private final AnchorPane headImagePane = new AnchorPane();
	private final ImageView headImageView = new ImageView();
	private final Rectangle headClip = new Rectangle();
	AnchorPane pane = new AnchorPane();

	private final VBox textBaseRootPane = new VBox();
	private final HBox textRootPane = new HBox();
	private final HBox infoPane = new HBox();
	private final HBox businessPane = new HBox();

	private final Label remarkLabel = new Label();
	private final Label nicknameLabel = new Label();
	private final Label numberLabel = new Label();
	private final Label statusLabel = new Label();
	private final Label textLabel = new Label();

	private String remark;
	private String nickname;
	private String status;
	private String showText;
	private final Timeline animation = new Timeline();

	private WritableImage grayImage = null;
	private Image normalImage = null;
	private boolean gray=false;

	public HeadItem() {
		initComponent();
		iniEvent();
		// iniTest();
	}

	private void initComponent() {
		this.getStyleClass().add("list-head-item");
		headBaseRootPane.getStyleClass().add("head-image-pane");

		headBaseRootPane.getChildren().add(headRootPane);

		// headRootPane.setPrefWidth(55);
		// headRootPane.setPrefHeight(55);
		headRootPane.setTop(getGapNode(3));
		headRootPane.setRight(getGapNode(5));
		headRootPane.setLeft(getGapNode(5));
		headRootPane.setBottom(getGapNode(3));
		headRootPane.setCenter(headImageShowPane);

		headImageShowPane.getChildren().add(pane);

		headImageShowPane.setPrefWidth(48);
		headImageShowPane.setPrefHeight(48);

		pane.getStyleClass().add("head-image-show");

		pane.getChildren().add(headImagePane);
		pane.setPrefWidth(44);
		pane.setPrefHeight(44);
		pane.setLayoutX(2);
		pane.setLayoutY(2);

		headImagePane.getChildren().add(headImageView);

		headImagePane.setPrefWidth(40);
		headImagePane.setPrefHeight(40);
		headImagePane.setLayoutX(2);
		headImagePane.setLayoutY(2);

		headClip.setArcHeight(8);
		headClip.setArcWidth(8);

		headClip.setWidth(40);
		headClip.setHeight(40);
		headImagePane.setClip(headClip);

		this.getChildren().add(headBaseRootPane);
		this.getChildren().add(textBaseRootPane);

		textBaseRootPane.getChildren().add(getGapNode(12));
		textBaseRootPane.getChildren().add(infoPane);
		textBaseRootPane.getChildren().add(textRootPane);
		// textBaseRootPane.getChildren().add(getGapNode(5));

		infoPane.getChildren().add(remarkLabel);
		infoPane.getChildren().add(nicknameLabel);
		infoPane.getChildren().add(numberLabel);
		infoPane.getChildren().add(statusLabel);

		textRootPane.getChildren().add(businessPane);
		textRootPane.getChildren().add(textLabel);

		headBaseRootPane.setPrefWidth(60);
		headBaseRootPane.setMinWidth(60);

		remarkLabel.setStyle("-fx-text-fill:#000000;-fx-font-size:13px;");
		nicknameLabel.setStyle("-fx-text-fill:#888888;-fx-font-size:13px;");
		numberLabel.setStyle("-fx-text-fill:#888888;-fx-font-size:13px;");
		statusLabel.setStyle("-fx-text-fill:#888888;-fx-font-size:13px;");
		textLabel.setStyle("-fx-text-fill:#888888;-fx-font-size:13px;");

	}

	private void iniEvent() {
		this.setOnMouseClicked((Event event) -> {
			if (!HeadItem.this.isFocused()) {
				HeadItem.this.requestFocus();
			}
		});
		// animation.setAutoReverse(true);
		animation.setCycleCount(Animation.INDEFINITE);

		KeyValue kx1 = new KeyValue(pane.layoutXProperty(), pane.getLayoutX() + 1);
		KeyValue ky1 = new KeyValue(pane.layoutYProperty(), pane.getLayoutY() - 1);

		KeyValue kx2 = new KeyValue(pane.layoutXProperty(), pane.getLayoutX() + 1);
		KeyValue ky2 = new KeyValue(pane.layoutYProperty(), pane.getLayoutY() + 1);

		KeyValue kx3 = new KeyValue(pane.layoutXProperty(), pane.getLayoutX() - 1);
		KeyValue ky3 = new KeyValue(pane.layoutYProperty(), pane.getLayoutY() - 1);

		KeyValue kx4 = new KeyValue(pane.layoutXProperty(), pane.getLayoutX() - 1);
		KeyValue ky4 = new KeyValue(pane.layoutYProperty(), pane.getLayoutY() + 1);

//		KeyValue kx5 = new KeyValue(pane.layoutXProperty(), pane.getLayoutX());
//		KeyValue ky5 = new KeyValue(pane.layoutYProperty(), pane.getLayoutY());

		KeyFrame kfx1 = new KeyFrame(new Duration(160), kx1);
		KeyFrame kfy1 = new KeyFrame(new Duration(160), ky1);
		KeyFrame kfx2 = new KeyFrame(new Duration(320), kx2);
		KeyFrame kfy2 = new KeyFrame(new Duration(320), ky2);
		KeyFrame kfx3 = new KeyFrame(new Duration(480), kx3);
		KeyFrame kfy3 = new KeyFrame(new Duration(480), ky3);
		KeyFrame kfx4 = new KeyFrame(new Duration(640), kx4);
		KeyFrame kfy4 = new KeyFrame(new Duration(640), ky4);
//		KeyFrame kfx5 = new KeyFrame(new Duration(250), kx5);
//		KeyFrame kfy5 = new KeyFrame(new Duration(250), ky5);

		// KeyFrame k2 = new KeyFrame(new Duration(460), kx2);
		// KeyFrame k3 = new KeyFrame(new Duration(360), kx3);
		// KeyFrame k4 = new KeyFrame(new Duration(460), kx4);
		// KeyFrame k5 = new KeyFrame(new Duration(460), kx4);
		//
		// KeyFrame k1 = new KeyFrame(new Duration(360), kx1);
		// KeyFrame k2 = new KeyFrame(new Duration(460), kx2);
		// KeyFrame k3 = new KeyFrame(new Duration(360), kx3);
		// KeyFrame k4 = new KeyFrame(new Duration(460), kx4);
		// KeyFrame k5 = new KeyFrame(new Duration(460), kx4);
		animation.getKeyFrames().add(kfx1);
		animation.getKeyFrames().add(kfy1);
		animation.getKeyFrames().add(kfx2);
		animation.getKeyFrames().add(kfy2);
		animation.getKeyFrames().add(kfx3);
		animation.getKeyFrames().add(kfy3);
		animation.getKeyFrames().add(kfx4);
		animation.getKeyFrames().add(kfy4);
		// animation.getKeyFrames().add(kfx5);
		// animation.getKeyFrames().add(kfy5);

		// animation.getKeyFrames().add(k3);
		// animation.getKeyFrames().add(k4);
		// animation.getKeyFrames().add(k5);
		//
		// KeyFrame k1 = new KeyFrame(new Duration(360), kx1, ky1 );
		// KeyFrame k2 = new KeyFrame(new Duration(560), kx2, ky2 );
		//// KeyFrame k2 = new KeyFrame(new Duration(460), kx2);
		//// KeyFrame k3 = new KeyFrame(new Duration(360), kx3);
		//// KeyFrame k4 = new KeyFrame(new Duration(460), kx4);
		//// KeyFrame k5 = new KeyFrame(new Duration(460), kx4);
		////
		//// KeyFrame k1 = new KeyFrame(new Duration(360), kx1);
		//// KeyFrame k2 = new KeyFrame(new Duration(460), kx2);
		//// KeyFrame k3 = new KeyFrame(new Duration(360), kx3);
		//// KeyFrame k4 = new KeyFrame(new Duration(460), kx4);
		//// KeyFrame k5 = new KeyFrame(new Duration(460), kx4);
		//
		// animation.getKeyFrames().add(k1);
		// animation.getKeyFrames().add(k2);
		//// animation.getKeyFrames().add(k3);
		//// animation.getKeyFrames().add(k4);
		//// animation.getKeyFrames().add(k5);
		// animation.play();
		// Runnable r = (new Runnable() {
		// @Override
		// public void run() {
		// while (true) {
		//
		// try {
		// pane.setLayoutX(pane.getLayoutX() - 1);
		// pane.setLayoutY(pane.getLayoutY() + 1);
		// Thread.sleep(160);
		// pane.setLayoutX(pane.getLayoutX() + 1);
		// pane.setLayoutY(pane.getLayoutY() - 1);
		// Thread.sleep(260);
		// pane.setLayoutX(pane.getLayoutX() + 1);
		// pane.setLayoutY(pane.getLayoutY() + 1);
		// Thread.sleep(160);
		// pane.setLayoutX(pane.getLayoutX() - 1);
		// pane.setLayoutY(pane.getLayoutY() - 1);
		// Thread.sleep(260);
		// } catch (InterruptedException ex) {
		// Logger.getLogger(HeadItem.class.getName()).log(Level.SEVERE, null,
		// ex);
		// }
		// }
		// }
		// });
		// new Thread(r).start();

		// headImagePane.setOnMouseClicked((MouseEvent me) -> {
		// if (me.getClickCount() == 2) {
		// if (animation.getStatus() == Animation.Status.RUNNING) {
		// animation.stop();
		// } else {
		// animation.play();
		// }
		// }
		// });
	}

	public void addAttribute(Object key, Object value) {
		attributeMap.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T getAttribute(Object key) {
		return (T) attributeMap.get(key);
	}

	public void setHeadImage(Image image) {
		this.normalImage = image;
		headImageView.setImage(image);
		setGrayImage(image);
	}

	private void setGrayImage(Image image) {
		PixelReader pixelReader = image.getPixelReader();
		grayImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
		PixelWriter pixelWriter = grayImage.getPixelWriter();

		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				Color color = pixelReader.getColor(x, y);
				color = color.grayscale();
				pixelWriter.setColor(x, y, color);
			}
		}
	}

	public boolean isGray() {
		return gray;
	}

	public void setGray(boolean gray) {
		this.gray = gray;
		if (gray) {
			headImageView.setImage(grayImage);
		} else {
			headImageView.setImage(normalImage);
		}
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
		remarkLabel.setText(remark);
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
		nicknameLabel.setText(nickname);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		statusLabel.setText(status);
	}

	public String getShowText() {
		return showText;
	}

	public void setShowText(String showText) {
		this.showText = showText;
		textLabel.setText(showText);
	}

	public void setPulse(boolean pulse) {
		if (!pulse) {
			if(animation.getStatus() == Animation.Status.RUNNING){
				animation.stop();
			}
		} else {
			animation.play();
		}
	}

	public void addBusinessIcon(Node node) {
		businessPane.getChildren().add(node);
	}

	public void removeBusinessIcon(Node node) {
		businessPane.getChildren().remove(node);
	}

	private Node getGapNode(double value) {
		AnchorPane tempPane = new AnchorPane();
		tempPane.setPrefWidth(value);
		tempPane.setPrefHeight(value);
		tempPane.setBackground(Background.EMPTY);
		return tempPane;
	}
}
