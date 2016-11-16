/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.test.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.oim.core.common.AppConstant;
import com.oim.core.common.util.ByteUtil;
import com.oim.core.common.util.ColorUtil;
import com.oim.core.common.util.DateUtil;
import com.oim.core.common.util.FileUtil;
import com.oim.core.net.message.data.chat.Content;
import com.oim.core.net.message.data.chat.Item;
import com.oim.core.net.message.data.chat.Section;
import com.oim.fx.common.box.FontBox;
import com.oim.fx.common.box.ImageBox;
import com.oim.fx.ui.ChatListFrame;
import com.oim.fx.ui.chat.ChatItem;
import com.oim.fx.ui.chat.ChatPanel;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author Only
 */
public class ChatListFrameTest extends Application {
	ChatListFrame chatListFrame = new ChatListFrame();

	ChatPanel cp = new ChatPanel();

	@Override
	public void start(Stage primaryStage) {
		chatListFrame.show();
		initTest();
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	private void initTest() {

		HBox box = new HBox();

		box.getChildren().add(new Button("kkk"));
		box.getChildren().add(new Button("kkk"));
		box.getChildren().add(new Button("kkk"));

		ChatItem chatItem = new ChatItem();
		chatItem.setText("哈哈哈");
		Image image = ImageBox.getImagePath("Resources/Images/Head/User/" + 4 + ".png", 34, 34, 8, 8);
		chatItem.setImage(image);

		// chatItem.getUserData();
		// chatListFrame.addItem(chatItem);
		//
		chatItem = new ChatItem();
		chatListFrame.addItem(chatItem);
		chatItem = new ChatItem();
		chatListFrame.addItem(chatItem);
		chatItem = new ChatItem();
		chatListFrame.addItem(chatItem);
		// itemBox.getChildren().add(new Button("kkk"));
		// itemBox.getChildren().add(new Button("kkk"));
		// itemBox.getChildren().add(new Button("kkk"));
		// itemBox.getChildren().add(new Button("kkk"));
		// itemBox.getChildren().add(new Button("kkk"));
		// itemBox.getChildren().add(new Button("kkk"));
		// itemBox.getChildren().add(new Button("kkk"));
		// itemBox.getChildren().add(new Button("kkk"));
		// itemBox.getChildren().add(new Button("kkk"));
		// itemBox.getChildren().add(new Button("kkk"));
		// itemBox.getChildren().add(new Button("kkk"));
		// itemBox.getChildren().add(new Button("kkk"));
		// itemBox.getChildren().add(new Button("kkk"));
		// itemBox.getChildren().add(new Button("kkk"));
		// itemBox.getChildren().add(new Button("kkk"));
		// itemBox.getChildren().add(new Button("kkk"));
		// itemBox.getChildren().add(new Button("kkk"));
		// itemBox.getChildren().add(new Button("kkk"));
		// itemBox.getChildren().add(new Button("kkk"));
		// itemBox.getChildren().add(new Button("kkk"));
		// itemBox.getChildren().add(new Button("kkk"));
		// itemBox.getChildren().add(new Button("kkk"));
		// itemBox.getChildren().add(new Button("kkk"));
		// itemBox.getChildren().add(new Button("kkk"));
		// itemBox.getChildren().add(new Button("kkk"));
		// itemBox.getChildren().add(new Button("kkk"));
		// itemBox.getChildren().add(new Button("kkk"));
		// itemBox.getChildren().add(new Button("kkk"));
		// itemBox.getChildren().add(new Button("kkk"));
		// itemBox.getChildren().add(new Button("kkk"));
		// itemBox.getChildren().add(new Button("kkk"));
		// itemBox.getChildren().add(new Button("kkk"));

		cp.setName("软件技术交流 47545jjj");
		cp.setText("软件技术交流 fgffffffffggggggggggggggggggggggggggg软件技术交流 ");
		chatListFrame.setChatPanel(cp);

		cp.setSendAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String name = "呵呵";
				String time = DateUtil.getCurrentDateTime();
				String color = ColorUtil.getColorInHexFromRGB(32, 143, 62);
				Content content = getContent(cp);
				if (null != content) {
					cp.initializeWriteHtml();
					insertShowChat(cp, name, color, time, content);
				}
			}
		});
	}

	private Content getContent(ChatPanel chatPanel) {
		String html = chatPanel.getHtml();

		boolean underline = chatPanel.isUnderline();
		boolean bold = chatPanel.isBold();
		String color = chatPanel.getWebColor();
		boolean italic = chatPanel.isItalic();
		String fontName = chatPanel.getFontName();
		int fontSize = chatPanel.getFontSize();

		Document htmlDocument = Jsoup.parse(html);
		List<Section> sectionList = new ArrayList<Section>();
		Content content = null;
		if (null != htmlDocument) {
			List<org.jsoup.nodes.Element> elementList = htmlDocument.getElementsByTag("body");

			if (!elementList.isEmpty()) {
				org.jsoup.nodes.Element body = elementList.get(0);

				List<org.jsoup.nodes.Node> nodeList = body.childNodes();
				if (null != nodeList && !nodeList.isEmpty()) {

					List<Item> itemList = new ArrayList<Item>();

					for (org.jsoup.nodes.Node node : nodeList) {
						if (node instanceof org.jsoup.nodes.TextNode) {
							String value = ((org.jsoup.nodes.TextNode) node).text();
							if (null != value && !"".equals(value)&&!"".equals(value.replace(" ", "").replace("	", ""))) {
								Item item = new Item();
								item.setType(Item.type_text);
								item.setValue(value);
								itemList.add(item);
							}
						}

						if (node instanceof org.jsoup.nodes.Element) {
							org.jsoup.nodes.Element n = (org.jsoup.nodes.Element) node;
							String tagName = n.tagName();

							if ("img".equals(tagName)) {
								String name = n.attr("name");
								String value = n.attr("value");

								if ("face".equals(name)) {
									Item item = new Item();
									item.setType(Item.type_face);
									item.setValue(value);
									itemList.add(item);
								} else {// 如果是发送图片，因为传送文件有点麻烦，就直接将图片转成String，在接受段解析回来就可以了
									File file = new File(value);
									if (file.exists()) {
										int size = (int) file.length();
										byte[] bytes = new byte[size];
										FileInputStream in = null;
										try {
											in = new FileInputStream(file);
											in.read(bytes);
											String image = ByteUtil.bytesToString(bytes);
											Item cvd = new Item();
											cvd.setType(Item.type_image);
											cvd.setValue(image);
											itemList.add(cvd);
										} catch (Exception ex) {
											// TODO: handle exception
										} finally {
											try {
												if (null != in) {
													in.close();
												}
											} catch (Exception ex) {
												// TODO: handle exception
											}
										}
									}
								}
							}
							if ("div".equals(tagName)) {
								if (!itemList.isEmpty()) {
									Section section = new Section();
									section.setItems(itemList);
									sectionList.add(section);
									itemList = new ArrayList<Item>();
								}

								Section s = getSection(n);
								sectionList.add(s);
							}
						}

					}
					if (!itemList.isEmpty()) {
						Section section = new Section();
						section.setItems(itemList);
						sectionList.add(section);
						itemList = new ArrayList<Item>();
					}
				} else {

					// StringBuilder style = getStyleValue();
					// for (org.jsoup.nodes.Element e : elementList) {
					// e.removeAttr("style");
					// e.attr("style", style.toString());
					// }
					// writeEditorPane.setText(htmlDocument.html());
				}
			}

			if (!sectionList.isEmpty()) {
				content = new Content();
				com.oim.core.net.message.data.chat.Font font = new com.oim.core.net.message.data.chat.Font();
				font.setBold(bold);
				font.setColor(color);
				font.setItalic(italic);
				font.setName(fontName);
				font.setSize(fontSize);
				font.setUnderline(underline);

				content.setFont(font);
				content.setSections(sectionList);
			}

		}
		return content;
	}

	private Section getSection(org.jsoup.nodes.Element e) {
		Section section = new Section();
		List<org.jsoup.nodes.Node> nodeList = e.childNodes();
		if (null != nodeList && !nodeList.isEmpty()) {
			List<Item> itemList = new ArrayList<Item>();
			for (org.jsoup.nodes.Node node : nodeList) {
				if (node instanceof org.jsoup.nodes.Element) {
					org.jsoup.nodes.Element n = (org.jsoup.nodes.Element) node;
					if ("img".equals(n.tagName())) {
						String name = n.attr("name");
						String value = n.attr("value");

						if ("face".equals(name)) {
							Item item = new Item();
							item.setType(Item.type_face);
							item.setValue(value);
							itemList.add(item);
						} else {// 如果是发送图片，因为传送文件有点麻烦，就直接将图片转成String，在接受段解析回来就可以了
							File file = new File(value);
							if (file.exists()) {
								int size = (int) file.length();
								byte[] bytes = new byte[size];
								FileInputStream in = null;
								try {
									in = new FileInputStream(file);
									in.read(bytes);
									String image = ByteUtil.bytesToString(bytes);
									Item cvd = new Item();
									cvd.setType(Item.type_image);
									cvd.setValue(image);
									itemList.add(cvd);
								} catch (Exception ex) {
									// TODO: handle exception
								} finally {
									try {
										if (null != in) {
											in.close();
										}
									} catch (Exception ex) {
										// TODO: handle exception
									}
								}
							}
						}
					}

				} else if (node instanceof org.jsoup.nodes.TextNode) {
					String value = ((org.jsoup.nodes.TextNode) node).text();
					if (null != value && !"".equals(value)) {
						Item item = new Item();
						item.setType(Item.type_text);
						item.setValue(value);
						itemList.add(item);
					}
				}
			}
			section.setItems(itemList);
		}
		return section;
	}

	private void insertShowChat(ChatPanel cp, String name, String color, String time, Content content) {
		if (null != cp) {
			StringBuilder nameText = new StringBuilder();
			nameText.append("<div ");
			nameText.append(getStyle("微软雅黑", 12, color, false, false, false));
			nameText.append(">");
			nameText.append(name);
			nameText.append(" ");
			nameText.append(time);
			nameText.append("</div>");
			cp.insertShowLastHtml(nameText.toString());

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
			List<Section> sections = content.getSections();
			if (null != sections) {
				com.oim.core.net.message.data.chat.Font font = content.getFont();
				StringBuilder sb = new StringBuilder();
				for (Section section : sections) {

					StringBuilder style = getStyle(font.getName(), font.getSize(), font.getColor(), font.isBold(), font.isUnderline(), font.isItalic());
					sb.append("<div ");
					sb.append(style);
					sb.append(">");
					List<Item> items = section.getItems();

					for (Item item : items) {
						if (Item.type_text.equals(item.getType())) {
							sb.append(item.getValue());
						}
						if (Item.type_face.equals(item.getType())) {
							String path = "Resources/Images/Face/" + item.getValue() + ".gif ";
							File file = new File(path);
							if (file.exists()) {
								String fullPath = file.getAbsolutePath();
								sb.append(getImageTag("", "face", item.getValue(), fullPath));
							}
						}
						if (Item.type_image.equals(item.getType())) {
							String bytesString = item.getValue();
							byte[] bytes = ByteUtil.stringToBytes(bytesString);
							String fileName = dateFormat.format(new Date()).toString() + ".jpg";
							String path = AppConstant.userHome + "/" + AppConstant.app_home_path + "/" + 6 + "/image/" + fileName;
							FileUtil.checkOrCreateFile(path);
							File file = new File(path);
							FileOutputStream out = null;
							try {
								out = new FileOutputStream(file);
								out.write(bytes);
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							} finally {
								if (null != out) {
									try {
										out.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}
							if (file.exists()) {
								String fullPath = file.getAbsolutePath();
								sb.append(getImageTag("", "", item.getValue(), fullPath));
							}
						}
					}
					sb.append("</div>");
				}
				cp.insertShowLastHtml(sb.toString());
			}
		}
	}

	private StringBuilder getStyle(String fontName, int fontSize, String color, boolean bold, boolean underline, boolean italic) {
		StringBuilder style = new StringBuilder();
		style.append("style=\"");
		style.append(getStyleValue(fontName, fontSize, color, bold, underline, italic));
		style.append("\"");
		return style;
	}

	/**
	 * 这里组装聊天内容的样式，字体、大小、颜色、下划线、粗体、倾斜等
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param fontName
	 * @param fontSize
	 * @param color
	 * @param bold
	 * @param underline
	 * @param italic
	 * @return
	 */
	private StringBuilder getStyleValue(String fontName, int fontSize, String color, boolean bold, boolean underline, boolean italic) {
		StringBuilder style = new StringBuilder();

		
		style.append("font-family:").append(FontBox.getFontName(fontName)).append(";");
		style.append("font-size:").append(fontSize).append("px;");
		if (underline) {
			style.append("margin-top:0;text-decoration:underline;");
		} else {
			style.append("margin-top:0;");
		}
		if (italic) {
			style.append("font-style:italic;");
		}
		if (bold) {
			style.append("font-weight:bold;");
		}
		if (null != color) {
			style.append("color:#");
			style.append(color);
			style.append(";");
		}
		return style;
	}

	private StringBuilder getImageTag(String id, String name, String value, String path) {
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
		return image.append("\" />");
	}
}
