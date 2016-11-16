package com.oim.business.manage;

import java.awt.Image;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.ImageIcon;

import com.oim.app.AppContext;
import com.oim.app.UIBox;
import com.oim.common.action.CallAction;
import com.oim.common.app.dto.PromptData;
import com.oim.common.app.manage.Manage;
import com.oim.common.sound.SoundHandler;
import com.oim.common.task.ExecuteTask;
import com.oim.net.thread.HeadPulseThread;
import com.oim.ui.component.list.HeadLabel;
import com.oim.ui.view.TrayView;

/**
 * 对消息提示管理，如头像跳动、系统托盘跳动
 * 
 * @Author: XiaHui
 * @Date: 2016年1月23日
 * @ModifyUser: XiaHui
 * @ModifyDate: 2016年1月23日
 */
public class PromptManage extends Manage {
	SoundHandler soundHandler = new SoundHandler();// 声音播放处理
	private Map<String, PromptData> map = new ConcurrentHashMap<String, PromptData>();

	public PromptManage(AppContext appContext) {
		super(appContext);
	}

	/**
	 * 播放提示声音
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param type
	 */
	public void playSound(int type) {
		soundHandler.play(type);
	}

	/**
	 * 显示用户头像跳动或者停止用户头像跳动
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param userId
	 * @param show
	 */
	public void showUserHeadPulse(String userId, boolean show) {
		HeadPulseThread hpt = appContext.getHeadPulseThread();
		HeadLabel head = UIBox.getUserHeadLabel(userId);
		if (show) {
			if (null != head && !hpt.has(userId)) {
				hpt.putHead(userId, head.getIocnPanel());
			}
		} else {
			if (hpt.has(userId)) {
				hpt.removeHead(userId);
			}
		}
	}

	/**
	 * 显示群头像跳动或者停止群头像跳动
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param groupId
	 * @param show
	 */
	public void showGroupHeadPulse(String groupId, boolean show) {
		HeadPulseThread hpt = appContext.getHeadPulseThread();
		HeadLabel head = UIBox.getGroupHeadLabel(groupId);
		if (show) {
			if (null != head && !hpt.has(groupId)) {
				hpt.putHead(groupId, head.getIocnPanel());
			}
		} else {
			if (hpt.has(groupId)) {
				hpt.removeHead(groupId);
			}
		}
	}

	public void put(String key, PromptData promptData) {
		map.put(key, promptData);
		TrayView trayView = this.getSingleView(TrayView.class);
		trayView.put(key);
	}

	public void put(String key, ImageIcon imageIcon, CallAction callAction) {
		put(key, new PromptData(imageIcon, callAction));
	}

	public void remove(String key) {
		map.remove(key);
	}

	/**
	 * 如果提示信息由执行操作，当点击头像或者托盘时，会执行其操作。
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param key
	 */
	public void execute(String key) {
		PromptData promptData = map.remove(key);
		if (null != promptData) {
			final CallAction ca = promptData.getExecuteAction();
			if (null != ca) {
				ExecuteTask executeTask = new ExecuteTask() {

					@Override
					public void execute() {
						ca.execute();
					}
				};
				this.appContext.add(executeTask);
			}
		}
	}

	/***
	 * 获取系统托盘要提示的头像
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param key
	 * @return
	 */
	public Image getTrayImage(String key) {
		PromptData promptData = map.get(key);
		Image image = (null != promptData && promptData.getImageIcon() != null) ? promptData.getImageIcon().getImage() : null;
		return image;
	}
}
