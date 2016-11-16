package com.im.business.common.service.api;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.im.business.common.data.BanChat;
import com.im.business.common.data.BanRoomChat;

/**
 * @author: XiaHui
 * @date: 2016年8月18日 上午10:53:49
 */
@Service
public class BanBaseService {
	protected final Logger logger = LogManager.getLogger(this.getClass());
	private Map<String, BanRoomChat> banRoomChatMap = new ConcurrentSkipListMap<String, BanRoomChat>();
	private Map<String, BanChat> banChatMap = new ConcurrentSkipListMap<String, BanChat>();

	private String key_address = "address_";
	private String key_id = "address_id_";

	public void setBanChatAddress(String address, long banTime) {
		long startTime = System.currentTimeMillis();
		long endTime = startTime + banTime;
		BanChat bc = new BanChat();
		bc.setEndTime(endTime);
		bc.setStartTime(startTime);
		bc.setBanTime(banTime);
		banChatMap.put(key_address + address, bc);

	}

	public void setBanChatId(String id, long banTime) {
		long startTime = System.currentTimeMillis();
		long endTime = startTime + banTime;
		BanChat bc = new BanChat();
		bc.setEndTime(endTime);
		bc.setStartTime(startTime);
		bc.setBanTime(banTime);
		banChatMap.put(key_id + id, bc);
	}

	public void setBanRoomChat(String roomId, List<String> excludeUserIds, List<String> excludeAddresses, long banTime) {
		long startTime = System.currentTimeMillis();
		long endTime = startTime + banTime;

		BanRoomChat brc = new BanRoomChat();
		brc.setBanTime(banTime);
		brc.setStartTime(startTime);
		brc.setEndTime(endTime);

		if (null != excludeUserIds && !excludeUserIds.isEmpty()) {
			for (String userId : excludeUserIds) {
				brc.putExcludeUserId(userId);
			}
		}

		if (null != excludeAddresses && !excludeAddresses.isEmpty()) {
			for (String address : excludeAddresses) {
				brc.putExcludeAddress(address);
			}
		}
		banRoomChatMap.put(roomId, brc);
	}

	public BanRoomChat getBanRoomChat(String roomId) {
		BanRoomChat bc = banRoomChatMap.get(roomId);
		if (null != bc) {
			if (bc.getBanTime() > 0 && bc.getEndTime() < System.currentTimeMillis()) {
				banRoomChatMap.remove(roomId);
				bc = null;
			}
		}
		return bc;
	}

	public BanChat getBanChatById(String id) {
		String key = key_id + id;
		BanChat bc = banChatMap.get(key);
		if (null != bc) {
			if (bc.getBanTime() > 0 && bc.getEndTime() <= System.currentTimeMillis()) {
				banChatMap.remove(key);
				bc = null;
			}
		}
		return bc;
	}

	public BanChat getBanChatByAddress(String address) {
		String key = key_address + address;
		BanChat bc = banChatMap.get(key);
		if (null != bc) {
			if (bc.getBanTime() > 0 && bc.getEndTime() <= System.currentTimeMillis()) {
				banChatMap.remove(key);
				bc = null;
			}
		}
		return bc;
	}
}
