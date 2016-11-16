/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.test.ui.component;

import java.io.File;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import com.oim.fx.ui.BaseFrame;
import com.oim.fx.ui.swing.WritePanel;
import com.sun.javafx.collections.ObservableListWrapper;

import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author Only
 */
public class SwingWritePanelFrame extends BaseFrame {

	// private String fontName = "微软雅黑";
	private int fontSize = 12;
	// private Color color = Color.BLACK;
	// private boolean bold = false;
	// private boolean underline = false;
	// private boolean italic = false;
	//
	// private static final String BOLD_COMMAND = "bold";
	// private static final String ITALIC_COMMAND = "italic";
	// private static final String UNDERLINE_COMMAND = "underline";
	// private static final String FONT_FAMILY_COMMAND = "fontname";
	// private static final String FONT_SIZE_COMMAND = "fontsize";
	// private static final String FOREGROUND_COLOR_COMMAND = "forecolor";
	// private static final String BACKGROUND_COLOR_COMMAND = "backcolor";
	ColorPicker colorPicker = new ColorPicker(Color.BLACK);

	VBox box = new VBox();

	WritePanel writePanel = new WritePanel();

	Button button = new Button("插入");
	
	Button insertImageButton = new Button("插入图片");
	ComboBox<String> fontFamilyComboBox = new ComboBox<String>();
	Button fontSizeA = new Button("fontSize+");
	Button fontSizeD = new Button("fontSize-");
	Button boldButton = new Button("bold");
	Button underlineButton = new Button("underline");
	Button italicButton = new Button("italic");
	Button htmlButton = new Button("html");

	TextArea textArea = new TextArea();
	File file = new File("Resources/Images/Head/User/90_100.gif");
	String fullPath = file.getAbsolutePath();
	// WebPage webPage;
	// WebEngine webEngine;
	public SwingWritePanelFrame() {
		init();
		initEvent();
	}

	private void init() {
		this.setBackground("Resources/Images/Wallpaper/18.jpg");
		this.setTitle("登录");
		this.setWidth(440);
		this.setHeight(360);
		this.setCenter(box);

		
		box.setStyle("-fx-background-color:rgba(255, 255, 255, 0.2)");

		//
		// ScrollPane htmlSP = new ScrollPane();
		// htmlSP.setFitToWidth(true);
		// //htmlSP.setPrefWidth(htmlEditor.prefWidth(-1)); // Workaround of
		// RT-21495
		// //htmlSP.setPrefHeight(245);
		// //htmlSP.setVbarPolicy(ScrollBarPolicy.NEVER);
		// htmlSP.setContent(webView);
		// htmlSP.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

		insertImageButton.setFocusTraversable(false);
		fontFamilyComboBox.setFocusTraversable(false);
		button.setFocusTraversable(false);
		fontSizeA.setFocusTraversable(false);
		fontSizeD.setFocusTraversable(false);
		boldButton.setFocusTraversable(false);
		underlineButton.setFocusTraversable(false);
		italicButton.setFocusTraversable(false);
		htmlButton.setFocusTraversable(false);

		TilePane tilePane = new TilePane();
		tilePane.setPrefColumns(3); // preferred columns
		tilePane.setAlignment(Pos.CENTER);

		
		
		
		tilePane.getChildren().add(insertImageButton);
		tilePane.getChildren().add(button);
		tilePane.getChildren().add(colorPicker);
		tilePane.getChildren().add(fontSizeA);
		tilePane.getChildren().add(fontFamilyComboBox);
		tilePane.getChildren().add(fontSizeD);
		tilePane.getChildren().add(boldButton);
		tilePane.getChildren().add(underlineButton);
		tilePane.getChildren().add(italicButton);
		tilePane.getChildren().add(htmlButton);

		Button gap = new Button("gap");

		box.getChildren().add(gap);
		box.getChildren().add(tilePane);
		SwingNode swingNode = new SwingNode(); 																											// fullPath
		//swingNode.prefHeight(250);
		//writePanel.setSize(300, 240);
		createSwingContent(swingNode);	
		//swingNode.set// +
		StackPane root = new StackPane();
		root.setPrefHeight(240);
		root.getChildren().add(swingNode);	
		box.getChildren().add(root);	
		//box.getChildren().add(textArea);

		// comboBox.setItems("");
		ObservableList<String> fonts = new ObservableListWrapper<String>(new ArrayList<String>()); // FXCollections.observableArrayList(Font.getFamilies());
		fontFamilyComboBox.setItems(fonts);
		// for (String fontFamily : fonts) {
		// if (DEFAULT_OS_FONT.equals(fontFamily)) {
		// fontFamilyComboBox.setValue(fontFamily);
		// }
		//
		// }

		fonts.add("宋体");
		fonts.add("小篆");
		fonts.add("Microsoft YaHei");
		fonts.add("Helvetica");
		fonts.add("TimesRoman");
		fonts.add("Courier");
		fonts.add("Helvetica");
		fonts.add("TimesRoman");
//		File file = new File("Resources/Images/Head/User/90_100.gif");
//		//
//		String fullPath = file.getAbsolutePath();
//		String cachedHTMLText = "<html><head></head><body contenteditable=\"true\"></body></html>";
//		String htmlText = "<html><head></head><body contenteditable=\"true\"><div id=\"main\"></div></body></html> ";// <di<lable
																														// id=\"show_text\">666</label>"
																														// +
																														// "<img
																														// src=\"file:/"
																														// +
																											// "\"
																														// />"
		// // WebPage webPage=new WebPage();
		// // WebEngine we = webView.getEngine();
		// // // we.loadContent(htmlText);
		// webEngine=writePanel.getEngine();
		// webPage = Accessor.getPageFor(webEngine);
		// webPage.setEditable(true);
		// webPage.load(webPage.getMainFrame(), htmlText, "text/html");
		// // webPage.setUserAgent(userAgent);
		// // webView.
		// //webView.getEngine().setUserStyleSheetLocation(getClass().getResource("/resources/common/css/webview.css").toExternalForm());
		// writePanel.setOnContextMenuRequested(new EventHandler<Event>() {
		//
		// @Override
		// public void handle(Event event) {
		// Object o = event.getSource();
		// System.out.println(o.getClass());
		// }
		// });
		// writePanel.setFocusTraversable(true);

		// textArea.setTextFormatter();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initEvent() {
		colorPicker.setOnAction(new EventHandler() {

			@Override
			public void handle(Event t) {
				Color newColor = colorPicker.getValue();
				writePanel.setColor((int)newColor.getGreen(),(int)newColor.getGreen(),(int)newColor.getBlue());

			}
		});

		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				//writePanel.insert("hhhh");
				//writePanel.ap
			}
		});

		fontFamilyComboBox.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String font = fontFamilyComboBox.getValue().toString();
				System.out.println(font);
				if (null != font) {
					writePanel.setFontName(font);
				}
			}

		});

		fontFamilyComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			writePanel.setFontName(newValue);
		});
		fontSizeA.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				fontSize++;
				String size = fontSize + "px";
				System.out.println(size);
				writePanel.setFontSize(fontSize);
			}
		});

		fontSizeD.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				fontSize--;
				String size = fontSize + "px";
				System.out.println(size);
				writePanel.setFontSize(fontSize);
			}
		});

		boldButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				writePanel.setBold(!writePanel.isBold());
			}
		});

		underlineButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				writePanel.setUnderline(!writePanel.isUnderline());
			}
		});

		italicButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				writePanel.setItalic(!writePanel.isItalic());
			}
		});

		htmlButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println(writePanel.getText());
			}
		});
		insertImageButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				writePanel.insertImage(fullPath);;
			}
		});
	}
	
	 private void createSwingContent(final SwingNode swingNode) {
	        SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	                swingNode.setContent(writePanel);
	            }
	        });
	    }
}
