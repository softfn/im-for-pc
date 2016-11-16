package com.only.net.connect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import com.only.net.action.ConnectBackAction;
import com.only.net.action.ConnectStatusAction;

/**
 * @author XiaHui
 * @date 2015年3月5日 上午11:56:38
 */
public class ConnectThread extends Thread {
	private static final Logger logger = Logger.getLogger(ConnectThread.class);

	private ConcurrentLinkedQueue<ConnectBackAction> backQueue = new ConcurrentLinkedQueue<ConnectBackAction>();// 返回信息队列
	protected Set<ConnectStatusAction> connectStatusActionSet = Collections.synchronizedSet(new HashSet<ConnectStatusAction>());
	protected boolean autoConnect = false;
	protected int autoConnectCount = 30;// 当连接断开，系统自动连接的次数
	protected int connectCount = 0;// 连接次数记录
	protected long sleepTime = 200;// 线程休眠时间
	protected long connectTimeOut;
	protected long autoCloseConnectTime = -1;// 自动断开连接时间，比如客服端进行登录失败后，一直没有关闭程序，就会设置自动断开时间（大于0便执行）
	protected long closeConnectStartTime = 0;// 记录开始设置自动断开时间的时间，用来计算时间是否达到。
	protected Connector connector;
	protected boolean connectedTemp;
	protected ConnectData connectData;

	public void setConnector(Connector connector) {
		this.connector = connector;
	}

	public Connector getConnector() {
		return connector;
	}

	public void startConnect() {
		this.setAutoConnect(true);
	}

	public void setConnectData(ConnectData connectData) {
		this.connectData = connectData;
	}

	public synchronized void addConnectBackAction(ConnectBackAction connectBackAction) {
		backQueue.add(connectBackAction);
	}

	public void addConnectStatusAction(ConnectStatusAction connectStatusAction) {
		getStatusActionSet().add(connectStatusAction);
	}

	public void removeConnectStatusAction(ConnectStatusAction connectStatusAction) {
		getStatusActionSet().remove(connectStatusAction);
	}

	private Set<ConnectStatusAction> getStatusActionSet() {
		synchronized (connectStatusActionSet) {
			return connectStatusActionSet;
		}
	}

	public void setAutoConnect(boolean autoConnect) {
		this.autoConnect = autoConnect;
	}

	public boolean isAutoConnect() {
		return this.autoConnect;
	}

	public void setAutoConnectCount(int autoConnectCount) {
		this.autoConnectCount = autoConnectCount;
	}

	public int getAutoConnectCount() {
		return autoConnectCount;
	}

	public void setConnectTimeOut(long connectTimeOut) {
		this.connectTimeOut = connectTimeOut;

	}

	public long getConnectTimeOut() {
		return connectTimeOut;
	}

	public void setAutoCloseConnectTime(long autoCloseConnectTime) {
		this.autoCloseConnectTime = autoCloseConnectTime;
		this.closeConnectStartTime = System.currentTimeMillis();
	}

	public boolean isConnected() {
		return null != connector && connector.isConnected();
	}

	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}

	protected void threadSleep(long time) {
		try {
			sleep(time);
		} catch (InterruptedException e) {
			logger.error(e);
		}
	}

	public boolean connect() {
		if (null != connector && null != connectData) {
			try {
				connector.connect(connectData);// 获取连接会话
				if (isConnected()) {
					connectCount = 0;
				} else {
					connectCount++;
				}
			} catch (Exception e) {
				connectCount++;
				logger.error("重连接" + connectCount + "次:");
			}

			if (isConnected()) {
				handleConnect(isConnected());// 处理连接成功执行的动作
			} else {
				if ((connectCount >= autoConnectCount)) {// 如果连接次数超过自动连接数，则不自动连接
					autoConnect = false;
					connectCount = 0;
					handleConnect(isConnected());// 处理连接失败的动作
				}
			}
		}
		return isConnected();
	}

	/**
	 * 下线
	 */
	public void closeConnect() {
		autoConnect = false;
		if (null != connector) {
			connector.closeConnect();
		}
	}

	public void run() {
		while (true) {
			handleConnect();// 处理连接
			handleConnectStatusChange();// 处理连接状态变化了的动作
			handleAutoOffline();// 处理自动下线
			threadSleep(sleepTime);
		}
	}

	/**
	 * 重连接
	 */
	protected void handleConnect() {
		if (!isConnected() && autoConnect) {
			connect();
			logger.debug("重连接" + connectCount + "次");
		}
	}

	/**
	 * 更新用户连接状态
	 */
	protected void handleConnectStatusChange() {
		if (isConnected() != connectedTemp) {
			connectedTemp = isConnected();
			handleConnectStatusChange(isConnected());
		}
	}

	protected void handleAutoOffline() {
		if ((autoCloseConnectTime > 0) && System.currentTimeMillis() - autoCloseConnectTime > autoCloseConnectTime) {
			autoCloseConnectTime = -1;
			closeConnect();
		}
	}

	private void handleConnect(boolean success) {
		ConnectBackAction connectAction;
		while (null != backQueue && backQueue.peek() != null) {
			connectAction = backQueue.poll();
			connectAction.connectBack(success);
		}
	}

	private void handleConnectStatusChange(boolean connected) {
		if(!getStatusActionSet().isEmpty()){
			List<ConnectStatusAction> list=new ArrayList<ConnectStatusAction>(getStatusActionSet());
			for (ConnectStatusAction connectStatusAction : list) {
				connectStatusAction.connectStatusChange(connected);
			}
		}
	}
}
