/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.only.layout;

import java.awt.AWTError;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

/**
 * 
 * @date 2013年12月28日 上午11:03:24 version 0.0.1
 */
public class ListLayout implements LayoutManager {
	public static final int X_AXIS = 0;
	public static final int Y_AXIS = 1;
	/**
	 * 对所有被排列的组件实行统一大小排列。
	 */
	public static final int INDENTIC_SIZE = 0;
	/**
	 * 对所有被排列的组件按自身大小排列。
	 */
	public static final int PREFER_SIZE = 1;

	private int gap = 0;
	private int size = 0;
	private int axis;
	protected int layoutModel;

	public ListLayout(int axis, int size, int gap) {
		if (axis != X_AXIS && axis != Y_AXIS) {
			throw new AWTError("Invalid axis");
		}
		this.axis = axis;

		if (gap > 0) {
			this.gap = gap;
		}
		if (size > 0) {
			this.size = size;
		}
	}

	public ListLayout(int axis, int size, int gap, int layoutModel) {
		if (axis != X_AXIS && axis != Y_AXIS) {
			throw new AWTError("Invalid axis");
		}

		if (layoutModel != PREFER_SIZE && layoutModel != INDENTIC_SIZE) {
			throw new AWTError("Invalid layoutModel");
		}
		this.axis = axis;
		this.layoutModel = layoutModel;

		if (gap > 0) {
			this.gap = gap;
		}
		if (size > 0) {
			this.size = size;
		}
	}

	/**
	 * Method addLayoutComponent 接口强制实现的方法，这里不做处理
	 * 
	 * @param name
	 * @param comp
	 */
	@Override
	public void addLayoutComponent(String name, Component comp) {
		// TODO: 在这添加你的代码
	}

	/**
	 * Method removeLayoutComponent 接口强制实现的方法，这里不做处理
	 * 
	 * @param comp
	 */
	@Override
	public void removeLayoutComponent(Component comp) {
		// TODO: 在这添加你的代码
	}

	/**
	 * Method preferredLayoutSize 计算容器的显示大小。这个方法将在顶级容器窗口frame调用 pack()方法时调用。
	 * 
	 * @param parent
	 *            持有布局的容器
	 * @return 容器调用时显示的最合适的大小
	 * 
	 */
	@Override
	public Dimension preferredLayoutSize(Container target) {
		// TODO: 在这添加你的代码
		synchronized (target.getTreeLock()) {

			int count = target.getComponentCount();

			int w = 0;
			int h = 0;

			int temp = 0;
			for (int i = 0; i < count; i++) {
				Component component = target.getComponent(i);
				if (component.isVisible()) {
					if (layoutModel == PREFER_SIZE) {
						if (X_AXIS == axis) {
							temp = temp + gap + component.getPreferredSize().width;
						} else {
							temp = temp + gap + component.getPreferredSize().height;
						}
					} else {
						temp = temp + gap + size;
					}
				}
			}

			if (X_AXIS == axis) {
				w = temp - gap;
			} else {
				h = temp - gap;
			}
			return new Dimension(w, h);
		}
	}

	/**
	 * Method minimumLayoutSize 接口强制实现的方法，这里不做处理
	 * 
	 * @param parent
	 * @return
	 */
	@Override
	public Dimension minimumLayoutSize(Container parent) {

		return new Dimension(0, 0);

	}

	/**
	 * Method layoutContainer 排列组件，将组件按网格对齐排列到持有组件的容器中。
	 * 这个方法来自接口LayoutManager,当容器是增加或移除 组件或改变窗口大小时，将会调用这个方法计算每个组件 的排列位置和大小。
	 * 
	 * @param parent
	 *            parent 拥有该布局的容器。
	 */
	@Override
	public void layoutContainer(Container parent) {
		synchronized (parent.getTreeLock()) {
			int numbers = parent.getComponentCount();
			int temp = 0;
			for (int i = 0; i < numbers; i++) {
				Component component = parent.getComponent(i);
				if (component.isVisible()) {
					int x = 0;
					int y = 0;
					int width = 0;
					int height = 0;

					if (layoutModel == PREFER_SIZE) {
						if (X_AXIS == axis) {
							x = temp;
							y = 0;
							width = component.getPreferredSize().width;
							height = parent.getHeight();
						} else {
							x = 0;
							y = temp;
							width = parent.getWidth();
							height = component.getPreferredSize().height;
						}

					} else {
						if (X_AXIS == axis) {
							x = temp;
							y = 0;
							width = size;
							height = parent.getHeight();
						} else {
							x = 0;
							y = temp;
							width = parent.getWidth();
							height = size;
						}
					}
					component.setBounds(x, y, width, height);
					if (layoutModel == PREFER_SIZE) {
						if (X_AXIS == axis) {
							temp = temp + gap + component.getPreferredSize().width;
						} else {
							temp = temp + gap + component.getPreferredSize().height;
						}
					} else {
						temp = temp + gap + size;
					}
				}
			}
		}
	}
}
