package com.oim.common.config.data;
/**
 * 保存系统背景图片和模糊度的信息
 * @author XiaHui
 * @date 2016年2月10日 下午11:52:00
 * @version 0.0.1
 */
public class Theme {

	public static final String config_file_path = "Resources/Config/Application/Theme.xml";
	protected String windowBackgroundImage = "Resources/Images/Wallpaper/004.jpg";
	protected float gaussian = 20f;

	public String getWindowBackgroundImage() {
		return windowBackgroundImage;
	}

	public void setWindowBackgroundImage(String windowBackgroundImage) {
		this.windowBackgroundImage = windowBackgroundImage;
	}

	public float getGaussian() {
		return gaussian;
	}

	public void setGaussian(float gaussian) {
		this.gaussian = gaussian;
	}

}
