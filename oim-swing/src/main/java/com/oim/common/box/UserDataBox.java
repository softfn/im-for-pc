package com.oim.common.box;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.ImageIcon;

import com.oim.bean.User;
import com.oim.common.util.ImageUtil;
import com.oim.net.message.data.UserData;

/**
 * 描述：这里是存放所有好友列表的用户
 * 
 * @author XiaHui
 * @date 2016年1月17日 下午1:49:49
 * @version 0.0.1
 */
public class UserDataBox {

	private static Map<String, UserData> userMap = new ConcurrentHashMap<String, UserData>();
	private static Map<String, ImageIcon> statusImageIconMap = new ConcurrentHashMap<String, ImageIcon>();

	public static void put(String id, UserData userData) {
		userMap.put(id, userData);
	}

	public static UserData get(String id) {
		return userMap.get(id);
	}

	public static ImageIcon getStatusImageIcon(String status) {
		ImageIcon image = statusImageIconMap.get(status);
		if (null == image) {
			switch (status) {
			case User.status_online:
				image = ImageUtil.getImageIcon("Resources/Images/Default/Status/FLAG/Big/imonline.png");
				break;
			case User.status_call_me:
				image = ImageUtil.getImageIcon("Resources/Images/Default/Status/FLAG/Big/Qme.png");
				break;
			case User.status_away:
				image = ImageUtil.getImageIcon("Resources/Images/Default/Status/FLAG/Big/away.png");
				break;
			case User.status_busy:
				image = ImageUtil.getImageIcon("Resources/Images/Default/Status/FLAG/Big/busy.png");
				break;
			case User.status_mute:
				image = ImageUtil.getImageIcon("Resources/Images/Default/Status/FLAG/Big/mute.png");
				break;
			case User.status_invisible:
				image = ImageUtil.getImageIcon("Resources/Images/Default/Status/FLAG/Big/invisible.png");
				break;
			case User.status_offline:
				image = ImageUtil.getImageIcon("Resources/Images/Default/Status/FLAG/Big/imoffline.png");
				break;
			default:
				image = ImageUtil.getImageIcon("Resources/Images/Default/Status/FLAG/Big/imoffline.png");
				break;
			}
			statusImageIconMap.put(status, image);
		}
		return image;
	}
}
