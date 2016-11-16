/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.ui.component.list;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.oim.common.util.ImageUtil;
import com.oim.ui.component.BasePanel;

/**
 * 
 * @author XiaHui
 */
public class Node extends BasePanel {

	private static final long serialVersionUID = 1L;
	private JPanel titlePanel = new JPanel();
	private JPanel nodePanel = new JPanel();

	private JLabel titleLabel = new JLabel();
	private JLabel titleIconLabel = new JLabel();
	private JLabel titleTextLabel = new JLabel();
	private JLabel titleCountLabel = new JLabel();

	private static ImageIcon closed = ImageUtil.getImageIcon("Resources/Images/List/closed.png", 20, 16);
	private static ImageIcon open = ImageUtil.getImageIcon("Resources/Images/List/open.png", 20, 16);
	private Color mouseEnteredColor = new Color(79, 197, 173);
	// private Color mouseEnteredColor = new Color(222, 235, 234);
	private ImageIcon icon = new ImageIcon("Resources/Images/List/team.png");
	private ImageIcon headIcon = new ImageIcon("Resources/Images/List/big.png");

	private Set<HeadLabelAction> clickedActionSet = new HashSet<HeadLabelAction>();
	private HeadLabelAction headLabelAction;
	private boolean mouseEntered = true;

	private int width = 520;
	private int height = 25;

	public Node() {
		initComponents();
		initEvent();
		setWidth(width);
	}

	private void initComponents() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setOpaque(false);

		titlePanel.setBackground(mouseEnteredColor);

		titleIconLabel.setIcon(closed);

		titleLabel.setLayout(new CardLayout());

		JPanel titleTempPanel = new JPanel();

		titleTempPanel.setOpaque(false);
		titleTempPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 3));

		titleTempPanel.add(titleIconLabel);
		titleTempPanel.add(titleTextLabel);
		titleTempPanel.add(titleCountLabel);

		titlePanel.setOpaque(false);
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));

		titlePanel.add(titleTempPanel);

		titleLabel.add(titlePanel);

		this.add(titleLabel);
		this.add(nodePanel);

		nodePanel.setLayout(new BoxLayout(nodePanel, BoxLayout.Y_AXIS));

		nodePanel.setVisible(false);
		nodePanel.setOpaque(false);

		titleIconLabel.setFont(new java.awt.Font("微软雅黑", 0, 12));
		titleTextLabel.setFont(new java.awt.Font("微软雅黑", 0, 12));

	}

	private void initEvent() {

		titlePanel.addMouseListener(new java.awt.event.MouseAdapter() {

			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				formMouseClicked(evt);
			}

			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				formMouseEntered(evt);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				formMouseExited(evt);
			}
		});
		headLabelAction=new HeadLabelAction() {
			
			@Override
			public void action(MouseEvent e, HeadLabel headLabel) {
				for(HeadLabelAction a:clickedActionSet){
					a.action(e, headLabel);
				}
			}
		};
	}

	private void formMouseEntered(java.awt.event.MouseEvent evt) {
		mouseEntered = true;
		update();
	}

	private void formMouseExited(java.awt.event.MouseEvent evt) {
		mouseEntered = false;
		update();
	}

	private void formMouseClicked(java.awt.event.MouseEvent evt) {
		if (evt.getButton() == MouseEvent.BUTTON3) {
			// teamList.formMouseClicked(evt, name);
		}
		if (evt.getButton() == MouseEvent.BUTTON1) {
			if (evt.getClickCount() == 1 || evt.getClickCount() == 2) {

				nodePanel.setVisible(!nodePanel.isVisible());
				titleIconLabel.setIcon(nodePanel.isVisible() ? open : closed);

			}
		}
	}

	private void update() {
		titlePanel.setOpaque(mouseEntered);
		this.repaint();

	}

	public void setTitleText(String text) {
		titleTextLabel.setText(text);
	}

	public void setCountText(String text) {
		titleCountLabel.setText(text);
	}

	public void setWidth(int width) {
		this.width = width;
		icon.setImage(icon.getImage().getScaledInstance((width), height, Image.SCALE_DEFAULT));
		titleLabel.setIcon(icon);
		headIcon.setImage(headIcon.getImage().getScaledInstance((width), headIcon.getIconHeight(), Image.SCALE_DEFAULT));
		Component[] components = nodePanel.getComponents();
		if (null != components) {
			for (int i = 0; i < components.length; i++) {
				if (components[i] instanceof HeadLabel) {
					((HeadLabel) components[i]).setIcon(headIcon);
				}
			}
		}
	}

	public void addNode(HeadLabel component) {
		nodePanel.add(component);
		component.setIcon(headIcon);
		component.addAction(headLabelAction);
	}

	public void addNode(HeadLabel component, int i) {
		nodePanel.add(component, i);
		component.setIcon(headIcon);
		component.addAction(headLabelAction);
	}

	public void removeNode(HeadLabel component) {
		nodePanel.remove(component);
	}

	public void removeNode(int index) {
		nodePanel.remove(index);
	}

	public void setTextVisible(boolean visible) {
		titleTextLabel.setVisible(visible);
		titleCountLabel.setVisible(visible);
	}
	
	public void addAction(HeadLabelAction headLabelAction) {
		clickedActionSet.add(headLabelAction);
	}
}
