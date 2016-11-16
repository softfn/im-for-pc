package com.oim.core.business.action;

import com.oim.core.app.AppContext;
import com.oim.core.bean.User;
import com.oim.core.common.annotation.ActionMapping;
import com.oim.core.common.annotation.MethodMapping;
import com.oim.core.common.annotation.Parameter;
import com.oim.core.common.app.action.AbstractAction;
import com.oim.core.net.message.Info;

/**
 * 描述：用户个人业务处理
 * 
 * @author XiaHui
 * @date 2016年1月7日 下午7:45:00
 * @version 0.0.1
 */
@ActionMapping(value = "100")
public class PersonalAction extends AbstractAction {

	public PersonalAction(AppContext appContext) {
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
	public void updateUserBack(Info info) {

	}
}
