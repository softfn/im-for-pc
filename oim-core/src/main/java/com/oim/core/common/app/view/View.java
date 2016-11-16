package com.oim.core.common.app.view;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2015年3月8日 下午3:09:56
 * @version 0.0.1
 */
public interface View {

	public void setVisible(boolean visible);

	public boolean isShowing();

	public void showPrompt(String text);
}
