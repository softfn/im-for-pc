package com.oim.business.handler;

import com.oim.app.AppContext;
import com.oim.common.app.handler.SendHandler;
import com.oim.net.message.Head;
import com.oim.net.message.Message;
import com.only.net.data.action.DataBackAction;

/**
 * @author XiaHui
 * @date 2015年3月16日 下午3:23:23
 */
public class VideoHandler extends SendHandler {

	public VideoHandler(AppContext appContext) {
		super(appContext);
	}

	/**
	 * 获取服务器的视频服务端口
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param dataBackAction
	 */
	public void getVideoServerPort(DataBackAction dataBackAction) {
		
		Message message = new Message();
		Head head = new Head();
		head.setAction("502");
		head.setMethod("0001");
		head.setTime(System.currentTimeMillis());
		message.setHead(head);
		
		this.write(message, dataBackAction);
	}

	/**
	 * 请求视频聊天
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 */
	public void requestVideo(String sendUserId, String receiveUserId) {
		
		Message message = new Message();
		message.put("receiveUserId", receiveUserId);
		message.put("sendUserId", sendUserId);
		
		Head head = new Head();
		head.setAction("502");
		head.setMethod("0003");
		head.setTime(System.currentTimeMillis());
		message.setHead(head);
		
		this.write(message);
	}
	
	public void responseVideo(String sendUserId, String receiveUserId,String actionType) {
		Message message = new Message();
		message.put("receiveUserId", receiveUserId);
		message.put("sendUserId", sendUserId);
		message.put("actionType", actionType);
		
		Head head = new Head();
		head.setAction("502");
		head.setMethod("0004");
		head.setTime(System.currentTimeMillis());
		message.setHead(head);
		this.write(message);
	}
	
	
	public void getUserVideoAddress(String userId,DataBackAction dataBackAction) {
		Message message = new Message();
		message.put("userId", userId);
		
		Head head = new Head();
		head.setAction("502");
		head.setMethod("0002");
		head.setTime(System.currentTimeMillis());
		message.setHead(head);
		this.write(message,dataBackAction);
	}
}
