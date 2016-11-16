/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.only.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

/**
 * 
 * @date 2013年12月28日 上午11:03:24
 * version 0.0.1
 */
public class TileLayout implements LayoutManager {

	/**
	 * 组件之间的水平间隙，即列距
	 */
	protected int hgap;
	/**
	 * 组件之间垂直间隙，即行距
	 */
	protected int vgap;
	/**
	 * 每行放置的组件数
	 */
	protected int column;
	protected int layModel;
	/**
	 * 每个组件占用的空间
	 */
	protected Dimension d;
	/**
	 * 对所有被排列的组件实行统一大小排列。
	 */
	public static final int INDENTIC_SIZE = 0;
	/**
	 * 对所有被排列的组件按自身大小排列。
	 */
	public static final int PREFER_SIZE = 1;

	/**
	 * 构造一个默认的TileLayout，默认按照每行4列的方式排列， 行距和列距都为20,组件大小按PREFER_SIZE排列。
	 */
	public TileLayout() {
		this(20, 20);
	}

	/**
	 * 构造一个指定列数的TileLayout，默认行距和列距都为20, 组件大小按PREFER_SIZE排列。
	 */
	public TileLayout(int column) {
		this(20, 20, column, INDENTIC_SIZE);
	}

	/**
	 * 构造一个指定行距和列距的TileLayout，默认按照每行4列 的方式排列,组件大小按PREFER_SIZE排列。
	 */
	public TileLayout(int hgap, int vgap) {
		this(hgap, vgap, 4, PREFER_SIZE);
	}

	/**
	 * 构造一个TileLayout
	 * 
	 * @param hgap 组件列距
	 * @param vgap 组件行距
	 * @param column 每行排列的组件数
	 * @param model 组件的Dimension显示模式
	 */
	public TileLayout(int hgap, int vgap, int column, int model) {
		this.hgap = hgap;
		this.vgap = vgap;
		setColumn(column);
		setModel(model);
	}

	/**
	 * 设定每行列数的组件数
	 */
	public void setColumn(int column) {
		this.column = column;
	}

	/**
	 * @param model 组件的Dimension显示模式
	 */
	public void setModel(int model) {
		layModel = model;
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
	 * 计算网格大小，即所有组件里最大的组件大小。
	 */
	private Dimension maxCompSize(Container target) {
		Dimension d = new Dimension(0, 0);
		synchronized (target.getTreeLock()) {
			int numbers = target.getComponentCount();
			for (int i = 0; i < numbers; i++) {
				Dimension di = target.getComponent(i).getPreferredSize();
				d = union(d, di);
			}
			return d;
		}
	}

	/**
	 * Method preferredLayoutSize 计算容器的显示大小。这个方法将在顶级容器窗口frame调用 pack()方法时调用。
	 * 
	 * @param parent 持有布局的容器
	 * @return 容器调用时显示的最合适的大小
	 * 
	 */
	@Override
	public Dimension preferredLayoutSize(Container parent) {
		// TODO: 在这添加你的代码
		synchronized (parent.getTreeLock()) {
			d = maxCompSize(parent);
			int num = parent.getComponentCount();
			//int r = (num - 1) / column + 1;
			int c = column;
			int w = c * d.width + (c + 1) * hgap;
			// int h = r * d.height + (r + 1) * vgap;
			int width = parent.getWidth();
			int nx = (width / (d.width + hgap));
			int ny = num / (0 == nx ? 1 : nx);
			int nyl = num % (0 == nx ? 1 : nx);
			if (nyl > 0) {
				ny++;
			}
			int h = ny * d.height + (ny + 1) * vgap;
			//			System.out.println(ny);
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
	 * @param parent parent 拥有该布局的容器。
	 */
	@Override
	public void layoutContainer(Container parent) {
		// TODO: 在这添加你的代码
		synchronized (parent.getTreeLock()) {
			d = maxCompSize(parent);
			int numbers = parent.getComponentCount();
			Dimension cd = parent.getSize();
			int c = cd.width / (d.width + hgap);
			if (c == 0) {
				c = 1;
			}
			for (int i = 0; i < numbers; i++) {
				Component comp = parent.getComponent(i);
				if (comp.isVisible()) {
					int x = i % c;
					int y = i / c;
					comp.setLocation(x * (hgap + d.width) + hgap, y * (vgap + d.height) + vgap);
					if (layModel == PREFER_SIZE) {
						comp.setSize(comp.getPreferredSize());
					} else if (layModel == INDENTIC_SIZE) {
						comp.setSize(d);
					}
				}
			}
		}
	}

	/**
	 * 计算两个区域联合大小，即取二者间最长的两个边组成一个区域。
	 */
	// private Dimension union(Dimension d1, Dimension d2) {
	// return new Dimension(d1.width > d2.width ? d1.width : d2.width, d1.height
	// > d2.height ? d1.height : d2.height);
	// }
	//
	private Dimension union(Dimension d1, Dimension d2) {
		return new Dimension(d1.width > d2.width ? d1.width : d2.width, d1.height > d2.height ? d1.height : d2.height);
	}
}
