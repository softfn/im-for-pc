package com.im.business.server.push;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.im.bean.Group;
import com.im.business.server.message.Head;
import com.im.business.server.message.Message;
import com.im.business.server.mq.MessageQueueWriteHandler;

/**
 * @author: XiaHui
 * @date: 2016年8月26日 上午9:21:41
 */
@Service
public class GroupPush {
	@Resource
	MessageQueueWriteHandler messageQueueWriteHandler;

	public void pushGroup(Head head, Group group, List<String> userIdList) {
		Message message = new Message();
		message.setHead(head);
		message.put("group", group);
		messageQueueWriteHandler.push(userIdList, message);
	}
	
	
	public void pushAddUser(String groupId,String userId,List<String> userIdList){
		Message message=new Message();
		Head head=new Head();
		head.setAction("200");
		head.setMethod("0011");
		head.setTime(System.currentTimeMillis());
		message.setHead(head);
		message.put("userId", userId);
		message.put("groupId", groupId);
		messageQueueWriteHandler.push(userIdList, message);
	}
}
