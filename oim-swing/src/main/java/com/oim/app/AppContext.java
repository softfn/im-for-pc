package com.oim.app;

import java.util.UUID;

import org.apache.log4j.Logger;

import com.oim.bean.User;
import com.oim.business.handler.PersonalHandler;
import com.oim.common.app.controller.ControllerFactory;
import com.oim.common.app.handler.SendHandler;
import com.oim.common.app.handler.SendHandlerFactory;
import com.oim.common.app.manage.Manage;
import com.oim.common.app.manage.ManageFactory;
import com.oim.common.app.service.Service;
import com.oim.common.app.service.ServiceFactory;
import com.oim.common.app.view.AbstractView;
import com.oim.common.app.view.ViewFactory;
import com.oim.common.box.PersonalBox;
import com.oim.common.task.ExecuteTask;
import com.oim.common.task.QueueTaskThread;
import com.oim.net.connect.MessageHandler;
import com.oim.net.connect.SocketConnector;
import com.oim.net.message.Data;
import com.oim.net.message.data.LoginData;
import com.oim.net.server.Handler;
import com.oim.net.server.MessageReadHandler;
import com.oim.net.thread.HeadPulseThread;
import com.oim.ui.view.LoginView;
import com.oim.ui.view.MainView;
import com.only.net.NetServer;
import com.only.net.action.ConnectBackAction;
import com.only.net.action.ConnectStatusAction;
import com.only.net.connect.ConnectThread;
import com.only.net.data.action.DataBackAction;
import com.only.net.thread.DataReadThread;
import com.only.net.thread.DataWriteThread;

/**
 * 这是程序的上下文，方便各个模块之间调用
 * 
 * @author XiaHui
 * @date 2015年3月6日 上午9:30:30
 */
public class AppContext {
	protected final Logger logger = Logger.getLogger(this.getClass().getName());

	private Handler handler;// 负责处理TCP接受到的消息
	private SocketConnector connector;// 连接实体
	private NetServer netServer;
	private ControllerFactory controllerFactory;
	private ViewFactory viewFactory = new ViewFactory(this);
	private ServiceFactory serviceFactory = new ServiceFactory(this);
	private ManageFactory manageFactory = new ManageFactory(this);
	private SendHandlerFactory sendHandlerFactory = new SendHandlerFactory(this);
	private HeadPulseThread headPulseThread = new HeadPulseThread();// 处理头像跳动的线程
	private QueueTaskThread queueTaskThread = new QueueTaskThread();// 用于执行一些耗时的线程任务
	private boolean login = false;// 用来标识是否已经成功登录了

	public AppContext() {
		long time = System.currentTimeMillis();
		initApp();
		System.out.println("initApp" + (System.currentTimeMillis() - time));
		initAction();
		System.out.println("initAction" + (System.currentTimeMillis() - time));
	}

	/**
	 * 初始化各个模块
	 */
	private void initApp() {

		controllerFactory = new ControllerFactory(this);
		handler = new Handler();
		connector = new SocketConnector(handler);

		netServer = new NetServer(connector);

		handler.addMessageHandler(new MessageHandler() {

			@Override
			public void setBackTime(long backTime) {
				netServer.getDataWriteThread().setBackTime(backTime);
			}

			@Override
			public void back(Object data) {
				netServer.getDataReadThread().back(data);
			}

			@Override
			public void addExceptionData(String key) {
				netServer.getDataReadThread().addExceptionData(key);
			}
		});
		MessageReadHandler messageReadHandler=new MessageReadHandler();
		messageReadHandler.setControllerFactory(controllerFactory);
		netServer.getDataReadThread().addReadHandler(messageReadHandler);
	}

	/**
	 * 初始化各个模块的
	 */
	private void initAction() {
		netServer.addConnectBackAction(new ConnectBackAction() {

			@Override
			public void connectBack(boolean success) {
				if (logger.isDebugEnabled()) {
					String message = success ? "连接成功。" : "连接失败！";
					logger.debug(message);
				}
			}
		});
		/*** 为处理连接的线程添加连接状态变化的action，当连接断开了或者连接成功触发 ***/
		netServer.addConnectStatusAction(new ConnectStatusAction() {

			@Override
			public void connectStatusChange(boolean isConnected) {
				if (isConnected && login) {// 如果是已经登录了，然后断网了，恢复网络后再次成功连接，那么进行自动重连接
					PersonalHandler ph = getHandler(PersonalHandler.class);
					ph.reconnect();
				}
				if(login){
					LoginData loginData = PersonalBox.get(LoginData.class);
					String status=isConnected?loginData.getStatus():User.status_offline;
					MainView mainView = getSingleView(MainView.class);
					mainView.updateStatus(status);
				}
				if (logger.isDebugEnabled()) {
					logger.debug("状态：" + isConnected);
				}
			}
		});
	}

	/**
	 * 启动各个线程
	 */
	public void start() {
		netServer.start();
		headPulseThread.start();
	}

	public ConnectThread getConnectThread() {
		return netServer.getConnectThread();
	}

	public DataReadThread getDataReadThread() {
		return netServer.getDataReadThread();
	}

	public DataWriteThread getDataWriteThread() {
		return netServer.getDataWriteThread();
	}

	public HeadPulseThread getHeadPulseThread() {
		return headPulseThread;
	}

	public boolean isLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}

	public <T> T getSingleView(Class<? extends AbstractView> classType) {
		return viewFactory.getSingleView(classType);
	}

	public <T> T getService(Class<? extends Service> classType) {
		return serviceFactory.getService(classType);
	}

	public <T> T getManage(Class<? extends Manage> classType) {
		return manageFactory.getManage(classType);
	}

	public <T> T getHandler(Class<? extends SendHandler> classType) {
		return sendHandlerFactory.getHandler(classType);
	}

	/**
	 * 发送信息
	 * 
	 * @param data
	 *            :信息
	 */
	public void write(Data data) {
		write(data, null);
	}

	/**
	 * 发送信息
	 * 
	 * @param data
	 *            :信息
	 * @param dataBackAction
	 *            :回调Action
	 */
	public void write(Data data, DataBackAction dataBackAction) {
		if (data.getHead() != null) {
			if (data.getHead().getKey() == null) {
				data.getHead().setKey(getKey());
			}
			String key = data.getHead().getKey();
			netServer.write(key, data, dataBackAction);
		}
	}

	public void exit() {
		netServer.closeConnect();
		System.exit(0);
	}

	/**
	 * 执行任务
	 * 
	 * @param executeTask
	 */
	public void add(ExecuteTask executeTask) {
		queueTaskThread.add(executeTask);
	}

	private String getKey() {
		return UUID.randomUUID().toString();
	}

	public static void main(String[] age) {
		AppContext appContext = new AppContext();
		LoginView loginView = appContext.getSingleView(LoginView.class);
		System.out.println(loginView);
	}
}
