package com.oim.swing;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.oim.fx.ui.BaseFrame;
import com.only.OnlyBorderDialog;
import com.only.OnlyBorderFrame;

import javafx.application.Platform;

/**
 * @author XiaHui
 * @date 2015年3月4日 下午3:36:30
 */
public class UIBox {

	private static Map<Object, Object> objectMap = new ConcurrentHashMap<Object, Object>();

	public static Set<BaseFrame> baseFrameSet = Collections.synchronizedSet(new HashSet<BaseFrame>());
	public static Set<OnlyBorderFrame> frameSet = Collections.synchronizedSet(new HashSet<OnlyBorderFrame>());
	public static Set<OnlyBorderDialog> dialogSet = Collections.synchronizedSet(new HashSet<OnlyBorderDialog>());
	

	public static void put(Object key, Object value) {
		objectMap.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(Object key) {
		return (T) objectMap.get(key);
	}

	/**
	 * 把系统的界面都放入map中，方便管理，如可以修改主题等
	 * 
	 * @param onlyBorderFrame
	 */
	public static void add(BaseFrame baseFrame) {
		baseFrameSet.add(baseFrame);
		String imagePath = UIBox.get("key_window_background_image_path");
		if (null != imagePath&&!"".equals(imagePath)) {
			baseFrame.setBackground(imagePath);
		}
	}
	
	public static void add(OnlyBorderFrame onlyBorderFrame) {
		frameSet.add(onlyBorderFrame);
		BufferedImage bi = UIBox.get("key_window_background_image");
		if (null != bi) {
			onlyBorderFrame.setBackgroundImage(bi);
		}
	}

	public static void add(OnlyBorderDialog onlyBorderDialog) {
		dialogSet.add(onlyBorderDialog);
		BufferedImage bi = UIBox.get("key_window_background_image");
		if (null != bi) {
			onlyBorderDialog.setBackgroundImage(bi);
		}
	}
	
	public static void update(){
		BufferedImage bi = UIBox.get("key_window_background_image");
		if (null != bi) {
			for (OnlyBorderFrame ourFrame : UIBox.frameSet) {
				ourFrame.setBackgroundImage(bi);
			}
			for (OnlyBorderDialog ourFrame : UIBox.dialogSet) {
				ourFrame.setBackgroundImage(bi);
			}
		}
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				String imagePath = UIBox.get("key_window_background_image_path");
				
				
				if (null != imagePath&&!"".equals(imagePath)) {
					for (BaseFrame baseFrame : UIBox.baseFrameSet) {
						baseFrame.setBackground(imagePath);
					}
				}
			}
		});
	}
}
