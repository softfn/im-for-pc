/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.fx.ui;

import java.util.Locale;

import org.comtel2000.keyboard.control.KeyBoardPopup;
import org.comtel2000.keyboard.control.KeyBoardPopupBuilder;

import com.oim.core.bean.User;
import com.oim.fx.common.box.ImageBox;
import com.oim.fx.ui.component.WaitingPanel;
import com.only.fx.TitlePane;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * 登录窗口
 *
 * @author XiaHui
 */
public class LoginFrame extends BaseFrame {

	private DropShadow dropShadow = new DropShadow();

	private Button loginButton = new Button();// 登录按钮
	private Button settingButton = new Button();// 顶部设置按钮
	private TextField accountField = new TextField();// 账号输入框
	private PasswordField passwordField = new PasswordField();

	private ImageView imageHeadView = new ImageView();
	private Button headButton = new Button();

	private CheckBox rememberCheckBox = new CheckBox();
	private CheckBox autoCheckBox = new CheckBox();

	private Label registerLabel = new Label();
	private Label forgetLabel = new Label();

	private Button addAccountButton = new Button();
	private Button towCodeButton = new Button();

	private StackPane centerPane = new StackPane();
	private WaitingPanel waitingPanel = new WaitingPanel();
	private AnchorPane userPane = new AnchorPane();
	private AnchorPane statusButton = new AnchorPane();
	private ImageView statusImageView = new ImageView();

	KeyBoardPopup popup = KeyBoardPopupBuilder.create().initLocale(Locale.ENGLISH).build();

	Image onlineImage = ImageBox.getImageClassPath("/resources/common/images/status/imonline.png");
	Image callMeImage = ImageBox.getImageClassPath("/resources/common/images/status/Qme.png");
	Image awayImage = ImageBox.getImageClassPath("/resources/common/images/status/away.png");
	Image busyImage = ImageBox.getImageClassPath("/resources/common/images/status/busy.png");
	Image muteImage = ImageBox.getImageClassPath("/resources/common/images/status/mute.png");
	Image invisibleImage = ImageBox.getImageClassPath("/resources/common/images/status/invisible.png");

	private String status = "1";

	public LoginFrame() {
		initComponent();
		iniEvent();
	}

	private void initComponent() {
		this.setTitle("登录");
		this.setResizable(false);
		this.setWidth(445);
		this.setHeight(345);
		this.setTitlePaneStyle(2);
		this.setRadius(5);
		VBox rootBox = new VBox();
		this.setCenter(rootBox);
		this.getScene().getStylesheets().add(this.getClass().getResource("/resources/login/css/login.css").toString());
		// this.getIcons().clear();

		rootBox.setBackground(Background.EMPTY);

		settingButton.setId("setting-button");
		settingButton.setPrefWidth(30);
		settingButton.setPrefHeight(27);

		TitlePane titlePane = getTitlePane();
		titlePane.getChildren().add(0, settingButton);

		AnchorPane topRootPane = new AnchorPane();
		topRootPane.setBackground(Background.EMPTY);
		topRootPane.setPrefWidth(430);
		topRootPane.setPrefHeight(180);

		AnchorPane backgroundPane = new AnchorPane();
		backgroundPane.setBackground(Background.EMPTY);
		backgroundPane.setPrefWidth(430);
		backgroundPane.setPrefHeight(180);

		Image logoIamge = new Image(this.getClass().getResource("/resources/login/logo.png").toExternalForm(), true);
		ImageView logoImageView = new ImageView();

		logoImageView.setImage(logoIamge);
		logoImageView.setLayoutX(125);
		logoImageView.setLayoutY(50);
		logoImageView.setEffect(dropShadow);

		backgroundPane.getChildren().add(logoImageView);

		WebView webView = new WebView();
		webView.setPrefSize(430, 180);

		WebEngine webEngine = webView.getEngine();
		webEngine.load(this.getClass().getResource("/resources/login/html/index.html").toString());

		topRootPane.getChildren().add(webView);
		topRootPane.getChildren().add(backgroundPane);

		userPane.setPrefWidth(428);
		userPane.setPrefHeight(150);
		userPane.setStyle("-fx-background-color:#ebf2f9;");

		accountField.getStyleClass().remove("text-field");
		passwordField.getStyleClass().remove("text-field");

		accountField.setBackground(Background.EMPTY);
		passwordField.setBackground(Background.EMPTY);

		accountField.setBorder(Border.EMPTY);
		passwordField.setBorder(Border.EMPTY);

		accountField.setPromptText("账号");
		passwordField.setPromptText("密码");

		// popup.addDoubleClickEventFilter(passwordField);
		/// popup.addFocusListener(this.getScene());

		accountField.setPrefWidth(170);
		passwordField.setPrefWidth(170);

		accountField.setPrefHeight(30);
		passwordField.setPrefHeight(29);

		loginButton.setText("登  录");
		loginButton.setId("login-button");
		loginButton.setLayoutX(135);
		loginButton.setLayoutY(107);
		loginButton.setPrefHeight(30);
		loginButton.setPrefWidth(194);

		rememberCheckBox.setText("记住密码");
		autoCheckBox.setText("自动登录");

		autoCheckBox.setPrefHeight(15);

		rememberCheckBox.setLayoutX(136);
		rememberCheckBox.setLayoutY(80);

		autoCheckBox.setLayoutX(260);
		autoCheckBox.setLayoutY(80);

		registerLabel.setText("注册账号");
		forgetLabel.setText("忘记密码");

		registerLabel.setLayoutX(340);
		registerLabel.setLayoutY(20);
		forgetLabel.setLayoutX(340);
		forgetLabel.setLayoutY(50);

		registerLabel.setCursor(Cursor.HAND);
		forgetLabel.setCursor(Cursor.HAND);

		registerLabel.setStyle("-fx-text-fill: #55A8E7;");
		forgetLabel.setStyle("-fx-text-fill: #55A8E7;");

		addAccountButton.setId("add-account-button");
		towCodeButton.setId("tow-code-button");

		addAccountButton.setLayoutX(10);
		addAccountButton.setLayoutY(120);
		addAccountButton.setPrefHeight(24);
		addAccountButton.setPrefWidth(24);

		towCodeButton.setLayoutX(398);
		towCodeButton.setLayoutY(120);
		towCodeButton.setPrefHeight(24);
		towCodeButton.setPrefWidth(24);

		Button accountButton = new Button();
		accountButton.setId("combo-box-button");
		accountButton.setPrefHeight(20);
		accountButton.setPrefWidth(20);
		accountButton.setLayoutX(170);
		accountButton.setLayoutY(4);

		AnchorPane accountPane = new AnchorPane();
		accountPane.setId("account-input");
		accountPane.setLayoutX(135);
		accountPane.setLayoutY(15);
		accountPane.setPrefWidth(194);
		accountPane.getChildren().add(accountField);
		accountPane.getChildren().add(accountButton);

		AnchorPane passwordButton = new AnchorPane();
		passwordButton.setId("password-button");
		passwordButton.setPrefHeight(16);
		passwordButton.setPrefWidth(15);
		passwordButton.setPrefSize(15, 15);
		passwordButton.setLayoutX(173);
		passwordButton.setLayoutY(6);

		popup.setAutoHide(true);
		popup.setOnKeyboardCloseButton(e -> popup.hide());
		passwordButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				passwordField.requestFocus();
				popup.show(LoginFrame.this, LoginFrame.this.getX() + 110, LoginFrame.this.getY() + 280);
			}
		});

		AnchorPane passwordPane = new AnchorPane();
		passwordPane.setId("password-input");
		passwordPane.setLayoutX(135);
		passwordPane.setLayoutY(44);
		passwordPane.setPrefWidth(194);
		passwordPane.setPrefHeight(30);
		passwordPane.getChildren().add(passwordField);
		passwordPane.getChildren().add(passwordButton);

		accountPane.setOnMouseEntered((Event event) -> {
			accountPane.toFront();
		});

		passwordPane.setOnMouseEntered((Event event) -> {
			passwordPane.toFront();
		});

		userPane.getChildren().addAll(accountPane, passwordPane);
		userPane.getChildren().addAll(loginButton);

		userPane.getChildren().addAll(rememberCheckBox);
		userPane.getChildren().addAll(autoCheckBox);

		userPane.getChildren().addAll(addAccountButton);
		userPane.getChildren().addAll(towCodeButton);

		userPane.getChildren().addAll(registerLabel);
		userPane.getChildren().addAll(forgetLabel);

		statusImageView.setImage(onlineImage);
		statusImageView.setLayoutY(3);
		statusImageView.setLayoutX(3);

		statusButton.setId("status-button");
		statusButton.setPrefHeight(13);
		statusButton.setPrefWidth(13);
		statusButton.setLayoutX(62);
		statusButton.setLayoutY(62);

		statusButton.getChildren().add(statusImageView);

		Rectangle clip = new Rectangle();
		clip.setArcHeight(8);
		clip.setArcWidth(8);

		clip.setWidth(80);
		clip.setHeight(80);

		imageHeadView.getStyleClass().add("image-head-view");
		imageHeadView.setClip(clip);

		AnchorPane headPane = new AnchorPane();

		headPane.setLayoutY(15);
		headPane.setLayoutX(40);

		headPane.getChildren().add(imageHeadView);
		headPane.getChildren().add(statusButton);

		userPane.getChildren().add(headPane);

		headButton.setGraphic(imageHeadView);
		headButton.setPrefWidth(80);
		headButton.setPrefHeight(80);

		waitingPanel.add(WaitingPanel.show_waiting, "登录中...", ImageBox.getImageClassPath("/resources/common/images/loading/loading_312_4.gif"));
		waitingPanel.show(WaitingPanel.show_waiting);

		waitingPanel.setPrefWidth(428);
		waitingPanel.setPrefHeight(150);
		waitingPanel.setStyle("-fx-background-color:#ebf2f9;");

		centerPane.getChildren().add(waitingPanel);
		centerPane.getChildren().add(userPane);

		rootBox.getChildren().add(topRootPane);
		rootBox.getChildren().add(centerPane);

		//////////////////////////////////////
		ContextMenu menu = new ContextMenu();

		ImageView iv = new ImageView();
		iv.setImage(onlineImage);
		MenuItem menuItem = new MenuItem("我在线上", iv);
		menu.getItems().add(menuItem);
		menuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				statusImageView.setImage(onlineImage);
				status = User.status_online;
			}
		});

		iv = new ImageView();
		iv.setImage(callMeImage);
		menuItem = new MenuItem("Q我吧", iv);
		menu.getItems().add(menuItem);
		menuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				statusImageView.setImage(callMeImage);
				status = User.status_call_me;
			}
		});

		iv = new ImageView();
		iv.setImage(awayImage);
		menuItem = new MenuItem("离开", iv);
		menu.getItems().add(menuItem);
		menuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				statusImageView.setImage(awayImage);
				status = User.status_away;
			}
		});

		iv = new ImageView();
		iv.setImage(busyImage);
		menuItem = new MenuItem("忙碌", iv);
		menu.getItems().add(menuItem);
		menuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				statusImageView.setImage(busyImage);
				status = User.status_busy;
			}
		});

		iv = new ImageView();
		iv.setImage(muteImage);
		menuItem = new MenuItem("请勿打扰", iv);
		menu.getItems().add(menuItem);
		menuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				statusImageView.setImage(muteImage);
				status = User.status_mute;
			}
		});

		iv = new ImageView();
		iv.setImage(invisibleImage);
		menuItem = new MenuItem("隐身", iv);
		menu.getItems().add(menuItem);
		menuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				statusImageView.setImage(invisibleImage);
				status = User.status_invisible;

			}
		});

		statusButton.setOnMouseClicked((Event event) -> {
			menu.show(LoginFrame.this, LoginFrame.this.getX() + 110, LoginFrame.this.getY() + 280);
		});
	}

	private void iniEvent() {

		Image image = ImageBox.getImagePath("Resources/Images/Head/User/1_100.gif", 80, 80);
		setHeadImage(image);

	}

	public <T> void addAccountChangeListener(ChangeListener<String> listener) {
		accountField.textProperty().addListener(listener);
	}

	public void setHeadImage(Image image) {
		imageHeadView.setImage(image);
	}

	public void showWaiting(boolean show) {
		waitingPanel.setVisible(show);
		userPane.setVisible(!show);
	}

	public void setLoginAction(EventHandler<ActionEvent> value) {
		loginButton.setOnAction(value);
	}

	public void setSettingAction(EventHandler<ActionEvent> value) {
		settingButton.setOnAction(value);
	}

	public void setRegisterOnMouseClicked(EventHandler<MouseEvent> value) {
		registerLabel.setOnMouseClicked(value);
		;
	}

	public String getStatus() {
		return status;
	}

	public String getAccount() {
		return accountField.getText();
	}

	public String getPassword() {
		return passwordField.getText();
	}

}
