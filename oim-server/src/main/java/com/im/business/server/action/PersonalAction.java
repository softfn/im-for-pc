package com.im.business.server.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.im.business.server.message.Message;
import com.im.business.server.message.data.LoginData;
import com.im.bean.User;
import com.im.business.common.service.PersonalService;
import com.im.business.common.service.UserService;
import com.only.action.annotation.ActionMapping;
import com.only.action.annotation.MethodMapping;
import com.only.net.session.SocketSession;
import com.only.parameter.annotation.Parameter;

/**
 * 描述： 个人信息业务处理
 * 
 * @author XiaHui
 * @date 2016年1月7日 下午7:45:48
 * @version 0.0.1
 */
@Controller
@ActionMapping(value = "100")
public class PersonalAction {

	@Resource
	private UserService userService;
	@Resource
	private PersonalService personalService;

	/**
	 * 不同地方登录
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年1月5日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年1月5日
	 * @param session
	 * @param number
	 */
	// private void checkDifferentLogin(String id) {
	// }

	/**
	 * 登录
	 * 
	 * @param userMessage
	 * @param dataWrite
	 * @return
	 */
	@MethodMapping(value = "0001", isIntercept = false)
	public Message login(SocketSession socketSession, @Parameter("loginData") LoginData loginData) {
		Message message = personalService.login(socketSession, loginData);
		return message;
	}

	/**
	 * 处理客户端断开连接后自动连接
	 * 
	 * @param userMessage
	 * @param dataWrite
	 * @return
	 */
	@MethodMapping(value = "0002", isIntercept = false)
	public Message connect(SocketSession socketSession, @Parameter("loginData") LoginData loginData) {
		Message message = personalService.login(socketSession, loginData);
		return message;
	}

	@MethodMapping(value = "0003")
	public Message getUserData(SocketSession socketSession) {
		Message message = new Message();
		User user = userService.getUser(socketSession.getKey());
		message.put("user", user);
		return message;
	}

	/**
	 * 修改个人信息
	 * 
	 * @param userMessage
	 * @param dataWrite
	 * @return
	 */
	@MethodMapping(value = "0004")
	public Object updateUser(@Parameter("user") User user) {
		return personalService.update(user);
	}

	/**
	 * 修改密码
	 * 
	 * @param socketSession
	 * @param password
	 * @return
	 */
	@MethodMapping(value = "0005")
	public Object updatePassword(SocketSession socketSession, @Parameter("password") String password) {
		return personalService.updatePassword(socketSession.getKey(), password);
	}
}
