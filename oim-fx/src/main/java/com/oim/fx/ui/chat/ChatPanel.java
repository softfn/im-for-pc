package com.oim.fx.ui.chat;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.oim.common.event.ExecuteAction;
import com.oim.core.common.AppConstant;
import com.oim.core.common.util.FileUtil;
import com.oim.fx.common.box.ImageBox;
import com.oim.fx.ui.component.IconButton;
import com.oim.ui.chat.FacePanel;
import com.oim.ui.chat.ScreenShotAction;
import com.oim.ui.chat.ScreenShotWindow;
import com.sun.javafx.collections.ObservableListWrapper;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class ChatPanel extends BorderPane {

	private Map<Object, Object> attributeMap = new HashMap<Object, Object>();

	TopPanel topPanel = new TopPanel();

	private ShowSplitPane showSplitPane = new ShowSplitPane();
	private StackPane topPane = new StackPane();
	private StackPane rightPane = new StackPane();

	// private VBox baseBox = new VBox();
	private ShowPanel showPanel = new ShowPanel();
	private VBox showBox = new VBox();

	private VBox writeBox = new VBox();
	private HBox middleToolBarBox = new HBox();
	private WritePanel writePanel = new WritePanel();
	private HBox bottomButtonBox = new HBox();

	private boolean showFontBox = false;
	private HBox fontBox = new HBox();

	private Button sendButton = new Button();
	private Button closeButton = new Button();
	ExecuteAction faceAction;
	FacePanel facePanel = FacePanel.getFacePanel();
	ScreenShotWindow ssw = ScreenShotWindow.getScreenShotWindow();
	ScreenShotAction screenShotAction;
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
	private FileChooser fileChooser;
	public ChatPanel() {
		initComponent();
		initMiddleToolBar();
		iniEvent();
		initFontBox();
	}

	private void initComponent() {
		this.setTop(topPane);
		this.setCenter(showSplitPane);
		this.setRight(rightPane);
		// this.getChildren().add(baseBox);

		
		fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("图片文件", "*.png","*.jpg", "*.bmp", "*.gif"));
		
		rightPane.setStyle("-fx-background-color:rgba(255, 255, 255, 0.5)");

		topPane.setPrefHeight(85);
		rightPane.setPrefWidth(140);

		topPane.getChildren().add(topPanel);

		WebView webView = showPanel.getWebView();
		webView.setPrefWidth(20);
		webView.setPrefHeight(50);

		showSplitPane.setTop(showBox);
		showSplitPane.setBottom(writeBox);
		showSplitPane.setStyle("-fx-background-color:rgba(255, 255, 255, 0.8)");

		showBox.getChildren().add(webView);
		VBox.setVgrow(webView, Priority.ALWAYS);

		HBox line = new HBox();
		line.setMinHeight(1);
		line.setStyle("-fx-background-color:rgba(180, 180, 180, 1);");

		VBox writeTempBox = new VBox();

		writeTempBox.getChildren().add(line);
		writeTempBox.getChildren().add(middleToolBarBox);

		writeBox.getChildren().add(writeTempBox);
		writeBox.getChildren().add(writePanel);
		writeBox.getChildren().add(bottomButtonBox);

		fontBox.setMinHeight(25);
		fontBox.setSpacing(2);

		middleToolBarBox.setMinHeight(25);
		middleToolBarBox.setSpacing(8);
		middleToolBarBox.setAlignment(Pos.CENTER_LEFT);
		// middleToolBarBox.setStyle("-fx-background-color:rgba(255, 255, 255,
		// 0.92)");

		bottomButtonBox.setSpacing(5);
		bottomButtonBox.setAlignment(Pos.CENTER_RIGHT);
		bottomButtonBox.setMinHeight(40);
		// bottomButtonBox.setStyle("-fx-background-color:rgba(255, 255, 255,
		// 0.92)");

		closeButton.setText("关闭");
		sendButton.setText("发送");

		closeButton.setPrefSize(72, 24);
		sendButton.setPrefSize(72, 24);

		closeButton.setFocusTraversable(false);
		sendButton.setFocusTraversable(false);

		bottomButtonBox.getChildren().add(closeButton);
		bottomButtonBox.getChildren().add(sendButton);

		// showSplitPane.setStyle("-fx-background-color:rgba(255, 255, 255,
		// 0.92)");
		// showBox.setStyle("-fx-background-color: #000000;");
		// rightPane.setStyle("-fx-background-color: #37c486;");
		// baseBox.setStyle("-fx-background-color: #AA0000;");
		// showSplitPane.setPrefHeight(140);
		// showSplitPane.setPrefHeight(240);

		// baseBox.setAlignment(Pos.BOTTOM_CENTER);
		// baseBox.getChildren().add(showBox);
		// baseBox.getChildren().add(showSplitPane );
		// VBox.setVgrow(showBox, Priority.ALWAYS);
		// VBox.setVgrow(writeBox, Priority.ALWAYS);
		// showSplitPane.setPrefHeight(baseBox.getHeight()-140);
	}

	/**
	 * 初始化中间工具按钮
	 */
	private void initMiddleToolBar() {
		// 字体设置按钮
		Image normalImage = ImageBox.getImageClassPath("/resources/chat/images/middletoolbar/aio_quickbar_font.png");
		Image hoverImage = ImageBox.getImageClassPath("/resources/chat/images/middletoolbar/aio_quickbar_font_hover.png");
		Image pressedImage = ImageBox.getImageClassPath("/resources/chat/images/middletoolbar/aio_quickbar_font_hover.png");

		IconButton iconButton = new IconButton(normalImage, hoverImage, pressedImage);
		this.addMiddleTool(iconButton);

		iconButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				showFontBox = !showFontBox;
				showFontBox(showFontBox);
			}
		});

		// 表情按钮
		normalImage = ImageBox.getImageClassPath("/resources/chat/images/middletoolbar/aio_quickbar_face.png");
		hoverImage = ImageBox.getImageClassPath("/resources/chat/images/middletoolbar/aio_quickbar_face_hover.png");
		pressedImage = ImageBox.getImageClassPath("/resources/chat/images/middletoolbar/aio_quickbar_face_hover.png");
		iconButton = new IconButton(normalImage, hoverImage, pressedImage);
		this.addMiddleTool(iconButton);
		iconButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				facePanel.setSelectAction(faceAction);
				facePanel.show((int) event.getScreenX() - 240, (int) event.getScreenY());
			}
		});
		// 发送图片按钮
		normalImage = ImageBox.getImageClassPath("/resources/chat/images/middletoolbar/aio_quickbar_sendpic.png");
		hoverImage = ImageBox.getImageClassPath("/resources/chat/images/middletoolbar/aio_quickbar_sendpic_hover.png");
		pressedImage = ImageBox.getImageClassPath("/resources/chat/images/middletoolbar/aio_quickbar_sendpic_hover.png");

		iconButton = new IconButton(normalImage, hoverImage, pressedImage);
		iconButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				sendPicture();
			}
		});
		this.addMiddleTool(iconButton);

		// 截屏按钮
		normalImage = ImageBox.getImageClassPath("/resources/chat/images/middletoolbar/aio_quickbar_cut.png");
		hoverImage = ImageBox.getImageClassPath("/resources/chat/images/middletoolbar/aio_quickbar_cut.png");
		pressedImage = ImageBox.getImageClassPath("/resources/chat/images/middletoolbar/aio_quickbar_cut.png");

		iconButton = new IconButton(normalImage, hoverImage, pressedImage);
		iconButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				showCut();
			}
		});
		this.addMiddleTool(iconButton);
	}

	private void initFontBox() {

		ComboBox<String> fontFamilyComboBox = new ComboBox<String>();
		fontFamilyComboBox.setFocusTraversable(false);
		ObservableList<String> fonts = new ObservableListWrapper<String>(new ArrayList<String>()); // FXCollections.observableArrayList(Font.getFamilies());
		fontFamilyComboBox.setItems(fonts);
		fonts.add("微软雅黑");
		fonts.add("宋体");
		fonts.add("小篆");
		fonts.add("Helvetica");
		fonts.add("TimesRoman");
		fonts.add("Courier");
		fonts.add("Helvetica");
		fonts.add("TimesRoman");

		fontFamilyComboBox.setValue("微软雅黑");
		fontFamilyComboBox.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String font = fontFamilyComboBox.getValue().toString();
				if (null != font) {
					writePanel.setFontName(font);
				}
			}
		});

		fontBox.getChildren().add(fontFamilyComboBox);
		//////////////
		ComboBox<Integer> fontSizeComboBox = new ComboBox<Integer>();
		fontSizeComboBox.setFocusTraversable(false);
		ObservableList<Integer> fontSizes = new ObservableListWrapper<Integer>(new ArrayList<Integer>()); // FXCollections.observableArrayList(Font.getFamilies());
		fontSizeComboBox.setItems(fontSizes);
		for (int i = 8; i < 23; i++) {
			fontSizes.add(i);
		}
		fontSizeComboBox.setValue(12);
		fontSizeComboBox.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Integer fontSize = fontSizeComboBox.getValue();
				if (null != fontSize) {
					writePanel.setFontSize(fontSize);
				}
			}
		});

		fontBox.getChildren().add(fontSizeComboBox);
		///////////////////
		Image normalImage = ImageBox.getImageClassPath("/resources/chat/images/middletoolbar/font/aio_quickbar_sysfont_bold_normal.png");
		Image hoverImage = ImageBox.getImageClassPath("/resources/chat/images/middletoolbar/font/aio_quickbar_sysfont_bold_highlight.png");
		Image pressedImage = ImageBox.getImageClassPath("/resources/chat/images/middletoolbar/font/aio_quickbar_sysfont_bold_push.png");

		IconButton iconButton = new IconButton(normalImage, hoverImage, pressedImage);
		iconButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				writePanel.setBold(!writePanel.isBold());
			}
		});

		fontBox.getChildren().add(iconButton);
		////////////////////
		normalImage = ImageBox.getImageClassPath("/resources/chat/images/middletoolbar/font/aio_quickbar_sysfont_italic_normal.png");
		hoverImage = ImageBox.getImageClassPath("/resources/chat/images/middletoolbar/font/aio_quickbar_sysfont_italic_highlight.png");
		pressedImage = ImageBox.getImageClassPath("/resources/chat/images/middletoolbar/font/aio_quickbar_sysfont_italic_push.png");

		iconButton = new IconButton(normalImage, hoverImage, pressedImage);
		iconButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				writePanel.setItalic(!writePanel.isItalic());
			}
		});
		fontBox.getChildren().add(iconButton);
		/////////////////////////
		normalImage = ImageBox.getImageClassPath("/resources/chat/images/middletoolbar/font/aio_quickbar_sysfont_underline_normal.png");
		hoverImage = ImageBox.getImageClassPath("/resources/chat/images/middletoolbar/font/aio_quickbar_sysfont_underline_highlight.png");
		pressedImage = ImageBox.getImageClassPath("/resources/chat/images/middletoolbar/font/aio_quickbar_sysfont_underline_push.png");

		iconButton = new IconButton(normalImage, hoverImage, pressedImage);
		iconButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				writePanel.setUnderline(!writePanel.isUnderline());
			}
		});
		fontBox.getChildren().add(iconButton);
		////////////////////
		ColorPicker colorPicker = new ColorPicker(Color.BLACK);
		colorPicker.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent t) {
				Color newColor = colorPicker.getValue();
				writePanel.setColor(newColor);

			}
		});
		fontBox.getChildren().add(colorPicker);
		// normalImage =
		// ImageBox.getImageClassPath("/resources/chat/images/middletoolbar/aio_quickbar_font.png");
		// hoverImage =
		// ImageBox.getImageClassPath("/resources/chat/images/middletoolbar/aio_quickbar_font_hover.png");
		// pressedImage =
		// ImageBox.getImageClassPath("/resources/chat/images/middletoolbar/aio_quickbar_font_hover.png");
		//
		// iconButton = new IconButton(normalImage, hoverImage, pressedImage);
		// iconButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
		//
		// @Override
		// public void handle(MouseEvent event) {
		// showFontBox = !showFontBox;
		// showFontBox(showFontBox);
		// }
		// });
		//
		// fontBox.getChildren().add(iconButton);
	}

	public void addMiddleTool(Node node) {
		middleToolBarBox.getChildren().add(node);
	}

	public void showFontBox(boolean showFontBox) {
		if (showFontBox) {
			showBox.getChildren().add(fontBox);
		} else {
			showBox.getChildren().remove(fontBox);
		}
	}

	private void iniEvent() {
		faceAction = new ExecuteAction() {

			@Override
			public <T, E> E execute(T value) {
				if (null != value) {
					Platform.runLater(new Runnable() {
						public void run() {
							insertFace(value.toString());
						}
					});
				}
				return null;
			}
		};
		screenShotAction = new ScreenShotAction() {

			@Override
			public void saveImage(BufferedImage image) {

				Platform.runLater(new Runnable() {
					public void run() {
						if (null != image) {
							saveBufferedImage(image);
						}
					}
				});
			}
		};
	}

	private void showCut() {
		ssw.setAction(screenShotAction);
		ssw.setVisible(true);
	}
	
	private void sendPicture(){
		
		List<File> fileList = fileChooser.showOpenMultipleDialog(this.getScene().getWindow());
		if (fileList != null&&!fileList.isEmpty()) {
			for (File file : fileList) {
				if (file.exists()) {
					String fullPath=file.getAbsolutePath();
					insertImage("", "", "", fullPath);
				}
			}
		}
	}

	private void insertFace(String value) {
		String path = "Resources/Images/Face/" + value + ".gif ";
		File file = new File(path);
		if (file.exists()) {
			String fullPath = file.getAbsolutePath();
			insertImage("", "face", value, fullPath);
		}
	}

	public void saveBufferedImage(BufferedImage image) {
		String fileName = dateFormat.format(new Date()).toString() + ".jpg";
		String path = AppConstant.userHome + "/" + AppConstant.app_home_path + "/screenshot/" + fileName;
		try {
			FileUtil.checkOrCreateFolder(path);
			ImageIO.write(image, "jpg", new File(path));
			insertImage("", "", path, path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertImage(String id, String name, String value, String path) {
		if (null != path && !"".equals(path)) {
			StringBuilder image = new StringBuilder();
			image.append("<img ");
			if (null != id && !"".equals(id)) {
				image.append(" id=\"");
				image.append(id);
				image.append("\"");
			}
			if (null != name && !"".equals(name)) {
				image.append(" name=\"");
				image.append(name);
				image.append("\"");
			}
			if (null != value && !"".equals(value)) {
				image.append(" value=\"");
				image.append(value);
				image.append("\"");
			}
			image.append(" src=\"file:/");
			image.append(path.replace("\\", "/"));
			image.append("\" />");
			writePanel.insertSelectionHtml(image.toString());
		}
	}

	public void setRightPane(Node node) {
		rightPane.getChildren().clear();
		rightPane.getChildren().add(node);
	}

	public void setRightPanePrefWidth(double value) {
		rightPane.setPrefWidth(value);
	}

	public void initializeWriteHtml() {
		writePanel.initializeHtml();
	}

	public void setName(String name) {
		topPanel.setName(name);
	}

	public void setText(String text) {
		topPanel.setText(text);
	}

	public void setSendAction(EventHandler<ActionEvent> value) {
		sendButton.setOnAction(value);
	}

	public void setCloseAction(EventHandler<ActionEvent> value) {
		closeButton.setOnAction(value);
	}

	public String getHtml() {
		return writePanel.getHtml();
	}

	public void insertShowLastHtml(String html) {
		showPanel.insertLastHtml(html);
	}

	public String getFontName() {
		return writePanel.getFontName();
	}

	public int getFontSize() {
		return writePanel.getFontSize();
	}

	public String getWebColor() {
		return writePanel.getWebColor();
	}

	public boolean isBold() {
		return writePanel.isBold();
	}

	public boolean isUnderline() {
		return writePanel.isUnderline();
	}

	public boolean isItalic() {
		return writePanel.isItalic();
	}

	public void addAttribute(Object key, Object value) {
		attributeMap.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T getAttribute(Object key) {
		return (T) attributeMap.get(key);
	}
}
