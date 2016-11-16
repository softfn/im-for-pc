package com.oim.net.message.data.chat;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2016年1月24日 下午12:30:06
 * @version 0.0.1
 */
public class Item {

	private String type;
	private String value;
	private String extension;
	
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static final String type_text = "text";
	public static final String type_image = "image";
	public static final String type_face = "face";
	public static final String type_url = "url";
}
