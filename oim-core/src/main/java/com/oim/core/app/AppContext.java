package com.oim.core.app;

import java.util.UUID;

import org.apache.log4j.Logger;

import com.oim.core.bean.User;
import com.oim.core.common.action.CallAction;
import com.oim.core.common.app.action.ActionFactory;
import com.oim.core.common.app.controller.BaseController;
import com.oim.core.common.app.controller.ControllerFactory;
import com.oim.core.common.app.manage.Manage;
import com.oim.core.common.app.manage.ManageFactory;
import com.oim.core.common.app.sender.Sender;
import com.oim.core.common.app.sender.SenderFactory;
import com.oim.core.common.app.service.Service;
import com.oim.core.common.app.service.ServiceFactory;
import com.oim.core.common.app.view.AbstractView;
import com.oim.core.common.app.view.MainView;
import com.oim.core.common.app.view.View;
import com.oim.core.common.app.view.ViewFactory;
import com.oim.core.common.box.PersonalBox;
import com.oim.core.common.component.VideoModule;
import com.oim.core.common.task.ExecuteTask;
import com.oim.core.common.task.QueueTaskThread;
import com.oim.core.net.connect.MessageHandler;
import com.oim.core.net.connect.SocketConnector;
import com.oim.core.net.message.Data;
import com.oim.core.net.message.data.LoginData;
import com.oim.core.net.server.Handler;
import com.oim.core.net.server.MessageReadHandler;
import com.oim.core.net.thread.HeadPulseThread;
import com.oim.core.net.thread.SocketDataHandler;
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
	private final ActionFactory actionFactory = new ActionFactory(this);
	private final ViewFactory viewFactory = new ViewFactory(this);
	private final ControllerFactory controllerFactory = new ControllerFactory(this);
	private final ServiceFactory serviceFactory = new ServiceFactory(this);
	private final ManageFactory manageFactory = new ManageFactory(this);
	private final SenderFactory senderFactory = new SenderFactory(this);
	private final HeadPulseThread headPulseThread = new HeadPulseThread();// 处理头像跳动的线程
	private final QueueTaskThread queueTaskThread = new QueueTaskThread();// 用于执行一些耗时的线程任务
	private VideoModule videoModule=new VideoModule();
	private boolean login = false;// 用来标识是否已经成功登录了
	private boolean viewReady = false;
	CallAction exitAction;

	public AppContext() {
		long time = System.currentTimeMillis();
		initApp();
		if (logger.isDebugEnabled()) {
			logger.debug("initApp" + (System.currentTimeMillis() - time));
		}
		initAction();
		if (logger.isDebugEnabled()) {
			logger.debug("initApp" + (System.currentTimeMillis() - time));
		}
	}

	/**
	 * 初始化各个模块
	 */
	private void initApp() {

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
		MessageReadHandler messageReadHandler = new MessageReadHandler();
		messageReadHandler.setControllerFactory(actionFactory);
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
		/**
		 * * 为处理连接的线程添加连接状态变化的action，当连接断开了或者连接成功触发 **
		 */
		netServer.addConnectStatusAction(new ConnectStatusAction() {

			@Override
			public void connectStatusChange(boolean isConnected) {
				if (isConnected && login) {// 如果是已经登录了，然后断网了，恢复网络后再次成功连接，那么进行自动重连接

				}
				if (login) {
					LoginData loginData = PersonalBox.get(LoginData.class);
					String status = isConnected ? loginData.getStatus() : User.status_offline;
					MainView mainView = getSingleView(MainView.class);
					mainView.setStatus(status);
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
		videoModule.start();
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

	public boolean isViewReady() {
		return viewReady;
	}

	public void setViewReady(boolean viewReady) {
		this.viewReady = viewReady;
	}

	public <T> T getSingleView(Class<? extends View> classType) {
		return viewFactory.getSingleView(classType);
	}

	public <T> T getService(Class<? extends Service> classType) {
		return serviceFactory.getService(classType);
	}

	public <T> T getManage(Class<? extends Manage> classType) {
		return manageFactory.getManage(classType);
	}

	public <T> T getSender(Class<? extends Sender> classType) {
		return senderFactory.getSender(classType);
	}

	public <T> T getController(Class<? extends BaseController> classType) {
		return controllerFactory.getController(classType);
	}

	public void register(Class<? extends View> classType, Class<? extends AbstractView> view) {
		viewFactory.register(classType, view);
	}

	public void addVideoHandler(SocketDataHandler socketDataHandler) {
		videoModule.addHandler(socketDataHandler);
	}

	public VideoModule getVideoModule(){
		return videoModule;
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
		if (null != exitAction) {
			exitAction.execute();
		} else {
			System.exit(0);
		}
	}

	public void setExitAction(CallAction exitAction) {
		this.exitAction = exitAction;
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
}
