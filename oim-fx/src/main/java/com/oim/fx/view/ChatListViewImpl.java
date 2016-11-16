package com.oim.fx.view;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.oim.core.app.AppContext;
import com.oim.core.bean.Group;
import com.oim.core.bean.GroupMember;
import com.oim.core.bean.User;
import com.oim.core.business.manage.ChatManage;
import com.oim.core.business.manage.LastManage;
import com.oim.core.business.manage.VideoManage;
import com.oim.core.business.sender.ChatSender;
import com.oim.core.business.sender.GroupSender;
import com.oim.core.business.sender.VideoSender;
import com.oim.core.common.AppConstant;
import com.oim.core.common.annotation.Parameter;
import com.oim.core.common.app.view.AbstractView;
import com.oim.core.common.app.view.ChatListView;
import com.oim.core.common.box.PersonalBox;
import com.oim.core.common.util.ByteUtil;
import com.oim.core.common.util.ColorUtil;
import com.oim.core.common.util.DateUtil;
import com.oim.core.common.util.FileUtil;
import com.oim.core.net.message.data.UserData;
import com.oim.core.net.message.data.chat.Content;
import com.oim.core.net.message.data.chat.Item;
import com.oim.core.net.message.data.chat.Section;
import com.oim.core.net.server.Back;
import com.oim.fx.common.box.FontBox;
import com.oim.fx.common.box.ImageBox;
import com.oim.fx.ui.ChatListFrame;
import com.oim.fx.ui.chat.ChatItem;
import com.oim.fx.ui.chat.ChatPanel;
import com.oim.fx.ui.chat.SimpleHead;
import com.oim.fx.ui.component.IconButton;
import com.oim.fx.ui.list.ListRootPanel;
import com.only.net.data.action.DataBackAction;
import com.only.net.data.action.DataBackActionAdapter;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2015年3月14日 上午11:14:38
 * @version 0.0.1
 */
public class ChatListViewImpl extends AbstractView implements ChatListView {

	ChatListFrame chatListFrame = new ChatListFrame();

	private Map<String, ChatPanel> chatPanelMap = new ConcurrentHashMap<String, ChatPanel>();
	private Map<String, ChatItem> chatItemMap = new ConcurrentHashMap<String, ChatItem>();
	private ConcurrentSkipListMap<String, ChatItem> itemMap = new ConcurrentSkipListMap<String, ChatItem>();

	private Map<String, ListRootPanel> groupUserListMap = new ConcurrentHashMap<String, ListRootPanel>();
	private Map<String, List<GroupMember>> groupMemberListMap = new ConcurrentHashMap<String, List<GroupMember>>();

	ChatItem tempChatItem;
	private long shakeTime = 0;// 记录收到或者发送抖动信息的时间，为了不过于频繁抖动。

	public ChatListViewImpl(AppContext appContext) {
		super(appContext);
		initEvent();
	}

	private void initEvent() {

	}

	public boolean isShowing() {
		return chatListFrame.isShowing();
	}

	@Override
	public void setVisible(boolean visible) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (visible) {
					chatListFrame.show();
					chatListFrame.toFront();
				} else {
					chatListFrame.hide();
				}
			}
		});
	}

	public boolean isGroupChatShowing(String groupId) {
		String key = this.getGroupKey(groupId);
		return chatListFrame.isShowing() && (null != tempChatItem && key.equals(tempChatItem.getAttribute("key")));
	}

	public boolean isUserChatShowing(String userId) {
		String key = this.getUserKey(userId);
		return chatListFrame.isShowing() && (null != tempChatItem && key.equals(tempChatItem.getAttribute("key")));
	}

	public void show(UserData userData) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				ChatItem chatItem = getUserChatItem(userData);
				ChatPanel chatPanel = getUserChatPanel(userData);
				select(chatItem, chatPanel);
				ChatManage chatManage = appContext.getManage(ChatManage.class);
				chatManage.showUserCaht(userData);
			}
		});
	}

	public void show(Group group) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				ChatItem chatItem = getGroupChatItem(group);
				ChatPanel chatPanel = getGroupChatPanel(group);
				select(chatItem, chatPanel);
				ChatManage chatManage = appContext.getManage(ChatManage.class);
				chatManage.showGroupCaht(group);
			}
		});
	}

	private ChatItem getUserChatItem(UserData userData) {
		String key = this.getUserKey(userData.getId());
		ChatItem item = chatItemMap.get(key);
		if (null == item) {
			item = new ChatItem();
			item.addAttribute("key", key);
			chatItemMap.put(key, item);
			item.addCloseAction(new ChatItemCloseEvent(key));
			item.setOnMouseClicked(new ChatItemClickedEvent(key));
		}

		if (null != userData.getRemark() && !"".equals(userData.getRemark())) {
			item.setText(userData.getRemark());
		} else {
			item.setText(userData.getNickname());
		}
		Image image = ImageBox.getImagePath("Resources/Images/Head/User/" + userData.getHead() + ".png", 34, 34, 8, 8);
		item.setImage(image);
		return item;
	}

	private ChatPanel getUserChatPanel(UserData userData) {
		String key = this.getUserKey(userData.getId());
		ChatPanel item = chatPanelMap.get(key);
		if (null == item) {
			item = new ChatPanel();
			item.addAttribute("key", key);
			item.addAttribute("userId", userData.getId());
			item.setSendAction(new ChatItemUserSendEvent(key));
			item.setCloseAction(new ChatItemCloseEvent(key));
			
			Image normalImage = ImageBox.getImageClassPath("/resources/chat/images/middletoolbar/GVideoTurnOnVideo.png");

			IconButton iconButton = new IconButton(normalImage);
			item.addMiddleTool(iconButton);

			iconButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					sendVideo(userData);
				}
			});
			chatPanelMap.put(key, item);
			StackPane rightPane = new StackPane();
			// rightPane.setPrefHeight(350);
			// rightPane.setMaxHeight(350);
			ImageView imageView = new ImageView();
			rightPane.getChildren().add(imageView);

			if (!"女".equals(userData.getGender())) {
				Image image = ImageBox.getImagePath("Resources/Images/Default/Show/default_av_girl_v3.png", 140, 380);
				imageView.setImage(image);
			} else {
				Image image = ImageBox.getImagePath("Resources/Images/Default/Show/default_av_boy_v3.png", 140, 380);
				imageView.setImage(image);
			}
			item.setRightPane(rightPane);
		}

		if (null != userData.getRemark() && !"".equals(userData.getRemark())) {
			item.setName(userData.getRemark());
		} else {
			item.setName(userData.getNickname());
		}
		item.setText(userData.getSignature());
		return item;
	}

	///////////////////////////////////////////////////////// group
	private ChatItem getGroupChatItem(Group group) {
		String key = this.getGroupKey(group.getId());
		ChatItem item = chatItemMap.get(key);
		if (null == item) {
			item = new ChatItem();
			item.addAttribute("key", key);
			chatItemMap.put(key, item);
			item.addCloseAction(new ChatItemCloseEvent(key));
			item.setOnMouseClicked(new ChatItemClickedEvent(key));
		}

		item.setText(group.getName());
		Image image = ImageBox.getImagePath("Resources/Images/Head/Group/" + group.getHead() + ".png", 34, 34, 8, 8);
		item.setImage(image);
		return item;
	}

	private ChatPanel getGroupChatPanel(Group group) {
		String key = this.getGroupKey(group.getId());
		ChatPanel item = chatPanelMap.get(key);
		if (null == item) {
			item = new ChatPanel();
			item.addAttribute("key", key);
			item.addAttribute("groupId", group.getId());
			item.setSendAction(new ChatItemGroupSendEvent(key));
			item.setCloseAction(new ChatItemCloseEvent(key));
			chatPanelMap.put(key, item);
			setGroupUserList(group.getId());
		}
		item.setName(group.getName());
		item.setText(group.getPublicNotice());
		return item;
	}

	//////////////////////////////////////////////////////////

	private void select(ChatItem item, ChatPanel chatPanel) {
		if (item != this.tempChatItem) {
			if (null != this.tempChatItem) {
				this.tempChatItem.setSelected(false);
			}
			item.setSelected(true);
		}
		this.tempChatItem = item;
		chatListFrame.addItem(item);
		chatListFrame.setChatPanel(chatPanel);
		if (!itemMap.containsValue(item)) {
			String key = item.getAttribute("key");
			itemMap.put(key, item);
		}
	}

	@Override
	public boolean hasGroupChat(String groupId) {
		String key = getGroupKey(groupId);
		return chatItemMap.containsKey(key);
	}

	@Override
	public boolean hasUserChat(String userId) {
		String key = getUserKey(userId);
		return chatItemMap.containsKey(key);
	}
	
	private void sendVideo(UserData userData) {
		User sendUser = PersonalBox.get(User.class);
		VideoManage vm = this.appContext.getManage(VideoManage.class);
		vm.showSendVideoFrame(userData);
		VideoSender vh = this.appContext.getSender(VideoSender.class);
		vh.requestVideo(sendUser.getId(), userData.getId());
	}
	@Override
	public void userChat(UserData userData, Content content) {
		if (null != userData) {
			String name = userData.getNickname();
			String time = DateUtil.getCurrentDateTime();
			String color = ColorUtil.getColorInHexFromRGB(32, 143, 62);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					ChatPanel chatPanel = getUserChatPanel(userData);
					insertShowChat(chatPanel, name, color, time, content);
				}
			});
			LastManage lastManage = this.appContext.getManage(LastManage.class);
			lastManage.addLastUserData(userData);
		}
	}

	public void groupChat(Group group, UserData userData, Content content) {
		if (null != userData) {
			String name = userData.getNickname();
			String time = DateUtil.getCurrentDateTime();
			String color = ColorUtil.getColorInHexFromRGB(32, 143, 62);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					ChatPanel chatPanel = getGroupChatPanel(group);
					insertShowChat(chatPanel, name, color, time, content);
				}
			});
			LastManage lastManage = this.appContext.getManage(LastManage.class);
			lastManage.addLastGroup(group);
		}
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

			User user = PersonalBox.get(User.class);
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
							String extension = item.getExtension();
							String bytesString = item.getValue();
							byte[] bytes = ByteUtil.stringToBytes(bytesString);
							String fileName = dateFormat.format(new Date()).toString() + (extension == null || "".equals(extension) ? ".jpg" : "."+extension);
							String path = AppConstant.userHome + "/" + AppConstant.app_home_path + "/" + user.getNumber() + "/image/" + fileName;
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

	private void sendUserMessage(String key) {
		User sendUser = PersonalBox.get(User.class);
		ChatPanel chatPanel = chatPanelMap.get(key);
		if (null != chatPanel) {
			Content content = getContent(chatPanel);
			String receiveUserId = chatPanel.getAttribute("userId");
			if (null != content) {
				chatPanel.initializeWriteHtml();

				String name = sendUser.getNickname();
				String time = DateUtil.getCurrentDateTime();
				String color = ColorUtil.getColorInHexFromRGB(Color.blue.getRed(), Color.blue.getGreen(), Color.blue.getBlue());

				insertShowChat(chatPanel, name, color, time, content);
				DataBackActionAdapter action = new DataBackActionAdapter() {

				};
				ChatSender ch = this.appContext.getSender(ChatSender.class);
				ch.sendUserChatMessage(receiveUserId, sendUser.getId(), content, action);
			}
		}
	}

	private void sendGroupMessage(String key) {
		User user = PersonalBox.get(User.class);
		ChatPanel chatPanel = chatPanelMap.get(key);
		if (null != chatPanel) {
			Content content = getContent(chatPanel);
			String groupId = chatPanel.getAttribute("groupId");
			if (null != content) {
				chatPanel.initializeWriteHtml();
				ChatSender ch = this.appContext.getSender(ChatSender.class);
				ch.sendGroupChatMessage(groupId, user.getId(), content);
			}
		}
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
							if (null != value && !"".equals(value) && !"".equals(value.replace(" ", "").replace("	", ""))) {
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
								String src = n.attr("src");

								if ("face".equals(name)) {
									Item item = new Item();
									item.setType(Item.type_face);
									item.setValue(value);
									itemList.add(item);
								} else {// 如果是发送图片，因为传送文件有点麻烦，就直接将图片转成String，在接受段解析回来就可以了
									if (null != src && !"".equals(src)) {
										File file = new File(src.replace("file:/", ""));
										if (file.exists()) {
											String fileName = file.getName();
											String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
											int size = (int) file.length();
											byte[] bytes = new byte[size];
											FileInputStream in = null;
											try {
												in = new FileInputStream(file);
												in.read(bytes);
												String image = ByteUtil.bytesToString(bytes);
												Item item = new Item();
												item.setType(Item.type_image);
												item.setValue(image);
												item.setExtension(extension);
												itemList.add(item);
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
						String src = n.attr("src");

						if ("face".equals(name)) {
							Item item = new Item();
							item.setType(Item.type_face);
							item.setValue(value);
							itemList.add(item);
						} else {// 如果是发送图片，因为传送文件有点麻烦，就直接将图片转成String，在接受段解析回来就可以了
							if (null != src && !"".equals(src)) {
								File file = new File(src.replace("file:/", ""));
								if (file.exists()) {
									String fileName = file.getName();
									String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
									int size = (int) file.length();
									byte[] bytes = new byte[size];
									FileInputStream in = null;
									try {
										in = new FileInputStream(file);
										in.read(bytes);
										String image = ByteUtil.bytesToString(bytes);
										Item item = new Item();
										item.setType(Item.type_image);
										item.setValue(image);
										item.setExtension(extension);
										itemList.add(item);
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

	private void selectChatItem(String key) {
		ChatItem ci = chatItemMap.get(key);
		ChatPanel cp = chatPanelMap.get(key);
		if (null != ci && null != cp) {
			select(ci, cp);
		}
	}

	private void removeChatItem(String key) {
		ChatItem item = chatItemMap.get(key);
		if (null != item) {
			chatListFrame.removeItem(item);
			itemMap.remove(key);

			if (itemMap.isEmpty()) {
				setVisible(false);
			} else {
				if (item.isSelected()) {
					Iterator<String> iterator = itemMap.keySet().iterator();
					if (iterator.hasNext()) {
						String nextKey = iterator.next();
						selectChatItem(nextKey);
					}
				}
			}
		}
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

	private String getUserKey(String userId) {
		StringBuilder sb = new StringBuilder();
		sb.append("user_");
		sb.append(userId);
		return sb.toString();
	}

	private String getGroupKey(String groupId) {
		StringBuilder sb = new StringBuilder();
		sb.append("group_");
		sb.append(groupId);
		return sb.toString();
	}

	public void setGroupUserList(final String groupId) {

		List<GroupMember> gml = groupMemberListMap.get(groupId);
		if (null == gml) {
			DataBackAction dataBackAction = new DataBackActionAdapter() {
				@Back
				public void back(@Parameter("userDataList") List<UserData> userDataList,
						@Parameter("groupMemberList") List<GroupMember> groupMemberList) {
					setGroupUserList(groupId, userDataList, groupMemberList);
				}
			};
			GroupSender gh = this.appContext.getSender(GroupSender.class);
			gh.getGroupMemberListWithUserDataList(groupId, dataBackAction);
		}
	}

	public void updateGroupUserList(final String groupId) {

		DataBackAction dataBackAction = new DataBackActionAdapter() {

			@Back
			public void back(@Parameter("userDataList") List<UserData> userDataList,
					@Parameter("groupMemberList") List<GroupMember> groupMemberList) {
				setGroupUserList(groupId, userDataList, groupMemberList);
			}
		};
		GroupSender gh = this.appContext.getSender(GroupSender.class);
		gh.getGroupMemberListWithUserDataList(groupId, dataBackAction);
	}

	private void setGroupUserList(String groupId, List<UserData> userDataList, List<GroupMember> groupMemberList) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				groupMemberListMap.put(groupId, groupMemberList);
				ListRootPanel groupUserList = groupUserListMap.get(groupId);
				if (groupUserList == null) {
					groupUserList = new ListRootPanel();
				}
				groupUserList.clearNode();
				for (UserData userData : userDataList) {
					SimpleHead item = new SimpleHead();

					item.setOnMouseClicked((MouseEvent me) -> {
						if (me.getClickCount() == 2) {
							ChatManage chatManage = appContext.getManage(ChatManage.class);
							chatManage.showCahtFrame(userData);
						}
						me.consume();
					});
					item.setText(userData.getNickname());
					Image image = ImageBox.getImagePath("Resources/Images/Head/User/" + userData.getHead() + ".png", 20, 20);
					item.setImage(image);
					item.addAttribute(UserData.class, userData);
					groupUserList.addNode(item);
				}

				String key = getGroupKey(groupId);
				ChatPanel chatPanel = chatPanelMap.get(key);
				if (null != chatPanel) {
					chatPanel.setRightPane(groupUserList);
				}
			}
		});

	}

	private class ChatItemGroupSendEvent implements EventHandler<ActionEvent> {
		String key;

		public ChatItemGroupSendEvent(String key) {
			this.key = key;
		}

		@Override
		public void handle(ActionEvent event) {
			sendGroupMessage(key);
		}
	}

	private class ChatItemUserSendEvent implements EventHandler<ActionEvent> {
		String key;

		public ChatItemUserSendEvent(String key) {
			this.key = key;
		}

		@Override
		public void handle(ActionEvent event) {
			sendUserMessage(key);
		}
	}

	private class ChatItemCloseEvent implements EventHandler<ActionEvent> {
		String key;

		public ChatItemCloseEvent(String key) {
			this.key = key;
		}

		@Override
		public void handle(ActionEvent event) {
			removeChatItem(key);
		}
	}

	private class ChatItemClickedEvent implements EventHandler<MouseEvent> {
		String key;

		public ChatItemClickedEvent(String key) {
			this.key = key;
		}

		@Override
		public void handle(MouseEvent event) {
			selectChatItem(key);
		}
	}

	@Override
	public void doShake(UserData userData) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				ChatItem chatItem = getUserChatItem(userData);
				ChatPanel chatPanel = getUserChatPanel(userData);
				select(chatItem, chatPanel);
				ChatManage chatManage = appContext.getManage(ChatManage.class);
				chatManage.showUserCaht(userData);
				shake();
			}
		});
	}

	private void shake() {
		if (System.currentTimeMillis() - shakeTime < 3000) {
			return;
		}

		for (int i = 0; i < 3; i++) {
			chatListFrame.setX(chatListFrame.getX() + 4);
			chatListFrame.setY(chatListFrame.getY() - 4);
			try {
				Thread.sleep(40);
				chatListFrame.setX(chatListFrame.getX() - 8);
				chatListFrame.setY(chatListFrame.getY());
				Thread.sleep(40);
				chatListFrame.setX(chatListFrame.getX());
				chatListFrame.setY(chatListFrame.getY() + 4);
				Thread.sleep(40);
				chatListFrame.setX(chatListFrame.getX() + 4);
				chatListFrame.setY(chatListFrame.getY());
				Thread.sleep(40);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		shakeTime = System.currentTimeMillis();
	}
}
