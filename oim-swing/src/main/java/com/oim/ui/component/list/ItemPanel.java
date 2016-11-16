package com.oim.ui.component.list;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.oim.common.component.AlphaPanel;
import com.only.OnlyLabel;

/**
 * @author XiaHui
 * @date 2015年3月13日 上午11:08:52
 */
public class ItemPanel extends AlphaPanel {

	private static final long serialVersionUID = 1L;

	private Map<Object, Object> attributeMap = new HashMap<Object, Object>();

	private JPanel headRootPanel = new JPanel();
	private JPanel headPanel = new JPanel();

	private IconPanel headLabel = new IconPanel();

	private JPanel textRootPanel = new JPanel();
	private OnlyLabel textLabel = new OnlyLabel();

	private Color mouseEnteredColor = new Color(255, 255, 255, 100);
	private Color mouseExitedColor = new Color(255, 255, 255, 50);

	private JPanel closePanel = new JPanel();

	public ItemPanel() {

		initComponents();
		initEvent();
	}

	private void initComponents() {
		this.setLayout(new java.awt.BorderLayout());
		this.setOpaque(false);
		this.setBackgroundColor(mouseExitedColor);

		headRootPanel.setOpaque(false);
		headRootPanel.setLayout(new java.awt.GridBagLayout());
		headRootPanel.add(headPanel);

		headPanel.setOpaque(false);
		headPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 1));
		headPanel.add(headLabel);

		textRootPanel.add(headPanel);
		textRootPanel.setOpaque(false);
		textRootPanel.setLayout(new BoxLayout(textRootPanel, BoxLayout.X_AXIS));

		JPanel dataRootPanel = new JPanel();
		dataRootPanel.setLayout(new java.awt.BorderLayout());
		dataRootPanel.setOpaque(false);

		JPanel dataNodePanel = new JPanel();
		dataNodePanel.setLayout(new java.awt.GridBagLayout());
		dataNodePanel.setOpaque(false);

		dataNodePanel.add(textLabel);

		dataRootPanel.add(dataNodePanel, java.awt.BorderLayout.WEST);

		textRootPanel.add(dataRootPanel);

		JPanel closeNodePanel = new JPanel();
		closeNodePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 1));
		closeNodePanel.setOpaque(false);

		closePanel.setLayout(new java.awt.GridBagLayout());
		closePanel.setOpaque(false);
		closePanel.add(closeNodePanel);

		this.add(headRootPanel, java.awt.BorderLayout.WEST);
		this.add(textRootPanel, java.awt.BorderLayout.CENTER);
		this.add(closePanel, java.awt.BorderLayout.EAST);
	}

	private void initEvent() {
		addMouseListener(new java.awt.event.MouseAdapter() {

			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
			}

			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				headMouseEntered(evt);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				headMouseExited(evt);
			}

			@Override
			public void mousePressed(java.awt.event.MouseEvent evt) {
			}

			@Override
			public void mouseReleased(java.awt.event.MouseEvent evt) {
			}
		});

	}

	private void headMouseExited(MouseEvent evt) {
		this.setBackgroundColor(mouseExitedColor);
	}

	private void headMouseEntered(MouseEvent evt) {
		this.setBackgroundColor(mouseEnteredColor);
	}

	public void addAttribute(Object key, Object value) {
		attributeMap.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T getAttribute(Object key) {
		return (T) attributeMap.get(key);
	}

	public void setIcon(ImageIcon icon) {
		headLabel.setIcon(icon);
	}
	
	public void setStatusIcon(Icon icon) {
		headLabel.setStatusIcon(icon);
	}
	
	public void setText(String text) {
		textLabel.setText(text);
	}
}
