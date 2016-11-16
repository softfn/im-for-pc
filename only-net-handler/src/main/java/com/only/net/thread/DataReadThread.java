package com.only.net.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.only.net.action.LostAction;
import com.only.net.connect.ReadHandler;
import com.only.net.data.bean.HandlerData;

/**
 * 返回数据处理
 * 
 * @author XiaHui
 * @date 2015年3月5日 下午6:25:04
 */
public class DataReadThread extends Thread {

	private static final Logger logger = Logger.getLogger(DataReadThread.class);
	private ExecutorService pool = Executors.newFixedThreadPool(10);
	private ConcurrentLinkedQueue<Object> dataQueue = new ConcurrentLinkedQueue<Object>();// 返回信息队列
	private ConcurrentLinkedQueue<String> exceptionDataQueue = new ConcurrentLinkedQueue<String>();// 异常信息队列
	private Map<String, HandlerData> handlerDataMap = new ConcurrentHashMap<String, HandlerData>();
	private Set<LostAction> lostActionSet = new CopyOnWriteArraySet<LostAction>();
	private Set<ReadHandler> readHandlerSet = new CopyOnWriteArraySet<ReadHandler>();

	private long timeOut = 10000;// 消息超时时间，当消息发送后，这个时间内，服务器未回应，便当超时处理
	private long sleepTime = 200;
	private long checkTime = 0;// 记录每次清理消息的临时变量
	private long checkIntervalTime = 1000;// 处理消息的间隔时间
	private boolean lost = false;// 是否系统发送发生异常，如果发生异常，则将未处理的消息弹出，说明发送异常

	public void addReadHandler(ReadHandler readHandler) {
		getReadHandlerSet().add(readHandler);
	}

	public void addLostAction(LostAction lostAction) {
		getLostActionSet().add(lostAction);
	}

	public void add(HandlerData handlerData) {
		handlerDataMap.put(handlerData.getKey(), handlerData);
	}

	public void addExceptionData(String key) {
		exceptionDataQueue.add(key);
	}

	public void back(Object data) {
		dataQueue.add(data);
	}

	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}

	public long getSleepTime() {
		return this.sleepTime;
	}

	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}

	public long getTimeOut() {
		return this.timeOut;
	}

	public void setLost(boolean lost) {
		this.lost = lost;
	}

	public void doLost() {
		this.lost = true;
	}

	private Set<LostAction> getLostActionSet() {
		synchronized (lostActionSet) {
			return lostActionSet;
		}
	}

	private Set<ReadHandler> getReadHandlerSet() {
		synchronized (readHandlerSet) {
			return readHandlerSet;
		}
	}

	@Override
	public void run() {

		while (true) {
			try {
				handleBack();// 处理服务器返回消息
				handleException();// 处理异常消息
				handleOutTime();// 处理超时消息
				handleLost();// 处理发送失败消息
				threadSleep(sleepTime);
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	private void handleLost() {
		if (lost) {
			handleLost(handlerDataMap);
		}
	}

	private void handleLost(Map<String, HandlerData> map) {
		if (map.size() > 0) {
			List<HandlerData> list = new ArrayList<HandlerData>();
			list.addAll(map.values());
			map.clear();
			handleLost(list);
		}
	}

	private void handleLost(List<HandlerData> list) {
		for (HandlerData handlerData : list) {
			handleLost(handlerData);
		}
	}

	private void handleException() {
		String key;
		while (null != exceptionDataQueue && exceptionDataQueue.peek() != null) {
			key = exceptionDataQueue.poll();
			handleLost(key);
		}
	}

	private void handleLost(String key) {
		if (null != key) {
			HandlerData handlerData = handlerDataMap.remove(key);
			if (null != handlerData) {
				handleLost(handlerData);
			}
		}
	}

	/**
	 * 处理服务器返回消息
	 */
	private void handleBack() {
		Object data;
		while (null != dataQueue && dataQueue.peek() != null) {
			data = dataQueue.poll();
			handleBack(data);
		}
	}

	/**
	 * 处理服务器返回消息
	 */
	private void handleBack(Object data) {
		if (null != data) {
			for (ReadHandler readHandler : getReadHandlerSet()) {
				readHandler.read(data, handlerDataMap);
			}
		}
	}

	/**
	 * 处理超时消息
	 */

	private void handleOutTime() {
		if (null != handlerDataMap && handlerDataMap.size() > 0 && (System.currentTimeMillis() - checkTime > checkIntervalTime)) {
			handleOutTime(handlerDataMap);
			this.checkTime = System.currentTimeMillis();
		}
	}

	private void handleOutTime(Map<String, HandlerData> map) {
		List<HandlerData> list = new ArrayList<HandlerData>();
		List<HandlerData> timeOutList = new ArrayList<HandlerData>();
		list.addAll(map.values());
		map.clear();
		for (HandlerData handlerData : list) {
			long now = System.currentTimeMillis() - handlerData.getSendTime();
			if (now > timeOut) {
				timeOutList.add(handlerData);
			} else {
				if (null != handlerData.getData()) {
					map.put(handlerData.getKey(), handlerData);
				}
			}
		}
		handleOutTime(timeOutList);
	}

	private void handleOutTime(List<HandlerData> list) {
		for (HandlerData handlerData : list) {
			handleOutTime(handlerData);
		}
	}

	/**
	 * 处理发送失败信息
	 * 
	 * @param handlerData
	 */
	private void handleLost(HandlerData handlerData) {
		pool.execute(new LostThread(handlerData));

	}

	/**
	 * 处理超时信息
	 * 
	 * @param handlerData
	 */
	private void handleOutTime(HandlerData handlerData) {
		pool.execute(new TimeOutThread(handlerData));

	}

	private void threadSleep(long time) throws InterruptedException {
		sleep(time);
	}

	class LostThread implements Runnable {

		HandlerData handlerData;

		public LostThread(HandlerData handlerData) {
			this.handlerData = handlerData;
		}

		public void run() {
			if (handlerData.getDataBackAction() != null) {
				handlerData.getDataBackAction().lost();
			}
		}
	}

	class TimeOutThread implements Runnable {

		HandlerData handlerData;

		public TimeOutThread(HandlerData handlerData) {
			this.handlerData = handlerData;
		}

		public void run() {
			if (handlerData.getDataBackAction() != null) {
				handlerData.getDataBackAction().timeOut();
			}
		}
	}
}
