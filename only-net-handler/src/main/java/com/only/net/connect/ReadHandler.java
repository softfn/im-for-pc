package com.only.net.connect;

import java.util.Map;

import com.only.net.data.bean.HandlerData;

/**
 * 返回数据处理
 * 
 * @author XiaHui
 * @date 2015年3月5日 下午6:25:04
 */
public interface ReadHandler {

	public void read(Object data,Map<String, HandlerData> handlerDataMap);
}
