package com.oim.common.app.view;

import com.oim.app.AppContext;


/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2015年3月8日 下午3:11:11
 * @version 0.0.1
 */
public abstract class AbstractView implements View {

	protected AppContext appContext;

	public AbstractView(AppContext appContext) {
		this.appContext = appContext;
	}

	
	public void setVisible(boolean visible){
		
	}
}
