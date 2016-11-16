package com.oim.core.business.manage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.oim.core.app.AppContext;
import com.oim.core.common.action.CallAction;
import com.oim.core.common.app.dto.PromptData;
import com.oim.core.common.app.dto.PromptData.IconType;
import com.oim.core.common.app.manage.Manage;
import com.oim.core.common.app.view.MainView;
import com.oim.core.common.app.view.TrayView;
import com.oim.core.common.sound.SoundHandler;
import com.oim.core.common.task.ExecuteTask;

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
	public void showUserHeadPulse(String userId, boolean pulse) {
		MainView mainView = this.appContext.getSingleView(MainView.class);
		mainView.showUserHeadPulse(userId, pulse);;
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
	public void showGroupHeadPulse(String groupId, boolean pulse) {
		MainView mainView = this.appContext.getSingleView(MainView.class);
		mainView.showGroupHeadPulse(groupId, pulse);
	}

	public void put(String key, PromptData promptData) {
		map.put(key, promptData);
		TrayView trayView = this.getSingleView(TrayView.class);
		trayView.put(key);
	}

	public void put(String key,IconType iconType, String icon, CallAction callAction) {
		put(key, new PromptData(iconType,icon, callAction));
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
	public PromptData getPromptData(String key) {
		PromptData promptData = map.get(key);
		return promptData;
	}
}
