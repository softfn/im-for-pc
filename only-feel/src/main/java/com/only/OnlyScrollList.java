package com.only;

import java.awt.Color;
import java.util.Vector;

import javax.swing.ListModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.only.box.UIBox;

public class OnlyScrollList extends OnlyScrollPane {
	
	private static final long serialVersionUID = -1708752774205915517L;

	private static final Color DISABLED_BG = UIBox.getColor(UIBox.key_color_scroll_list_text_disabled_background);
	private Border border;
	private Border disabledBorder;
	private Color background;
	protected OnlyList<?> list;

	public OnlyScrollList(OnlyList<?> list) {
		super();
		setViewportView(this.list = list);
		init();
	}

	public OnlyScrollList() {
		this(new OnlyList<Object>());
	}


	public OnlyScrollList(Vector<?> listData) {
		this(new OnlyList<Object>(listData));
	}

	@SuppressWarnings("rawtypes")
	public OnlyScrollList(ListModel dataModel) {
		this(new OnlyList(dataModel));
	}

	private void init() {
		setBorder(new LineBorder(new Color(84, 165, 213)));
		setDisabledBorder(new LineBorder(new Color(84, 165, 213, 128)));
		setBackground(UIBox.getWhiteColor());
		setHeaderVisible(false);
		list.setBorder(new EmptyBorder(0, 0, 0, 0));
		list.setDisabledBorder(list.getBorder());
		list.setVisibleInsets(0, 0, 0, 0);
		list.setAlpha(0.0f);
	}

	public Border getDisabledBorder() {
		return disabledBorder;
	}

	public void setDisabledBorder(Border disabledBorder) {
		this.disabledBorder = disabledBorder;

		if (!this.isEnabled()) {
			super.setBorder(disabledBorder);
		}
	}

	public void setBorder(Border border) {
		this.border = border;
		super.setBorder(border);
	}

	public Color getDisabledForeground() {
		return list.getDisabledForeground();
	}

	public void setDisabledForeground(Color disabledForeground) {
		list.setDisabledForeground(disabledForeground);
	}

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		super.setBorder(enabled ? border : disabledBorder);
		super.setBackground(enabled ? background : DISABLED_BG);
	}

	public void setBackground(Color background) {
		this.background = background;
		super.setBackground(background);
	}

	public OnlyList<?> getList() {
		return list;
	}
}