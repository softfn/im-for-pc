package com.oim.swing.view;

import javax.swing.JFrame;

import com.oim.core.app.AppContext;
import com.oim.core.common.app.view.AbstractView;
import com.oim.core.common.app.view.NetSettingView;
import com.oim.ui.LoginSettingDialog;

/**
 * @author XiaHui
 * @date 2015年3月16日 上午11:48:23
 */
public class NetSettingViewImpl  extends AbstractView implements NetSettingView{
	
	LoginSettingDialog netSettingDialog = new LoginSettingDialog(new JFrame(), "网络录设置", false);;
	
	public NetSettingViewImpl(AppContext appContext) {
		super(appContext);
	}

	@Override
	public void setVisible(boolean visible) {
		netSettingDialog.setVisible(visible);
	}

	@Override
	public boolean isShowing() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void showPrompt(String text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAddress(String address) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAddress() {
		// TODO Auto-generated method stub
		return null;
	}

}
