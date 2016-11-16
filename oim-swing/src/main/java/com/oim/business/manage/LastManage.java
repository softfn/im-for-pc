package com.oim.business.manage;

import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.oim.app.AppContext;
import com.oim.bean.Group;
import com.oim.bean.User;
import com.oim.common.app.manage.Manage;
import com.oim.common.box.HeadImageIconBox;
import com.oim.common.box.UserDataBox;
import com.oim.net.message.data.UserData;
import com.oim.ui.component.list.HeadLabel;
import com.oim.ui.component.list.HeadLabelAction;
import com.oim.ui.view.MainView;

/**
 * 描述：对主界面的记录列表的管理
 * 
 * @author XiaHui
 * @date 2015年4月12日 上午10:18:18
 * @version 0.0.1
 */
public class LastManage extends Manage {
	private Map<String, HeadLabel> lastMap = new ConcurrentHashMap<String, HeadLabel>();
	private HeadLabelAction userLabelAction;
	private HeadLabelAction groupLabelAction;

	public LastManage(AppContext appContext) {
		super(appContext);
		initEvent();
	}

	private void initEvent() {
		userLabelAction = new HeadLabelAction() {

			@Override
			public void action(MouseEvent e, HeadLabel headLabel) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (e.getClickCount() == 2) {
						UserData userData = headLabel.getAttribute(UserData.class.getName());
						ChatManage chatManage = appContext.getManage(ChatManage.class);
						chatManage.showCahtFrame(userData);
					}
				}
				if (e.getButton() == MouseEvent.BUTTON3) {

				}
			}
		};

		groupLabelAction = new HeadLabelAction() {

			@Override
			public void action(MouseEvent e, HeadLabel headLabel) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (e.getClickCount() == 2) {
						Group group = headLabel.getAttribute(Group.class);
						ChatManage chatManage = appContext.getManage(ChatManage.class);
						chatManage.showCahtFrame(group);
					}
				}
			}
		};
	}

	/**
	 * 将与用户聊天插入历史列表
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param userData
	 */
	public void addLastUserData(UserData userData) {

		HeadLabel head = lastMap.get(userData.getId());
		if (null == head) {
			head = new HeadLabel();
			lastMap.put(userData.getId(), head);
		}
		head.addAction(userLabelAction);//添加点击动作
		head.addAttribute(UserData.class.getName(), userData);
		head.setRoundedCorner(40, 40);
		head.setHeadIcon(HeadImageIconBox.getUserHeadImageIcon(userData.getHead(), 40, 40));

		head.setRemark(userData.getNickname());
		head.setNickname("(" + userData.getAccount() + ")");
		head.setShowText(userData.getSignature());

		// head.setStatusIcon(new
		// ImageIcon("Resources/Images/Default/Status/FLAG/Big/MobilePhoneQQOn.png"));

		head.setStatus("[2G]");
		// head.addBusinessAttribute(new JLabel(new
		// ImageIcon("Resources/Images/Default/Status/FLAG/Big/imonline.png")));
		head.setShowSeparator(true);

		if (isGray(userData.getStatus())) {
			head.setGray(true);
		}

		MainView mainView = this.getSingleView(MainView.class);
		mainView.addLastNode(head);
	}

	public void updateLastUserData(UserData userData) {
		HeadLabel head = lastMap.get(userData.getId());
		if (null != head) {
			head.addAttribute(UserData.class.getName(), userData);
			head.setRoundedCorner(40, 40);
			head.setHeadIcon(HeadImageIconBox.getUserHeadImageIcon(userData.getHead(), 40, 40));

			head.setRemark(userData.getNickname());
			head.setNickname("(" + userData.getAccount() + ")");
			head.setShowText(userData.getSignature());

			// head.setStatusIcon(new
			// ImageIcon("Resources/Images/Default/Status/FLAG/Big/MobilePhoneQQOn.png"));

			if (isGray(userData.getStatus())) {
				head.setGray(true);
			} else {
				head.setGray(false);
			}
			head.addAttribute(UserData.class.getName(), userData);
			head.setRoundedCorner(40, 40);
			head.setHeadIcon(HeadImageIconBox.getUserHeadImageIcon(userData.getHead(), 40, 40));
			head.setStatus("[2G]");
			ImageIcon icon = UserDataBox.getStatusImageIcon((UserData.status_invisible == userData.getStatus()) ? UserData.status_offline : userData.getStatus());
			JLabel statusLabel = head.getAttribute("statusLabel");
			if (null == statusLabel) {
				statusLabel = new JLabel(icon);
				head.addBusinessAttribute("statusLabel", statusLabel);
			} else {
				statusLabel.setIcon(icon);
			}
			head.revalidate();
		}

	}

	public void addLastGroup(Group group) {

		HeadLabel head = lastMap.get(group.getId());
		if (null == head) {
			head = new HeadLabel();
			lastMap.put(group.getId(), head);
		}
		head.addAction(groupLabelAction);
		head.addAttribute(Group.class, group);
		head.setRoundedCorner(40, 40);
		head.setHeadIcon(HeadImageIconBox.getGroupHeadImageIcon(group.getHead(), 40, 40));

		head.setRemark(group.getName());
		head.setNickname("(" + group.getNumber() + ")");
		head.setShowText(group.getIntroduce());
		head.setShowSeparator(true);

		MainView mainView = this.getSingleView(MainView.class);
		mainView.addLastNode(head);
	}

	public void updateLastGroup(Group group) {
		HeadLabel head = lastMap.get(group.getId());
		if (null != head) {
			head.setRemark(group.getName());
			head.setNickname("(" + group.getNumber() + ")");
			head.setShowText(group.getIntroduce());
			head.addAttribute(Group.class, group);
			head.setRoundedCorner(40, 40);
			head.setHeadIcon(HeadImageIconBox.getGroupHeadImageIcon(group.getHead(), 40, 40));
			head.setStatus("[2G]");
		}
	}
	
	private boolean isGray(String status){
		boolean gray=true;
		switch (status) {
		case User.status_online:
			gray=false;
			break;
		case User.status_call_me:
			gray=false;
			break;
		case User.status_away:
			gray=false;
			break;
		case User.status_busy:
			gray=false;
			break;
		case User.status_mute:
			gray=false;
			break;
		case User.status_invisible:
			gray=true;
			break;
		case User.status_offline:
			gray=true;
			break;
		default:
			gray=true;
			break;
		}
		return gray;
	}
}
