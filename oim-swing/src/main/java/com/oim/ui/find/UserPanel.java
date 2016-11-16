/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.ui.find;

import com.oim.ui.component.BasePanel;
import com.only.OnlyComboBox;
import com.only.OnlyTextField;

/**
 * date 2012-9-21 22:50:42
 * 
 * @author XiaHui
 */
public class UserPanel extends BasePanel {

	private static final long serialVersionUID = 1L;

	private OnlyTextField nicknameTextField = new OnlyTextField();
	private OnlyComboBox<Object> sexComboBox = new OnlyComboBox<Object>();
	private OnlyComboBox<Object> ageComboBox = new OnlyComboBox<Object>();
	private OnlyComboBox<Object> bloodComboBox = new OnlyComboBox<Object>();
	private OnlyComboBox<Object> constellationComboBox = new OnlyComboBox<Object>();

	public UserPanel() {
		initComponents();
		initEvent();
		resizedComponent();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initComponents() {
		this.setLayout(null);
		//		this.setBackground(new java.awt.Color(255, 255, 255));
		this.setBackground(new java.awt.Color(235, 242, 249));

		nicknameTextField.setLabelText("帐号/昵称");

		ageComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "年龄", "16-22", "23-30", "31-40", "40以上" }));
		sexComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "性别", "男", "女" }));
		constellationComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "星座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座" }));
		bloodComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "血型", "A", "B", "AB", "O", "其他" }));

		nicknameTextField.setBounds(100, 20, 360, 25);
		ageComboBox.setBounds(100, 55, 80, 22);
		sexComboBox.setBounds(195, 55, 80, 22);
		constellationComboBox.setBounds(285, 55, 80, 22);
		bloodComboBox.setBounds(380, 55, 80, 22);

		add(bloodComboBox);
		add(constellationComboBox);
		add(nicknameTextField);
		add(sexComboBox);
		add(ageComboBox);

	}

	private void initEvent() {

	}

	private void resizedComponent() {

	}

}
