package com.oim.core.common.component;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.oim.core.bean.User;
import com.oim.core.common.box.PersonalBox;
import com.oim.core.common.util.ByteUtil;
import com.oim.core.net.message.data.AddressData;
import com.oim.core.net.thread.SocketDataHandler;
import com.oim.core.net.thread.SocketThread;

/**
 * @author: XiaHui
 * @date: 2016年10月14日 下午2:28:07
 */
public class VideoModule {

	private Map<String, AddressData> vaMap = new ConcurrentHashMap<String, AddressData>();
	private SocketThread videoSocketThread = new SocketThread();
	ImageThread imageThread = new ImageThread();
	CameraVideo cameraVideo = new CameraVideo();

	ByteDataHandler byteDataHandler;
	public VideoModule() {
		init();
	}

	public void start() {
		videoSocketThread.start();
		imageThread.start();
	}

	private void init() {

	}

	public void setByteDataHandler(ByteDataHandler byteDataHandler){
		this.byteDataHandler=byteDataHandler;
	}
	
	public void addHandler(SocketDataHandler socketDataHandler) {
		videoSocketThread.addSocketDataHandler(socketDataHandler);
	}

	public void connectServer(byte[] bytes, SocketAddress address) {
		videoSocketThread.send(bytes, address);
	}

	public void put(String key, AddressData addressData) {
		vaMap.put(key, addressData);
	}

	public void remove(String key) {
		vaMap.remove(key);
	}
	
	public boolean has(String key) {
		return vaMap.containsKey(key);
	}
	
	public void removeAll() {
		vaMap.clear();
	}
	
	public Set<String> keySet() {
		return vaMap.keySet();
	}
	
	public boolean isEmpty() {
		return vaMap.isEmpty();
	}

	class ImageThread extends Thread {

		public void run() {
			while (true) {
				createImage();
				threadSleep(30);
			}
		}

		private void threadSleep(long time) {
			try {
				sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		public void createImage() {
			if (vaMap.isEmpty()) {
				if (cameraVideo.isStart()) {
					cameraVideo.stopVideo();
				}
				threadSleep(1000);
			} else {
				if (!cameraVideo.isStart()) {
					cameraVideo.startVideo();
				}
				if (cameraVideo.isStart()) {
					try {
						
						byte[] data = cameraVideo.getVideoBytes();
						
						if(null!=byteDataHandler){
							byteDataHandler.byteData(data);
						}

						User user = PersonalBox.get(User.class);
						String userId = (null == user) ? "" : user.getId();

						byte[] keyByte = userId.getBytes();
						
						int size = keyByte.length;
						byte[] sizeBytes = ByteUtil.intToBytes(size);

						byte[] allData = ByteUtil.byteMerger(ByteUtil.byteMerger(sizeBytes, keyByte), data);
						List<AddressData> list = new ArrayList<AddressData>(vaMap.values());
						for (AddressData va : list) {
							SocketAddress socketAddress = new InetSocketAddress(va.getAddress(), va.getPort());
							videoSocketThread.send(allData, socketAddress);
						}
					} catch (Exception e) {
					}
				}
			}
		}
	}
}
