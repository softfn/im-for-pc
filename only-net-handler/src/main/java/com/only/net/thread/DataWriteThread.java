package com.only.net.thread;

import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.log4j.Logger;

import com.only.net.connect.WriteHandler;
import com.only.net.data.action.DataBackAction;
import com.only.net.data.action.DataSentAction;
import com.only.net.data.bean.HandlerData;

/**
 * 处理发送数据的线程
 * 
 * @author XiaHui
 * @date 2015年3月5日 下午12:26:12
 */
public class DataWriteThread extends Thread {
	private static final Logger logger = Logger.getLogger(DataWriteThread.class);

	private ConcurrentLinkedQueue<HandlerData> handlerDataQueue = new ConcurrentLinkedQueue<HandlerData>();// 消息队列
	private Set<DataSentAction> dataSentActionSet =  new CopyOnWriteArraySet<DataSentAction>();
	private Set<WriteHandler> writeHandlerSet =  new CopyOnWriteArraySet<WriteHandler>();

	private long intervalBeatTime = 1000 * 60;// 消息发送时间间隔(毫秒)
	private long currentBeatIntervalTime = 1000 * 60;// 心跳本次发送间隔时间
	private long backTime = 0;// 记录最后服务器返回消息的时间
	private long lastSendTime = 0;// 记录最后一次，客服端发送信息的时间
	private long sleepTime = 200;// 线程休眠时间
	private boolean sendBeatData = false;//
	private HandlerData beatData;// 发送心跳包的内容

	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}

	public long getSleepTime() {
		return this.sleepTime;
	}

	public long getLastSendTime() {
		return this.lastSendTime;
	}

	public void setSendBeatData(boolean sendBeatData) {
		this.sendBeatData = sendBeatData;
	}

	public boolean iSsendBeatData() {
		return this.sendBeatData;
	}

	public void setBeatData(String key, Object data) {
		this.beatData = new HandlerData(key, data);
	}

	public void setIntervalBeatTime(long intervalBeatTime) {
		this.intervalBeatTime = intervalBeatTime;
	}

	public long getIntervalBeatTime() {
		return this.intervalBeatTime;
	}

	public void setBackTime(long backTime) {
		this.backTime = backTime;
		this.currentBeatIntervalTime = (intervalBeatTime - ((backTime - this.lastSendTime)));
	}

	public void addDataSentAction(DataSentAction dataSentAction) {
		getDataSentActionSet().add(dataSentAction);
	}

	public void removeDataSentAction(DataSentAction dataSentAction) {
		getDataSentActionSet().remove(dataSentAction);
	}

	public void addWriteHandler(WriteHandler writeHandler) {
		getWriteHandlerSet().add(writeHandler);
	}

	public void removeWriteHandler(WriteHandler writeHandler) {
		getWriteHandlerSet().remove(writeHandler);
	}

	private Set<WriteHandler> getWriteHandlerSet() {
		synchronized (writeHandlerSet) {
			return writeHandlerSet;
		}
	}

	private Set<DataSentAction> getDataSentActionSet() {
		synchronized (dataSentActionSet) {
			return dataSentActionSet;
		}
	}

	public void write(String key, Object data) {
		write(key, data, null);
	}

	public void write(String key, Object data, DataBackAction dataBackAction) {
		handlerDataQueue.add(new HandlerData(key, data, dataBackAction));
	}

	@Override
	public void run() {
		while (true) {
			handleBeatData();// 处理心跳包
			handlerData();// 处理发送消息
			threadSleep(sleepTime);
		}
	}

	protected void threadSleep(long time) {
		try {
			sleep(time);
		} catch (InterruptedException e) {
			logger.error(e);
		}
	}

	private void handlerData() {
		HandlerData handlerData;
		while (null != handlerDataQueue && handlerDataQueue.peek() != null) {
			handlerData = handlerDataQueue.poll();
			handlerData(handlerData);
		}
	}

	/*
	 * 测连接，发送心跳包
	 */
	private void handleBeatData() {
		if (null != beatData && System.currentTimeMillis() - backTime > currentBeatIntervalTime && sendBeatData) {
			send(beatData);
		}
	}

	/**
	 * 处理要发送的信息
	 * 
	 * @param handlerData
	 */
	private void handlerData(HandlerData handlerData) {
		if (null != handlerData.getDataBackAction()) {// 如果消息有Key，说明消息需要回调
			handlerData.setSendTime(System.currentTimeMillis());
			for (DataSentAction dataSentAction : getDataSentActionSet()) {
				dataSentAction.execute(handlerData);
			}
		}
		send(handlerData);
	}

	private void send(HandlerData data) {
		if (null != data) {
			for (WriteHandler writeHandler : getWriteHandlerSet()) {
				writeHandler.write(data);
			}
			lastSendTime = System.currentTimeMillis();
		}
	}
}
