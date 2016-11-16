package com.oim.ui.view;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.JList;
import javax.swing.JOptionPane;

import com.oim.app.AppContext;
import com.oim.bean.Group;
import com.oim.bean.GroupCategory;
import com.oim.bean.GroupCategoryMember;
import com.oim.bean.UserCategory;
import com.oim.bean.UserCategoryMember;
import com.oim.business.handler.GroupCategoryHandler;
import com.oim.business.handler.UserCategoryHandler;
import com.oim.business.manage.ListManage;
import com.oim.business.service.GroupService;
import com.oim.business.service.UserService;
import com.oim.common.annotation.Parameter;
import com.oim.common.app.view.AbstractView;
import com.oim.common.box.HeadImageIconBox;
import com.oim.net.message.Info;
import com.oim.net.message.Message;
import com.oim.net.message.data.UserData;
import com.oim.net.server.Back;
import com.oim.ui.AddFrame;
import com.only.laf.OnlyListCellRenderer;
import com.only.net.data.action.DataBackActionAdapter;

/**
 * 描述： 添加好友或者加入群显示窗口
 * 
 * @author XiaHui
 * @date 2015年3月16日 下午10:42:19
 * @version 0.0.1
 */
public class AddView extends AbstractView {

	AddFrame addFrame = new AddFrame();
	private int addType = 0;
	private UserData userData;
	private Group group;

	public AddView(AppContext appContext) {
		super(appContext);
		initUI();
		initEvent();
	}

	@SuppressWarnings({ "unchecked", "serial" })
	private void initUI() {
		addFrame.setComboBoxRenderer(new OnlyListCellRenderer() {
			@SuppressWarnings("rawtypes")
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				if (value instanceof GroupCategory) {
					setText((value == null) ? "" : ((GroupCategory) value).getName());
				} else if (value instanceof UserCategory) {
					setText((value == null) ? "" : ((UserCategory) value).getName());
				} else {
					super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				}
				setOpaque(isSelected);
				setForeground(isSelected ? Color.white : Color.black);
				setBackground(isSelected ? Color.blue : Color.white);
				return this;
			}
		});

	}

	private void initEvent() {
		addFrame.addCategoryMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				addMouseClicked(evt);
			}
		});
		addFrame.addDoneAction(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				agreeActionPerformed(evt);
			}
		});
	}

	public void setUserCategoryList(List<UserCategory> userCategoryList) {
		addType = 0;
		addFrame.removeAllItems();
		for (UserCategory userCategory : userCategoryList) {
			addFrame.addItem(userCategory);
		}
		addFrame.setTitleText("添加好友");
	}

	public void setGroupCategoryList(List<GroupCategory> groupCategoryList) {
		addType = 1;
		addFrame.removeAllItems();
		for (GroupCategory groupCategory : groupCategoryList) {
			addFrame.addItem(groupCategory);
		}
		addFrame.setTitleText("加入群");
	}

	public void setVisible(boolean visible) {
		addFrame.setVisible(visible);
	}

	public boolean isShowing() {
		return addFrame.isShowing();

	}

	public void setUserData(UserData userData) {
		this.userData = userData;
		addFrame.setShowIcon(HeadImageIconBox.getUserHeadImageIcon(userData.getHead(), 60, 60));
		addFrame.setShowText(userData.getSignature());
		addFrame.setShowName(userData.getNickname());
	}

	public void setGroup(Group group) {
		this.group = group;
		addFrame.setShowIcon(HeadImageIconBox.getGroupHeadImageIcon(group.getHead(), 60, 60));
		addFrame.setShowText(group.getClassification());
		addFrame.setShowName(group.getName());
	}

	public void set(UserData userData, List<UserCategory> userCategoryList) {
		setUserData(userData);
		setUserCategoryList(userCategoryList);
	}

	public void set(Group group, List<GroupCategory> groupCategoryList) {
		setGroup(group);
		setGroupCategoryList(groupCategoryList);
	}

	private void addMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jLabel4MouseClicked
		String name = JOptionPane.showInputDialog(addFrame, "请输入组名");
		if (null != name && !"".equals(name)) {
			if (addType == 0) {
				addUserCategory(name);
			} else {
				addGroupCategory(name);
			}
		}
	}

	private void agreeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
		Object object = this.addFrame.getSelectedItem();
		String remarks = addFrame.getRemark();
		if (object instanceof UserCategory) {
			addUserCategoryMember(userData, (UserCategory) object, remarks);
		}

		if (object instanceof GroupCategory) {
			addGroupCategoryMember(group, (GroupCategory) object, remarks);
		}
	}

	public void addUserCategory(String name) {
		if (null != name && !"".equals(name)) {

			DataBackActionAdapter action = new DataBackActionAdapter() {

				@Back
				public void back(@Parameter("userCategory") UserCategory userCategory) {
					UserService userService = appContext.getService(UserService.class);
					userService.addUserCategory(userCategory);
					addFrame.addItem(userCategory);
				}
			};

			UserCategoryHandler uch = this.appContext.getHandler(UserCategoryHandler.class);
			uch.addUserCategory(name, action);
		}
	}

	public void addGroupCategory(String name) {
		if (null != name && !"".equals(name)) {

			DataBackActionAdapter action = new DataBackActionAdapter() {

				@Back
				public void back(@Parameter("groupCategory") GroupCategory groupCategory) {
					GroupService groupService = appContext.getService(GroupService.class);
					groupService.addGroupCategory(groupCategory);
					addFrame.addItem(groupCategory);
				}
			};
			GroupCategoryHandler gch = this.appContext.getHandler(GroupCategoryHandler.class);
			gch.addGroupCategory(name, action);
		}
	}

	public void addUserCategoryMember(UserData user, UserCategory userCategory, String remark) {

		addFrame.showWaiting(true);
		DataBackActionAdapter action = new DataBackActionAdapter() {
			@Override
			public void lost() {
				addFrame.showWaiting(false);
			}

			@Override
			public void timeOut() {
				addFrame.showWaiting(false);
			}

			@Back
			public void back(Info info, @Parameter("userCategoryMember") UserCategoryMember userCategoryMember) {
				addFrame.showWaiting(false);
				if (Message.code_success.equals(info.getCode())) {
					addFrame.setVisible(false);
					ListManage listManage = appContext.getManage(ListManage.class);
					UserData user = this.getAttribute(UserData.class.getName());
					listManage.add(user, userCategoryMember);
				} else {
					addFrame.showPromptMessage("添加失败！");
				}
			}
		};
		action.addAttribute(UserData.class.getName(), user);
		UserCategoryHandler uch = this.appContext.getHandler(UserCategoryHandler.class);
		uch.addUserCategoryMember(userCategory.getId(), user.getId(), remark, action);
	}

	public void addGroupCategoryMember(Group group, GroupCategory groupCategory, String remark) {

		addFrame.showWaiting(true);
		DataBackActionAdapter action = new DataBackActionAdapter() {

			@Override
			public void lost() {
				addFrame.showWaiting(false);
			}

			@Override
			public void timeOut() {
				addFrame.showWaiting(false);
			}

			@Back
			public void back(Info info, @Parameter("groupCategoryMember") GroupCategoryMember groupCategoryMember) {
				addFrame.showWaiting(false);
				if (Message.code_success.equals(info.getCode())) {
					addFrame.setVisible(false);
					ListManage listManage = appContext.getManage(ListManage.class);
					Group group = this.getAttribute(Group.class);
					listManage.add(group, groupCategoryMember);
				} else {
					addFrame.showPromptMessage("加入失败！");
				}
			}
		};
		action.addAttribute(Group.class, group);
		GroupCategoryHandler gch = this.appContext.getHandler(GroupCategoryHandler.class);
		gch.addGroupCategoryMember(groupCategory.getId(), group.getId(), remark, action);
	}

	public void showWaiting(boolean show) {
		addFrame.showWaiting(show);
	}
}
