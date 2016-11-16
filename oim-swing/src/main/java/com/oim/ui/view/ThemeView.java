package com.oim.ui.view;

import com.oim.app.AppContext;
import com.oim.common.app.view.AbstractView;
import com.oim.ui.ThemeFrame;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2015年3月16日 下午10:42:19
 * @version 0.0.1
 */
public class ThemeView extends AbstractView {

	ThemeFrame findFrame = new ThemeFrame(this);

	public ThemeView(AppContext appContext) {
		super(appContext);

	}

	public void setVisible(boolean visible) {
		findFrame.setVisible(visible);
	}

	public boolean isShowing() {
		return findFrame.isShowing();

	}

}
