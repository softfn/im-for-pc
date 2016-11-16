package com.oim.swing.view;

import com.oim.core.app.AppContext;
import com.oim.core.common.app.view.AbstractView;
import com.oim.core.common.app.view.ThemeView;
import com.oim.ui.ThemeFrame;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2015年3月16日 下午10:42:19
 * @version 0.0.1
 */
public class ThemeViewImpl extends AbstractView implements ThemeView {

	ThemeFrame findFrame = new ThemeFrame();

	public ThemeViewImpl(AppContext appContext) {
		super(appContext);
	}

	public void setVisible(boolean visible) {
		findFrame.setVisible(visible);
	}

	public boolean isShowing() {
		return findFrame.isShowing();
	}

	@Override
	public void showPrompt(String text) {
		
	}
}
