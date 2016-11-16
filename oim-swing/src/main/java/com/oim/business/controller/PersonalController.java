package com.oim.business.controller;

import com.oim.app.AppContext;
import com.oim.bean.User;
import com.oim.business.service.PersonalService;
import com.oim.common.annotation.ActionMapping;
import com.oim.common.annotation.MethodMapping;
import com.oim.common.annotation.Parameter;
import com.oim.common.app.controller.AbstractController;
import com.oim.net.message.Info;
import com.oim.ui.view.MainView;

/**
 * 描述：用户个人业务处理
 * 
 * @author XiaHui
 * @date 2016年1月7日 下午7:45:00
 * @version 0.0.1
 */
@ActionMapping(value = "100")
public class PersonalController extends AbstractController {

	public PersonalController(AppContext appContext) {
		super(appContext);
	}

	/**
	 * 登录后执行操作
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param userMessage
	 */
	@MethodMapping(value = "0001")
	public void loginBack(Info info, @Parameter("user") User user) {
		PersonalService ps = this.appContext.getService(PersonalService.class);
		ps.loginBack(info, user);
	}

	/**
	 * 接受账号另外被登录信息
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param message
	 */
	@MethodMapping(value = "0003")
	public void handleDifferentLogin() {
		this.appContext.getConnectThread().setAutoConnect(false);// 另外登录了账号后，设置为不自动连接
		this.appContext.getConnectThread().closeConnect();// 关闭连接
		MainView mainView = this.appContext.getSingleView(MainView.class);
		mainView.setVisible(true);
		mainView.shwoDifferentLogin();// 弹出被另外登录提示框
	}

	/***
	 * 修改个人信息后接受服务器推送更新
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param userMessage
	 */
	@MethodMapping(value = "0004")
	public void updateUserBack() {

	}
}
