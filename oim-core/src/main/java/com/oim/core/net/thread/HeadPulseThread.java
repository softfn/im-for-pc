package com.oim.core.net.thread;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author XiaHui
 * @date 2015年3月13日 上午9:04:43
 */
public class HeadPulseThread extends Thread {

	private Map<String, Component> headMap = new ConcurrentHashMap<String, Component>();

	@Override
	public void run() {
		while (true) {
			if (headMap.isEmpty()) {
				sleep();
			} else {
				pulse(headMap);
			}
		}
	}

	private void sleep() {
		try {
			sleep(1000);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	private void pulse(Map<String, Component> map) {
		try {
			List<Component> list = new ArrayList<Component>();
			list.addAll(map.values());
			pulse(list, -1, +1);
			sleep(160);
			pulse(list, +1, -1);
			sleep(260);
			pulse(list, +1, +1);
			sleep(160);
			pulse(list, -1, -1);
			sleep(260);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	public void pulse(List<Component> List, int x, int y) {
		for (Component component : List) {
			pulse(component, x, y);
		}
	}

	public void pulse(Component component, int x, int y) {
		if (null != component) {
			component.setBounds(component.getX() + x, component.getY() + y, component.getWidth(), component.getHeight());
		}
	}

	public synchronized void putHead(String key, Component head) {
		headMap.put(key, head);
	}

	public synchronized Component removeHead(String key) {
		return headMap.remove(key);
	}

	public synchronized Component getHead(String key) {
		return headMap.get(key);
	}

	public boolean has(String key) {
		return headMap.containsKey(key);
	}
}
