package com.oim.ui.component;

import com.only.OnlyPopupMenu;
import com.only.OnlyTextArea;
import com.only.util.OnlyUIUtil;

/**
 * 消息提示窗口
 * 
 * @author 夏辉
 * @date 2014年5月26日 上午1:45:48
 * @version 0.0.1
 */
public class TextPromptPopup extends OnlyPopupMenu {

	private static final long serialVersionUID = 1L;

	private OnlyTextArea textArea = new OnlyTextArea();

	public TextPromptPopup() {
		initComponents();
	}

	private void initComponents() {
		this.setLayout(new java.awt.CardLayout());

		textArea.setBorder(null);
		textArea.setEditable(false);
		textArea.setFont(OnlyUIUtil.getDefaultFont()); // NOI18N

		add(textArea);

	}

	public void setText(String text) {
		textArea.setText(text);
	}

}
