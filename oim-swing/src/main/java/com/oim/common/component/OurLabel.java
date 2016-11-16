/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.oim.common.component;



import javax.swing.Icon;

import com.only.OnlyLabel;

/**
 * 2013-8-29 15:23:58
 * @author XiaHui
 */
@SuppressWarnings("serial")
public class OurLabel extends OnlyLabel{

	public OurLabel() {
		super();
	}

	public OurLabel(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
	}

	public OurLabel(Icon image) {
		super(image);
	}

	public OurLabel(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
	}

	public OurLabel(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
	}

	public OurLabel(String text) {
		super(text);
	}

}
