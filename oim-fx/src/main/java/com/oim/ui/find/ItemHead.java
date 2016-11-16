/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.ui.find;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.oim.common.event.ExecuteAction;
import com.oim.ui.component.BasePanel;
import com.only.OnlyButton;

/**
 * date 2012-9-22 8:21:14
 * 
 * @author XiaHui
 */
public class ItemHead extends BasePanel {

	/**
     *
     */
	private static final long serialVersionUID = 1L;

	JPanel headPanel = new JPanel();
	JPanel textPanel = new JPanel();

	private JLabel headLabel = new JLabel();
	private JLabel nameLabel = new JLabel();
	private JLabel textLabel = new JLabel();
	private OnlyButton addButton = createButton();
	private Color mouseEnteredColor = new Color(70, 190, 200);
	private Color mouseExitedColor = new Color(239, 234, 234);

	Set<ExecuteAction> executeActionSet = new HashSet<ExecuteAction>();

	public ItemHead() {
		initComponents();
		initEvent();
	}

	private void initComponents() {
		this.setLayout(new java.awt.BorderLayout());
		this.setBackground(mouseExitedColor);

		addButton.setText("添加    ");

		JPanel headNodePanel = new JPanel();
		headNodePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 2));
		headNodePanel.setOpaque(false);

		headNodePanel.add(headLabel);

		headPanel.setLayout(new java.awt.GridBagLayout());
		headPanel.setOpaque(false);
		headPanel.add(headNodePanel);

		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		textPanel.setOpaque(false);

		textPanel.add(nameLabel);
		textPanel.add(textLabel);
		//textPanel.add(addButton);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 1));
		buttonPanel.setOpaque(false);
		buttonPanel.add(addButton);

		JPanel baseTextPanel = new JPanel();
		baseTextPanel.setLayout(new java.awt.BorderLayout());
		baseTextPanel.setOpaque(false);

		baseTextPanel.add(textPanel, java.awt.BorderLayout.CENTER);
		baseTextPanel.add(buttonPanel, java.awt.BorderLayout.PAGE_END);

		add(headPanel, java.awt.BorderLayout.WEST);
		add(baseTextPanel, java.awt.BorderLayout.CENTER);
	}

	private OnlyButton createButton() {
		OnlyButton button = new OnlyButton();
		button.setForeground(Color.white);
		button.setNormalImageInsets(6, 6, 6, 6);
		button.setNormalImage(new ImageIcon("Resources/Images/Default/Button/blue_normal.png").getImage());
		button.setRolloverImage(new ImageIcon("Resources/Images/Default/Button/blue_hover.png").getImage());
		button.setPressedImage(new ImageIcon("Resources/Images/Default/Button/blue_down.png").getImage());
		button.setFocusImage(new ImageIcon("Resources/Images/Default/Button/blue_down.png").getImage());

		button.setIcon(new ImageIcon("Resources/Images/Default/FindFrame/AreaMainIcon_2.png"));
		button.setPressedIcon(new ImageIcon("Resources/Images/Default/FindFrame/AreaMainIcon.png"));
		button.setRolloverIcon(new ImageIcon("Resources/Images/Default/FindFrame/AreaMainIcon.png"));

		button.setPreferredSize(new Dimension(80, 20));

		return button;
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
		addButton.addMouseListener(new java.awt.event.MouseAdapter() {

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
		headLabel.addMouseListener(new java.awt.event.MouseAdapter() {

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

		addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonAction();
			}
		});
	}

	private void headMouseExited(MouseEvent evt) {
		this.setBackground(mouseExitedColor);
	}

	private void headMouseEntered(MouseEvent evt) {
		this.setBackground(mouseEnteredColor);
	}

	private void buttonAction() {
		for (ExecuteAction executeAction : executeActionSet) {
			if (null != executeAction) {
				executeAction.execute(this);
			}
		}
	}

	public void setHeadIcon(Icon icon) {
		headLabel.setIcon(icon);
	}

	public void setName(String text) {
		nameLabel.setText(text);
	}

	public void setShowText(String text) {
		textLabel.setText(text);
	}

	public void add(ExecuteAction executeAction) {
		executeActionSet.add(executeAction);
	}

	public boolean remove(ExecuteAction executeAction) {
		return executeActionSet.remove(executeAction);
	}

}
