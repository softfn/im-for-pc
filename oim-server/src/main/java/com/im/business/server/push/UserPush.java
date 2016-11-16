package com.im.business.server.push;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.im.business.server.message.Head;
import com.im.business.server.message.Message;
import com.im.business.server.message.data.UserData;
import com.im.business.server.mq.MessageQueueWriteHandler;

/**
 * @author: XiaHui
 * @date: 2016年8月26日 上午9:21:41
 */
@Service
public class UserPush {
	@Resource
	MessageQueueWriteHandler messageQueueWriteHandler;
	
	public void pushUserStatus(Head head,String userId,String status,List<String> userIdList){
		Message message=new Message();
		message.setHead(head);
		message.put("userId", userId);
		message.put("status", status);
		messageQueueWriteHandler.push(userIdList, message);
	}
	
	public void pushUserUpdate(Head head,String userId,List<String> userIdList){
		Message message=new Message();
		message.setHead(head);
		message.put("userId", userId);
		messageQueueWriteHandler.push(userIdList, message);
	}
	
	public void pushUserUpdate(Head head,UserData userData,List<String> userIdList){
		Message message=new Message();
		message.setHead(head);
		message.put("userData", userData);
		messageQueueWriteHandler.push(userIdList, message);
	}
}
