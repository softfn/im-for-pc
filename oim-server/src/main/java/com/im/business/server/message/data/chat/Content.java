package com.im.business.server.message.data.chat;

import java.util.List;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2015年4月16日 下午8:09:32
 * @version 0.0.1
 */
public class Content {

	private Font font;
	private List<Section> sections;
	private long time;

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public List<Section> getSections() {
		return sections;
	}

	public void setSections(List<Section> sections) {
		this.sections = sections;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
