package com.only.net;

import org.apache.log4j.Logger;

import com.only.net.action.ConnectBackAction;
import com.only.net.action.ConnectStatusAction;
import com.only.net.connect.ConnectThread;
import com.only.net.connect.Connector;
import com.only.net.connect.WriteHandler;
import com.only.net.data.action.DataBackAction;
import com.only.net.data.action.DataSentAction;
import com.only.net.data.bean.HandlerData;
import com.only.net.thread.DataReadThread;
import com.only.net.thread.DataWriteThread;

/**
 * 网络处理的服务
 * 
 * @Author: XiaHui
 * @Date: 2016年1月4日
 * @ModifyUser: XiaHui
 * @ModifyDate: 2016年1月4日
 */
public class NetServer {
	protected final Logger logger = Logger.getLogger(this.getClass().getName());

	Connector connector;
	ConnectThread connectThread;
	DataReadThread dataReadThread;
	DataWriteThread dataWriteThread;

	public NetServer(Connector connector) {
		this.connector = connector;
		initServer();
		initAction();
	}

	private void initServer() {
		dataWriteThread = new DataWriteThread();// 数据发送线程
		dataReadThread = new DataReadThread();//
		connectThread = new ConnectThread();
		connectThread.setConnector(connector);
	}

	private void initAction() {

		connectThread.addConnectBackAction(new ConnectBackAction() {

			@Override
			public void connectBack(boolean success) {
			}
		});
		/*** 为处理连接的线程添加连接状态变化的action，当连接断开了或者连接成功触发 ***/
		connectThread.addConnectStatusAction(new ConnectStatusAction() {

			@Override
			public void connectStatusChange(boolean isConnected) {
				dataReadThread.setLost(!isConnected);// 当连接断开了，将
														// 已发送信息标记为失败，让处理读信息的线程处理
			}
		});
		dataWriteThread.addDataSentAction(new DataSentAction() {

			@Override
			public void execute(HandlerData handlerData) {
				dataReadThread.add(handlerData);// 如果发送的消息有回掉处理，那么将其添加到回掉处理线程的队列
				if (logger.isDebugEnabled()) {
					logger.debug("发送消息：" + handlerData.getKey());
				}
			}
		});

		dataWriteThread.addWriteHandler(new WriteHandler() {

			@Override
			public void write(HandlerData data) {
				if (connector.isConnected()) {
					connector.write(data.getData());
				} else {
					dataReadThread.addExceptionData(data.getKey());
				}
			}
		});
	}

	public void start() {
		connectThread.start();
		dataWriteThread.start();
		dataReadThread.start();
	}

	public ConnectThread getConnectThread() {
		return connectThread;
	}

	public DataReadThread getDataReadThread() {
		return dataReadThread;
	}

	public DataWriteThread getDataWriteThread() {
		return dataWriteThread;
	}

	/**
	 * 发送信息
	 * 
	 * @param data
	 *            :信息
	 */
	public void write(String key, Object data) {
		this.write(key, data, null);
	}

	/**
	 * 发送信息
	 * 
	 * @param data
	 *            :信息
	 * @param dataBackAction
	 *            :回调Action
	 */
	public void write(String key, Object data, DataBackAction dataBackAction) {
		dataWriteThread.write(key, data, dataBackAction);
	}

	public void addConnectBackAction(ConnectBackAction connectBackAction) {
		connectThread.addConnectBackAction(connectBackAction);
	}

	public void addConnectStatusAction(ConnectStatusAction connectStatusAction) {
		connectThread.addConnectStatusAction(connectStatusAction);
	}

	public void closeConnect() {
		connectThread.closeConnect();
	}
}
