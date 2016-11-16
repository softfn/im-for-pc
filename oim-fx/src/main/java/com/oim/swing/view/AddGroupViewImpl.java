package com.oim.swing.view;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.JList;
import javax.swing.JOptionPane;

import com.oim.core.app.AppContext;
import com.oim.core.bean.Group;
import com.oim.core.bean.GroupCategory;
import com.oim.core.bean.GroupCategoryMember;
import com.oim.core.business.manage.ListManage;
import com.oim.core.business.sender.GroupCategorySender;
import com.oim.core.business.service.GroupService;
import com.oim.core.common.annotation.Parameter;
import com.oim.core.common.app.view.AbstractView;
import com.oim.core.common.app.view.AddGroupView;
import com.oim.core.net.message.Info;
import com.oim.core.net.message.Message;
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
public class AddGroupViewImpl extends AbstractView implements AddGroupView {

	AddFrame addFrame = new AddFrame();
	private Group group;

	public AddGroupViewImpl(AppContext appContext) {
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

	public void setGroupCategoryList(List<GroupCategory> groupCategoryList) {
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

	public void setGroup(Group group) {
		this.group = group;
		addFrame.setShowIcon(HeadImageIconBox.getGroupHeadImageIcon(group.getHead(), 60, 60));
		addFrame.setShowText(group.getClassification());
		addFrame.setShowName(group.getName());
	}

	public void set(Group group, List<GroupCategory> groupCategoryList) {
		setGroup(group);
		setGroupCategoryList(groupCategoryList);
	}

	private void addMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jLabel4MouseClicked
		String name = JOptionPane.showInputDialog(addFrame, "请输入组名");
		if (null != name && !"".equals(name)) {
			addGroupCategory(name);
		}
	}

	private void agreeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
		Object object = this.addFrame.getSelectedItem();
		String remarks = addFrame.getRemark();
		addGroupCategoryMember(group, (GroupCategory) object, remarks);
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
			GroupCategorySender gch = this.appContext.getSender(GroupCategorySender.class);
			gch.addGroupCategory(name, action);
		}
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
		GroupCategorySender gch = this.appContext.getSender(GroupCategorySender.class);
		gch.addGroupCategoryMember(groupCategory.getId(), group.getId(), remark, action);
	}

	public void showWaiting(boolean show) {
		addFrame.showWaiting(show);
	}

	@Override
	public void showPrompt(String text) {
		// TODO Auto-generated method stub

	}
}
