package com.im.business.server.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.im.business.server.message.Message;
import com.im.bean.GroupCategory;
import com.im.bean.GroupCategoryMember;
import com.im.business.common.service.GroupCategoryService;
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
@ActionMapping(value = "201")
public class GroupCategoryAction   {
	@Resource
	private GroupCategoryService groupCategorySerivce;

	/**
	 * 
	 * @param groupMessage
	 * @param dataWrite
	 * @return
	 */
	@MethodMapping(value = "0001")
	public Message addGroupCategory(SocketSession socketSession,@Parameter("groupCategory")GroupCategory groupCategory) {
		if(null==groupCategory.getUserId()||"".equals(groupCategory.getUserId())){
			groupCategory.setUserId(socketSession.getKey());
		}
		return groupCategorySerivce.addGroupCategory(groupCategory);
	}

	/**
	 * 
	 * @param groupMessage
	 * @param dataWrite
	 * @return
	 */
	@MethodMapping(value = "0002")
	public Message addGroupCategoryMember(SocketSession socketSession,@Parameter("groupCategoryMember")GroupCategoryMember groupCategoryMember) {
		if(null==groupCategoryMember.getUserId()||"".equals(groupCategoryMember.getUserId())){
			groupCategoryMember.setUserId(socketSession.getKey());
		}
		return groupCategorySerivce.addGroupCategoryMember(groupCategoryMember);
	}
}
