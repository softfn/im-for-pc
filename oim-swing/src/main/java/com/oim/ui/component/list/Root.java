/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.ui.component.list;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.only.OnlyScrollPane;

/**
 * 
 * @author XiaHui
 */
public class Root extends JPanel {

	private static final long serialVersionUID = 1L;

	private JPanel rootPanel = new JPanel();
	private OnlyScrollPane scrollPane = new OnlyScrollPane();
	//private Color color = new Color(255, 255, 255, 180);
	private Color color = new Color(230, 230, 230,220);
	private ImageIcon headIcon = new ImageIcon("Resources/Images/List/big.png");
	private HeadLabelAction headLabelAction;

	public Root() {
		initComponents();
		initEvent();
	}

	private void initComponents() {
		this.setOpaque(false);
		this.setLayout(new CardLayout());

		rootPanel.setOpaque(false);
		rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));

		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(null);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setHeaderVisible(false);
		scrollPane.setAlpha(0.0f);
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);

		scrollPane.setViewportView(rootPanel);
		headIcon.setImage(headIcon.getImage().getScaledInstance((520), headIcon.getIconHeight(), Image.SCALE_DEFAULT));

		this.add(scrollPane);
	}

	private void initEvent() {
		headLabelAction = new HeadLabelAction() {
			HeadLabel headLabel;

			@Override
			public void action(MouseEvent e, HeadLabel headLabel) {
				if (headLabel != this.headLabel) {
					if (null != this.headLabel) {
						this.headLabel.setSelected(false);
					}
					headLabel.setSelected(true);
				}
				this.headLabel = headLabel;
			}
		};
	}

	public void addNode(Component component) {
		rootPanel.add(component);
		if (component instanceof HeadLabel) {
			((HeadLabel) component).setIcon(headIcon);
			((HeadLabel) component).addAction(headLabelAction);
		}
		if (component instanceof Node) {
			((Node) component).addAction(headLabelAction);
		}
	}

	public void addNode(Component component, int index) {
		rootPanel.add(component, index);
		if (component instanceof HeadLabel) {
			((HeadLabel) component).setIcon(headIcon);
			((HeadLabel) component).addAction(headLabelAction);
		}
		if (component instanceof Node) {
			((Node) component).addAction(headLabelAction);
		}
	}

	public void removeNode(Component component) {
		rootPanel.remove(component);
	}

	public void removeNode(int index) {
		rootPanel.remove(index);
	}
	
	public void addListMouseListener(MouseListener l){
		rootPanel.addMouseListener(l);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setPaint(color);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
	}
}
