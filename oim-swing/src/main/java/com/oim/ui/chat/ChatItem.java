package com.oim.ui.chat;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.oim.common.component.AlphaPanel;
import com.oim.ui.component.event.ExecuteAction;
import com.only.OnlyButton;
import com.only.OnlyLabel;

/**
 * @author XiaHui
 * @date 2015年3月13日 上午11:08:52
 */
public class ChatItem extends AlphaPanel {

	private static final long serialVersionUID = 1L;

	private Map<Object, Object> attributeMap = new HashMap<Object, Object>();

	private JPanel headRootPanel = new JPanel();
	private JPanel headPanel = new JPanel();

	private OnlyLabel headLabel = new OnlyLabel();

	private JPanel textRootPanel = new JPanel();
	private OnlyLabel textLabel = new OnlyLabel();

	private OnlyButton closeButton = createButton();
	private Color selectColor = new Color(255, 255, 255, 200);
	private Color mouseEnteredColor = new Color(255, 255, 255, 100);
	private Color mouseExitedColor = new Color(255, 255, 255, 50);

	private JPanel closePanel = new JPanel();

	private Set<ExecuteAction> closeExecuteActionSet = new HashSet<ExecuteAction>();

	private boolean mouseEntered = false;
	private boolean selected = false;

	public ChatItem() {

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

		closeButton.setVisible(false);

		JPanel closeNodePanel = new JPanel();
		closeNodePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 1));
		closeNodePanel.setOpaque(false);
		closeNodePanel.add(closeButton);

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
				headMouseEntered();
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				headMouseExited();
			}

			@Override
			public void mousePressed(java.awt.event.MouseEvent evt) {
			}

			@Override
			public void mouseReleased(java.awt.event.MouseEvent evt) {
			}
		});
		closeButton.addMouseListener(new java.awt.event.MouseAdapter() {

			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				headMouseEntered();
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				headMouseExited();
			}

		});
		closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				closeAction();
			}
		});
	}

	private void headMouseExited() {
		mouseEntered = false;
		update();
	}

	private void headMouseEntered() {
		mouseEntered = true;
		update();
	}

	private void update() {
		if (mouseEntered) {
			if (this.isSelected()) {
				this.setBackgroundColor(selectColor);
			} else {
				this.setBackgroundColor(mouseEnteredColor);
			}
			closeButton.setVisible(true);
		} else {
			if (this.isSelected()) {
				this.setBackgroundColor(selectColor);
			} else {
				this.setBackgroundColor(mouseExitedColor);
			}
			closeButton.setVisible(false);
		}
	}

	private void closeAction() {
		for (ExecuteAction executeAction : closeExecuteActionSet) {
			executeAction.execute(this);
		}
		update();
	}

	private OnlyButton createButton() {
		OnlyButton button = new OnlyButton();
		button.setForeground(Color.white);
		button.setNormalImage(null);
		button.setRolloverImage(null);
		button.setPressedImage(null);
		button.setFocusImage(null);

		button.setBorder(null);
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
		button.setDoubleBuffered(true);

		button.setFocusable(false);
		button.setActionCommand(null);
		button.setSelected(false);

		button.setIcon(new ImageIcon("Resources/Images/Default/ChatFrame/main_search_delhighlight.png"));
		button.setPressedIcon(new ImageIcon("Resources/Images/Default/ChatFrame/main_search_deldown.png"));
		button.setRolloverIcon(new ImageIcon("Resources/Images/Default/ChatFrame/main_search_delhighdown.png"));

		// button.setPreferredSize(new Dimension(25, 25));

		return button;
	}

	public void addAttribute(Object key, Object value) {
		attributeMap.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T getAttribute(Object key) {
		return (T) attributeMap.get(key);
	}

	public void setIcon(Icon icon) {
		headLabel.setIcon(icon);
	}

	public void setText(String text) {
		textLabel.setText(text);
	}

	public void addCloseExecuteAction(ExecuteAction executeAction) {
		closeExecuteActionSet.add(executeAction);
	}

	public boolean removeCloseExecuteAction(ExecuteAction executeAction) {
		return closeExecuteActionSet.remove(executeAction);
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		if (this.selected != selected) {
			this.selected = selected;
			this.update();
		}
	}
}
