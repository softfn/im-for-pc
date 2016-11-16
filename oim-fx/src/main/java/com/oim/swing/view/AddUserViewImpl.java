package com.oim.swing.view;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.JList;
import javax.swing.JOptionPane;

import com.oim.core.app.AppContext;
import com.oim.core.bean.UserCategory;
import com.oim.core.bean.UserCategoryMember;
import com.oim.core.business.manage.ListManage;
import com.oim.core.business.sender.UserCategorySender;
import com.oim.core.business.service.UserService;
import com.oim.core.common.annotation.Parameter;
import com.oim.core.common.app.view.AbstractView;
import com.oim.core.common.app.view.AddUserView;
import com.oim.core.net.message.Info;
import com.oim.core.net.message.Message;
import com.oim.core.net.message.data.UserData;
import com.oim.core.net.server.Back;
import com.oim.swing.common.box.HeadImageIconBox;
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
public class AddUserViewImpl extends AbstractView implements AddUserView{

	AddFrame addFrame = new AddFrame();
	private UserData userData;

	public AddUserViewImpl(AppContext appContext) {
		super(appContext);
		initUI();
		initEvent();
	}

	@SuppressWarnings({ "unchecked", "serial" })
	private void initUI() {
		addFrame.setComboBoxRenderer(new OnlyListCellRenderer() {
			@SuppressWarnings("rawtypes")
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				if (value instanceof UserCategory) {
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
		addFrame.removeAllItems();
		for (UserCategory userCategory : userCategoryList) {
			addFrame.addItem(userCategory);
		}
		addFrame.setTitleText("添加好友");
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

	public void set(UserData userData, List<UserCategory> userCategoryList) {
		setUserData(userData);
		setUserCategoryList(userCategoryList);
	}

	private void addMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jLabel4MouseClicked
		String name = JOptionPane.showInputDialog(addFrame, "请输入组名");
		if (null != name && !"".equals(name)) {
			addUserCategory(name);
		}
	}

	private void agreeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
		Object object = this.addFrame.getSelectedItem();
		String remarks = addFrame.getRemark();
		addUserCategoryMember(userData, (UserCategory) object, remarks);
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

			UserCategorySender uch = this.appContext.getSender(UserCategorySender.class);
			uch.addUserCategory(name, action);
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
		UserCategorySender uch = this.appContext.getSender(UserCategorySender.class);
		uch.addUserCategoryMember(userCategory.getId(), user.getId(), remark, action);
	}


	public void showWaiting(boolean show) {
		addFrame.showWaiting(show);
	}

	@Override
	public void showPrompt(String text) {
		
	}
}
