package com.im.business.server.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.im.business.server.message.Message;
import com.im.bean.UserCategory;
import com.im.bean.UserCategoryMember;
import com.im.business.common.service.UserCategoryService;
import com.only.action.annotation.ActionMapping;
import com.only.action.annotation.MethodMapping;
import com.only.net.session.SocketSession;
import com.only.parameter.annotation.Parameter;


/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2016年1月8日 下午8:12:59
 * @version 0.0.1
 */
@Controller
@ActionMapping(value = "102")
public class UserCategoryAction {
	@Resource
	private UserCategoryService userCategorySerivce;

	/**
	 * 
	 * @param userMessage
	 * @param dataWrite
	 * @return
	 */
	@MethodMapping(value = "0001")
	public Message addUserCategory(SocketSession socketSession,@Parameter("userCategory")UserCategory userCategory) {
		if(null==userCategory.getUserId()||"".equals(userCategory.getUserId())){
			userCategory.setUserId(socketSession.getKey());
		}
		return userCategorySerivce.addUserCategory(userCategory);
	}

	/**
	 * 
	 * @param userMessage
	 * @param dataWrite
	 * @return
	 */
	@MethodMapping(value = "0002")
	public Message addUserCategoryMember(SocketSession socketSession,@Parameter("userCategoryMember")UserCategoryMember userCategoryMember) {
		if(null==userCategoryMember.getOwnUserId()||"".equals(userCategoryMember.getOwnUserId())){
			userCategoryMember.setOwnUserId(socketSession.getKey());
		}
		return userCategorySerivce.addUserCategoryMember(userCategoryMember);
	}
}
