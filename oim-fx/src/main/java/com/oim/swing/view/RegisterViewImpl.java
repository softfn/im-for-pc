package com.oim.swing.view;

import javax.swing.JFrame;

import com.oim.core.app.AppContext;
import com.oim.core.common.app.view.AbstractView;
import com.oim.core.common.app.view.RegisterView;
import com.oim.ui.RegisterDialog;

public class RegisterViewImpl extends AbstractView implements RegisterView{
	
	RegisterDialog registerDialog = new RegisterDialog(new JFrame(), "用户注册", false);;
	
	public RegisterViewImpl(AppContext appContext) {
		super(appContext);
	}

	@Override
	public void setVisible(boolean visible) {
		registerDialog.setVisible(visible);
	}

	@Override
	public boolean isShowing() {
		// TODO Auto-generated method stub
		return false;
	}

}
