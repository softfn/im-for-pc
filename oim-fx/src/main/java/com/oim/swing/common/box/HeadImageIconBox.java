package com.oim.swing.common.box;

import java.util.HashMap;

import javax.swing.ImageIcon;

import com.oim.swing.common.util.ImageUtil;


/**
 * 管理头像图片的盒子，为了减少io操作，将头像存入map中（也不知道有没有达到这样的效果）
 * @Author: XiaHui
 * @Date: 2016年1月23日
 * @ModifyUser: XiaHui
 * @ModifyDate: 2016年1月23日
 */
public class HeadImageIconBox {
	static String user_head_path = "Resources/Images/Head/User/";
	static String group_head_path = "Resources/Images/Head/Group/";

	private static HashMap<String, ImageIcon> imageIconMap = new HashMap<String, ImageIcon>();

	public static ImageIcon getUserHeadImageIcon100(String head) {
		return getUserHeadImageIcon100(head, false);
	}

	public static ImageIcon getUserHeadImageIcon60(String head) {
		return getUserHeadImageIcon60(head, false);
	}

	public static ImageIcon getUserHeadImageIcon40(String head) {
		return getUserHeadImageIcon40(head, false);
	}

	public static ImageIcon getUserHeadImageIcon(String head, int size) {
		return getUserHeadImageIcon(head, size, false);
	}

	public static ImageIcon getUserHeadImageIcon(String head, int size, int corners) {
		return getUserHeadImageIcon(head, size, corners, false);
	}

	public static ImageIcon getUserHeadImageIcon100(String head, boolean createNew) {
		String path = user_head_path + head + "_100.gif";
		String key = path;
		ImageIcon imageIcon = imageIconMap.get(key);
		if (null == imageIcon || createNew) {
			imageIcon = new ImageIcon(path);
			imageIconMap.put(key, imageIcon);
		}
		return imageIcon;
	}

	public static ImageIcon getUserHeadImageIcon60(String head, boolean createNew) {
		String path = user_head_path + head + "_100.gif";
		String key = path + "_60";
		ImageIcon imageIcon = imageIconMap.get(key);
		if (null == imageIcon || createNew) {
			imageIcon = ImageUtil.getImageIcon(path, 60, 60);
			imageIconMap.put(key, imageIcon);
		}
		return imageIcon;
	}

	public static ImageIcon getUserHeadImageIcon40(String head, boolean createNew) {
		String path = user_head_path + head + ".png";
		String key = path;
		ImageIcon imageIcon = imageIconMap.get(key);
		if (null == imageIcon || createNew) {
			imageIcon = new ImageIcon(path);
			imageIconMap.put(key, imageIcon);
		}
		return imageIcon;
	}

	public static ImageIcon getUserHeadImageIcon(String head, int size, boolean createNew) {
		String path = user_head_path + head + ((size > 40) ? "_100.gif" : ".png");
		String key = path + "_" + size;
		ImageIcon imageIcon = imageIconMap.get(key);
		if (null == imageIcon || createNew) {
			imageIcon = ImageUtil.getImageIcon(path, size, size);
			imageIconMap.put(key, imageIcon);
		}
		return imageIcon;
	}

	public static ImageIcon getUserHeadImageIcon(String head, int size, int corners, boolean createNew) {
		String path = user_head_path + head + ((size > 40) ? "_100.gif" : ".png");
		String key = path + "_" + size + "_" + corners;
		ImageIcon imageIcon = imageIconMap.get(key);
		if (null == imageIcon || createNew) {
			imageIcon = ImageUtil.getRoundedCornerImageIcon(path, size, size, corners, corners);
			imageIconMap.put(key, imageIcon);
		}
		return imageIcon;
	}

	/////////////////////////////////////////////////////
	public static ImageIcon getGroupHeadImageIcon40(String head) {
		return getGroupHeadImageIcon40(head, false);
	}
	public static ImageIcon getGroupHeadImageIcon(String head, int size) {
		return getGroupHeadImageIcon(head, size, false);
	}
	
	public static ImageIcon getGroupHeadImageIcon(String head, int size, int corners) {
		return getGroupHeadImageIcon(head, size, corners, false);
	}

	public static ImageIcon getGroupHeadImageIcon40(String head, boolean createNew) {
		String path = group_head_path + head + ".png";
		String key = path;
		ImageIcon imageIcon = imageIconMap.get(key);
		if (null == imageIcon || createNew) {
			imageIcon = new ImageIcon(path);
			imageIconMap.put(key, imageIcon);
		}
		return imageIcon;
	}

	public static ImageIcon getGroupHeadImageIcon(String head, int size, boolean createNew) {
		String path = group_head_path + head + ".png";
		String key = path + "_" + size;
		ImageIcon imageIcon = imageIconMap.get(key);
		if (null == imageIcon || createNew) {
			imageIcon = ImageUtil.getImageIcon(path, size, size);
			imageIconMap.put(key, imageIcon);
		}
		return imageIcon;
	}

	public static ImageIcon getGroupHeadImageIcon(String head, int size, int corners, boolean createNew) {
		String path = group_head_path + head + (".png");
		String key = path + "_" + size + "_" + corners;
		ImageIcon imageIcon = imageIconMap.get(key);
		if (null == imageIcon || createNew) {
			imageIcon = ImageUtil.getRoundedCornerImageIcon(path, size, size, corners, corners);
			imageIconMap.put(key, imageIcon);
		}
		return imageIcon;
	}
}
