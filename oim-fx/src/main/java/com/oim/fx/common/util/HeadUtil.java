package com.oim.fx.common.util;

import com.oim.core.bean.User;

/**
 * @author: XiaHui
 * @date: 2016年10月11日 下午12:50:33
 */
public class HeadUtil {
	
	public static boolean isGray(String status){
		boolean gray=true;
		switch (status) {
		case User.status_online:
			gray=false;
			break;
		case User.status_call_me:
			gray=false;
			break;
		case User.status_away:
			gray=false;
			break;
		case User.status_busy:
			gray=false;
			break;
		case User.status_mute:
			gray=false;
			break;
		case User.status_invisible:
			gray=true;
			break;
		case User.status_offline:
			gray=true;
			break;
		default:
			gray=true;
			break;
		}
		return gray;
	}
}
