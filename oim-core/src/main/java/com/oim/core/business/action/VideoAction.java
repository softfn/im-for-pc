package com.oim.core.business.action;

import com.oim.core.app.AppContext;
import com.oim.core.business.constant.VideoConstant;
import com.oim.core.business.manage.VideoManage;
import com.oim.core.business.sender.UserSender;
import com.oim.core.business.sender.VideoSender;
import com.oim.core.common.annotation.ActionMapping;
import com.oim.core.common.annotation.MethodMapping;
import com.oim.core.common.annotation.Parameter;
import com.oim.core.common.app.action.AbstractAction;
import com.oim.core.common.box.UserDataBox;
import com.oim.core.common.box.UserDataTempBox;
import com.oim.core.net.message.data.AddressData;
import com.oim.core.net.message.data.UserData;
import com.oim.core.net.server.Back;
import com.only.net.data.action.DataBackAction;
import com.only.net.data.action.DataBackActionAdapter;

/**
 * 描述：
 * 
 * @author 夏辉
 * @date 2014年6月14日 下午9:31:55
 * @version 0.0.1
 */
@ActionMapping(value = "502")
public class VideoAction extends AbstractAction {

	public VideoAction(AppContext appContext) {
		super(appContext);
	}

	@MethodMapping(value = "0003")
	public void getRequest(@Parameter("sendUserId")String sendUserId) {
		UserData userData = UserDataBox.get(sendUserId);// 先从好友集合里面获取用户信息，如果用户不是好友，那么从服务器下载用户信息
		if (null == userData) {// 为null说明发送信息的不在好友列表，那么就要从服务器获取发送信息用户的信息了
			
			DataBackAction dataBackAction = new DataBackActionAdapter() {
				@Back
				public void back(@Parameter("userData") UserData userData) {
					UserDataTempBox.put(userData.getId(), userData);
					showGetVideoFrame(userData);
				}
			};
			UserSender uh = this.appContext.getSender(UserSender.class);
			uh.getUserDataById(sendUserId, dataBackAction);
		} else {
			showGetVideoFrame(userData);
		}
	}

	private void showGetVideoFrame(UserData userData) {
		VideoManage vm = this.appContext.getManage(VideoManage.class);
		vm.showGetVideoFrame(userData);
	}

	@MethodMapping(value = "0004")
	public void getResponse(@Parameter("sendUserId")String sendUserId,@Parameter("actionType")String actionType) {

		VideoManage vm = this.appContext.getManage(VideoManage.class);
		if (VideoConstant.action_type_agree.equals(actionType)) {
			DataBackAction dataBackAction = new DataBackActionAdapter() {
				@Back
				public void back(@Parameter("videoAddress") AddressData videoAddress) {
					vm.getAgree(sendUserId, videoAddress);
				}
			};
			VideoSender vh = this.appContext.getSender(VideoSender.class);
			vh.getUserVideoAddress(sendUserId, dataBackAction);
		} else {
			vm.getShut(sendUserId);
		}
	}
	
	@MethodMapping(value = "0005")
	public void receivedVideoServerBack() {
		VideoManage vm = this.appContext.getManage(VideoManage.class);
		vm.receivedServerBack();
	}
}
