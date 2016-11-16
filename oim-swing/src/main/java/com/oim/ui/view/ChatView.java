package com.oim.ui.view;

import com.oim.app.AppContext;
import com.oim.common.app.view.AbstractView;

/**
 * 描述：
 * @author XiaHui 
 * @date 2015年3月14日 上午11:14:38
 * @version 0.0.1
 */
public class ChatView extends AbstractView {

	public ChatView(AppContext appContext) {
		super(appContext);
	}

	public boolean isShowing(){
		return false;
	}
}
