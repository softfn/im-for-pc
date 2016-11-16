package com.oim.ui.user;

import com.oim.swing.common.box.ImageBox;
import com.oim.ui.component.BasePanel;
import com.only.OnlyLabel;
import com.only.OnlyScrollPane;
import com.only.OnlyTextArea;
import com.only.OnlyTextField;
import com.over.image.OverDecoratedImage;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2016年1月29日 下午6:09:22
 * @version 0.0.1
 */
public class GroupDataPanel extends BasePanel {

	private static final long serialVersionUID = 1L;

	private OverDecoratedImage headLabel = new OverDecoratedImage();
	private OnlyLabel nameLabel = new OnlyLabel("名称");
	private OnlyLabel typeLabel = new OnlyLabel("类型");
	private OnlyLabel introduceLabel = new OnlyLabel("介绍");

	private OnlyTextField nameField = new OnlyTextField();
	private OnlyTextField typeField = new OnlyTextField();
	private OnlyTextArea introduceTextArea = new OnlyTextArea();
	private OnlyScrollPane scrollPane = new OnlyScrollPane();

	public GroupDataPanel() {
		init();
		initTestData();
	}

	private void init() {
		this.setLayout(null);
		this.setOpaque(false);
		headLabel.setLayout(null);

		headLabel.setRound(60);
		headLabel.setDrawGlassLayer(false);
		headLabel.setDrawBorder(true);

		scrollPane.setViewportView(introduceTextArea);
		scrollPane.setHeaderVisible(false);
		headLabel.setBounds(30, 10, 50, 50);

		nameLabel.setBounds(30, 60, 40, 40);
		nameField.setBounds(30, 95, 220, 25);
		typeLabel.setBounds(30, 130, 40, 25);
		typeField.setBounds(30, 165, 220, 25);
		introduceLabel.setBounds(30, 200, 40, 25);
		scrollPane.setBounds(30, 235, 300, 240);

		add(headLabel);
		add(nameLabel);
		add(typeLabel);
		add(introduceLabel);
		add(nameField);
		add(typeField);
		add(scrollPane);
	}

	private void initTestData() {
		headLabel.setIcon(ImageBox.getImageIcon("Resources/Images/Head/Group/1.png"));
	}

	public String getGroupName() {
		return nameField.getText();
	}

	public String getType() {
		return typeField.getText();
	}

	public String getIntroduce() {
		return introduceTextArea.getText();
	}

	public void setGroupName(String name) {
		nameField.setText(name);
	}

	public void setType(String type) {
		typeField.setText(type);
	}

	public void setIntroduce(String introduce) {
		introduceTextArea.setText(introduce);
	}
}
