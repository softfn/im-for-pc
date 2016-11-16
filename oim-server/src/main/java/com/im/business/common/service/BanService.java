package com.im.business.common.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.im.business.common.data.BanChat;
import com.im.business.common.data.BanRoomChat;
import com.im.business.server.message.Head;
import com.im.business.server.message.Message;
import com.im.business.common.service.api.BanBaseService;

/**
 * @author: XiaHui
 * @date: 2016年8月18日 下午1:55:58
 */
@Service
public class BanService {
	protected final Logger logger = LogManager.getLogger(this.getClass());
	@Resource
	BanBaseService banBaseService;

	public void banChat(String userId, String address, long banTime) {
		if (null != userId && !"".equals(userId)) {
			banChatUserId(userId, banTime);
		}
		if (null != address && !"".equals(address)) {
			banChatAddress(address, banTime);
		}
	}


	public void banChatAddress(String address, long banTime) {
		banBaseService.setBanChatAddress(address, banTime);
	}

	public void banChatUserId(String userId, long banTime) {
		banBaseService.setBanChatId(userId, banTime);
	}

	public void banRoomChat(String roomId, List<String> excludeUserIds, List<String> excludeAddresses, long banTime) {
		banBaseService.setBanRoomChat(roomId, excludeUserIds, excludeAddresses, banTime);
	}

	public boolean isBan(Head head, String roomId, String userId, String address) {
		boolean isBan = false;

		long banTime = 0;// 禁止时间长度
		// long startTime;// 开始禁止的时间
		long endTime = 0;// 到期时间
		BanRoomChat brc = banBaseService.getBanRoomChat(roomId);

		if (null != brc) {
			boolean isExcludeAddress = brc.isExcludeAddress(address);
			boolean isExcludeUserId = brc.isExcludeUserId(userId);
			isBan = (!isExcludeAddress && !isExcludeUserId);// ip和id都没有被排除，那么就被禁言了
			banTime = brc.getBanTime();// 禁止时间长度
			// startTime = brc.getStartTime();// 开始禁止的时间
			endTime = brc.getStartTime();// 到期时间
		} else {
			BanChat abc = banBaseService.getBanChatByAddress(address);// 地址被禁言了
			BanChat ibc = banBaseService.getBanChatById(userId);// id被禁言了

			isBan = ((null != abc) || (null != ibc));// 只要其中一个被禁言了就是被禁言了
			if (null != abc && null != ibc) {
				if (abc.getEndTime() > ibc.getEndTime()) {
					banTime = abc.getBanTime();// 禁止时间长度
					// startTime = abc.getStartTime();// 开始禁止的时间
					endTime = abc.getStartTime();// 到期时间
				} else {
					banTime = ibc.getBanTime();// 禁止时间长度
					// startTime = ibc.getStartTime();// 开始禁止的时间
					endTime = ibc.getStartTime();// 到期时间
				}
			} else if (null != abc) {
				banTime = abc.getBanTime();// 禁止时间长度
				// startTime = abc.getStartTime();// 开始禁止的时间
				endTime = abc.getStartTime();// 到期时间
			} else if (null != ibc) {
				banTime = ibc.getBanTime();// 禁止时间长度
				// startTime = ibc.getStartTime();// 开始禁止的时间
				endTime = ibc.getStartTime();// 到期时间
			}
		}

		if (isBan) {
			long time = endTime - System.currentTimeMillis();
			long second = time / 1000;
			long minute = second / 60;

			long banSecond = banTime / 1000;
			long banMinute = banSecond / 60;

			String text = "您已被禁言";
			if (banMinute > 0) {
				text += (banMinute + "分钟！");
			} else {
				text += ("！");
			}

			if (minute > 0) {
				text += (minute + "分钟后将会解除禁言。");
			}

			head.setAction("");
			head.setMethod("");
			head.setTime(System.currentTimeMillis());
			Message m = new Message();
			m.setHead(head);
			m.setInfoCode(Message.code_fail);
			m.setInfoValue(text);
		}
		return isBan;
	}
}
