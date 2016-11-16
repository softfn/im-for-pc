package com.oim.core.business.manage;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Timer;
import java.util.TimerTask;

import com.oim.core.app.AppContext;
import com.oim.core.bean.User;
import com.oim.core.common.app.manage.Manage;
import com.oim.core.common.app.view.VideoView;
import com.oim.core.common.box.PersonalBox;
import com.oim.core.common.task.ExecuteTask;
import com.oim.core.net.message.data.AddressData;
import com.oim.core.net.message.data.UserData;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2016年1月15日 下午7:52:44
 * @version 0.0.1
 */
public class VideoManage extends Manage {
	
	private AddressData videoServerAddress;

	private long time=0;
	
	public VideoManage(AppContext appContext) {
		super(appContext);
		init();
	}

	public void receivedServerBack(){
		time=System.currentTimeMillis();
	}
	
	private void init() {
		Timer timer = new Timer();
		timer.schedule(new UpdateVideoTask(), 1000, 20000);
	}



	public void getAgree(String userId, AddressData videoAddress) {
		VideoView videoView=this.appContext.getSingleView(VideoView.class);
		videoView.getAgree(userId, videoAddress);
	}

	public void getShut(String userId) {
		VideoView videoView=this.appContext.getSingleView(VideoView.class);
		videoView.getShut(userId);
	}


	public void setVideoServerAddress(AddressData videoServerAddress) {
		this.videoServerAddress = videoServerAddress;
		ExecuteTask et = new ExecuteTask() {

			@Override
			public void execute() {
				for (int i = 0; i < 5; i++) {
					handlerVideoServerAddress();
					try {
						Thread.sleep(800);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					long tempTime=(System.currentTimeMillis()-time);
					if(tempTime>(1000*60*10)){
						break;
					}
				}
			}
		};
		this.appContext.add(et);
	}

	private void handlerVideoServerAddress() {
		User user = PersonalBox.get(User.class);
		String address = (null == videoServerAddress) ? null : videoServerAddress.getAddress();
		int port = (null == videoServerAddress) ? -1 : videoServerAddress.getPort();
		String userId = (null == user) ? null : user.getId();
		if (null != address && !"".endsWith(address) && -1 != port && null != userId && !"".equals(userId)) {
			SocketAddress socketAddress = new InetSocketAddress(address, port);
			String key=userId+","+"1";
			appContext.getVideoModule().connectServer(key.getBytes(), socketAddress);
		}
	}

	class UpdateVideoTask extends TimerTask {

		@Override
		public void run() {
			long tempTime=(System.currentTimeMillis()-time);
			if(tempTime>(1000*60*10)){
				handlerVideoServerAddress();
			}
		}
	}

	

	public void showSendVideoFrame(UserData userData) {
		VideoView videoView=this.appContext.getSingleView(VideoView.class);
		videoView.showSendVideoFrame(userData);
	}

	public void showGetVideoFrame(UserData userData) {
		VideoView videoView=this.appContext.getSingleView(VideoView.class);
		videoView.showGetVideoFrame(userData);
	}
}
