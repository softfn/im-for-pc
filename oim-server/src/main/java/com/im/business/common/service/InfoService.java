package com.im.business.common.service;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.im.business.server.action.BaseAction;
import com.im.business.server.message.Head;
import com.im.business.server.message.Message;
import com.only.net.server.SessionServer;

/**
 * @author Only
 * @date 2016年5月20日 上午11:45:04
 */
@Service
public class InfoService {
	protected final Logger logger = LogManager.getLogger(this.getClass());
	@Resource
	SessionServer dataHandler;

	/**
	 * 通用给指定用户推送弹出信息
	 * @author: XiaHui
	 * @createDate: 2016年8月22日 下午2:27:46
	 * @update: XiaHui
	 * @updateDate: 2016年8月22日 下午2:27:46
	 */
	public void pushPopupInfo(String userId, String code, String text) {
		Head head = new Head();
		head.setAction(BaseAction.action_info);
		head.setMethod("1-0001");
		head.setTime(System.currentTimeMillis());
		Message m = new Message();
		m.setHead(head);
		m.setInfoCode(code);
		m.setInfoValue(text);
		dataHandler.push(userId, m);
	}
}
