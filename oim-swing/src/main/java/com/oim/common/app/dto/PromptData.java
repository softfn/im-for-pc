package com.oim.common.app.dto;

import javax.swing.ImageIcon;

import com.oim.common.action.CallAction;

/**
 * @Author: XiaHui
 * @Date: 2016年1月23日
 * @ModifyUser: XiaHui
 * @ModifyDate: 2016年1月23日
 */
public class PromptData {

	private ImageIcon imageIcon;
	private CallAction executeAction;

	public PromptData(ImageIcon imageIcon, CallAction executeAction) {
		super();
		this.imageIcon = imageIcon;
		this.executeAction = executeAction;
	}

	public ImageIcon getImageIcon() {
		return imageIcon;
	}

	public void setImageIcon(ImageIcon imageIcon) {
		this.imageIcon = imageIcon;
	}

	public CallAction getExecuteAction() {
		return executeAction;
	}

	public void setExecuteAction(CallAction executeAction) {
		this.executeAction = executeAction;
	}

}
