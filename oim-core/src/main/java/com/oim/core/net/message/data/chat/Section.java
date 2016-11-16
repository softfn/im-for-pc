package com.oim.core.net.message.data.chat;

import java.util.List;

/**
 * 聊天内容信息列表
 * 
 * @author XiaHui
 * @date 2015年3月2日 下午2:51:24
 */
public class Section {

	private List<Item> items;

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

}
