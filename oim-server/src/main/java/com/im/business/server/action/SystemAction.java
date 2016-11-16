package com.im.business.server.action;

import org.springframework.stereotype.Controller;

import com.only.action.annotation.ActionMapping;
import com.only.action.annotation.MethodMapping;

/**
 * 描述：
 * 
 * @author 夏辉
 * @date 2014年6月14日 下午9:31:55
 * @version 0.0.1
 */
@Controller
@ActionMapping(value = "000")
public class SystemAction {

	@MethodMapping(value = "0001")
	public void heartbeat() {
		System.out.println(1111);
	}
}
