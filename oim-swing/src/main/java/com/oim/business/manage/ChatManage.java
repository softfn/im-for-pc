package com.oim.business.manage;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.ImageIcon;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.oim.app.AppContext;
import com.oim.bean.Group;
import com.oim.bean.GroupMember;
import com.oim.bean.User;
import com.oim.business.handler.ChatHandler;
import com.oim.business.handler.GroupHandler;
import com.oim.business.handler.VideoHandler;
import com.oim.common.AppConstant;
import com.oim.common.annotation.Parameter;
import com.oim.common.app.manage.Manage;
import com.oim.common.box.HeadImageIconBox;
import com.oim.common.box.PersonalBox;
import com.oim.common.box.UserDataBox;
import com.oim.common.util.ByteUtil;
import com.oim.common.util.ColorUtil;
import com.oim.common.util.DateUtil;
import com.oim.common.util.FileUtil;
import com.oim.net.message.data.UserData;
import com.oim.net.message.data.chat.Content;
import com.oim.net.message.data.chat.Item;
import com.oim.net.message.data.chat.Section;
import com.oim.net.server.Back;
import com.oim.ui.ListChatFrame;
import com.oim.ui.chat.ChatItem;
import com.oim.ui.chat.ChatPanel;
import com.oim.ui.chat.GroupUserList;
import com.oim.ui.component.event.ExecuteAction;
import com.oim.ui.component.list.ItemPanel;
import com.only.net.data.action.DataBackAction;
import com.only.net.data.action.DataBackActionAdapter;

/**
 * 对聊天相关的一些管理，如不同用户聊天界面
 * 
 * @author XiaHui
 * @date 2015年3月16日 下午1:37:57
 */
public class ChatManage extends Manage {

	private Map<String, ChatItem> userChatPanelItemMap = new ConcurrentHashMap<String, ChatItem>();
	private Map<String, ChatItem> groupChatPanelItemMap = new ConcurrentHashMap<String, ChatItem>();
	private Map<String, ChatPanel> userChatPanelMap = new ConcurrentHashMap<String, ChatPanel>();
	private Map<String, ChatPanel> groupChatPanelMap = new ConcurrentHashMap<String, ChatPanel>();

	private Map<String, GroupUserList> groupUserListMap = new ConcurrentHashMap<String, GroupUserList>();
	private Map<String, List<GroupMember>> groupMemberListMap = new ConcurrentHashMap<String, List<GroupMember>>();

	private SelectChatItem selectChatItem = new SelectChatItem();
	private ListChatFrame listChatFrame = new ListChatFrame();
	private Set<ChatItem> chatItemSet = new HashSet<ChatItem>();// 储存聊天窗口左边列表内容
	private ExecuteAction executeAction;
	private ExecuteAction sendUserAction;// 执行用户信息发送动作
	private ExecuteAction sendGroupAction;// 执行群信息发送动作
	private ExecuteAction colseAction;
	private MouseAdapter chatItemMouseAdapter;
	private MouseAdapter userItemMouseAdapter;
	private ExecuteAction twitterAction;
	private ExecuteAction videoAction;
	private ImageIcon twitterIcon = new ImageIcon("Resources/Images/Default/ChatFrame/MidToolbar/aio_quickbar_twitter.png");
	private ImageIcon videoIcon = new ImageIcon("Resources/Images/Default/ChatFrame/MidToolbar/video.png");

	private long shakeTime = 0;// 记录收到或者发送抖动信息的时间，为了不过于频繁抖动。

	public ChatManage(AppContext appContext) {
		super(appContext);
		initEvent();
	}

	private void initEvent() {
		executeAction = new ExecuteAction() {

			@Override
			public <T, E> E execute(T value) {
				if (value instanceof ChatItem) {
					Runnable runnable = new Runnable() {
						ChatItem chatItem;

						@Override
						public void run() {
							removeChatItem(chatItem);
						}

						public Runnable setChatItem(ChatItem chatItem) {
							this.chatItem = chatItem;
							return this;
						}

					}.setChatItem(((ChatItem) value));
					java.awt.EventQueue.invokeLater(runnable);
				}
				return null;
			}
		};

		sendUserAction = new ExecuteAction() {

			@Override
			public <T, E> E execute(T value) {
				if (value instanceof ChatPanel) {
					sendUserMessage((ChatPanel) value);
				}
				return null;
			}
		};
		sendGroupAction = new ExecuteAction() {

			@Override
			public <T, E> E execute(T value) {
				if (value instanceof ChatPanel) {
					sendGroupMessage((ChatPanel) value);
				}
				return null;
			}
		};
		colseAction = new ExecuteAction() {

			@Override
			public <T, E> E execute(T value) {
				if (value instanceof ChatPanel) {
					UserData user = ((ChatPanel) value).getAttribute(UserData.class);
					if (null != user) {
						ChatItem item = userChatPanelItemMap.get(user.getId());
						if (null != item) {
							removeChatItem(item);
						}
					}
					Group group = ((ChatPanel) value).getAttribute(Group.class);
					if (null != group) {
						ChatItem item = groupChatPanelItemMap.get(group.getId());
						if (null != item) {
							removeChatItem(item);
						}
					}
				}
				return null;
			}
		};
		chatItemMouseAdapter = new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				if (e.getSource() instanceof ChatItem) {
					ChatItem item = (ChatItem) e.getSource();
					if (e.getButton() == MouseEvent.BUTTON1) {
						changeChatItem(item);
					}
				}
			}
		};
		twitterAction = new ExecuteAction() {

			@Override
			public <T, E> E execute(T value) {
				if (value instanceof ChatPanel) {
					UserData user = ((ChatPanel) value).getAttribute(UserData.class);
					if (null != user) {
						sendShake(user.getId());
					}
				}
				return null;
			}
		};

		videoAction = new ExecuteAction() {

			@Override
			public <T, E> E execute(T value) {
				if (value instanceof ChatPanel) {
					UserData user = ((ChatPanel) value).getAttribute(UserData.class);
					if (null != user) {
						sendVideo(user);
					}
				}
				return null;
			}
		};

		userItemMouseAdapter = new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				if (e.getSource() instanceof ItemPanel) {
					ItemPanel item = (ItemPanel) e.getSource();
					if (e.getButton() == MouseEvent.BUTTON1) {
						if (e.getClickCount() == 2) {
							UserData userData = item.getAttribute(UserData.class);
							showCahtFrame(userData);
						}
					}
				}
			}
		};
	}

	/**
	 * 发送抖动操作
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param receiveId
	 */
	private void sendShake(String receiveId) {
		User sendUser = PersonalBox.get(User.class);
		ChatHandler ch = this.appContext.getHandler(ChatHandler.class);
		ch.sendShake(receiveId, sendUser.getId());// 发送给接受方
		shake();// 自己执行抖动
	}

	private void sendVideo(UserData userData) {
		User sendUser = PersonalBox.get(User.class);
		VideoManage vm = this.appContext.getManage(VideoManage.class);
		vm.showSendVideoFrame(userData);
		VideoHandler vh = this.appContext.getHandler(VideoHandler.class);
		vh.requestVideo(sendUser.getId(), userData.getId());
	}

	/**
	 * 接受到抖动信息，执行抖动
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param sendId
	 */
	public void doShake(String sendId) {
		UserData user = UserDataBox.get(sendId);
		if (null == user) {// 如果发送抖动的不是好友，暂时不支持抖动
			return;
		}
		showCahtFrame(user);// 如果聊天窗口没有显示，则显示聊天窗口
		shake();
	}

	/**
	 * 判断发送消息用户与自己聊天的窗口是否已经显示了。
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param sendId
	 * @return
	 */
	public boolean isShowUserChat(String sendId) {
		ChatPanel chatPanel = userChatPanelMap.get(sendId);
		if (null == chatPanel) {
			return false;
		}
		boolean mark = listChatFrame.isShowing() && chatPanel.isShowing();
		return mark;
	}

	/**
	 * 判断群消息聊天窗口是否已经显示了
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param sendId
	 * @return
	 */
	public boolean isShowGroupChat(String groupId) {
		ChatPanel chatPanel = groupChatPanelMap.get(groupId);
		if (null == chatPanel) {
			return false;
		}
		boolean mark = listChatFrame.isShowing() && chatPanel.isShowing();
		return mark;
	}

	/**
	 * 显示用户聊天信息
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param user
	 * @param chatDataList
	 * @return
	 */
	public boolean userChat(UserData user, Content content) {
		ChatPanel chatPanel = getChatPanel(user);
		if (null == chatPanel) {
			return false;
		}
		String name = null;
		if (null != user) {
			name = user.getNickname();
		}
		boolean mark = showChat(name, chatPanel, content);
		return mark;
	}

	/**
	 * 显示群信息
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param group
	 * @param userData
	 * @param chatDataList
	 * @return
	 */
	public boolean groupChat(Group group, UserData userData, Content content) {
		ChatPanel chatPanel = getChatPanel(group);
		if (null == chatPanel) {
			return false;
		}
		String name = null;
		if (null != userData) {
			name = userData.getNickname();
		}
		boolean mark = showChat(name, chatPanel, content);
		return mark;
	}

	public boolean showChat(String name, ChatPanel chatPanel, Content content) {
		if (null == chatPanel) {
			return false;
		}
		if (null != name) {
			String text = name + " " + DateUtil.getCurrentDateTime();
			chatPanel.insertShowText(text, "微软雅黑", 9, 32, 143, 62, false, false, false);
		}
		User user =PersonalBox.get(User.class);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
		List<Section> sections = content.getSections();
		if (null != sections) {
			com.oim.net.message.data.chat.Font font = content.getFont();
			for (Section section : sections) {
				StringBuilder style = getStyle(font.getName(), font.getSize(), font.getColor(), font.isBold(), font.isUnderline(), font.isItalic());
				StringBuilder p = new StringBuilder();
				p.append("<p ");
				p.append(style);
				p.append(">");
				List<Item> items = section.getItems();
				for (Item item : items) {
					if (Item.type_text.equals(item.getType())) {
						p.append(item.getValue());
					} else if (Item.type_face.equals(item.getType())) {
						String path = "Resources/Images/Face/" + item.getValue() + ".gif ";
						File file = new File(path);
						if (file.exists()) {
							StringBuilder image = new StringBuilder();
							image.append("<img  src=\"file:/");
							image.append(file.getAbsolutePath());
							image.append("\" />");
							p.append(image);
						}
					} else if (Item.type_image.equals(item.getType())) {
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
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
						if (file.exists()) {
							StringBuilder image = new StringBuilder();
							image.append("<img  src=\"file:/");
							image.append(file.getAbsolutePath());
							image.append("\" />");
							p.append(image);
						}
					}
				}
				p.append("</p>");

				chatPanel.insertHtmlText(p.toString());
			}
		}

		boolean mark = listChatFrame.isShowing() && chatPanel.isShowing();
		return mark;
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

		style.append("font-family:'").append(fontName).append("';");
		style.append("font-size:'").append(fontSize).append("px';");
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

		style.append("color:#");
		style.append(color);
		style.append(";");
		return style;
	}

	public void showShake(String sendUserId) {
		UserData user = UserDataBox.get(sendUserId);
		ChatItem item = getChatItem(user);
		if (!chatItemSet.contains(item)) {
			chatItemSet.add(item);
			listChatFrame.addChatItem(item);
			item.addAttribute(ChatPanel.class, getChatPanel(user));
		}
		ChatPanel chatPanel = getChatPanel(user);
		selectChatItem.selectItem(item);

		listChatFrame.setChatPanel(chatPanel);
		listChatFrame.setIconImage(HeadImageIconBox.getUserHeadImageIcon40(user.getHead()).getImage());
		listChatFrame.setVisible(true);
	}

	public ChatPanel getChatPanel(UserData userData) {
		ChatPanel chatPanel = userChatPanelMap.get(userData.getId());
		if (null == chatPanel) {
			chatPanel = new ChatPanel();
			userChatPanelMap.put(userData.getId(), chatPanel);
			chatPanel.addAttribute(UserData.class, userData);
			chatPanel.setIcon(HeadImageIconBox.getUserHeadImageIcon(userData.getHead(), 40, 40));

			if (null != userData.getRemark() && !"".equals(userData.getRemark())) {
				chatPanel.setName(userData.getRemark());
			} else {
				chatPanel.setName(userData.getNickname());
			}
			chatPanel.setText(userData.getSignature());
			chatPanel.addCloseExecuteAction(colseAction);
			chatPanel.addSendExecuteAction(sendUserAction);
			chatPanel.addFunctionButton(twitterIcon, twitterAction);
			chatPanel.addFunctionButton(videoIcon, videoAction);
		}
		return chatPanel;
	}

	public ChatPanel getChatPanel(Group group) {
		ChatPanel chatPanel = groupChatPanelMap.get(group.getId());
		if (null == chatPanel) {
			chatPanel = new ChatPanel();
			groupChatPanelMap.put(group.getId(), chatPanel);
			chatPanel.addAttribute(Group.class, group);
			chatPanel.setIcon(HeadImageIconBox.getGroupHeadImageIcon(group.getHead(), 40, 40));
			chatPanel.setName(group.getName());
			chatPanel.setText(group.getIntroduce());
			chatPanel.addCloseExecuteAction(colseAction);
			chatPanel.addSendExecuteAction(sendGroupAction);
			setGroupUserList(group.getId());
			// GroupUserList groupUserList = new GroupUserList();
			// for (int i = 0; i < 13; i++) {
			// ItemPanel item = new ItemPanel();
			// HeadImageIconBox
			// item.setText("jjjjjjjjkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkjas");
			// item.setIcon(ImageUtil.getRoundedCornerIcon("Resources/Images/Default/UserHead/"
			// + (i + 1) + ".png", 20, 20, 20, 20));
			// groupUserList.add(item);
			// }
			//
			// chatPanel.setUserListPanel(groupUserList);
		}
		return chatPanel;
	}

	public ChatItem getChatItem(UserData user) {
		ChatItem item = userChatPanelItemMap.get(user.getId());
		if (null == item) {
			item = new ChatItem();
			userChatPanelItemMap.put(user.getId(), item);
			if (null != user.getRemark() && !"".equals(user.getRemark())) {
				item.setText(user.getRemark());
			} else {
				item.setText(user.getNickname());
			}
			item.addCloseExecuteAction(executeAction);
			item.addMouseListener(chatItemMouseAdapter);
			item.addAttribute(UserData.class, user);
			item.setIcon(HeadImageIconBox.getUserHeadImageIcon(user.getHead(), 40, 40));
		}
		return item;
	}

	public ChatItem getChatItem(Group group) {
		ChatItem item = groupChatPanelItemMap.get(group.getId());
		if (null == item) {
			item = new ChatItem();
			groupChatPanelItemMap.put(group.getId(), item);
			item.setText(group.getName());
			item.addCloseExecuteAction(executeAction);
			item.addMouseListener(chatItemMouseAdapter);
			item.addAttribute(Group.class, group);
			item.setIcon(HeadImageIconBox.getGroupHeadImageIcon(group.getHead(), 40, 40));
		}
		return item;
	}

	public void changeChatItem(ChatItem item) {
		UserData user = item.getAttribute(UserData.class);
		if (null != user) {
			ChatPanel chatPanel = getChatPanel(user);
			listChatFrame.setChatPanel(chatPanel);
			PromptManage pm = this.appContext.getManage(PromptManage.class);
			pm.showUserHeadPulse(user.getId(), false);// 停止头像跳动
			pm.remove(user.getId());
		}
		Group group = item.getAttribute(Group.class);
		if (null != group) {
			ChatPanel chatPanel = getChatPanel(group);
			listChatFrame.setChatPanel(chatPanel);
			PromptManage pm = this.appContext.getManage(PromptManage.class);
			pm.showGroupHeadPulse(group.getId(), false);// 停止头像跳动
			pm.remove(group.getId());
		}
		selectChatItem.selectItem(item);
		listChatFrame.repaint();
	}

	public void removeChatItem(ChatItem item) {

		listChatFrame.removeChatItem(item);
		chatItemSet.remove(item);
		if (chatItemSet.size() == 0) {
			listChatFrame.setVisible(false);
		} else {
			if (item.isSelected()) {
				Iterator<ChatItem> iterator = chatItemSet.iterator();
				if (iterator.hasNext()) {
					ChatItem i = iterator.next();
					UserData user = i.getAttribute(UserData.class);
					if (null != user) {
						selectChatItem.selectItem(i);
						ChatPanel chatPanel = getChatPanel(user);
						listChatFrame.setChatPanel(chatPanel);
					}
					Group group = i.getAttribute(Group.class);
					if (null != group) {
						selectChatItem.selectItem(i);
						ChatPanel chatPanel = getChatPanel(group);
						listChatFrame.setChatPanel(chatPanel);
					}
				}
			}
		}
		listChatFrame.repaint();
	}

	public void showCahtFrame(UserData user) {
		ChatItem item = getChatItem(user);
		if (!chatItemSet.contains(item)) {
			chatItemSet.add(item);
			listChatFrame.addChatItem(item);
			item.addAttribute(ChatPanel.class, getChatPanel(user));

		}
		PromptManage pm = this.appContext.getManage(PromptManage.class);
		pm.showUserHeadPulse(user.getId(), false);// 停止头像跳动
		pm.remove(user.getId());// 系统托盘停止跳动

		ChatPanel chatPanel = getChatPanel(user);
		selectChatItem.selectItem(item);

		listChatFrame.setChatPanel(chatPanel);
		listChatFrame.setIconImage(HeadImageIconBox.getUserHeadImageIcon(user.getHead(), 40, 40).getImage());

		listChatFrame.setVisible(true);

	}

	public void showCahtFrame(Group group) {
		ChatItem item = getChatItem(group);
		if (!chatItemSet.contains(item)) {
			chatItemSet.add(item);
			listChatFrame.addChatItem(item);
			item.addAttribute(ChatPanel.class, getChatPanel(group));

		}
		PromptManage pm = this.appContext.getManage(PromptManage.class);
		pm.showGroupHeadPulse(group.getId(), false);// 停止头像跳动
		pm.remove(group.getId());// 系统托盘停止跳动

		ChatPanel chatPanel = getChatPanel(group);

		selectChatItem.selectItem(item);

		listChatFrame.setChatPanel(chatPanel);
		listChatFrame.setIconImage(HeadImageIconBox.getGroupHeadImageIcon(group.getHead(), 40, 40).getImage());

		listChatFrame.setVisible(true);

	}

	/**
	 * 处理聊天窗口左边列表被选中的效果
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 */
	class SelectChatItem {
		ChatItem chatItem;// 当前被选中的对象

		public void selectItem(ChatItem item) {
			if (item != this.chatItem) {
				if (null != this.chatItem) {
					this.chatItem.setSelected(false);
				}
				item.setSelected(true);
			}
			this.chatItem = item;
		}
	}

	public void sendUserMessage(ChatPanel chatPanel) {

		User sendUser = PersonalBox.get(User.class);
		UserData receiveUser = chatPanel.getAttribute(UserData.class);

		List<Section> sections = new ArrayList<Section>();
		List<String> oweList = new ArrayList<String>();
		setChatData(sections, oweList, chatPanel);
		if (sections.isEmpty()) {
			return;
		}
		if (null != sendUser) {
			String value = sendUser.getNickname() + " " + DateUtil.getCurrentDateTime();
			chatPanel.insertShowText(value, "微软雅黑", 9, Color.blue.getRed(), Color.blue.getGreen(), Color.blue.getBlue(), false, false, false);
		}

		for (String htmlText : oweList) {
			chatPanel.insertHtmlText(htmlText);
		}
		chatPanel.initWriteText();// 发送信息后，清空输入框内容。

		boolean underline = chatPanel.isUnderline();
		boolean bold = chatPanel.isBold();
		Color colorObject = chatPanel.getColor();
		boolean italic = chatPanel.isItalic();
		String fontName = chatPanel.getFontName();
		int fontSize = chatPanel.getFontSize();
		String color = ColorUtil.getColorInHexFromRGB(colorObject.getRed(), colorObject.getGreen(), colorObject.getBlue());

		com.oim.net.message.data.chat.Font font = new com.oim.net.message.data.chat.Font();
		font.setBold(bold);
		font.setColor(color);
		font.setItalic(italic);
		font.setName(fontName);
		font.setSize(fontSize);
		font.setUnderline(underline);

		Content content = new Content();
		content.setFont(font);
		content.setSections(sections);
		ChatHandler ch = this.appContext.getHandler(ChatHandler.class);
		ch.sendUserChatMessage(receiveUser.getId(), sendUser.getId(), content);
		LastManage lastManage = this.appContext.getManage(LastManage.class);
		lastManage.addLastUserData(receiveUser);
	}

	public void sendGroupMessage(ChatPanel chatPanel) {

		User sendUser = PersonalBox.get(User.class);
		Group receiveGroup = chatPanel.getAttribute(Group.class);
		List<Section> sections = new ArrayList<Section>();
		List<String> oweList = new ArrayList<String>();
		setChatData(sections, oweList, chatPanel);
		if (sections.isEmpty()) {
			return;
		}

		chatPanel.initWriteText();

		boolean underline = chatPanel.isUnderline();
		boolean bold = chatPanel.isBold();
		Color colorObject = chatPanel.getColor();
		boolean italic = chatPanel.isItalic();
		String fontName = chatPanel.getFontName();
		int fontSize = chatPanel.getFontSize();
		String color = ColorUtil.getColorInHexFromRGB(colorObject.getRed(), colorObject.getGreen(), colorObject.getBlue());

		com.oim.net.message.data.chat.Font font = new com.oim.net.message.data.chat.Font();
		font.setBold(bold);
		font.setColor(color);
		font.setItalic(italic);
		font.setName(fontName);
		font.setSize(fontSize);
		font.setUnderline(underline);

		Content content = new Content();
		content.setFont(font);
		content.setSections(sections);

		ChatHandler ch = this.appContext.getHandler(ChatHandler.class);
		ch.sendGroupChatMessage(receiveGroup.getId(), sendUser.getId(), content);
		LastManage lastManage = this.appContext.getManage(LastManage.class);
		lastManage.addLastGroup(receiveGroup);
	}

	private void setChatData(List<Section> sections, List<String> oweList, ChatPanel chatPanel) {

		boolean underline = chatPanel.isUnderline();
		boolean bold = chatPanel.isBold();
		Color colorObject = chatPanel.getColor();
		boolean italic = chatPanel.isItalic();
		String fontName = chatPanel.getFontName();
		int fontSize = chatPanel.getFontSize();
		String color = ColorUtil.getColorInHexFromRGB(colorObject.getRed(), colorObject.getGreen(), colorObject.getBlue());
		String style = getStyleValue(fontName, fontSize, color, bold, underline, italic).toString();
		String text = chatPanel.getWriteText();
		Document htmlDocument = Jsoup.parse(text);
		if (null != htmlDocument) {
			List<org.jsoup.nodes.Element> elementList = htmlDocument.getElementsByTag("p");
			Section chatData;
			if (!elementList.isEmpty()) {
				if (elementList.size() == 1) {
					org.jsoup.nodes.Element e = elementList.get(0);
					chatData = getChatData(e);
					if (null != chatData) {

						sections.add(chatData);
					}
					e.attr("style", style);
					oweList.add(e.toString());
				} else {
					for (org.jsoup.nodes.Element e : elementList) {
						chatData = getChatData(e);
						if (null != chatData) {
							// chatData.setBold(bold);
							// chatData.setColor(color);
							// chatData.setFontName(fontName);
							// chatData.setFontSize(fontSize);
							// chatData.setItalic(italic);
							// chatData.setUnderline(underline);
							sections.add(chatData);
						}
						e.attr("style", style);
						oweList.add(e.toString());
					}
				}
			}
		}
	}

	private Section getChatData(org.jsoup.nodes.Element e) {
		Section chatData = null;
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
							Item cvd = new Item();
							cvd.setType(Item.type_face);
							cvd.setValue(value);
							itemList.add(cvd);
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
						Item cvd = new Item();
						cvd.setType(Item.type_text);
						cvd.setValue(value);
						itemList.add(cvd);
					}
				}
			}
			if (!itemList.isEmpty()) {
				chatData = new Section();
				chatData.setItems(itemList);
			}
		}
		return chatData;
	}

	public void shake() {
		if (System.currentTimeMillis() - shakeTime < 3000) {
			return;
		}
		java.awt.EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 3; i++) {
					listChatFrame.setBounds(listChatFrame.getX() + 4, listChatFrame.getY() - 4, listChatFrame.getWidth(), listChatFrame.getHeight());
					listChatFrame.formResized();
					try {
						Thread.sleep(40);
						listChatFrame.setBounds(listChatFrame.getX() - 8, listChatFrame.getY(), listChatFrame.getWidth(), listChatFrame.getHeight());
						listChatFrame.formResized();
						Thread.sleep(40);
						listChatFrame.setBounds(listChatFrame.getX(), listChatFrame.getY() + 4, listChatFrame.getWidth(), listChatFrame.getHeight());
						listChatFrame.formResized();
						Thread.sleep(40);
						listChatFrame.setBounds(listChatFrame.getX() + 4, listChatFrame.getY(), listChatFrame.getWidth(), listChatFrame.getHeight());
						listChatFrame.formResized();
						Thread.sleep(40);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				}
				shakeTime = System.currentTimeMillis();
			}
		});
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
			GroupHandler gh = this.appContext.getHandler(GroupHandler.class);
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
		GroupHandler gh = this.appContext.getHandler(GroupHandler.class);
		gh.getGroupMemberListWithUserDataList(groupId, dataBackAction);
	}

	private void setGroupUserList(String groupId, List<UserData> userDataList, List<GroupMember> groupMemberList) {
		groupMemberListMap.put(groupId, groupMemberList);
		GroupUserList groupUserList = groupUserListMap.get(groupId);
		if (groupUserList == null) {
			groupUserList = new GroupUserList();

		}
		for (UserData userData : userDataList) {
			ItemPanel item = new ItemPanel();

			item.addMouseListener(userItemMouseAdapter);
			item.setText(userData.getNickname());
			item.setIcon(HeadImageIconBox.getUserHeadImageIcon(userData.getHead(), 20));
			item.addAttribute(UserData.class, userData);
			groupUserList.add(item);
		}
		ChatPanel chatPanel = groupChatPanelMap.get(groupId);
		if (null != chatPanel) {
			chatPanel.setUserListPanel(groupUserList);
			chatPanel.validate();
		}
	}
}
