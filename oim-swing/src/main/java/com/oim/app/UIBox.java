package com.oim.app;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.oim.ui.component.list.HeadLabel;
import com.oim.ui.component.list.Node;
import com.only.OnlyBorderDialog;
import com.only.OnlyBorderFrame;

/**
 * @author XiaHui
 * @date 2015年3月4日 下午3:36:30
 */
public class UIBox {

	private static Map<Object, Object> objectMap = new ConcurrentHashMap<Object, Object>();

	public static Set<OnlyBorderFrame> frameSet = Collections.synchronizedSet(new HashSet<OnlyBorderFrame>());
	public static Set<OnlyBorderDialog> dialogSet = Collections.synchronizedSet(new HashSet<OnlyBorderDialog>());
	/***** 存放好友分组列表组件 *****/
	private static Map<String, Node> userListNodeMap = new ConcurrentHashMap<String, Node>();
	/*** 存放群分组组件 **/
	private static Map<String, Node> groupListNodeMap = new ConcurrentHashMap<String, Node>();
	/** 存放单个好友组件 ***/
	private static Map<String, HeadLabel> userHeadLabelMap = new ConcurrentHashMap<String, HeadLabel>();
	/** 存放单个群 **/
	private static Map<String, HeadLabel> groupHeadLabelMap = new ConcurrentHashMap<String, HeadLabel>();

	public static void put(Object key, Object value) {
		objectMap.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(Object key) {
		return (T) objectMap.get(key);
	}

	/**
	 * 把系统的界面都放入map中，方便管理，如可以修改主题等
	 * 
	 * @param onlyBorderFrame
	 */
	public static void add(OnlyBorderFrame onlyBorderFrame) {
		frameSet.add(onlyBorderFrame);
		BufferedImage bi = UIBox.get("key_window_background_image");
		if (null != bi) {
			onlyBorderFrame.setBackgroundImage(bi);
		}
	}

	public static void add(OnlyBorderDialog onlyBorderDialog) {
		dialogSet.add(onlyBorderDialog);
		BufferedImage bi = UIBox.get("key_window_background_image");
		if (null != bi) {
			onlyBorderDialog.setBackgroundImage(bi);
		}
	}

	public static void putUserListNode(String key, Node value) {
		userListNodeMap.put(key, value);
	}

	public static Node getUserListNode(String key) {
		return userListNodeMap.get(key);
	}

	public static Node removeUserListNode(String key) {
		return userListNodeMap.remove(key);
	}

	// ////////////////////
	public static void putGroupListNode(String key, Node value) {
		groupListNodeMap.put(key, value);
	}

	public static Node getGroupListNode(String key) {
		return groupListNodeMap.get(key);
	}

	public static Node removeGroupNode(int key) {
		return groupListNodeMap.remove(key);
	}

	// //////////////////
	public static void putUserHeadLabel(String key, HeadLabel value) {
		userHeadLabelMap.put(key, value);
	}

	public static HeadLabel getUserHeadLabel(String key) {
		return userHeadLabelMap.get(key);
	}

	public static HeadLabel removeUserHeadLabel(String key) {
		return userHeadLabelMap.remove(key);
	}

	// ////////////////////
	public static void putGroupHeadLabel(String key, HeadLabel value) {
		groupHeadLabelMap.put(key, value);
	}

	public static HeadLabel getGroupHeadLabel(String key) {
		return groupHeadLabelMap.get(key);
	}

	public static HeadLabel removeGroupHeadLabel(String key) {
		return groupHeadLabelMap.remove(key);
	}
}
