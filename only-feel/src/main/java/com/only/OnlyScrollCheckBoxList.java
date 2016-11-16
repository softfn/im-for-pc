package com.only;

import java.util.Vector;

import javax.swing.ListModel;

public class OnlyScrollCheckBoxList extends OnlyScrollList {
	private static final long serialVersionUID = -6886223334688958453L;

	public OnlyScrollCheckBoxList(OnlyCheckBoxList<?> list) {
		super(list);
	}

	public OnlyScrollCheckBoxList() {
		this(new OnlyCheckBoxList<Object>());
	}

	public OnlyScrollCheckBoxList(Vector<?> listData) {
		this(new OnlyCheckBoxList<Object>(listData));
	}

	@SuppressWarnings("rawtypes")
	public OnlyScrollCheckBoxList(ListModel dataModel) {
		this(new OnlyCheckBoxList(dataModel));
	}

	public OnlyCheckBoxList<?> getList() {
		return (OnlyCheckBoxList<?>) list;
	}
}
