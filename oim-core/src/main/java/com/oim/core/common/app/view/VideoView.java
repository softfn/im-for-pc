package com.oim.core.common.app.view;

import com.oim.core.net.message.data.AddressData;
import com.oim.core.net.message.data.UserData;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2015年3月16日 下午10:42:19
 * @version 0.0.1
 */
public interface VideoView  extends View {
	
	public void getAgree(String userId, AddressData videoAddress);
	
	public void getShut(String userId);
	
	public void showGetVideoFrame(UserData userData) ;
	
	public void showSendVideoFrame(UserData userData);

}
