package com.oim.ui.main;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JPanel;

import com.oim.common.component.AlphaPanel;
import com.only.OnlyPanel;

/**
 * @author XiaHui
 * @date 2015年2月28日 下午2:53:44
 */
public class TabPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private AlphaPanel tabPanel = new AlphaPanel();
	private OnlyPanel panel = new OnlyPanel();
	private Map<Tab, Component> map = new HashMap<Tab, Component>();
	private Tab tab;

	// private Set<Component> set = new HashSet<Component>();

	public TabPanel() {
		initComponent();
		initEvent();
	}

	private void initComponent() {
		this.setOpaque(false);
		//this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.setLayout(new java.awt.BorderLayout());

		
		//tabPanel.setOpaque(false);
		tabPanel.setLayout(new BoxLayout(tabPanel, BoxLayout.LINE_AXIS));
		//tabPanel.setLayout(new ListLayout(ListLayout.X_AXIS, 30, 2,ListLayout.INDENTIC_SIZE));
		tabPanel.setBackground(new Color(40, 63, 85));
		//tabPanel.setOpaque(false);
		//tabPanel.setBackgroundColorAlpha(180);
		
		panel.setOpaque(false);
		panel.setLayout(new CardLayout());

//		add(tabPanel);
//		add(panel);
		
		add(tabPanel, java.awt.BorderLayout.PAGE_START);
		add(panel, java.awt.BorderLayout.CENTER);
		
	}

	private void initEvent() {

	}

	public void add(Icon normalIcon, Icon rolloverIcon, Icon selectedIcon, Component component) {

		Tab tabTemp = new Tab(normalIcon, rolloverIcon, selectedIcon);
		if (panel.getComponentCount() > 0) {
			component.setVisible(false);
		} else {
			tabTemp.setSelected(true);
			tab = tabTemp;
		}
		tabPanel.add(tabTemp);
		panel.add(component);
		map.put(tabTemp, component);
		tabTemp.setComponent(component);
		tabTemp.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				tabMouseClicked(evt);
			}
		});
	}

	private void tabMouseClicked(java.awt.event.MouseEvent evt) {
		if (evt.getSource() instanceof Tab) {
			Tab tabTemp = (Tab) evt.getSource();
			if (!tabTemp.equals(tab)) {
				if (null != tab) {
					tab.setSelected(false);
					Component component = tab.getComponent();
					if (null != component) {
						component.setVisible(false);
					}
				}
				tab = tabTemp;
			}
			if (!tabTemp.isSelected()) {
				tabTemp.setSelected(true);
				Component component = tabTemp.getComponent();
				if (null != component) {
					component.setVisible(true);
				}
			}
		}
	}
}
