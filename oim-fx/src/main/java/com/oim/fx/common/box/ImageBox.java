/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.fx.common.box;

import com.oim.core.bean.User;
import com.oim.fx.common.util.ImageUtil;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

/**
 *
 * @author XiaHui
 */
public class ImageBox {

	private static final HashMap<String, Image> classPathImageMap = new HashMap<String, Image>();
	private static final HashMap<String, Image> imageMap = new HashMap<String, Image>();

	public static Image getImageClassPath(String classPath) {
		Image image = getImageClassPath(classPath, false);
		return image;
	}

	public static Image getImageClassPath(String classPath, boolean loadNew) {
		Image image = classPathImageMap.get(classPath);
		if (null == image || loadNew) {
			image = new Image(classPath, true);
		}
		return image;
	}

	public static Image getImagePath(String classPath) {
		Image image = getImagePath(classPath, false);
		return image;
	}

	public static Image getImagePath(String imagePath, boolean loadNew) {
		Image image = imageMap.get(imagePath);
		if (null == image || loadNew) {
			image = getImage(imagePath);
		}
		return image;
	}

	public static Image getImagePath(String imagePath, int w, int h) {
		String key = imagePath + "_" + w + "_" + h;
		Image image = imageMap.get(key);
		if (null == image) {
			WritableImage writableImage = new WritableImage(w, h);
			BufferedImage bufferedImage = ImageUtil.getBufferedImage(imagePath, w, h, 0, 0);
			SwingFXUtils.toFXImage(bufferedImage, writableImage);
			image = writableImage;
		}
		return image;
		// BufferedImage bufferedImage =
		// ImageUtil.getRoundedCornerBufferedImage("Resources/Images/Head/User/90_100.gif",
		// 80, 80, 8, 8);
		// SwingFXUtils.toFXImage(bufferedImage, image);
	}

	public static Image getImagePath(String imagePath, int w, int h, int cornersWidth, int cornerHeight) {
		String key = imagePath + "_" + w + "_" + h + "_" + cornersWidth + "_" + cornerHeight;
		Image image = imageMap.get(key);
		if (null == image) {
			WritableImage writableImage = new WritableImage(w, h);
			BufferedImage bufferedImage = ImageUtil.getBufferedImage(imagePath, w, h, cornersWidth, cornerHeight);
			SwingFXUtils.toFXImage(bufferedImage, writableImage);
			image = writableImage;
		}
		return image;
		// BufferedImage bufferedImage =
		// ImageUtil.getRoundedCornerBufferedImage("Resources/Images/Head/User/90_100.gif",
		// 80, 80, 8, 8);
		// SwingFXUtils.toFXImage(bufferedImage, image);
	}

	private static Image getImage(String imagePath) {
		Image image = null;
		try {
			String pathString = new File(imagePath).toURI().toURL().toString();
			image = new Image(pathString, true);
		} catch (MalformedURLException ex) {
			Logger.getLogger(ImageBox.class.getName()).log(Level.SEVERE, null, ex);
		}
		return image;
	}

	private static Map<String, Image> statusImageIconMap = new ConcurrentHashMap<String, Image>();

	public static Image getStatusImageIcon(String status) {
		Image image = statusImageIconMap.get(status);
		if (null == image) {
			switch (status) {
			case User.status_online:
				image = ImageBox.getImageClassPath("/resources/common/images/status/imonline.png");
				break;
			case User.status_call_me:
				image = ImageBox.getImageClassPath("/resources/common/images/status/Qme.png");
				break;
			case User.status_away:
				image = ImageBox.getImageClassPath("/resources/common/images/status/away.png");
				break;
			case User.status_busy:
				image = ImageBox.getImageClassPath("/resources/common/images/status/busy.png");
				break;
			case User.status_mute:
				image = ImageBox.getImageClassPath("/resources/common/images/status/mute.png");
				break;
			case User.status_invisible:
				image = ImageBox.getImageClassPath("/resources/common/images/status/invisible.png");
				break;
			case User.status_offline:
				image = ImageBox.getImageClassPath("/resources/common/images/status/imoffline.png");
				break;
			default:
				image = ImageBox.getImageClassPath("/resources/common/images/status/imoffline.png");
				break;
			}
			statusImageIconMap.put(status, image);
		}
		return image;
	}
}
