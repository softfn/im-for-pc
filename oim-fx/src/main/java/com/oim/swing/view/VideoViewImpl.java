package com.oim.swing.view;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.oim.common.event.ExecuteAction;
import com.oim.core.app.AppContext;
import com.oim.core.bean.User;
import com.oim.core.business.constant.VideoConstant;
import com.oim.core.business.sender.VideoSender;
import com.oim.core.common.annotation.Parameter;
import com.oim.core.common.app.view.AbstractView;
import com.oim.core.common.app.view.VideoView;
import com.oim.core.common.box.PersonalBox;
import com.oim.core.common.component.ByteDataHandler;
import com.oim.core.common.util.ByteUtil;
import com.oim.core.net.message.data.AddressData;
import com.oim.core.net.message.data.UserData;
import com.oim.core.net.server.Back;
import com.oim.core.net.thread.SocketData;
import com.oim.core.net.thread.SocketDataHandler;
import com.oim.ui.VideoFrame;
import com.oim.ui.video.VideoShowPanel;
import com.only.OnlyMessageBox;
import com.only.net.data.action.DataBackAction;
import com.only.net.data.action.DataBackActionAdapter;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2015年3月16日 下午10:42:19
 * @version 0.0.1
 */
public class VideoViewImpl extends AbstractView implements VideoView {

	VideoFrame videoFrame = new VideoFrame();
	private Map<String, VideoShowPanel> vspMap = new ConcurrentHashMap<String, VideoShowPanel>();
	private ExecuteAction shutExecuteAction;
	private ExecuteAction agreeExecuteAction;

	public VideoViewImpl(AppContext appContext) {
		super(appContext);
		initEvent();
		init();
	}

	public void setVisible(boolean visible) {
		videoFrame.setVisible(visible);
	}

	@Override
	public boolean isShowing() {
		return false;
	}

	private void init() {
		videoFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		appContext.getVideoModule().addHandler(new SocketDataHandler() {
			public void received(SocketData socketData) {
				setVideoData(socketData);
			}
		});
		appContext.getVideoModule().setByteDataHandler(new ByteDataHandler() {

			@Override
			public void byteData(byte[] bytes) {
				ByteArrayInputStream in = new ByteArrayInputStream(bytes);
				Image image;
				try {

					image = ImageIO.read(in);
					image = image.getScaledInstance(480, 320, Image.SCALE_SMOOTH);
					ImageIcon icon = new ImageIcon(image);
					videoFrame.setOwnImageIcon(icon);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	private void initEvent() {
		videoFrame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent evt) {
				formWindowClosing(evt);
			}
		});

		shutExecuteAction = new ExecuteAction() {

			@Override
			public <T, E> E execute(T value) {
				if (value instanceof VideoShowPanel) {
					UserData userData = ((VideoShowPanel) value).getAttribute(UserData.class);
					shut(userData.getId());
				}
				return null;
			}
		};
		agreeExecuteAction = new ExecuteAction() {

			@Override
			public <T, E> E execute(T value) {
				if (value instanceof VideoShowPanel) {
					UserData userData = ((VideoShowPanel) value).getAttribute(UserData.class);
					agree(userData);
				}
				return null;
			}
		};
	}

	private void formWindowClosing(java.awt.event.WindowEvent evt) {
		if (!appContext.getVideoModule().isEmpty()) {
			int button = OnlyMessageBox.createQuestionMessageBox(videoFrame, "关闭", "是否关闭所有视频通信？").open();
			if (OnlyMessageBox.YES_OPTION == button) {
				closing();
				videoFrame.removeAllTab();
				handerVideoFrame();
			}
		} else {
			closing();
			videoFrame.removeAllTab();
			handerVideoFrame();
		}
	}
	
	private void closing(){
		Set<String> keySet=appContext.getVideoModule().keySet();
		for(String key:keySet){
			shut(key);
		}
		clear();
	}
	
	private void clear(){
		appContext.getVideoModule().removeAll();
	}

	private void agree(UserData userData) {
		String userId = userData.getId();
		VideoShowPanel vsp = getVideoShowPanel(userData);
		vsp.showGetRequest(false);
		User user = PersonalBox.get(User.class);
		String sendId = (null == user) ? null : user.getId();
		VideoSender vh = this.appContext.getSender(VideoSender.class);
		vh.responseVideo(sendId, userId, VideoConstant.action_type_agree);

		DataBackAction dataBackAction = new DataBackActionAdapter() {
			@Back
			public void back(@Parameter("videoAddress") AddressData videoAddress) {
				setUserVideoAddress(userId, videoAddress);
			}
		};
		vh.getUserVideoAddress(userId, dataBackAction);
	}

	private void shut(String userId) {
		remove(userId);
		User user = PersonalBox.get(User.class);
		String sendId = (null == user) ? null : user.getId();
		VideoSender vh = this.appContext.getSender(VideoSender.class);
		vh.responseVideo(sendId, userId, VideoConstant.action_type_shut);
	}

	private void setUserVideoAddress(String userId, AddressData videoAddress) {
		appContext.getVideoModule().put(userId, videoAddress);
	}

	public void getAgree(String userId, AddressData videoAddress) {
		setUserVideoAddress(userId, videoAddress);
	}

	public void getShut(String userId) {
		remove(userId);
	}

	public void remove(String key) {
		appContext.getVideoModule().remove(key);
		VideoShowPanel vsp = vspMap.remove(key);
		if (null != vsp) {
			videoFrame.removeTab(vsp);
		}
		handerVideoFrame();
	}

	public void handerVideoFrame() {
		if (appContext.getVideoModule().isEmpty()) {
			videoFrame.stopVideo();
		}
		if (videoFrame.isTabEmpty()) {
			videoFrame.setVisible(false);
		}
	}

	public void showSendVideoFrame(UserData userData) {
		if (!appContext.getVideoModule().has(userData.getId())) {
			VideoShowPanel vsp = getVideoShowPanel(userData);
			vsp.showGetRequest(false);
			videoFrame.addTab(userData.getNickname(), vsp);
			videoFrame.setVisible(true);
		}
	}

	public void showGetVideoFrame(UserData userData) {
		if (!appContext.getVideoModule().has(userData.getId())) {
			VideoShowPanel vsp = getVideoShowPanel(userData);
			vsp.showGetRequest(true);
			videoFrame.addTab(userData.getNickname(), vsp);
			videoFrame.setVisible(true);
		}
	}

	private VideoShowPanel getVideoShowPanel(UserData userData) {
		VideoShowPanel vsp = vspMap.get(userData.getId());
		if (null == vsp) {
			vsp = new VideoShowPanel();
			vsp.addAttribute(UserData.class, userData);
			vsp.addShutExecuteAction(shutExecuteAction);
			vsp.addAgreeExecuteAction(agreeExecuteAction);
			vspMap.put(userData.getId(), vsp);
		}
		return vsp;
	}

	private void setVideoData(SocketData socketData) {

		byte[] bytes = socketData.getBytes();
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		byte[] sizeBytes = new byte[4];
		try {
			in.read(sizeBytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int size = ByteUtil.bytesToInt(sizeBytes);
		byte[] keyByte = new byte[size];
		try {
			in.read(keyByte);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String key = new String(keyByte);
		VideoShowPanel vsp = vspMap.get(key);
		if (null != vsp) {
			Image image;
			try {

				image = ImageIO.read(in);
				image = image.getScaledInstance(480, 320, Image.SCALE_SMOOTH);
				ImageIcon icon = new ImageIcon(image);
				vsp.setVideoIcon(icon);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
